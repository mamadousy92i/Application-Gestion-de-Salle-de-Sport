from django.urls import path, include
from rest_framework.routers import DefaultRouter
from . import views

router = DefaultRouter()
router.register('categories', views.CourseCategoryViewSet)
router.register('courses', views.CourseViewSet)
router.register('schedules', views.CourseScheduleViewSet)
router.register('bookings', views.CourseBookingViewSet)
router.register('reviews', views.CourseReviewViewSet)

urlpatterns = [
    path('', include(router.urls)),
]
