from django.urls import path, include
from rest_framework.routers import DefaultRouter
from . import views

router = DefaultRouter()
router.register('categories', views.EquipmentCategoryViewSet)
router.register('equipment', views.EquipmentViewSet)
router.register('maintenance', views.MaintenanceRecordViewSet)

urlpatterns = [
    path('', include(router.urls)),
]
