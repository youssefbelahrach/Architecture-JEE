# Digital Banking — Application de gestion de comptes bancaires

Application full-stack de gestion de comptes bancaires : un backend Spring Boot exposant une API REST, un frontend Angular, le tout sécurisé par Spring Security et JSON Web Token (JWT).

---

## 1. Description fonctionnelle

Une banque gère des clients. Chaque client possède un ou plusieurs comptes bancaires. Un compte subit des opérations de type **DEBIT** (retrait) ou **CREDIT** (versement). Il existe deux types de comptes :

- **Compte courant** (`CurrentAccount`) : avec autorisation de découvert (`overDraft`).
- **Compte épargne** (`SavingAccount`) : avec taux d'intérêt (`interestRate`).

Fonctionnalités couvertes : gestion des clients (liste, recherche, ajout, suppression), consultation d'un compte avec historique paginé des opérations, et opérations de débit, crédit et virement — le tout protégé par une authentification à base de rôles.

---

## 2. Structure du projet

```
digital-banking/
├── README.md                      ← Description du projet
├── digital-banking-backend/       ← projet Spring Boot
│   ├── pom.xml
│   └── src/main/
│       ├── java/ma/enset/digitalbanking/
│       │   ├── entities/
│       │   ├── enums/
│       │   ├── repositories/
│       │   ├── dtos/
│       │   ├── mappers/
│       │   ├── exceptions/
│       │   ├── services/
│       │   ├── security/
│       │   ├── web/
│       │   └── DigitalBankingApplication.java
│       └── resources/
│           └── application.properties
└── digital-banking-frontend/      ← projet Angular
    ├── package.json
    ├── angular.json
    └── src/app/
        ├── model/
        ├── services/
        ├── interceptors/
        ├── guards/
        ├── customers/ new-customer/ accounts/ login/ navbar/ not-authorized/
        ├── app.module.ts / app-routing.module.ts
        └── ...
```

---

## 3. Procédure de test en local

### Étape 1 — Lancer le backend

```bash
cd digital-banking-backend
mvn spring-boot:run
```

Le backend démarre sur **http://localhost:8085**. La base H2 en mémoire est créée automatiquement et remplie de données de test (3 clients, leurs comptes courants/épargne, et des opérations).

Console H2 (facultatif) : http://localhost:8085/h2-console
(JDBC URL : `jdbc:h2:mem:banking-db`, utilisateur `sa`, mot de passe vide).

### Étape 2 — Lancer le frontend

Dans un second terminal :

```bash
cd digital-banking-frontend
npm install
ng serve
```

Le frontend est accessible sur **http://localhost:4200**.

### Étape 3 — Se connecter

Deux comptes de test sont disponibles :

| Utilisateur | Mot de passe | Rôles        | Droits                                        |
| ----------- | ------------ | ------------ | --------------------------------------------- |
| `user1`     | `12345`      | USER         | Lecture seule (consultation)                  |
| `admin`     | `12345`      | USER + ADMIN | Accès complet (opérations, ajout/suppression) |

### Principaux endpoints

| Méthode | URL                                             | Rôle requis |
| ------- | ----------------------------------------------- | ----------- |
| POST    | `/auth/login`                                   | public      |
| GET     | `/api/customers`                                | USER        |
| GET     | `/api/customers/search?keyword=`                | USER        |
| POST    | `/api/customers`                                | ADMIN       |
| DELETE  | `/api/customers/{id}`                           | ADMIN       |
| GET     | `/api/accounts`                                 | USER        |
| GET     | `/api/accounts/{id}/pageOperations?page=&size=` | USER        |
| POST    | `/api/accounts/debit`                           | ADMIN       |
| POST    | `/api/accounts/credit`                          | ADMIN       |
| POST    | `/api/accounts/transfer`                        | ADMIN       |

---
