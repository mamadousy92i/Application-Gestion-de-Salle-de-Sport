from rest_framework import viewsets, permissions, status
from rest_framework.decorators import action
from rest_framework.response import Response
from django.utils import timezone
from django.db.models import Avg
from .models import CourseCategory, Course, CourseSchedule, CourseBooking, CourseReview
from .serializers import (
    CourseCategorySerializer, CourseSerializer, CourseScheduleSerializer,
    CourseBookingSerializer, CourseReviewSerializer
)

class CourseCategoryViewSet(viewsets.ModelViewSet):
    queryset = CourseCategory.objects.all()
    serializer_class = CourseCategorySerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_permissions(self):
        if self.action == 'list':
            return [permissions.AllowAny()]
        return [permissions.IsAdminUser()]

class CourseViewSet(viewsets.ModelViewSet):
    queryset = Course.objects.all()
    serializer_class = CourseSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_permissions(self):
        if self.action in ['list', 'retrieve']:
            return [permissions.AllowAny()]
        return [permissions.IsAdminUser()]

    @action(detail=True, methods=['get'])
    def schedules(self, request, pk=None):
        course = self.get_object()
        schedules = CourseSchedule.objects.filter(
            course=course,
            start_time__gte=timezone.now(),
            is_cancelled=False
        )
        serializer = CourseScheduleSerializer(schedules, many=True)
        return Response(serializer.data)

    @action(detail=True, methods=['get'])
    def reviews(self, request, pk=None):
        course = self.get_object()
        reviews = CourseReview.objects.filter(
            booking__schedule__course=course
        )
        average_rating = reviews.aggregate(Avg('rating'))['rating__avg']
        serializer = CourseReviewSerializer(reviews, many=True)
        return Response({
            'average_rating': average_rating,
            'reviews': serializer.data
        })

class CourseScheduleViewSet(viewsets.ModelViewSet):
    queryset = CourseSchedule.objects.all()
    serializer_class = CourseScheduleSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        queryset = CourseSchedule.objects.all()
        if not self.request.user.is_staff:
            queryset = queryset.filter(start_time__gte=timezone.now())
        return queryset

    @action(detail=True, methods=['post'])
    def cancel(self, request, pk=None):
        schedule = self.get_object()
        reason = request.data.get('reason', '')
        schedule.is_cancelled = True
        schedule.cancellation_reason = reason
        schedule.save()

        # Annuler toutes les réservations
        bookings = CourseBooking.objects.filter(schedule=schedule, status='booked')
        for booking in bookings:
            booking.status = 'cancelled'
            booking.cancellation_reason = 'Course cancelled by administrator'
            booking.cancellation_date = timezone.now()
            booking.save()

        return Response({'status': 'Course cancelled'})

class CourseBookingViewSet(viewsets.ModelViewSet):
    queryset = CourseBooking.objects.all()
    serializer_class = CourseBookingSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        if self.request.user.is_staff:
            return CourseBooking.objects.all()
        return CourseBooking.objects.filter(member__user=self.request.user)

    @action(detail=True, methods=['post'])
    def cancel(self, request, pk=None):
        booking = self.get_object()
        if booking.status != 'booked':
            return Response({
                'error': 'Can only cancel booked sessions'
            }, status=status.HTTP_400_BAD_REQUEST)

        booking.status = 'cancelled'
        booking.cancellation_date = timezone.now()
        booking.cancellation_reason = request.data.get('reason', '')
        booking.save()
        return Response({'status': 'Booking cancelled'})

    @action(detail=True, methods=['post'])
    def mark_attendance(self, request, pk=None):
        if not request.user.is_staff:
            return Response({
                'error': 'Permission denied'
            }, status=status.HTTP_403_FORBIDDEN)

        booking = self.get_object()
        if booking.status != 'booked':
            return Response({
                'error': 'Can only mark attendance for booked sessions'
            }, status=status.HTTP_400_BAD_REQUEST)

        booking.status = 'completed'
        booking.attendance_date = timezone.now()
        booking.save()
        return Response({'status': 'Attendance marked'})

class CourseReviewViewSet(viewsets.ModelViewSet):
    queryset = CourseReview.objects.all()
    serializer_class = CourseReviewSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        if self.request.user.is_staff:
            return CourseReview.objects.all()
        return CourseReview.objects.filter(booking__member__user=self.request.user)

    def perform_create(self, serializer):
        booking = CourseBooking.objects.get(id=serializer.validated_data['booking_id'])
        if booking.member.user != self.request.user and not self.request.user.is_staff:
            raise permissions.PermissionDenied("You can only review your own bookings.")
        serializer.save()
