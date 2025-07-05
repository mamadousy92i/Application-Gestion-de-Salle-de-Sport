from django.urls import path, include
from rest_framework.routers import DefaultRouter
from . import views

router = DefaultRouter()
router.register('products', views.ProductViewSet)
router.register('sales', views.SaleViewSet)
router.register('sale-items', views.SaleItemViewSet)
router.register('inventory', views.InventoryViewSet)

urlpatterns = [
    path('', include(router.urls)),
]
