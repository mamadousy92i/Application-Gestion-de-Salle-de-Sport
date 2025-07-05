# apps/programs/views.py
from django.shortcuts import render
from rest_framework import viewsets, permissions, status, serializers
from rest_framework.decorators import action
from rest_framework.response import Response
from .models import Goal, Exercise, DietPlan
from .serializers import GoalSerializer, ExerciseSerializer, DietPlanSerializer
from apps.accounts.models import Member

class GoalViewSet(viewsets.ModelViewSet):
    serializer_class = GoalSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        return Goal.objects.filter(user__user=self.request.user)

    def perform_create(self, serializer):
        try:
            member = Member.objects.get(user=self.request.user)
            goal = serializer.save(user=member)

            if goal.name == 'Prise de masse':
                exercises = Exercise.objects.filter(category='strength')
                diet_plan = DietPlan.objects.filter(name='Muscle Gain Plan')
            elif goal.name == 'Perte de poids':
                exercises = Exercise.objects.filter(category='cardio')
                diet_plan = DietPlan.objects.filter(name='Weight Loss Plan')
            else:
                exercises = Exercise.objects.filter(category='full_body')
                diet_plan = DietPlan.objects.filter(name='Maintenance Plan')

            goal.exercises.set(exercises)
            goal.diet_plans.set(diet_plan)

        except Member.DoesNotExist:
            raise serializers.ValidationError("Utilisateur non trouvé.")

    @action(detail=True, methods=['get'])
    def exercises(self, request, pk=None):
        """
        Récupérer tous les exercices associés à un objectif
        """
        goal = self.get_object()
        exercises = goal.exercises.all()
        serializer = ExerciseSerializer(exercises, many=True)
        return Response(serializer.data)

    @action(detail=True, methods=['get'])
    def diet_plans(self, request, pk=None):
        """
        Récupérer tous les régimes associés à un objectif
        """
        goal = self.get_object()
        diet_plans = goal.diet_plans.all()
        serializer = DietPlanSerializer(diet_plans, many=True)
        return Response(serializer.data)

    @action(detail=True, methods=['post'])
    def add_exercise(self, request, pk=None):
        """
        Ajouter un exercice à un objectif spécifique
        """
        goal = self.get_object()
        data = request.data.copy()
        data['goal'] = goal.id

        serializer = ExerciseSerializer(data=data)
        if serializer.is_valid():
            exercise = serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    @action(detail=True, methods=['post'])
    def add_diet_plan(self, request, pk=None):
        """
        Ajouter un régime à un objectif spécifique
        """
        goal = self.get_object()
        data = request.data.copy()
        data['goal'] = goal.id

        serializer = DietPlanSerializer(data=data)
        if serializer.is_valid():
            diet_plan = serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    @action(detail=False, methods=['patch'], url_path='change-goal')
    def change_goal(self, request):
        """
        Change l'objectif actif de l'utilisateur connecté.
        Exemple de payload : {"name": "Perte de poids"}
        """
        try:
            member = Member.objects.get(user=self.request.user)
        except Member.DoesNotExist:
            return Response({"error": "Utilisateur non trouvé."}, status=status.HTTP_404_NOT_FOUND)

        existing_goal = Goal.objects.filter(user=member).first()
        serializer = self.get_serializer(data=request.data, instance=existing_goal, partial=True)
        if not serializer.is_valid():
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

        if existing_goal:
            goal = serializer.save()
        else:
            goal = serializer.save(user=member)

        Goal.objects.filter(user=member).exclude(id=goal.id).delete()

        if goal.name == 'Prise de masse':
            exercises = Exercise.objects.filter(category='strength')
            diet_plan = DietPlan.objects.filter(name='Muscle Gain Plan')
        elif goal.name == 'Perte de poids':
            exercises = Exercise.objects.filter(category='cardio')
            diet_plan = DietPlan.objects.filter(name='Weight Loss Plan')
        else:
            exercises = Exercise.objects.filter(category='full_body')
            diet_plan = DietPlan.objects.filter(name='Maintenance Plan')
        goal.exercises.set(exercises)
        goal.diet_plans.set(diet_plan)

        return Response(serializer.data, status=status.HTTP_200_OK)

class ExerciseViewSet(viewsets.ModelViewSet):
    """
    ViewSet pour gérer les exercices (Exercise).
    Expose les actions CRUD pour les exercices associés aux objectifs.
    """
    queryset = Exercise.objects.all()
    serializer_class = ExerciseSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        return Exercise.objects.filter(goal__user__user=self.request.user)

class DietPlanViewSet(viewsets.ModelViewSet):
    """
    ViewSet pour gérer les régimes (DietPlan).
    Expose les actions CRUD pour les régimes associés aux objectifs.
    """
    queryset = DietPlan.objects.all()
    serializer_class = DietPlanSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        return DietPlan.objects.filter(goal__user__user=self.request.user)