# 🏋️‍♂️ Application de Gestion de Salle de Sport

<div align="center">
  <img src="https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=white" alt="Python">
  <img src="https://img.shields.io/badge/Django-092E20?style=for-the-badge&logo=django&logoColor=white" alt="Django">
  <img src="https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Android">
  <img src="https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white" alt="Java">
  <img src="https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL">
  <img src="https://img.shields.io/badge/ngrok-1F1E37?style=for-the-badge&logo=ngrok&logoColor=white" alt="ngrok">
</div>

<br>

Une application mobile complète pour la gestion d'une salle de sport avec backend Django REST API et frontend Android natif.

## 📋 Sommaire

- [📱 Aperçu](#-aperçu)
- [✨ Fonctionnalités Principales](#-fonctionnalités-principales)
- [🏗️ Architecture](#️-architecture)
- [📂 Structure du Projet](#-structure-du-projet)
- [🚀 Installation et Configuration](#-installation-et-configuration)
  - [Prérequis](#prérequis)
  - [Configuration Backend](#configuration-backend)
  - [Configuration Frontend](#configuration-frontend)
- [🔧 API Endpoints](#-api-endpoints)
- [📱 Fonctionnalités Détaillées](#-fonctionnalités-détaillées)
  - [🔐 Système d'Authentification](#-système-dauthentification)
  - [💳 Gestion des Abonnements](#-gestion-des-abonnements)
  - [🏋️‍♂️ Programmes Fitness Personnalisés](#️️-programmes-fitness-personnalisés)
  - [🍎 Plans Nutritionnels Interactifs](#-plans-nutritionnels-interactifs)
  - [📊 Suivi des Performances](#-suivi-des-performances)
- [🎨 Design et Interface](#-design-et-interface)
- [🔄 Flux d'Application](#-flux-dapplication)
- [🛠️ Technologies Utilisées](#️-technologies-utilisées)
- [🔒 Sécurité](#-sécurité)
- [📈 Performance et Optimisation](#-performance-et-optimisation)
- [🧪 Tests et Qualité](#-tests-et-qualité)
- [🚀 Déploiement](#-déploiement)
- [🤝 Contribution](#-contribution)
- [📄 Licence](#-licence)
- [👨‍💻 Développeur](#-développeur)
- [📞 Contact et Support](#-contact-et-support)

## 📱 Aperçu

Cette application offre une solution complète pour la gestion d'une salle de sport moderne, incluant la gestion des membres, des abonnements, des programmes d'entraînement personnalisés, et bien plus encore.

### ✨ Fonctionnalités Principales

- **Authentification sécurisée** avec gestion des rôles (Admin, Manager, Coach, Membre)
- **Gestion des abonnements** avec plans personnalisables et paiements
- **Programmes fitness personnalisés** selon les objectifs (Prise de masse, Perte de poids, Maintien)
- **Plans nutritionnels** adaptés à chaque objectif
- **Suivi des performances** avec mesures corporelles et progression
- **Interface moderne** avec animations fluides et design Material

## 🏗️ Architecture

### Backend - Django REST API
- **Framework**: Django 5.1.5 avec Django REST Framework
- **Base de données**: PostgreSQL
- **Authentification**: Django Knox (tokens dans cookies)
- **Architecture**: API REST avec sérialisation JSON

### Frontend - Android Natif
- **Langage**: Java
- **Interface**: Material Design avec animations personnalisées
- **Communication**: Retrofit + OkHttp avec gestion des cookies
- **Architecture**: MVP avec adapters RecyclerView

## 📂 Structure du Projet

```
gym-app/
├── backend/                          # Django REST API
│   ├── core/                        # Configuration principale
│   │   ├── settings.py             # Configuration Django
│   │   ├── urls.py                 # URLs principales
│   │   └── wsgi.py                 # WSGI configuration
│   ├── apps/
│   │   ├── accounts/               # Gestion des utilisateurs
│   │   │   ├── models.py          # User, Coach, Member
│   │   │   ├── views.py           # API endpoints auth
│   │   │   ├── serializers.py     # Sérialisation données
│   │   │   └── authentication.py  # Auth par cookies
│   │   ├── courses/               # Gestion des cours
│   │   │   ├── models.py         # Course, Schedule, Booking
│   │   │   ├── views.py          # API endpoints cours
│   │   │   └── serializers.py    # Sérialisation cours
│   │   ├── programs/             # Programmes fitness
│   │   │   ├── models.py        # Goal, Exercise, DietPlan
│   │   │   ├── views.py         # API programmes
│   │   │   └── serializers.py   # Sérialisation programmes
│   │   ├── subscriptions/       # Gestion abonnements
│   │   │   ├── models.py       # SubscriptionPlan, Subscription, Payment
│   │   │   ├── views.py        # API abonnements
│   │   │   └── serializers.py  # Sérialisation abonnements
│   │   ├── equipment/          # Gestion équipements
│   │   └── sales/             # Gestion ventes
│   └── slider/               # Carousel d'images
└── frontend/                 # Application Android
    ├── app/src/main/java/com/example/salledesport/
    │   ├── activities/                    # Activités principales
    │   │   ├── LoginActivity.java        # Connexion
    │   │   ├── RegisterActivity.java     # Inscription
    │   │   ├── Home.java                 # Écran d'accueil
    │   │   ├── ProfileActivity.java      # Profil utilisateur
    │   │   ├── Abonnement.java          # Liste abonnements
    │   │   └── PaymentActivity.java     # Paiements
    │   ├── api/                          # Services API
    │   │   ├── RetrofitClient.java      # Client HTTP
    │   │   ├── AuthService.java         # Service auth
    │   │   ├── ApiService.java          # Services généraux
    │   │   └── ProfileService.java      # Service profil
    │   ├── model/                       # Modèles de données
    │   │   ├── Utilisateur.java        # Modèle utilisateur
    │   │   ├── Subscription.java       # Modèle abonnement
    │   │   ├── Goal.java               # Modèle objectif
    │   │   └── Payment.java            # Modèle paiement
    │   ├── adapters/                    # Adapters RecyclerView
    │   │   ├── SubscriptionAdapter.java
    │   │   ├── PaymentAdapter.java
    │   │   └── GoalAdapter.java
    │   ├── exercise/                    # Modules d'exercices
    │   │   ├── prisedemasse/           # Exercices prise de masse
    │   │   ├── pertedepoids/           # Exercices perte de poids
    │   │   └── maintien/               # Exercices maintien
    │   ├── validation/                  # Activités de validation
    │   └── utils/                      # Utilitaires
    │       ├── BaseActivity.java      # Activité de base
    │       └── DateUtils.java         # Utilitaires dates
    └── app/src/main/res/              # Ressources Android
        ├── layout/                    # Layouts XML
        ├── drawable/                  # Images et icônes
        ├── values/                    # Couleurs, strings, styles
        └── anim/                     # Animations
```

## 🚀 Installation et Configuration

### Prérequis

**Backend:**
- Python 3.8+
- PostgreSQL 12+
- pip ou pipenv

**Frontend:**
- Android Studio 4.0+
- JDK 8+
- SDK Android 21+ (Android 5.0)

### Configuration Backend

1. **Cloner le repository**
```bash
git clone <repository-url>
cd gym-app/backend
```

2. **Créer l'environnement virtuel**
```bash
python -m venv venv
source venv/bin/activate  # Linux/Mac
# ou
venv\Scripts\activate     # Windows
```

3. **Installer les dépendances**
```bash
pip install -r requirements.txt
```

4. **Configuration base de données**
```bash
# Créer une base PostgreSQL
createdb gym_db

# Configurer les variables d'environnement
cp .env.example .env
```

5. **Variables d'environnement (.env)**
```env
DB_NAME=gym_db
DB_USER=your_db_user
DB_PASSWORD=your_db_password
DB_HOST=localhost
DB_PORT=5432
SECRET_KEY=your-secret-key-here
DEBUG=True
```

6. **Migrations et données initiales**
```bash
python manage.py makemigrations
python manage.py migrate
python manage.py createsuperuser
python manage.py collectstatic
```

7. **Lancer le serveur**
```bash
python manage.py runserver
```

Le backend sera accessible sur `http://localhost:8000`

### Configuration Frontend

1. **Ouvrir dans Android Studio**
```bash
cd gym-app/frontend
# Ouvrir le dossier dans Android Studio
```

2. **Configuration réseau**

Modifier l'URL dans `RetrofitClient.java`:
```java
// Pour émulateur Android
private static final String BASE_URL = "http://10.0.2.2:8000/";

// Pour appareil physique (remplacer par votre IP)
private static final String BASE_URL = "http://192.168.1.XXX:8000/";

// Pour production
private static final String BASE_URL = "https://your-domain.com/";
```

3. **Build et installation**
```bash
./gradlew assembleDebug
# ou depuis Android Studio: Build > Build APK
```

## 🔧 API Endpoints

### Authentification
```http
POST /api/accounts/login/           # Connexion
POST /api/accounts/users/           # Inscription
GET  /api/accounts/users/me/        # Profil utilisateur
PATCH /api/accounts/users/me/       # Mise à jour profil
DELETE /api/accounts/users/me/      # Suppression compte
```

### Abonnements
```http
GET  /api/subscriptions/plans/                    # Plans disponibles
POST /api/subscriptions/subscriptions/           # Créer abonnement
GET  /api/subscriptions/subscriptions/           # Mes abonnements
POST /api/subscriptions/subscriptions/{id}/renew/ # Renouveler
POST /api/subscriptions/subscriptions/{id}/cancel/ # Annuler
```

### Paiements
```http
GET  /api/subscriptions/payments/     # Historique paiements
POST /api/subscriptions/payments/     # Effectuer paiement
```

### Programmes Fitness
```http
GET  /api/programs/goals/             # Mes objectifs
POST /api/programs/goals/             # Créer objectif
PATCH /api/programs/goals/change-goal/ # Changer objectif
GET  /api/programs/exercises/         # Exercices
GET  /api/programs/diet-plans/        # Plans nutritionnels
```

### Cours et Réservations
```http
GET  /api/courses/courses/            # Cours disponibles
GET  /api/courses/schedules/          # Planning cours
POST /api/courses/bookings/           # Réserver cours
```

## 📱 Fonctionnalités Détaillées

### 🔐 Système d'Authentification
- **Inscription/Connexion** avec validation
- **Gestion des rôles** : Admin, Manager, Coach, Membre
- **Authentification par cookies** sécurisée
- **Gestion de session** automatique

### 💳 Gestion des Abonnements
- **Plans personnalisables** avec tarifs et durées
- **Souscription en ligne** avec paiement intégré
- **Suivi des abonnements** avec barres de progression
- **Renouvellement automatique** optionnel
- **Historique des paiements** détaillé

### 🏋️‍♂️ Programmes Fitness Personnalisés

#### Objectifs Disponibles:
1. **Prise de Masse**
   - Exercices de force (Développé couché, Squats, Soulevé de terre)
   - Exercices musculaires ciblés
   - Plan nutritionnel hypercalorique (3500+ kcal)

2. **Perte de Poids**
   - Exercices cardio intensifs
   - Circuits training brûle-graisse
   - Plan nutritionnel hypocalorique (1500 kcal)

3. **Maintien de Forme**
   - Exercices d'équilibre (Yoga, étirements)
   - Programme mixte équilibré
   - Plan nutritionnel équilibré (2100 kcal)

### 🍎 Plans Nutritionnels Interactifs
- **Suivi des calories** avec compteurs animés
- **Rappels d'hydratation** automatiques
- **Suivi fruits & légumes** gamifié
- **Repas personnalisables** avec validation
- **Encouragements** contextuels

### 📊 Suivi des Performances
- **Mesures corporelles** (poids, taille, IMC)
- **Progression des objectifs** avec pourcentages
- **Historique détaillé** des entraînements
- **Statistiques visuelles** avec graphiques

## 🎨 Design et Interface

### Thème Visuel
- **Design moderne** avec Material Design 3
- **Couleurs dynamiques** selon les objectifs:
  - 🟠 Orange pour Prise de masse
  - 🔴 Rouge pour Perte de poids  
  - 🔵 Bleu pour Maintien
- **Animations fluides** et micro-interactions
- **Mode Edge-to-Edge** immersif

### Fonctionnalités UI/UX
- **Navigation bottom** avec indicateurs actifs
- **Transitions animées** entre écrans
- **Feedback visuel** pour toutes les actions
- **Estados de chargement** avec progress bars
- **Messages d'encouragement** contextuels

## 🔄 Flux d'Application

### Parcours Utilisateur Complet

1. **Onboarding**
   - Splash screen avec carousel d'images
   - Choix de l'objectif fitness
   - Validation avec animations

2. **Authentification**
   - Inscription en 2 étapes (infos personnelles + compte)
   - Connexion avec sauvegarde automatique
   - Récupération de session

3. **Configuration Profil**
   - Saisie des mesures corporelles
   - Upload photo de profil
   - Sélection des préférences

4. **Programme Personnalisé**
   - Génération automatique selon l'objectif
   - Exercices détaillés avec instructions
   - Plan nutritionnel adapté

5. **Suivi et Progression**
   - Validation des repas et exercices
   - Mise à jour des mesures
   - Visualisation des progrès

## 🛠️ Technologies Utilisées

### Backend
- **Django 5.1.5** - Framework web Python
- **Django REST Framework** - API REST
- **PostgreSQL** - Base de données relationnelle
- **Django Knox** - Authentification par tokens
- **Pillow** - Traitement d'images
- **django-cors-headers** - Gestion CORS

### Frontend Android
- **Java** - Langage principal
- **Material Design Components** - UI moderne
- **Retrofit** - Client HTTP REST
- **OkHttp** - Gestion réseau avancée
- **Glide** - Chargement d'images optimisé
- **RecyclerView** - Listes performantes
- **ViewPager2** - Carousel et navigation

### Outils et Dépendances
```xml
<!-- Frontend Android -->
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.github.bumptech.glide:glide:4.14.2'
implementation 'com.google.android.material:material:1.8.0'
implementation 'androidx.recyclerview:recyclerview:1.3.0'
```

```python
# Backend Django
Django==5.1.5
djangorestframework==3.14.0
django-knox==4.2.0
psycopg2-binary==2.9.5
django-cors-headers==3.14.0
Pillow==9.4.0
python-decouple==3.7
```

## 🔒 Sécurité

### Mesures Implémentées
- **Authentification par cookies** HTTPOnly sécurisés
- **Validation côté serveur** pour toutes les données
- **Permissions granulaires** par rôle utilisateur
- **Protection CSRF** avec tokens
- **Hachage sécurisé** des mots de passe
- **Validation des uploads** d'images

### Configuration Sécurité
```python
# settings.py
SECURE_BROWSER_XSS_FILTER = True
SECURE_CONTENT_TYPE_NOSNIFF = True
X_FRAME_OPTIONS = 'DENY'
SECURE_HSTS_SECONDS = 31536000
CORS_ALLOW_CREDENTIALS = True
```

## 📈 Performance et Optimisation

### Backend
- **Pagination automatique** des résultats API
- **Optimisation des requêtes** avec select_related
- **Cache statique** pour les médias
- **Compression Gzip** activée

### Frontend
- **Lazy loading** des images avec Glide
- **RecyclerView** pour les listes longues
- **Animation hardware-accelerated**
- **Gestion mémoire** optimisée

## 🧪 Tests et Qualité

### Tests Backend
```bash
# Lancer les tests Django
python manage.py test

# Coverage
pip install coverage
coverage run --source='.' manage.py test
coverage report
```

### Tests Frontend
```bash
# Tests unitaires Android
./gradlew test

# Tests d'interface
./gradlew connectedAndroidTest
```

## 🚀 Déploiement

### Backend (Production)
```bash
# Variables d'environnement production
DEBUG=False
ALLOWED_HOSTS=your-domain.com
SECURE_SSL_REDIRECT=True

# Collecte des fichiers statiques
python manage.py collectstatic --noinput

# Serveur WSGI (Gunicorn)
pip install gunicorn
gunicorn core.wsgi:application
```

### Frontend (Release)
```bash
# Build release
./gradlew assembleRelease

# Signature APK
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore your-keystore.jks app-release-unsigned.apk your-alias
```

## 🤝 Contribution

### Standards de Code
- **Python**: PEP 8 avec Black formatter
- **Java**: Google Java Style Guide
- **Commits**: Convention Conventional Commits

### Process de Développement
1. Fork du repository
2. Création d'une branche feature
3. Développement avec tests
4. Pull Request avec description détaillée

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## 👨‍💻 Développeur

**Mamadou SY**
- 🎓 **Formation** : Licence en Télécommunication, Réseaux et Informatique  
  Spécialité : Développement d'Applications Réparties (DAR)
- 🏫 **École** : ESMT (École Supérieure Multinationale des Télécommunications)
- 📧 **Contact** : 92mamadousy@gmail.com
- 💼 **Rôles** : 
  - Développement Backend (Django REST API)
  - Développement Frontend (Application Android native)
  - Architecture système et base de données
  - UI/UX Design et expérience utilisateur

## 🛠️ Compétences Techniques Démontrées

### Backend
- **Django REST Framework** - API robuste et sécurisée
- **PostgreSQL** - Modélisation et optimisation BDD
- **Authentification avancée** - Knox tokens + cookies
- **Architecture API REST** - Endpoints complets et documentés

### Frontend Mobile
- **Android natif Java** - Interface moderne Material Design
- **Retrofit/OkHttp** - Communication API optimisée
- **RecyclerView/Adapters** - Listes performantes
- **Animations personnalisées** - UX fluide et engageante

### Architecture & Conception
- **Modélisation BDD** - Relations complexes et optimisées
- **Patterns MVP** - Architecture claire et maintenable
- **Gestion d'état** - Sessions et données persistantes
- **Sécurité** - Validation, permissions, protection

## 📞 Contact et Support

Pour toute question technique ou collaboration :
- **Email personnel** : 92mamadousy@gmail.com
- **LinkedIn** : [Mamadou SY - Développeur Full Stack](https://www.linkedin.com/in/mamadou-sy-02166829b/)
- **GitHub** : Retrouvez mes autres projets sur GitHub

---

**Version** : 1.0.0  
**Développeur** : Mamadou SY - ESMT DAR  
**Dernière mise à jour** : 05 Juillet 2025  
**Compatibilité** : Android 5.0+ (API 21+), Python 3.8+

> 💡 *Ce projet démontre une maîtrise complète du développement d'applications réparties, de la conception backend à l'interface utilisateur mobile, en passant par l'architecture API et la sécurité.*