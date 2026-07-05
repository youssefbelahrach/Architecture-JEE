# TP Spring Data JPA — Gestion de produits

Projet Spring Boot réalisant les 6 étapes du TP.

## Dépendances

- Spring Data JPA
- H2 Database
- Spring Web
- Lombok
- (MySQL Connector ajouté pour l'étape 6)

## Structure

```
src/main/java/ma/enset/productsapp/
├── ProductsAppApplication.java   → main + tests (CommandLineRunner)
├── entities/Product.java          → entité JPA (étape 2)
└── repositories/ProductRepository.java → interface JpaRepository (étape 4)
src/main/resources/
├── application.properties         → config H2 (étape 3)
└── application-mysql.properties   → config MySQL (étape 6)
```

## Console H2

http://localhost:8085/h2-console
JDBC URL : `jdbc:h2:mem:products-db` — User : `sa` — Password : (vide)

## Migration vers MySQL (étape 6)

Copier le contenu de `application-mysql.properties` dans `application.properties`,

Prérequis : un serveur MySQL en cours d'exécution sur `localhost:3306`.
