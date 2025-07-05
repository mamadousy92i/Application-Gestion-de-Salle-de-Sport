from django.db import models
from apps.accounts.models import Coach, Member

class CourseCategory(models.Model):
    name = models.CharField(max_length=100)
    description = models.TextField()
    
    def __str__(self):
        return self.name
    
    class Meta:
        verbose_name_plural = "Course Categories"

class Course(models.Model):
    DIFFICULTY_CHOICES = (
        ('beginner', 'Débutant'),
        ('intermediate', 'Intermédiaire'),
        ('advanced', 'Avancé'),
    )
    
    name = models.CharField(max_length=100)
    category = models.ForeignKey(CourseCategory, on_delete=models.PROTECT)
    description = models.TextField()
    coach = models.ForeignKey(Coach, on_delete=models.SET_NULL, null=True)
    max_participants = models.IntegerField()
    difficulty_level = models.CharField(max_length=20, choices=DIFFICULTY_CHOICES)
    duration_minutes = models.IntegerField()
    equipment_needed = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True)
    
    def __str__(self):
        return f"{self.name} - {self.get_difficulty_level_display()}"

class CourseSchedule(models.Model):
    course = models.ForeignKey(Course, on_delete=models.CASCADE)
    start_time = models.DateTimeField()
    end_time = models.DateTimeField()
    room = models.CharField(max_length=50)
    is_cancelled = models.BooleanField(default=False)
    cancellation_reason = models.TextField(blank=True)
    
    def __str__(self):
        return f"{self.course.name} - {self.start_time.strftime('%d/%m/%Y %H:%M')}"

class CourseBooking(models.Model):
    STATUS_CHOICES = (
        ('booked', 'Réservé'),
        ('cancelled', 'Annulé'),
        ('completed', 'Terminé'),
        ('no_show', 'Non présenté'),
    )
    
    member = models.ForeignKey(Member, on_delete=models.CASCADE)
    schedule = models.ForeignKey(CourseSchedule, on_delete=models.CASCADE)
    booking_date = models.DateTimeField(auto_now_add=True)
    status = models.CharField(max_length=20, choices=STATUS_CHOICES, default='booked')
    attendance_date = models.DateTimeField(null=True, blank=True)
    cancellation_date = models.DateTimeField(null=True, blank=True)
    cancellation_reason = models.TextField(blank=True)
    
    def __str__(self):
        return f"{self.member.user.get_full_name()} - {self.schedule.course.name}"

class CourseReview(models.Model):
    booking = models.OneToOneField(CourseBooking, on_delete=models.CASCADE)
    rating = models.IntegerField(choices=[(i, i) for i in range(1, 6)])
    comment = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)
    
    def __str__(self):
        return f"{self.booking.member.user.get_full_name()} - {self.booking.schedule.course.name} - {self.rating}★"
