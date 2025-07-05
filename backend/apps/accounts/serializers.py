from django.contrib.auth import authenticate
from rest_framework import serializers
from .models import User, Coach, Member

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ('id', 'email', 'username', 'first_name', 'last_name','gender', 'age', 'taille', 'poids','imc', 'role',
                  'phone_number', 'address', 'date_of_birth', 'profile_picture', 'password')
        read_only_fields = ('id', 'date_of_birth','imc')  # Retirer 'username' de read_only_fields
        extra_kwargs = {
            'password': {'write_only': True},
            'profile_picture': {'required': False, 'allow_null': True},
            'taille': {'required': False, 'allow_null': True},
            'poids': {'required': False, 'allow_null': True},
            'role': {'required': False, 'allow_null': True},
        }

    def create(self, validated_data):
        # Extraire les champs pour create_user
        username = validated_data.pop('username')
        email = validated_data.pop('email')
        password = validated_data.pop('password')
        # Utiliser la valeur par défaut 'member' si role n'est pas fourni
        role = validated_data.pop('role', 'member')

        # Créer l'utilisateur avec les champs requis
        user = User.objects.create_user(
            username=username,
            email=email,
            password=password,
            role=role,
            **validated_data  # Passer les autres champs (first_name, last_name, etc.)
        )
        return user

    def update(self, instance, validated_data):
        validated_data.pop('id', None)
        validated_data.pop('username', None)  # Empêcher la modification de username
        validated_data.pop('date_of_birth', None)
        validated_data.pop('role', None)  # Empêcher la modification du role
        return super().update(instance, validated_data)

class LoginSerializer(serializers.Serializer):
    email = serializers.EmailField()
    password = serializers.CharField()

    def validate(self, data):
        email = data.get('email')
        password = data.get('password')

        try:
            user = User.objects.get(email=email)
        except User.DoesNotExist:
            raise serializers.ValidationError("Identifiants incorrects.")

        user = authenticate(username=email, password=password)
        if user and user.is_active:
            return user
        raise serializers.ValidationError("Identifiants incorrects.")

class CoachSerializer(serializers.ModelSerializer):
    user = UserSerializer()

    class Meta:
        model = Coach
        fields = ('id', 'user', 'specialization', 'bio', 'certifications',
                  'availability', 'hourly_rate')
        read_only_fields = ('id',)

    def create(self, validated_data):
        user_data = validated_data.pop('user')
        user_data['role'] = 'coach'
        user = User.objects.create_user(
            username=user_data.pop('username'),
            email=user_data.pop('email'),
            password=user_data.pop('password'),
            **user_data
        )
        coach = Coach.objects.create(user=user, **validated_data)
        return coach

class MemberSerializer(serializers.ModelSerializer):
    user = UserSerializer()

    class Meta:
        model = Member
        fields = ('id', 'user', 'emergency_contact', 'medical_conditions',
                  'membership_start_date', 'membership_end_date', 'is_active')
        read_only_fields = ('id', 'membership_start_date')

    def create(self, validated_data):
        user_data = validated_data.pop('user')
        user_data['role'] = 'member'
        user = User.objects.create_user(
            username=user_data.pop('username'),
            email=user_data.pop('email'),
            password=user_data.pop('password'),
            **user_data
        )
        member = Member.objects.create(user=user, **validated_data)
        return member

