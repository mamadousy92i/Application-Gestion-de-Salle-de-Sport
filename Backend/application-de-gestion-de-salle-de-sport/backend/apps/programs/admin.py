from django.contrib import admin
from .models import Goal, Exercise, DietPlan


@admin.register(Goal)
class GoalAdmin(admin.ModelAdmin):
    list_display = ('id', 'name', 'user', 'priority', 'progress_percentage', 'start_date', 'end_date', 'is_complete')
    list_filter = ('name', 'priority', 'start_date', 'end_date')
    search_fields = ('user__user__username', 'user__user__first_name', 'user__user__last_name', 'description')
    date_hierarchy = 'start_date'
    readonly_fields = ('completion_date',)

    def is_complete(self, obj):
        return obj.is_complete()

    is_complete.boolean = True  # Pour afficher une icône ✅❌ dans l'admin
    is_complete.short_description = 'Complet ?'


@admin.register(Exercise)
class ExerciseAdmin(admin.ModelAdmin):
    list_display = ('id', 'name', 'category', 'duration_minutes', 'goal')
    list_filter = ('category',)
    search_fields = ('name', 'equipment_needed', 'goal__name')


@admin.register(DietPlan)
class DietPlanAdmin(admin.ModelAdmin):
    list_display = ('id', 'name', 'calories', 'goal')
    search_fields = ('name', 'meals', 'goal__name')
