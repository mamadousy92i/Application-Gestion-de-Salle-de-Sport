# Documentation du Backend - Projet Salle de Sport

## 1. Architecture Générale

Le backend est construit avec Django et Django REST Framework, suivant une architecture modulaire avec plusieurs applications distinctes. Chaque application gère un aspect spécifique du système de gestion de la salle de sport.

### Structure du Projet
```
backend/
├── .env                    # Variables d'environnement
├── apps/                   # Applications Django
│   ├── accounts/           # Gestion des utilisateurs
│   ├── courses/            # Gestion des cours
│   ├── equipment/          # Gestion des équipements
│   ├── sales/              # Gestion des ventes
│   └── subscriptions/      # Gestion des abonnements
├── core/                   # Configuration principale
│   ├── settings.py         # Paramètres Django
│   ├── urls.py             # URLs principales
│   ├── asgi.py             # Configuration ASGI
│   └── wsgi.py             # Configuration WSGI
├── manage.py               # Script de gestion Django
└── requirements.txt        # Dépendances du projet
```

## 2. Configuration et Variables d'Environnement

### Fichier .env
Le fichier `.env` contient les variables d'environnement pour la configuration de la base de données:
```
DB_NAME=votre_nom_db
DB_USER=votre_utilisateur
DB_PASSWORD=votre_mot_de_passe
DB_HOST=localhost
DB_PORT=5432
```

Ces variables sont utilisées pour configurer la connexion à la base de données PostgreSQL, bien que le projet utilise actuellement SQLite comme indiqué dans `settings.py`.

### Dépendances (requirements.txt)
Les principales dépendances incluent:
- Django 5.0.1
- Django REST Framework 3.14.0
- django-cors-headers 4.3.1
- Knox pour l'authentification par token
- Pillow pour la gestion des images
- Celery pour les tâches asynchrones
- Outils de test (pytest, coverage)
- Outils de formatage et de linting (black, flake8, isort)

## 3. Applications et Fonctionnalités

### 3.1 Application Accounts (Gestion des utilisateurs)

#### Modèles
- **User**: Extension du modèle User de Django avec des champs supplémentaires:
  - `role`: Admin, Manager, Coach ou Membre
  - `email`: Utilisé comme identifiant principal
  - Informations personnelles (téléphone, adresse, date de naissance)
  - Photo de profil

- **Coach**: Informations spécifiques aux coachs:
  - Spécialisation, biographie, certifications
  - Disponibilité et tarif horaire

- **Member**: Informations spécifiques aux membres:
  - Contact d'urgence, conditions médicales
  - Dates de début et fin d'adhésion
  - Statut d'activité

#### Fonctionnalités principales
- Authentification par token avec Knox
- Enregistrement et connexion des utilisateurs
- Gestion des profils utilisateurs
- Gestion des coachs avec leurs disponibilités
- Gestion des membres avec leur statut d'adhésion

### 3.2 Application Subscriptions (Gestion des abonnements)

#### Modèles
- **SubscriptionPlan**: Définition des formules d'abonnement:
  - Nom, description, prix, durée
  - Options incluses (coach, cours collectifs, piscine)
  - Nombre maximum de séances par semaine

- **Subscription**: Abonnements des membres:
  - Lien vers le membre et le plan d'abonnement
  - Dates de début et de fin
  - Statut (actif, expiré, annulé, en attente)
  - Option de renouvellement automatique

- **Payment**: Paiements liés aux abonnements:
  - Montant, date, méthode de paiement
  - ID de transaction et statut

#### Fonctionnalités principales
- Création et gestion des formules d'abonnement
- Souscription des membres aux abonnements
- Renouvellement et annulation d'abonnements
- Gestion des paiements
- Rapports de revenus mensuels

### 3.3 Application Courses (Gestion des cours)

#### Modèles
- **CourseCategory**: Catégories de cours (ex: yoga, musculation)

- **Course**: Définition des cours:
  - Nom, catégorie, description
  - Coach, nombre maximum de participants
  - Niveau de difficulté, durée
  - Équipement nécessaire

- **CourseSchedule**: Planification des cours:
  - Cours, horaires de début et de fin
  - Salle, statut d'annulation

- **CourseBooking**: Réservations des membres:
  - Membre, séance réservée
  - Statut (réservé, annulé, terminé, non présenté)
  - Dates de présence ou d'annulation

- **CourseReview**: Évaluations des cours:
  - Note (1-5), commentaire

#### Fonctionnalités principales
- Création et gestion des cours
- Planification des séances
- Système de réservation pour les membres
- Annulation de cours ou de réservations
- Suivi des présences
- Système d'évaluation des cours

### 3.4 Application Equipment (Gestion des équipements)

#### Modèles
- **EquipmentCategory**: Catégories d'équipements

- **Equipment**: Équipements de la salle:
  - Nom, catégorie, description
  - Numéro de série, date et prix d'achat
  - Statut (disponible, en utilisation, en maintenance, hors service)
  - Emplacement, dates de maintenance

- **MaintenanceRecord**: Historique de maintenance:
  - Type de maintenance (routine, réparation, inspection, remplacement)
  - Description, coût, responsable
  - Date de la prochaine maintenance

#### Fonctionnalités principales
- Inventaire des équipements
- Suivi du statut des équipements
- Planification et suivi de la maintenance
- Alertes pour les équipements nécessitant une maintenance
- Statistiques sur les équipements et les coûts de maintenance

### 3.5 Application Sales (Gestion des ventes)

#### Modèles
- **Product**: Produits vendus dans la salle:
  - Nom, catégorie, description, prix
  - Quantité en stock, stock minimum
  - Image, code-barres

- **Sale**: Ventes effectuées:
  - Membre, montant total
  - Méthode de paiement, date de transaction

- **SaleItem**: Articles vendus:
  - Vente, produit, quantité
  - Prix unitaire, prix total

- **Inventory**: Mouvements de stock:
  - Produit, changement de quantité
  - Date, raison, notes

#### Fonctionnalités principales
- Gestion des produits et du stock
- Enregistrement des ventes
- Suivi des mouvements de stock
- Alertes pour les produits en rupture ou à faible stock
- Rapports de ventes (journaliers, périodiques)
- Analyse des produits les plus vendus

## 4. Sécurité et Permissions

Le backend utilise le système de permissions de Django REST Framework:
- Certaines actions sont accessibles uniquement aux administrateurs
- Les utilisateurs authentifiés ont accès à leurs propres données
- Certaines vues sont accessibles publiquement (liste des cours, plans d'abonnement)

L'authentification est gérée par Knox, qui fournit des tokens sécurisés avec une durée de vie limitée (48 heures) et une option de rafraîchissement automatique.

## 5. Configuration CORS

Le backend est configuré pour accepter des requêtes depuis le frontend en développement:
```python
CORS_ALLOW_ALL_ORIGINS = True  # Uniquement pour le développement
CORS_ALLOWED_ORIGINS = [
    "http://localhost:3000",
    "http://127.0.0.1:3000",
]
```

## 6. Points d'API

Le backend expose les points d'API suivants:
- `/api/accounts/` - Gestion des utilisateurs, coachs et membres
- `/api/courses/` - Gestion des cours, planifications et réservations
- `/api/equipment/` - Gestion des équipements et de la maintenance
- `/api/sales/` - Gestion des produits, ventes et inventaire
- `/api/subscriptions/` - Gestion des abonnements et paiements

## 7. Base de données

Le projet est configuré pour utiliser SQLite en développement, mais les variables d'environnement sont prévues pour une migration vers PostgreSQL en production.

## 8. Utilisation du fichier .env

Le fichier `.env` est chargé via la bibliothèque `python-dotenv` et est utilisé pour configurer les paramètres de base de données dans `settings.py`. Bien que les variables soient définies pour PostgreSQL, la configuration actuelle utilise SQLite.

## Conclusion

Le backend du projet de salle de sport est bien structuré avec une séparation claire des responsabilités entre les différentes applications. Il fournit toutes les fonctionnalités nécessaires pour gérer une salle de sport moderne, incluant la gestion des membres, des abonnements, des cours, des équipements et des ventes.

Le système est conçu pour être sécurisé, avec des permissions appropriées pour différents types d'utilisateurs, et extensible grâce à son architecture modulaire.
