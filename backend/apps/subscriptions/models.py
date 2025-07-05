from django.db import models
from apps.accounts.models import Member

class SubscriptionPlan(models.Model):
    name = models.CharField(max_length=100)
    description = models.TextField()
    price = models.DecimalField(max_digits=10, decimal_places=2)
    duration_months = models.IntegerField()
    max_sessions_per_week = models.IntegerField()
    includes_coach = models.BooleanField(default=False)
    includes_group_classes = models.BooleanField(default=False)
    includes_pool = models.BooleanField(default=False)
    image = models.ImageField(upload_to='subscription_plans/', null=True, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    def __str__(self):
        return f"{self.name} - {self.duration_months} mois"

class Subscription(models.Model):
    STATUS_CHOICES = (
        ('active', 'Actif'),
        ('expired', 'Expiré'),
        ('cancelled', 'Annulé'),
        ('pending', 'En attente'),
    )
    
    member = models.ForeignKey(Member, on_delete=models.CASCADE)
    plan = models.ForeignKey(SubscriptionPlan, on_delete=models.PROTECT)
    start_date = models.DateField()
    end_date = models.DateField()
    status = models.CharField(max_length=20, choices=STATUS_CHOICES, default='pending')
    payment_status = models.BooleanField(default=False)
    auto_renewal = models.BooleanField(default=False)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    def __str__(self):
        return f"{self.member.user.get_full_name()} - {self.plan.name}"

class Payment(models.Model):
    PAYMENT_METHOD_CHOICES = (
        ('cash', 'Espèces'),
        ('card', 'Carte bancaire'),
        ('transfer', 'Virement'),
        ('check', 'Chèque'),
    )
    
    subscription = models.ForeignKey(Subscription, on_delete=models.CASCADE)
    amount = models.DecimalField(max_digits=10, decimal_places=2)
    payment_date = models.DateTimeField(auto_now_add=True)
    payment_method = models.CharField(max_length=20, choices=PAYMENT_METHOD_CHOICES)
    transaction_id = models.CharField(max_length=100, blank=True)
    status = models.CharField(max_length=20, default='completed')
    notes = models.TextField(blank=True)
    
    def __str__(self):
        return f"{self.subscription.member.user.get_full_name()} - {self.amount} €"
