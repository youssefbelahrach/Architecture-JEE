# Partie 2 - Mini Framework d'injection des dépendances

Ce projet implémente un mini conteneur IoC inspiré de Spring. Il permet de créer et de relier automatiquement des composants Java selon deux approches :

1. **Configuration XML avec JAXB (JAX-Binding / OXM)** ;
2. **Configuration par annotations**.

Les trois formes d'injection demandées sont présentes :

- injection par **constructeur** ;
- injection par **setter** ;
- injection directe par **attribut / field**.

## Exemple de configuration XML

Le fichier `src/main/resources/beans.xml` décrit les beans et leurs dépendances.

```xml
<bean id="xmlDao" class="ma.enset.miniioc.demo.xmlcomponents.XmlDao"/>

<bean id="xmlConstructorService" class="ma.enset.miniioc.demo.xmlcomponents.XmlConstructorService">
    <constructor-arg ref="xmlDao"/>
</bean>

<bean id="xmlSetterService" class="ma.enset.miniioc.demo.xmlcomponents.XmlSetterService">
    <property name="dataProvider" ref="xmlDao"/>
</bean>

<bean id="xmlFieldService" class="ma.enset.miniioc.demo.xmlcomponents.XmlFieldService">
    <field name="dataProvider" ref="xmlDao"/>
</bean>
```

Le conteneur charge ce document avec JAXB, puis utilise la réflexion Java pour :

- appeler le constructeur compatible ;
- rechercher et appeler le setter `setDataProvider(...)` ;
- affecter directement le champ privé `dataProvider`.

## Exemple avec annotations

```java
@Component("annotationDao")
public class AnnotationDao implements DataProvider { ... }

@Component
public class AnnotationConstructorService {
    private final DataProvider dataProvider;

    @Autowired
    public AnnotationConstructorService(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }
}
```

Le conteneur `AnnotationApplicationContext` scanne le package indiqué, repère les classes `@Component`, puis résout les dépendances annotées par `@Autowired`.
