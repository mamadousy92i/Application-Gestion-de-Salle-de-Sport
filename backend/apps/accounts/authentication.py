from knox.auth import TokenAuthentication

class CookieTokenAuthentication(TokenAuthentication):
    def get_token(self, request):
        # Cherche le token dans le cookie 'auth_token' au lieu du header
        return request.COOKIES.get('auth_token')
