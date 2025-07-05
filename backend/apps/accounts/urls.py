from django.urls import path, include
from rest_framework.routers import DefaultRouter
from knox import views as knox_views
from . import views
from ..subscriptions.views import CookieLogoutView

router = DefaultRouter()
router.register('users', views.UserViewSet)
router.register('coaches', views.CoachViewSet)
router.register('members', views.MemberViewSet)

urlpatterns = [
    path('', include(router.urls)),
    path('login/', views.UserViewSet.as_view({'post': 'login'}), name='login'),
    path('logout/', CookieLogoutView.as_view(), name='logout'),
    path('logoutall/', knox_views.LogoutAllView.as_view(), name='logoutall'),
]
