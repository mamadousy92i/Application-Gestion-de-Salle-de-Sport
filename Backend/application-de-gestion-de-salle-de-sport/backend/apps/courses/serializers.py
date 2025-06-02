from rest_framework import serializers
from .models import CourseCategory, Course, CourseSchedule, CourseBooking, CourseReview
from apps.accounts.serializers import CoachSerializer, MemberSerializer

class CourseCategorySerializer(serializers.ModelSerializer):
    class Meta:
        model = CourseCategory
        fields = '__all__'

class CourseSerializer(serializers.ModelSerializer):
    category = CourseCategorySerializer(read_only=True)
    category_id = serializers.IntegerField(write_only=True)
    coach = CoachSerializer(read_only=True)
    coach_id = serializers.IntegerField(write_only=True, required=False, allow_null=True)

    class Meta:
        model = Course
        fields = ('id', 'name', 'category', 'category_id', 'description',
                 'coach', 'coach_id', 'max_participants', 'difficulty_level',
                 'duration_minutes', 'equipment_needed', 'created_at', 'updated_at')
        read_only_fields = ('id', 'created_at', 'updated_at')

class CourseScheduleSerializer(serializers.ModelSerializer):
    course = CourseSerializer(read_only=True)
    course_id = serializers.IntegerField(write_only=True)
    available_spots = serializers.SerializerMethodField()

    class Meta:
        model = CourseSchedule
        fields = ('id', 'course', 'course_id', 'start_time', 'end_time',
                 'room', 'is_cancelled', 'cancellation_reason', 'available_spots')
        read_only_fields = ('id',)

    def get_available_spots(self, obj):
        booked_spots = CourseBooking.objects.filter(
            schedule=obj,
            status='booked'
        ).count()
        return obj.course.max_participants - booked_spots

class CourseBookingSerializer(serializers.ModelSerializer):
    member = MemberSerializer(read_only=True)
    member_id = serializers.IntegerField(write_only=True)
    schedule = CourseScheduleSerializer(read_only=True)
    schedule_id = serializers.IntegerField(write_only=True)

    class Meta:
        model = CourseBooking
        fields = ('id', 'member', 'member_id', 'schedule', 'schedule_id',
                 'booking_date', 'status', 'attendance_date',
                 'cancellation_date', 'cancellation_reason')
        read_only_fields = ('id', 'booking_date')

    def validate(self, data):
        schedule = CourseSchedule.objects.get(id=data['schedule_id'])
        if schedule.is_cancelled:
            raise serializers.ValidationError("This course has been cancelled.")
        
        booked_spots = CourseBooking.objects.filter(
            schedule=schedule,
            status='booked'
        ).count()
        
        if booked_spots >= schedule.course.max_participants:
            raise serializers.ValidationError("This course is fully booked.")
        
        return data

class CourseReviewSerializer(serializers.ModelSerializer):
    booking = CourseBookingSerializer(read_only=True)
    booking_id = serializers.IntegerField(write_only=True)

    class Meta:
        model = CourseReview
        fields = ('id', 'booking', 'booking_id', 'rating', 'comment', 'created_at')
        read_only_fields = ('id', 'created_at')

    def validate_booking_id(self, value):
        booking = CourseBooking.objects.get(id=value)
        if booking.status != 'completed':
            raise serializers.ValidationError(
                "You can only review completed courses."
            )
        if CourseReview.objects.filter(booking=booking).exists():
            raise serializers.ValidationError(
                "You have already reviewed this course."
            )
        return value
