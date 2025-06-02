from django.db import models
from django.core.exceptions import ValidationError
import datetime
from apps.accounts.models import Member  # Import Member for ForeignKey
# ...paste Goal, Exercise, DietPlan models here...

class Goal(models.Model):
    
    GOAL_CHOICES = (
        ('Perte de poids', 'Perte de poids'),
        ('Prise de masse', 'Prise de masse'),
        ('Maintien', 'Maintien'),
    )
    PRIORITY_CHOICES = (
        ('low', 'Basse'),
        ('medium', 'Moyenne'),
        ('high', 'Haute'),
    )

    priority = models.CharField(max_length=6, choices=PRIORITY_CHOICES, default='medium')  # Priorité de l'objectif
    progress_percentage = models.FloatField(default=0.0)  # Suivi de la progression
    name = models.CharField(max_length=100, choices=GOAL_CHOICES)  # Type d'objectif
    user = models.ForeignKey(Member, on_delete=models.CASCADE, related_name='goals')  # Référence au membre
    start_date = models.DateField()  # Date de début
    end_date = models.DateField()  # Date de fin estimée
    description = models.TextField(blank=True)  # Description de l'objectif, optionnelle
    completion_date = models.DateField(null=True, blank=True)
    feedback = models.TextField(blank=True)  # Feedback du coach, optionnel
    recurrence_period = models.IntegerField(choices=[(1, 'Mensuel'), (3, 'Trimestriel'), (6, 'Semestriel')], null=True, blank=True)


    def mark_as_complete(self):
        self.progress_percentage = 100
        self.completion_date = datetime.now().date()
        self.save()
        
    def __str__(self):
        return f"{self.name} - {self.user.user.get_full_name() or self.user.user.username}"

    def is_complete(self):
        """ Vérifie si l'objectif est complet. """
        return self.progress_percentage == 100
    
    def clean(self):
        if self.end_date < self.start_date:
            raise ValidationError("La date de fin ne peut pas être avant la date de début.")



class Exercise(models.Model):

    WORKOUT_CHOICES = (
        ('full_body', 'Full Body'),
        ('upper_body', 'Haut du corps'),
        ('lower_body', 'Bas du corps'),
        ('arms', 'Bras'),
        ('legs', 'Jambes'),
        ('shoulders', 'Epaules'),
        ('chest', 'Pecs'),
        ('cardio', 'Cardio'),
        ('strength', 'Force'),
        # etc.
    )

    name = models.CharField(max_length=100)  # Nom de l'exercice
    category = models.CharField(max_length=20, choices=WORKOUT_CHOICES)  # Catégorie de l'exercice (full body, haut du corps, etc.)
    duration_minutes = models.IntegerField()  # Durée en minutes
    equipment_needed = models.TextField()  # Équipement nécessaire
    goal = models.ForeignKey(Goal, related_name='exercises', on_delete=models.CASCADE)  # Référence à l'objectif

    def __str__(self):
        return f"{self.name} - {self.category}"

class DietPlan(models.Model):
    name = models.CharField(max_length=100)  # Nom du régime
    calories = models.IntegerField()  # Nombre de calories par jour
    meals = models.TextField()  # Description des repas
    goal = models.ForeignKey(Goal, related_name='diet_plans', on_delete=models.CASCADE)  # Référence à l'objectif

    def __str__(self):
        return f"{self.name} - {self.calories} calories"