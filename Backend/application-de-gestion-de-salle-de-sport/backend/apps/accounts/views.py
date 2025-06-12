
from rest_framework import viewsets, permissions, status
from rest_framework.decorators import action
from rest_framework.response import Response
from knox.models import AuthToken

from .models import User, Coach, Member
from .serializers import UserSerializer, CoachSerializer, MemberSerializer, LoginSerializer
from ..programs.models import Goal
from ..programs.serializers import GoalSerializer


class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all()
    serializer_class = UserSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        return User.objects.filter(id=self.request.user.id)

    def create(self, request, *args, **kwargs):
        serializer = self.get_serializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        user = serializer.save()

        # Créer automatiquement un Member si le rôle est 'member'
        if user.role == 'member':
            Member.objects.create(
                user=user,
                emergency_contact=''  # Valeur par défaut
            )

        return Response({
            "user": UserSerializer(user).data
        }, status=status.HTTP_201_CREATED)

    def get_permissions(self):
        if self.action in ['create', 'login']:
            return [permissions.AllowAny()]
        return super().get_permissions()

    @action(detail=False, methods=['post'])
    def login(self, request):
        serializer = LoginSerializer(data=request.data)
        serializer.is_valid(raise_exception=True)
        user = serializer.validated_data
        token = AuthToken.objects.create(user)[1]

        # Récupération du membre lié à cet utilisateur
        try:
            member = user.member  # suppose que user est lié à un modèle Member (OneToOneField)
            member_id = member.id
        except Exception:
            member_id = None

        response = Response({
            "user": UserSerializer(user).data,
            "member_id": member_id,
            "message": "Connexion réussie"
        })

        response.set_cookie(
            key='auth_token',
            value=token,
            httponly=True,
            secure=False,  # True en prod avec HTTPS
            samesite='Lax',
            max_age=3600 * 24 * 7
        )

        return response

    @action(detail=False, methods=['get', 'put', 'patch', 'delete'])
    def me(self, request):
        user = request.user

        if request.method == 'GET':
            serializer = UserSerializer(user)
            data = serializer.data.copy()

            # Calcul de l'IMC
            if user.taille and user.poids:
                taille_en_cm = user.taille * 100
                data['imc'] = round(user.poids / (taille_en_cm / 100) ** 2, 2)
            else:
                data['imc'] = None

            try:
                member = Member.objects.get(user=user)
                goals = Goal.objects.filter(user=member)
                data["goals"] = GoalSerializer(goals, many=True).data
                data["member_id"] = member.id  # Ajoute l'ID du membre ici
            except Member.DoesNotExist:
                data["goals"] = []
                data["member_id"] = None

            return Response(data)

        elif request.method in ['PUT', 'PATCH']:
            partial = (request.method == 'PATCH')
            serializer = UserSerializer(user, data=request.data, partial=partial)
            serializer.is_valid(raise_exception=True)
            serializer.save()
            return Response(serializer.data, status=status.HTTP_200_OK)

        elif request.method == 'DELETE':
            user.delete()
            return Response(status=status.HTTP_204_NO_CONTENT)
        return None


class CoachViewSet(viewsets.ModelViewSet):
    queryset = Coach.objects.all()
    serializer_class = CoachSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        if self.request.user.is_staff:
            return Coach.objects.all()
        return Coach.objects.filter(user=self.request.user)

    @action(detail=True, methods=['get'])
    def availability(self, request, pk=None):
        coach = self.get_object()
        return Response({
            'availability': coach.availability
        })

class MemberViewSet(viewsets.ModelViewSet):
    queryset = Member.objects.all()
    serializer_class = MemberSerializer
    permission_classes = [permissions.IsAuthenticated]

    def get_queryset(self):
        if self.request.user.is_staff:
            return Member.objects.all()
        return Member.objects.filter(user=self.request.user)

    @action(detail=True, methods=['post'])
    def toggle_active(self, request, pk=None):
        member = self.get_object()
        member.is_active = not member.is_active
        member.save()
        return Response({
            'status': 'success',
            'is_active': member.is_active
        })

    @action(detail=False, methods=['get'])
    def my_goals(self, request):
        try:
            member = Member.objects.get(user=request.user)
        except Member.DoesNotExist:
            return Response({"detail": "Aucun profil membre associé."}, status=404)

        goals = Goal.objects.filter(user=member)
        serializer = GoalSerializer(goals, many=True)
        return Response(serializer.data)
