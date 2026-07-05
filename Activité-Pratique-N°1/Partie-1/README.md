# Partie 1 — Inversion de contrôle et injection des dépendances

Ce projet illustre le **couplage faible** : `MetierImpl` dépend de l'interface `IDao`, et non directement de `DaoImpl`. La logique métier réalise le calcul suivant :

```text
calcul() = getData() × 5
getData() = 10.0
Résultat = 50.0
```

## Objectifs couverts

- `IDao` avec la méthode `getData()` ;
- `DaoImpl`, implémentation de `IDao` ;
- `IMetier` avec la méthode `calcul()` ;
- `MetierImpl`, dépendant de l'abstraction `IDao` ;
- injection par instanciation statique ;
- injection dynamique avec réflexion et `config.txt` ;
- injection Spring IoC par fichier XML ;
- injection Spring IoC par annotations.

## 1. Injection statique

Les objets sont créés et assemblés dans le code Java :

```java
IDao dao = new DaoImpl();
MetierImpl metier = new MetierImpl();
metier.setDao(dao);
System.out.println(metier.calcul());
```

## 2. Injection dynamique

Les noms complets des classes sont stockés dans `config.txt`. `PresentationDynamic` utilise `Class.forName(...)`, crée les objets par réflexion, puis appelle `setDao(...)` à l'aide de `Method.invoke(...)`.

```text
ma.enset.iocdi.dao.DaoImpl
ma.enset.iocdi.metier.MetierImpl
```

## 3. Spring — configuration XML

Le fichier `spring-context.xml` déclare les beans et injecte le DAO dans le bean métier :

```xml
<bean id="dao" class="ma.enset.iocdi.dao.DaoImpl"/>

<bean id="metier" class="ma.enset.iocdi.metier.MetierImpl">
    <property name="dao" ref="dao"/>
</bean>
```

Le contexte est chargé par :

```java
new ClassPathXmlApplicationContext("spring-context.xml");
```

## 4. Spring — configuration par annotations

- `@Repository` : sur `DaoImpl` ;
- `@Service` : sur `MetierImpl` ;
- `@Autowired` : sur le constructeur de `MetierImpl` ;
- `@Configuration` + `@ComponentScan` : dans `AppConfig`.

## Exécution dans IntelliJ IDEA

1. Ouvrir le dossier du projet comme projet Maven.
2. Vérifier que le SDK du projet est Java 17 ou supérieur.
3. Attendre le téléchargement des dépendances Maven.
4. Ouvrir `Application.java`.
5. Cliquer sur le bouton vert à côté de `main`, puis choisir **Run 'Application.main()'**.

## Résultats

```text
===== INJECTION DES DÉPENDANCES =====
[1. Instanciation statique] Résultat = 50.0
[2. Instanciation dynamique] Résultat = 50.0
[3. Spring XML] Résultat = 50.0
[4. Spring annotations] Résultat = 50.0
```
