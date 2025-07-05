from rest_framework import viewsets, permissions, status
from rest_framework.decorators import action
from rest_framework.response import Response
from django.utils import timezone
from django.db.models import Sum, Count
from datetime import timedelta
from .models import Product, Sale, SaleItem, Inventory
from .serializers import (
    ProductSerializer, SaleSerializer, SaleItemSerializer, InventorySerializer
)

class ProductViewSet(viewsets.ModelViewSet):
    queryset = Product.objects.all()
    serializer_class = ProductSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_permissions(self):
        if self.action in ['list', 'retrieve']:
            return [permissions.AllowAny()]
        return [permissions.IsAdminUser()]

    @action(detail=False, methods=['get'])
    def low_stock(self, request):
        products = Product.objects.filter(
            stock_quantity__lte=models.F('minimum_stock')
        )
        serializer = self.get_serializer(products, many=True)
        return Response(serializer.data)

    @action(detail=False, methods=['get'])
    def statistics(self, request):
        total_products = Product.objects.count()
        low_stock_products = Product.objects.filter(
            stock_quantity__lte=models.F('minimum_stock')
        ).count()
        out_of_stock = Product.objects.filter(stock_quantity=0).count()
        total_value = Product.objects.aggregate(
            total=Sum(models.F('stock_quantity') * models.F('price'))
        )['total'] or 0

        return Response({
            'total_products': total_products,
            'low_stock_products': low_stock_products,
            'out_of_stock': out_of_stock,
            'total_inventory_value': total_value
        })

class SaleViewSet(viewsets.ModelViewSet):
    queryset = Sale.objects.all()
    serializer_class = SaleSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        if self.request.user.is_staff:
            return Sale.objects.all()
        return Sale.objects.filter(member__user=self.request.user)

    @action(detail=False, methods=['get'])
    def daily_sales(self, request):
        if not request.user.is_staff:
            return Response({
                'error': 'Permission denied'
            }, status=status.HTTP_403_FORBIDDEN)

        today = timezone.now().date()
        sales = Sale.objects.filter(
            transaction_date__date=today
        )
        
        total_amount = sales.aggregate(total=Sum('total_amount'))['total'] or 0
        total_sales = sales.count()
        
        return Response({
            'date': today,
            'total_amount': total_amount,
            'total_sales': total_sales
        })

    @action(detail=False, methods=['get'])
    def sales_report(self, request):
        if not request.user.is_staff:
            return Response({
                'error': 'Permission denied'
            }, status=status.HTTP_403_FORBIDDEN)

        days = int(request.query_params.get('days', 30))
        end_date = timezone.now()
        start_date = end_date - timedelta(days=days)
        
        sales = Sale.objects.filter(
            transaction_date__range=(start_date, end_date)
        )
        
        daily_sales = sales.values('transaction_date__date').annotate(
            total=Sum('total_amount'),
            count=Count('id')
        ).order_by('transaction_date__date')
        
        top_products = SaleItem.objects.filter(
            sale__in=sales
        ).values('product__name').annotate(
            total_quantity=Sum('quantity'),
            total_amount=Sum('total_price')
        ).order_by('-total_quantity')[:5]
        
        return Response({
            'start_date': start_date,
            'end_date': end_date,
            'total_sales': sales.count(),
            'total_amount': sales.aggregate(total=Sum('total_amount'))['total'] or 0,
            'daily_sales': daily_sales,
            'top_products': top_products
        })

class SaleItemViewSet(viewsets.ModelViewSet):
    queryset = SaleItem.objects.all()
    serializer_class = SaleItemSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        if self.request.user.is_staff:
            return SaleItem.objects.all()
        return SaleItem.objects.filter(sale__member__user=self.request.user)

class InventoryViewSet(viewsets.ModelViewSet):
    queryset = Inventory.objects.all()
    serializer_class = InventorySerializer
    permission_classes = [permissions.IsAdminUser]

    @action(detail=False, methods=['post'])
    def adjust_stock(self, request):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        self.perform_create(serializer)
        
        return Response(serializer.data, status=status.HTTP_201_CREATED)

    @action(detail=False, methods=['get'])
    def movements(self, request):
        product_id = request.query_params.get('product_id')
        if not product_id:
            return Response({
                'error': 'Product ID is required'
            }, status=status.HTTP_400_BAD_REQUEST)
            
        movements = Inventory.objects.filter(
            product_id=product_id
        ).order_by('-date')
        
        serializer = self.get_serializer(movements, many=True)
        return Response(serializer.data)
