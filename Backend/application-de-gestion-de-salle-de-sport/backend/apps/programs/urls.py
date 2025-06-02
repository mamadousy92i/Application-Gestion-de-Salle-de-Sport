from django.urls import path, include
from rest_framework.routers import DefaultRouter
from . import views

router = DefaultRouter()
router.register('goals', views.GoalViewSet)
router.register('exercises', views.ExerciseViewSet)
router.register('diet-plans', views.DietPlanViewSet)

urlpatterns = [
    path('', include(router.urls)),
]
