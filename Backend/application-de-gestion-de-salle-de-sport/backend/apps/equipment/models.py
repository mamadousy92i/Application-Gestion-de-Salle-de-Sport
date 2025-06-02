from django.db import models

class EquipmentCategory(models.Model):
    name = models.CharField(max_length=100)
    description = models.TextField()
    
    def __str__(self):
        return self.name
    
    class Meta:
        verbose_name_plural = "Equipment Categories"

class Equipment(models.Model):
    STATUS_CHOICES = (
        ('available', 'Disponible'),
        ('in_use', 'En utilisation'),
        ('maintenance', 'En maintenance'),
        ('broken', 'Hors service'),
    )
    
    name = models.CharField(max_length=100)
    category = models.ForeignKey(EquipmentCategory, on_delete=models.PROTECT)
    description = models.TextField()
    serial_number = models.CharField(max_length=100, unique=True)
    purchase_date = models.DateField()
    purchase_price = models.DecimalField(max_digits=10, decimal_places=2)
    status = models.CharField(max_length=20, choices=STATUS_CHOICES, default='available')
    location = models.CharField(max_length=100)
    last_maintenance_date = models.DateField(null=True, blank=True)
    next_maintenance_date = models.DateField(null=True, blank=True)
    notes = models.TextField(blank=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    def __str__(self):
        return f"{self.name} - {self.serial_number}"

class MaintenanceRecord(models.Model):
    MAINTENANCE_TYPE_CHOICES = (
        ('routine', 'Maintenance de routine'),
        ('repair', 'Réparation'),
        ('inspection', 'Inspection'),
        ('replacement', 'Remplacement'),
    )
    
    equipment = models.ForeignKey(Equipment, on_delete=models.CASCADE)
    maintenance_date = models.DateField()
    maintenance_type = models.CharField(max_length=20, choices=MAINTENANCE_TYPE_CHOICES)
    description = models.TextField()
    cost = models.DecimalField(max_digits=10, decimal_places=2)
    performed_by = models.CharField(max_length=100)
    next_maintenance_date = models.DateField(null=True, blank=True)
    notes = models.TextField(blank=True)
    created_at = models.DateTimeField(auto_now_add=True)
    
    def __str__(self):
        return f"{self.equipment.name} - {self.maintenance_date}"
