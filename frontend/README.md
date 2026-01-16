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
│   ├── dashboard/          # Dashboard gestionnaire
│   ├── deliveries/         # Gestion des colis
│   ├── couriers/           # Gestion des livreurs
│   └── zones/              # Gestion des zones
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
- `ROLE_ADMIN` : Accès complet
- `ROLE_MANAGER` : Gestion complète (colis, livreurs, zones)
- `ROLE_COURIER` : Accès à ses colis assignés
- `ROLE_SENDER` : Création et suivi de ses colis

## 📋 Fonctionnalités

### Authentification
- ✅ Connexion avec email/mot de passe
- ✅ Inscription pour les clients expéditeurs
- ✅ OAuth2 (Google)
- ✅ Gestion des tokens JWT

### Gestion des colis
- ✅ Liste paginée et filtrée des colis
- ✅ Création de colis (SENDER)
- ✅ Détails d'un colis
- ✅ Mise à jour du statut (COURIER)
- ✅ Suivi public par numéro de suivi

### Dashboard (MANAGER/ADMIN)
- ✅ Vue d'ensemble des statistiques
- ✅ Liste des colis récents
- ✅ Métriques clés (total, livrés, en transit)

### Gestion des livreurs (MANAGER/ADMIN)
- ✅ Liste des livreurs
- ✅ Création de livreurs

### Gestion des zones (MANAGER/ADMIN)
- ✅ Liste des zones
- ✅ Création de zones

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
- `/couriers` - Gestion des livreurs
- `/zones` - Gestion des zones
- `/products` - Gestion des produits

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
