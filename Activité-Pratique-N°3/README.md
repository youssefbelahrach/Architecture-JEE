# ProductApp — Gestion de produits (Spring Boot / JPA / Thymeleaf / Spring Security)

Application web JEE qui permet de gérer un catalogue de produits, construite selon
les étapes du TP.

## Stack technique
- Spring Boot 3.3, Java 17
- Spring Web (MVC) + Thymeleaf + Thymeleaf Layout Dialect
- Spring Data JPA + Hibernate
- Base H2 (par défaut) ou MySQL
- Spring Security 6
- Spring Validation (Bean Validation)
- Lombok, Bootstrap 5 (webjars)

## Lancer l'application
```bash
mvn spring-boot:run
```
Puis ouvrir : http://localhost:8080

> Note : le projet utilise le plugin `spring-boot-maven-plugin`. Si vous n'avez pas
> Maven installé, ouvrez simplement le dossier dans IntelliJ IDEA / Eclipse / VS Code
> comme projet Maven ; l'IDE téléchargera les dépendances et vous pourrez lancer
> la classe `ProductAppApplication`.

## Comptes de démonstration
| Utilisateur | Mot de passe | Rôles         | Droits                                  |
|-------------|--------------|---------------|-----------------------------------------|
| `user`      | `1234`       | USER          | Consulter et rechercher                 |
| `admin`     | `1234`       | USER + ADMIN  | Ajouter, éditer, supprimer en plus      |

## Correspondance avec les étapes du TP
1. **Projet + dépendances** → `pom.xml`
2. **Entité JPA Product** → `entities/Product.java` (avec annotations de validation)
3. **ProductRepository** → `repository/ProductRepository.java`
4. **Test de la couche DAO** → `CommandLineRunner` dans `ProductAppApplication`
   + tests JUnit `ProductRepositoryTest`
5. **Désactiver la sécurité par défaut** → commenter le bean `securityFilterChain`
   dans `security/SecurityConfig.java` (voir commentaire dans le fichier)
6. **Contrôleur MVC + vues Thymeleaf** → `web/ProductController.java`,
   `templates/template1.html` (layout Bootstrap), `products.html`, `new-product.html`
7. **Sécuriser l'application** → `security/SecurityConfig.java`, `login.html`,
   `web/SecurityController.java`, `notAuthorized.html`
8. **Fonctionnalités supplémentaires** :
   - Recherche par mot-clé (`findByNameContainingIgnoreCase`)
   - Pagination
   - Édition / mise à jour (`edit-product.html`)
   - Badge visuel de stock faible, confirmation de suppression, page 403,
     déconnexion, données de démo au démarrage

## Passer de H2 à MySQL
Dans `src/main/resources/application.properties`, commentez le bloc H2 et
décommentez le bloc MySQL. Créez d'abord la base (ou laissez
`createDatabaseIfNotExist=true` s'en charger).

## Console H2
Disponible sur http://localhost:8080/h2-console
JDBC URL : `jdbc:h2:mem:products-db` — user : `sa` — pas de mot de passe.
