from django.contrib import admin
from .models import SubscriptionPlan, Subscription, Payment

@admin.register(SubscriptionPlan)
class SubscriptionPlanAdmin(admin.ModelAdmin):
    list_display = ('id','name', 'price', 'duration_months', 'max_sessions_per_week', 'includes_coach', 'includes_group_classes', 'includes_pool', 'created_at')
    search_fields = ('name',)
    list_filter = ('includes_coach', 'includes_group_classes', 'includes_pool', 'duration_months')
    ordering = ('-created_at',)


@admin.register(Subscription)
class SubscriptionAdmin(admin.ModelAdmin):
    list_display = ('id','member', 'plan', 'start_date', 'end_date', 'status', 'payment_status', 'auto_renewal', 'created_at')
    list_filter = ('status', 'payment_status', 'auto_renewal', 'created_at')
    search_fields = ('member__user__username', 'plan__name')
    ordering = ('-created_at',)


@admin.register(Payment)
class PaymentAdmin(admin.ModelAdmin):
    list_display = ('id','subscription', 'amount', 'payment_date', 'payment_method', 'transaction_id', 'status')
    list_filter = ('payment_method', 'status', 'payment_date')
    search_fields = ('subscription__member__user__username', 'transaction_id')
    ordering = ('-payment_date',)
