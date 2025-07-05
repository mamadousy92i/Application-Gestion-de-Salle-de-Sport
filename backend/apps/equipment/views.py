from rest_framework import viewsets, permissions, status
from rest_framework.decorators import action
from rest_framework.response import Response
from django.utils import timezone
from django.db.models import Sum
from .models import EquipmentCategory, Equipment, MaintenanceRecord
from .serializers import (
    EquipmentCategorySerializer, EquipmentSerializer, MaintenanceRecordSerializer
)

class EquipmentCategoryViewSet(viewsets.ModelViewSet):
    queryset = EquipmentCategory.objects.all()
    serializer_class = EquipmentCategorySerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_permissions(self):
        if self.action == 'list':
            return [permissions.AllowAny()]
        return [permissions.IsAdminUser()]

class EquipmentViewSet(viewsets.ModelViewSet):
    queryset = Equipment.objects.all()
    serializer_class = EquipmentSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_permissions(self):
        if self.action in ['list', 'retrieve']:
            return [permissions.AllowAny()]
        return [permissions.IsAdminUser()]

    @action(detail=False, methods=['get'])
    def maintenance_needed(self, request):
        today = timezone.now().date()
        equipment_list = Equipment.objects.filter(
            next_maintenance_date__lte=today,
            status='available'
        )
        serializer = self.get_serializer(equipment_list, many=True)
        return Response(serializer.data)

    @action(detail=True, methods=['post'])
    def change_status(self, request, pk=None):
        equipment = self.get_object()
        new_status = request.data.get('status')
        if new_status not in dict(Equipment.STATUS_CHOICES):
            return Response({
                'error': 'Invalid status'
            }, status=status.HTTP_400_BAD_REQUEST)

        equipment.status = new_status
        equipment.save()
        return Response(self.get_serializer(equipment).data)

    @action(detail=False, methods=['get'])
    def statistics(self, request):
        total_equipment = Equipment.objects.count()
        available_equipment = Equipment.objects.filter(status='available').count()
        maintenance_needed = Equipment.objects.filter(
            next_maintenance_date__lte=timezone.now().date()
        ).count()
        total_value = Equipment.objects.aggregate(
            total=Sum('purchase_price')
        )['total'] or 0

        return Response({
            'total_equipment': total_equipment,
            'available_equipment': available_equipment,
            'maintenance_needed': maintenance_needed,
            'total_value': total_value
        })

class MaintenanceRecordViewSet(viewsets.ModelViewSet):
    queryset = MaintenanceRecord.objects.all()
    serializer_class = MaintenanceRecordSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        queryset = MaintenanceRecord.objects.all()
        equipment_id = self.request.query_params.get('equipment_id', None)
        if equipment_id is not None:
            queryset = queryset.filter(equipment_id=equipment_id)
        return queryset

    @action(detail=False, methods=['get'])
    def maintenance_costs(self, request):
        if not request.user.is_staff:
            return Response({
                'error': 'Permission denied'
            }, status=status.HTTP_403_FORBIDDEN)

        year = request.query_params.get('year', timezone.now().year)
        records = MaintenanceRecord.objects.filter(
            maintenance_date__year=year
        )
        
        total_cost = records.aggregate(total=Sum('cost'))['total'] or 0
        monthly_costs = []
        
        for month in range(1, 13):
            month_cost = records.filter(
                maintenance_date__month=month
            ).aggregate(total=Sum('cost'))['total'] or 0
            monthly_costs.append({
                'month': month,
                'cost': month_cost
            })

        return Response({
            'year': year,
            'total_cost': total_cost,
            'monthly_costs': monthly_costs
        })
