from rest_framework import permissions


class IsAdminOrManager(permissions.BasePermission):
    """
    Permission personnalisée pour autoriser uniquement les administrateurs et les gérants.
    """

    def has_permission(self, request, view):
        # Vérifie si l'utilisateur est authentifié et a le rôle 'admin' ou 'manager'
        return (
                request.user and
                request.user.is_authenticated and
                (request.user.role in ['admin', 'manager'] or request.user.is_staff)
        )
