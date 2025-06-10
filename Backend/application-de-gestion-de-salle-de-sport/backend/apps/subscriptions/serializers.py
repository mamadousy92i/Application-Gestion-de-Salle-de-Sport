from rest_framework import serializers
from .models import SubscriptionPlan, Subscription, Payment
from apps.accounts.serializers import MemberSerializer
from django.core.exceptions import ObjectDoesNotExist
from rest_framework import serializers

class SubscriptionPlanSerializer(serializers.ModelSerializer):
    image = serializers.ImageField(required=False)

    class Meta:
        model = SubscriptionPlan
        fields = ('id', 'name', 'description', 'price', 'duration_months',
                  'max_sessions_per_week', 'includes_coach', 'includes_group_classes',
                  'includes_pool', 'image', 'created_at', 'updated_at')
        read_only_fields = ('id', 'created_at', 'updated_at')


class SubscriptionSerializer(serializers.ModelSerializer):
    member = MemberSerializer(read_only=True)
    member_id = serializers.IntegerField(write_only=True)
    plan = SubscriptionPlanSerializer(read_only=True)
    plan_id = serializers.IntegerField(write_only=True)

    class Meta:
        model = Subscription
        fields = ('id', 'member', 'member_id', 'plan', 'plan_id', 'start_date',
                  'end_date', 'status', 'payment_status', 'auto_renewal',
                  'created_at', 'updated_at')
        read_only_fields = ('id', 'created_at', 'updated_at')

    def create(self, validated_data):
        try:
            member_id = validated_data.pop('member_id')
            plan_id = validated_data.pop('plan_id')
            subscription = Subscription.objects.create(
                member_id=member_id,
                plan_id=plan_id,
                **validated_data
            )
            return subscription
        except ObjectDoesNotExist:
            raise serializers.ValidationError({
                'error': 'Member or SubscriptionPlan does not exist.'
            })
        except Exception as e:
            raise serializers.ValidationError({
                'error': f'An error occurred: {str(e)}'
            })


class PaymentSerializer(serializers.ModelSerializer):
    subscription = SubscriptionSerializer(read_only=True)
    subscription_id = serializers.IntegerField(write_only=True)

    class Meta:
        model = Payment
        fields = ('id', 'subscription', 'subscription_id', 'amount',
                  'payment_date', 'payment_method', 'transaction_id',
                  'status', 'notes')
        read_only_fields = ('id', 'payment_date')

    def create(self, validated_data):
        subscription_id = validated_data.pop('subscription_id')
        payment = Payment.objects.create(
            subscription_id=subscription_id,
            **validated_data
        )
        # Mettre à jour le statut de paiement de l'abonnement
        subscription = payment.subscription
        subscription.payment_status = True
        subscription.save()
        return payment
