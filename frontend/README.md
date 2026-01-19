# SmartLogi Frontend - Angular Application

Application frontend Angular pour le système de gestion logistique SmartLogi (SDMS).

## 🚀 Technologies

- **Angular 21** - Framework frontend
- **TypeScript** - Langage de programmation
- **Tailwind CSS** - Framework CSS
- **RxJS** - Programmation réactive

## 📁 Architecture

L'application suit une architecture modulaire et organisée :

```
src/app/
├── core/                    # Code partagé et services centraux
│   ├── guards/             # Guards de routage (AuthGuard, RoleGuard)
│   ├── interceptors/       # Interceptors HTTP (JWT, Error)
│   ├── models/             # Modèles de données TypeScript
│   └── services/           # Services HTTP (Auth, Delivery, Courier, Zone, Product)
├── features/               # Modules fonctionnels
│   ├── auth/               # Authentification (Login, Register)
│   ├── dashboard/          # Dashboards (Manager, Sender, Courier)
│   ├── deliveries/         # Gestion des colis
│   ├── clients/            # Gestion des clients (Senders/Recipients)
│   ├── couriers/           # Gestion des livreurs
│   ├── zones/              # Gestion des zones
│   ├── products/           # Gestion des produits (Senders)
│   └── tracking/           # Suivi public des livraisons
├── layouts/                # Layouts de l'application
│   └── main-layout/        # Layout principal avec navigation
├── shared/                  # Composants partagés
│   └── unauthorized/       # Page d'erreur 403
├── app.config.ts           # Configuration de l'application
├── app.routes.ts           # Configuration du routing
└── app.ts                  # Composant racine
```

## 🔐 Sécurité

### Authentification JWT
- Gestion automatique des tokens via interceptors
- Stockage sécurisé dans localStorage
- Rafraîchissement automatique des tokens

### Guards de routage
- **AuthGuard** : Vérifie l'authentification
- **RoleGuard** : Vérifie les permissions par rôle

### Rôles utilisateurs
- `ROLE_ADMIN` : Accès complet + gestion des permissions
- `ROLE_MANAGER` : Gestion complète (colis, livreurs, zones, clients)
- `ROLE_COURIER` : Accès à ses colis assignés, mise à jour de statut
- `ROLE_SENDER` : Création et suivi de ses colis, gestion de ses produits

## 📋 Fonctionnalités

### Authentification
- ✅ Connexion avec email/mot de passe
- ✅ Inscription pour les clients expéditeurs
- ✅ OAuth2 (Google)
- ✅ Gestion des tokens JWT
- ✅ Redirection automatique selon le rôle après connexion

### Gestion des colis
- ✅ Liste paginée et filtrée des colis (MANAGER/ADMIN)
- ✅ Création de colis avec produits (SENDER)
- ✅ Détails d'un colis avec historique
- ✅ Mise à jour du statut (MANAGER/COURIER)
- ✅ Assignation des livreurs (collecte et livraison) (MANAGER)
- ✅ Liste des mes livraisons (SENDER/COURIER)
- ✅ Suivi public par numéro de suivi (route `/track`)

### Dashboard
- ✅ **Dashboard Manager/Admin** : Vue d'ensemble avec statistiques complètes
- ✅ **Dashboard Sender** : Vue personnalisée avec statistiques de ses livraisons
- ✅ **Dashboard Courier** : Vue des livraisons assignées avec mise à jour de statut rapide
- ✅ Redirection automatique selon le rôle

### Gestion des clients (MANAGER/ADMIN)
- ✅ Liste des expéditeurs (Senders)
- ✅ Liste des destinataires (Recipients)
- ✅ Recherche et filtrage
- ✅ Mise à jour des informations

### Gestion des livreurs (MANAGER/ADMIN)
- ✅ Liste des livreurs avec pagination
- ✅ Création de livreurs (inscription)
- ✅ Mise à jour des informations
- ✅ Assignation par zone

### Gestion des zones (MANAGER/ADMIN)
- ✅ Liste des zones avec codes postaux
- ✅ Création de zones
- ✅ Mise à jour des zones
- ✅ Ajout de codes postaux à une zone
- ✅ Suppression de codes postaux d'une zone
- ✅ Création de zone avec codes postaux en une seule opération

### Gestion des produits (SENDER)
- ✅ Liste de mes produits
- ✅ Création de produits
- ✅ Mise à jour de produits
- ✅ Utilisation dans la création de livraisons

### Suivi public
- ✅ Page de tracking publique (`/track`)
- ✅ Recherche par numéro de suivi
- ✅ Affichage des informations de livraison sans authentification
- ✅ Interface moderne et responsive

## 🛠️ Installation et Démarrage

### Prérequis
- Node.js 18+
- npm ou yarn

### Installation

```bash
# Installer les dépendances
npm install

# Démarrer le serveur de développement
npm start
# ou
ng serve
```

L'application sera accessible sur `http://localhost:4200`

### Configuration

Le fichier `environment.ts` contient la configuration de l'API :

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api/v3',
  // ...
};
```

## 📦 Build

```bash
# Build de production
ng build

# Build avec optimisations
ng build --configuration production
```

## 🧪 Tests

```bash
# Lancer les tests unitaires
ng test

# Lancer les tests avec couverture
ng test --code-coverage
```

## 🔄 Intégration avec le Backend

L'application consomme l'API REST Spring Boot disponible sur `http://localhost:8080/api/v3`.

### Endpoints principaux
- `/auth/login` - Authentification
- `/auth/register` - Inscription
- `/deliveries` - Gestion des colis
- `/deliveries/my-deliveries` - Mes livraisons (Sender/Courier)
- `/deliveries/new` - Créer une livraison (Sender)
- `/couriers` - Gestion des livreurs
- `/zones` - Gestion des zones
- `/clients` - Gestion des clients
- `/products/my-products` - Mes produits (Sender)
- `/track` - Suivi public (sans authentification)

## 📝 Bonnes pratiques

- **Standalone Components** : Tous les composants sont standalone
- **Lazy Loading** : Chargement à la demande des routes
- **Reactive Forms** : Utilisation de Reactive Forms pour la validation
- **Type Safety** : TypeScript strict pour la sécurité des types
- **Separation of Concerns** : Séparation claire UI / Logique / Données

## 🎨 UI/UX

- Design moderne avec Tailwind CSS
- Interface responsive
- Feedback utilisateur clair (messages d'erreur, loading states)
- Navigation intuitive selon les rôles

## 📚 Ressources

- [Angular Documentation](https://angular.dev)
- [Tailwind CSS](https://tailwindcss.com)
- [RxJS](https://rxjs.dev)
