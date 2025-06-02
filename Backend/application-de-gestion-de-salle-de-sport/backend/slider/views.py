from django.shortcuts import render

# Create your views here.
from rest_framework.generics import ListAPIView
from rest_framework.permissions import AllowAny

from .models import Slide
from .serializers import SlideSerializer

class SlideListView(ListAPIView):
    permission_classes = [AllowAny]
    queryset = Slide.objects.filter(is_active=True).order_by('created_at')
    serializer_class = SlideSerializer
