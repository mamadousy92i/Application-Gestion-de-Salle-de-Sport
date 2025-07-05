from django.urls import path
from .views import SlideListView

urlpatterns = [
    path('slides/', SlideListView.as_view(), name='slide-list'),
]
