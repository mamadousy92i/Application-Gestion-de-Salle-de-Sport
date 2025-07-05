# apps/programs/urls.py
from django.urls import path, include
from rest_framework.routers import DefaultRouter
from . import views

router = DefaultRouter()
router.register('goals', views.GoalViewSet, basename='goals')
router.register('exercises', views.ExerciseViewSet, basename='exercises')
router.register('diet-plans', views.DietPlanViewSet, basename='diet-plans')

urlpatterns = [
    path('', include(router.urls)),
]