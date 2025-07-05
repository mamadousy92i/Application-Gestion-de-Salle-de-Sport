from rest_framework import serializers
from .models import Goal, Exercise, DietPlan
from apps.accounts.models import Member

class ExerciseSerializer(serializers.ModelSerializer):
    class Meta:
        model = Exercise
        fields = '__all__'

class DietPlanSerializer(serializers.ModelSerializer):
    class Meta:
        model = DietPlan
        fields = '__all__'

class GoalSerializer(serializers.ModelSerializer):
    exercises = ExerciseSerializer(many=True, read_only=True)
    diet_plans = DietPlanSerializer(many=True, read_only=True)

    class Meta:
        model = Goal
        fields = (
            'id', 'name', 'user', 'start_date', 'end_date', 'description',
            'exercises', 'diet_plans', 'priority', 'progress_percentage',
            'completion_date', 'feedback'
        )
        read_only_fields = ('id',)
    def validate_user(self, value):
        """
        Vérifier que le membre existe
        """
        try:
            member = Member.objects.get(id=value.id)
            return member
        except Member.DoesNotExist:
            raise serializers.ValidationError(f"Le membre avec l'ID {value.id} n'existe pas.")

    def to_representation(self, instance):
        """
        Ajouter des informations supplémentaires sur le membre
        """
        representation = super().to_representation(instance)
        try:
            member = instance.user
            representation['member_info'] = {
                'id': member.id,
                'user_id': member.user.id,
                'username': member.user.username,
                'full_name': member.user.get_full_name() or member.user.username
            }
        except:
            representation['member_info'] = None
        
        return representation