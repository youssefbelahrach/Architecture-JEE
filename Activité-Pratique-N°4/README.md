# TP — Angular + Spring Boot

Ce dépôt contient deux projets indépendants (frontend et backend) :

## 1. Description du projet

L'application permet de gérer un catalogue de produits.
Le backend Spring Boot expose une API REST consommée par le frontend Angular.

### Fonctionnalités

- Lister tous les produits
- Rechercher un produit par son nom
- Ajouter un nouveau produit
- Modifier un produit existant
- Supprimer un produit (avec confirmation)
- Calcul automatique de la valeur totale du stock

---

## 3. Backend — `products-service`

### 3.1 Structure

```
products-service/src/main/java/net/youssfi/webmvcproducts/
├── WebMvcProductsApplication.java      # Point d'entrée + jeu de données initial
├── entities/
│   └── Product.java                    # Entité JPA (id, name, price, quantity, selected)
├── repository/
│   └── ProductRepository.java          # JpaRepository + requête dérivée de recherche
└── web/
    ├── ProductRestAPI.java             # Contrôleur REST (API consommée par Angular)
    └── ProductController.java          # Contrôleur MVC (vue Thymeleaf /index)
```

### 3.2 API REST

Base URL : `http://localhost:8083`

| Méthode  | URL                            | Description                 |
| -------- | ------------------------------ | --------------------------- |
| `GET`    | `/products`                    | Liste de tous les produits  |
| `GET`    | `/products/search?keyword=xxx` | Recherche par nom           |
| `GET`    | `/products/{id}`               | Détail d'un produit         |
| `POST`   | `/products`                    | Création d'un produit       |
| `PUT`    | `/products/{id}`               | Mise à jour d'un produit    |
| `PATCH`  | `/products/{id}/select`        | Bascule du champ `selected` |
| `DELETE` | `/products/{id}`               | Suppression d'un produit    |

---

## 4. Frontend — `angular-front-products`

### 4.1 Structure

```
angular-front-products/src/
├── main.ts
├── index.html
├── styles.css
├── environments/
│   ├── environment.ts              # backendHost = http://localhost:8083
│   └── environment.development.ts
└── app/
    ├── app.component.ts/html/css   # Composant racine (navbar + router-outlet)
    ├── app.config.ts               # Providers : Router + HttpClient
    ├── app.routes.ts               # Définition des routes
    ├── model/
    │   └── product.model.ts        # Interface Product
    ├── services/
    │   └── products.service.ts     # Appels HTTP vers l'API REST
    └── components/
        ├── navbar/                 # Barre de navigation
        ├── product-list/           # Tableau, recherche, suppression, sélection
        ├── new-product/            # Formulaire d'ajout
        └── edit-product/           # Formulaire d'édition
```

### 4.3 Routes

| Route               | Composant              | Description          |
| ------------------- | ---------------------- | -------------------- |
| `/products`         | `ProductListComponent` | Liste + recherche    |
| `/new-product`      | `NewProductComponent`  | Ajout d'un produit   |
| `/edit-product/:id` | `EditProductComponent` | Édition d'un produit |

### 4.4 Lancer le frontend

```bash
cd angular-front-products
npm install
npm start            # similaire à : ng serve
```

L'application est accessible sur <http://localhost:4200>.

Démarrez **d'abord le backend** (port 8083), puis le frontend.

---
