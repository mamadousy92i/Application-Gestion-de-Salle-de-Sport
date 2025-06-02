from django.db import models
from apps.accounts.models import Member

class Product(models.Model):
    CATEGORY_CHOICES = (
        ('supplement', 'Suppléments'),
        ('equipment', 'Équipement'),
        ('clothing', 'Vêtements'),
        ('beverage', 'Boissons'),
        ('other', 'Autre'),
    )
    
    name = models.CharField(max_length=100)
    category = models.CharField(max_length=20, choices=CATEGORY_CHOICES)
    description = models.TextField()
    price = models.DecimalField(max_digits=10, decimal_places=2)
    stock_quantity = models.IntegerField()
    minimum_stock = models.IntegerField()
    image = models.ImageField(upload_to='products/', null=True, blank=True)
    barcode = models.CharField(max_length=100, unique=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    def __str__(self):
        return f"{self.name} - {self.get_category_display()}"

class Sale(models.Model):
    PAYMENT_METHOD_CHOICES = (
        ('cash', 'Espèces'),
        ('card', 'Carte bancaire'),
        ('transfer', 'Virement'),
    )
    
    member = models.ForeignKey(Member, on_delete=models.SET_NULL, null=True)
    total_amount = models.DecimalField(max_digits=10, decimal_places=2)
    payment_method = models.CharField(max_length=20, choices=PAYMENT_METHOD_CHOICES)
    transaction_date = models.DateTimeField(auto_now_add=True)
    notes = models.TextField(blank=True)
    
    def __str__(self):
        return f"Vente #{self.id} - {self.transaction_date.strftime('%d/%m/%Y')}"

class SaleItem(models.Model):
    sale = models.ForeignKey(Sale, on_delete=models.CASCADE)
    product = models.ForeignKey(Product, on_delete=models.PROTECT)
    quantity = models.IntegerField()
    unit_price = models.DecimalField(max_digits=10, decimal_places=2)
    total_price = models.DecimalField(max_digits=10, decimal_places=2)
    
    def save(self, *args, **kwargs):
        self.total_price = self.quantity * self.unit_price
        super().save(*args, **kwargs)
    
    def __str__(self):
        return f"{self.product.name} x{self.quantity}"

class Inventory(models.Model):
    product = models.ForeignKey(Product, on_delete=models.CASCADE)
    quantity_change = models.IntegerField()
    date = models.DateTimeField(auto_now_add=True)
    reason = models.CharField(max_length=100)
    notes = models.TextField(blank=True)
    
    def __str__(self):
        return f"{self.product.name} - {self.quantity_change}"
    
    class Meta:
        verbose_name_plural = "Inventory"
