from django.shortcuts import render
from rest_framework import viewsets, permissions, status, serializers
from rest_framework.decorators import action
from rest_framework.response import Response
from .models import Goal, Exercise, DietPlan
from .serializers import GoalSerializer, ExerciseSerializer, DietPlanSerializer
from apps.accounts.models import Member  # Import Member for ForeignKey
from apps.programs.models import Exercise, DietPlan  # Assurez-vous d'importer correctement ces modèles

class GoalViewSet(viewsets.ModelViewSet):
    queryset = Goal.objects.all()
    serializer_class = GoalSerializer
    permission_classes = [permissions.AllowAny]  # Autoriser tous les utilisateurs

    def perform_create(self, serializer):
        member_id = self.request.data.get('user')
        try:
            member = Member.objects.get(id=member_id)
            # Sauvegarder l'objectif avec le membre
            goal = serializer.save(user=member)  # Création de l'objectif

            # En fonction de l'objectif, lier les exercices associés
            if goal.name == 'muscle_gain':  # Si l'objectif est "Prise de masse"
                exercises = Exercise.objects.filter(category='strength')
            elif goal.name == 'weight_loss':  # Si l'objectif est "Perte de poids"
                exercises = Exercise.objects.filter(category='cardio')
            else:  # Pour "Maintien"
                exercises = Exercise.objects.filter(category='full_body')

            # Associer les exercices à l'objectif
            for exercise in exercises:
                goal.exercises.add(exercise)

            # Ajouter le plan alimentaire lié à l'objectif
            if goal.name == 'muscle_gain':
                diet_plan = DietPlan.objects.filter(name='Muscle Gain Plan')
            elif goal.name == 'weight_loss':
                diet_plan = DietPlan.objects.filter(name='Weight Loss Plan')
            else:
                diet_plan = DietPlan.objects.filter(name='Maintenance Plan')

            # Associer le plan alimentaire à l'objectif
            for plan in diet_plan:
                goal.diet_plans.add(plan)

        except Member.DoesNotExist:
            raise serializers.ValidationError(f"Le membre avec l'ID {member_id} n'existe pas.")
    
    @action(detail=True, methods=['get'])
    def exercises(self, request, pk=None):
        """
        Récupérer tous les exercices associés à un objectif
        """
        goal = self.get_object()
        exercises = Exercise.objects.filter(goal=goal)
        serializer = ExerciseSerializer(exercises, many=True)
        return Response(serializer.data)
    
    @action(detail=True, methods=['get'])
    def diet_plans(self, request, pk=None):
        """
        Récupérer tous les régimes associés à un objectif
        """
        goal = self.get_object()
        diet_plans = DietPlan.objects.filter(goal=goal)
        serializer = DietPlanSerializer(diet_plans, many=True)
        return Response(serializer.data)
    
    @action(detail=True, methods=['post'])
    def add_exercise(self, request, pk=None):
        """
        Ajouter un exercice à un objectif spécifique
        """
        goal = self.get_object()
        
        # Ajouter automatiquement l'ID de l'objectif aux données
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
        
        # Ajouter automatiquement l'ID de l'objectif aux données
        data = request.data.copy()
        data['goal'] = goal.id
        
        serializer = DietPlanSerializer(data=data)
        if serializer.is_valid():
            diet_plan = serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class ExerciseViewSet(viewsets.ModelViewSet):
    """
    ViewSet pour gérer les exercices (Exercise).
    Expose les actions CRUD pour les exercices associés aux objectifs.
    """
    queryset = Exercise.objects.all()
    serializer_class = ExerciseSerializer
    permission_classes = [permissions.AllowAny]  # Autoriser tous les utilisateurs

class DietPlanViewSet(viewsets.ModelViewSet):
    """
    ViewSet pour gérer les régimes (DietPlan).
    Expose les actions CRUD pour les régimes associés aux objectifs.
    """
    queryset = DietPlan.objects.all()
    serializer_class = DietPlanSerializer
    permission_classes = [permissions.AllowAny]  # Autoriser tous les utilisateurs
