from django.contrib import admin
from django.contrib.auth.admin import UserAdmin as BaseUserAdmin
from .models import User, Coach, Member

@admin.register(User)
class CustomUserAdmin(BaseUserAdmin):
    model = User

    # Champs affichés dans la liste des utilisateurs
    list_display = ('id','email', 'username', 'first_name', 'last_name', 'role', 'is_staff', 'is_active')
    list_filter = ('role', 'is_staff', 'is_active')

    # Champs visibles en modification
    fieldsets = BaseUserAdmin.fieldsets + (
        (None, {
            'fields': ('role', 'phone_number', 'address', 'date_of_birth', 'gender','profile_picture','poids','taille','imc'),
        }),
    )

    # Pour créer un utilisateur
    add_fieldsets = BaseUserAdmin.add_fieldsets + (
        (None, {
            'fields': ('email', 'role', 'phone_number', 'address', 'date_of_birth', 'profile_picture'),
        }),
    )

    search_fields = ('email', 'username', 'first_name', 'last_name')
    ordering = ('email',)

# Enregistre aussi Coach et Member si tu veux les modifier dans l’admin
@admin.register(Coach)
class CoachAdmin(admin.ModelAdmin):
    list_display = ('user', 'specialization', 'hourly_rate')

@admin.register(Member)
class MemberAdmin(admin.ModelAdmin):
    list_display = ('id','user', 'emergency_contact', 'is_active')
