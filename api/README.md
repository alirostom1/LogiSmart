# LogiSmart API - Système de Gestion Logistique Sécurisé

## 📋 Description

API REST sécurisée pour la gestion complète du cycle logistique : collecte, stockage, livraison, suivi des colis, gestion des clients et des livreurs. Implémentation complète de la sécurité avec authentification JWT stateless et gestion des permissions.

## 🚀 Technologies

- **Java 17**
- **Spring Boot 3.5.7**
- **Spring Security** (JWT stateless)
- **PostgreSQL** (Base de données)
- **Redis** (Blacklist des tokens)
- **Docker & Docker Compose**
- **MapStruct** (Mapping DTOs)
- **Liquibase** (Migrations)
- **Swagger/OpenAPI** (Documentation API)

## 🔐 Sécurité Implémentée



### Authentification JWT Stateless
- ✅ Endpoint `/api/v3/auth/login` - Authentification
- ✅ Endpoint `/api/v3/auth/register` - Inscription (Clients)
- ✅ Endpoint `/api/v3/auth/refresh` - Rafraîchissement des tokens
- ✅ Filtre JWT automatique sur toutes les requêtes
- ✅ Tokens avec permissions incluses

### Autorisation par Rôles et Permissions

#### Rôles
- **ROLE_ADMIN** : Accès complet + gestion des permissions
- **ROLE_MANAGER** : Gestion complète (colis, livreurs, zones)
- **ROLE_COURIER** : Accès uniquement à ses colis assignés
- **ROLE_SENDER** : Création et suivi de ses propres colis

#### Permissions
- Gestion des permissions dynamique via AdminController
- Permissions stockées dans les tokens JWT
- Vérifications propriétaires pour isolation des données

### CORS Strict
- Seuls les frontends internes autorisés (localhost:4200, 3000, 8080)
- Headers autorisés : Authorization, Content-Type, X-Requested-With

## 📁 Structure du Projet

```
src/main/java/io/github/alirostom1/logismart/
├── config/          # Configuration (Security, DatabaseSeeder)
├── controller/      # Contrôleurs REST
├── service/         # Logique métier
├── repository/      # Accès aux données
├── model/          # Entités JPA
├── dto/            # Data Transfer Objects
├── filter/         # Filtres (JWT)
├── exception/      # Exceptions personnalisées
└── util/           # Utilitaires
```

## 🛠️ Installation et Démarrage

### Prérequis
- Java 17+
- Maven 3.8+
- Docker & Docker Compose
- PostgreSQL 14+ (ou via Docker)
- Redis (ou via Docker)

### Configuration

1. **Créer un fichier `.env` à la racine :**
```env
# Database
DB_URL=jdbc:postgresql://localhost:5432/logismart
DB_USERNAME=postgres
DB_PASSWORD=postgres
POSTGRES_DB=logismart

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# JWT (256 bits minimum, en base64)
JWT_SECRET=dGhpc2lzYXZlcnlsb25nc2VjcmV0a2V5Zm9yan3R0dG9rZW5zZ2VuZXJhdGlvbnB1cnBvc2Vz
JWT_ACCESS_EXPIRATION=900000
JWT_REFRESH_EXPIRATION=604800000
```

2. **Démarrer avec Docker Compose :**
```bash
docker-compose up -d
```

3. **Ou démarrer manuellement :**
```bash
# Démarrer PostgreSQL et Redis
# Puis :
mvn spring-boot:run
```

### Initialisation de la Base de Données

Le `DatabaseSeeder` s'exécute automatiquement au démarrage et crée :
- ✅ Tous les rôles (ADMIN, MANAGER, COURIER, SENDER)
- ✅ Toutes les permissions
- ✅ Assignation des permissions aux rôles

## 📚 Endpoints Principaux

### Authentification
- `POST /api/v3/auth/login` - Connexion
- `POST /api/v3/auth/register` - Inscription
- `POST /api/v3/auth/refresh` - Rafraîchir tokens

### Colis (Deliveries)
- `POST /api/v3/deliveries` - Créer un colis (SENDER)
- `GET /api/v3/deliveries/{id}` - Détails (MANAGER)
- `GET /api/v3/deliveries/tracking/{trackingNumber}` - Suivi (Public)
- `PATCH /api/v3/deliveries/{id}/status` - Mettre à jour statut
- `GET /api/v3/deliveries/my-deliveries` - Mes colis (COURIER/SENDER)
- `POST /api/v3/deliveries/search` - Recherche (MANAGER)

### Livreurs (Couriers)
- `POST /api/v3/couriers` - Créer (MANAGER)
- `GET /api/v3/couriers` - Liste (MANAGER)
- `GET /api/v3/couriers/{id}` - Détails (MANAGER)

### Zones
- `GET /api/v3/zones` - Liste (MANAGER)
- `POST /api/v3/zones` - Créer (MANAGER)

### Produits (Products)
- `POST /api/v3/products` - Créer (SENDER)
- `GET /api/v3/products/my-products` - Mes produits (SENDER)

### Administration (Admin uniquement)
- `POST /api/v3/admin/permissions` - Créer permission
- `GET /api/v3/admin/permissions` - Liste permissions
- `POST /api/v3/admin/roles/assign-permission` - Assigner permission
- `POST /api/v3/admin/roles/revoke-permission` - Retirer permission
- `GET /api/v3/admin/roles/{id}/permissions` - Permissions d'un rôle

## 🔑 Utilisation de l'API

### 1. Connexion
```bash
curl -X POST http://localhost:8080/api/v3/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "manager@example.com",
    "password": "password"
  }'
```

Réponse :
```json
{
  "success": true,
  "message": "Succesfully logged in!",
  "data": {
    "tokenPair": {
      "accessToken": "eyJhbGc...",
      "refreshToken": "eyJhbGc...",
      "tokenType": "Bearer"
    },
    "role": "ROLE_MANAGER"
  }
}
```

### 2. Utiliser le Token
```bash
curl -X GET http://localhost:8080/api/v3/deliveries \
  -H "Authorization: Bearer eyJhbGc..."
```

## 🧪 Tests Manuels

### Test 1 : Login sans token
```bash
curl -X GET http://localhost:8080/api/v3/deliveries
# Attendu : 401 Unauthorized
```

### Test 2 : Login avec token invalide
```bash
curl -X GET http://localhost:8080/api/v3/deliveries \
  -H "Authorization: Bearer invalid_token"
# Attendu : 401 Unauthorized
```

### Test 3 : Accès non autorisé
```bash
# Login en tant que COURIER
# Essayer d'accéder à /api/v3/couriers
# Attendu : 403 Forbidden
```

### Test 4 : Accès autorisé
```bash
# Login en tant que MANAGER
# Accéder à /api/v3/couriers
# Attendu : 200 OK avec liste des livreurs
```

## 📖 Documentation API

Une fois l'application démarrée, accéder à Swagger UI :
```
http://localhost:8080/swagger-ui/index.html
```

## 🐳 Docker

### Build et Run
```bash
docker-compose up --build
```

### Arrêter
```bash
docker-compose down
```

### Voir les logs
```bash
docker-compose logs -f app
```

## 🔒 Sécurité - Détails Techniques

### JWT Token Structure
```json
{
  "role": "ROLE_MANAGER",
  "email": "manager@example.com",
  "permissions": ["DELIVERY_READ", "COURIER_SAVE", ...],
  "type": "access",
  "exp": 1234567890
}
```

### Vérifications Propriétaires
- **Couriers** : Ne peuvent voir que leurs colis assignés
- **Senders** : Ne peuvent voir que leurs propres colis et produits
- Vérifications implémentées dans les services

### Mode Stateless
- Aucune session stockée côté serveur
- Toutes les informations dans le JWT
- Redis utilisé uniquement pour blacklist des refresh tokens

## 📝 Notes Importantes

1. **JWT Secret** : Doit être en base64, 256 bits minimum
2. **DatabaseSeeder** : S'exécute automatiquement au premier démarrage
3. **CORS** : Configuré uniquement pour les frontends internes
4. **Permissions** : Stockées dans les tokens pour performance

## 🎯 Fonctionnalités Clés

✅ Authentification JWT stateless complète  
✅ Gestion dynamique des permissions (Admin)  
✅ Isolation des données par rôle  
✅ CORS strict configuré  
✅ Dockerisation complète  
✅ Documentation Swagger  
✅ Gestion d'erreurs centralisée  
✅ Logging des opérations  

## 👨‍💻 Auteur

Projet développé dans le cadre d'un cours de sécurité des applications.

## 📄 Licence

Projet académique.

