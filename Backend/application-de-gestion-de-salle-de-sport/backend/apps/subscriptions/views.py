from rest_framework import viewsets, permissions, status
from rest_framework.decorators import action
from rest_framework.response import Response
from django.utils import timezone
from .models import SubscriptionPlan, Subscription, Payment
from .permission import IsAdminOrManager
from .serializers import SubscriptionPlanSerializer, SubscriptionSerializer, PaymentSerializer

class SubscriptionPlanViewSet(viewsets.ModelViewSet):
    queryset = SubscriptionPlan.objects.all()
    serializer_class = SubscriptionPlanSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_permissions(self):
        if self.action == 'list':
            return [permissions.AllowAny()]
        return [IsAdminOrManager()]

    def get_serializer_context(self):
        context = super().get_serializer_context()
        context['request'] = self.request
        return context

class SubscriptionViewSet(viewsets.ModelViewSet):
    queryset = Subscription.objects.all()
    serializer_class = SubscriptionSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        if hasattr(self.request.user, 'role') and self.request.user.role == 'admin':
            return Subscription.objects.all()
        return Subscription.objects.filter(member__user=self.request.user)

    @action(detail=True, methods=['post'])
    def renew(self, request, pk=None):
        subscription = self.get_object()

        # Vérifier si l'abonnement est expiré
        current_date = timezone.now().date()  # Convertir en datetime.date
        is_expired = subscription.end_date < current_date

        # Autoriser le renouvellement uniquement si l'abonnement est annulé ou expiré
        if subscription.status == 'cancelled' or is_expired:
            # Définir la date de début du nouvel abonnement
            new_start_date = current_date if is_expired else subscription.end_date
            # Créer un nouvel abonnement avec les nouvelles dates
            new_subscription = Subscription.objects.create(
                member=subscription.member,
                plan=subscription.plan,
                start_date=new_start_date,
                end_date=new_start_date + timezone.timedelta(days=30 * subscription.plan.duration_months),
                auto_renewal=subscription.auto_renewal,
                status='active'  # L'abonnement renouvelé est "active"
            )
            # Optionnel : Marquer l'ancien abonnement comme "inactive"
            subscription.status = 'inactive'
            subscription.save()

            return Response({
                'message': 'Abonnement renouvelé avec succès',
                'subscription': SubscriptionSerializer(new_subscription).data
            })

        return Response({
            'error': 'Seul un abonnement annulé ou expiré peut être renouvelé'
        }, status=status.HTTP_400_BAD_REQUEST)

    @action(detail=True, methods=['post'])
    def cancel(self, request, pk=None):
        subscription = self.get_object()
        subscription.status = 'cancelled'
        subscription.auto_renewal = False
        subscription.save()
        return Response({'status': 'subscription cancelled'})

class PaymentViewSet(viewsets.ModelViewSet):
    queryset = Payment.objects.all()
    serializer_class = PaymentSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        if self.request.user.is_staff:
            return Payment.objects.all()
        return Payment.objects.filter(subscription__member__user=self.request.user)

    def perform_create(self, serializer):
        payment = serializer.save()
        # Mettre à jour le statut de l'abonnement
        subscription = payment.subscription
        subscription.payment_status = True
        subscription.status = 'active'
        subscription.save()

    @action(detail=False, methods=['get'])
    def monthly_revenue(self, request):
        if not hasattr(request.user, 'role') or request.user.role != 'admin':
            return Response({
                'error': 'Permission denied'
            }, status=status.HTTP_403_FORBIDDEN)

        today = timezone.now()
        start_date = today.replace(day=1, hour=0, minute=0, second=0, microsecond=0)
        end_date = (start_date + timezone.timedelta(days=32)).replace(day=1) - timezone.timedelta(seconds=1)
        
        monthly_payments = Payment.objects.filter(
            payment_date__range=(start_date, end_date),
            status='completed'
        )
        
        total_revenue = sum(payment.amount for payment in monthly_payments)
        
        return Response({
            'start_date': start_date,
            'end_date': end_date,
            'total_revenue': total_revenue,
            'number_of_payments': monthly_payments.count()
        })

from knox.views import LogoutView
from rest_framework.response import Response

class CookieLogoutView(LogoutView):
    def post(self, request, *args, **kwargs):
        response = super().post(request, *args, **kwargs)
        response.delete_cookie('auth_token')
        return response

