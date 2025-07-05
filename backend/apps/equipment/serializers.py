from rest_framework import serializers
from .models import EquipmentCategory, Equipment, MaintenanceRecord

class EquipmentCategorySerializer(serializers.ModelSerializer):
    class Meta:
        model = EquipmentCategory
        fields = '__all__'

class EquipmentSerializer(serializers.ModelSerializer):
    category = EquipmentCategorySerializer(read_only=True)
    category_id = serializers.IntegerField(write_only=True)
    days_until_maintenance = serializers.SerializerMethodField()

    class Meta:
        model = Equipment
        fields = ('id', 'name', 'category', 'category_id', 'description',
                 'serial_number', 'purchase_date', 'purchase_price', 'status',
                 'location', 'last_maintenance_date', 'next_maintenance_date',
                 'notes', 'created_at', 'updated_at', 'days_until_maintenance')
        read_only_fields = ('id', 'created_at', 'updated_at')

    def get_days_until_maintenance(self, obj):
        if not obj.next_maintenance_date:
            return None
        from django.utils import timezone
        today = timezone.now().date()
        return (obj.next_maintenance_date - today).days

class MaintenanceRecordSerializer(serializers.ModelSerializer):
    equipment = EquipmentSerializer(read_only=True)
    equipment_id = serializers.IntegerField(write_only=True)

    class Meta:
        model = MaintenanceRecord
        fields = ('id', 'equipment', 'equipment_id', 'maintenance_date',
                 'maintenance_type', 'description', 'cost', 'performed_by',
                 'next_maintenance_date', 'notes', 'created_at')
        read_only_fields = ('id', 'created_at')

    def create(self, validated_data):
        maintenance_record = super().create(validated_data)
        
        # Mettre à jour les dates de maintenance de l'équipement
        equipment = maintenance_record.equipment
        equipment.last_maintenance_date = maintenance_record.maintenance_date
        equipment.next_maintenance_date = maintenance_record.next_maintenance_date
        equipment.save()
        
        return maintenance_record
