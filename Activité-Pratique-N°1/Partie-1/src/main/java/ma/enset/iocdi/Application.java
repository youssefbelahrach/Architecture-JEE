package ma.enset.iocdi;

import ma.enset.iocdi.presentation.PresentationDynamic;
import ma.enset.iocdi.presentation.PresentationSpringAnnotations;
import ma.enset.iocdi.presentation.PresentationSpringXml;
import ma.enset.iocdi.presentation.PresentationStatic;

/**
 * Point d'entrée qui exécute les quatre variantes demandées.
 */
public class Application {
    public static void main(String[] args) {
        System.out.println("===== INJECTION DES DÉPENDANCES =====");
        System.out.printf("[1. Instanciation statique] Résultat = %.1f%n", PresentationStatic.run());
        System.out.printf("[2. Instanciation dynamique] Résultat = %.1f%n", PresentationDynamic.run());
        System.out.printf("[3. Spring XML] Résultat = %.1f%n", PresentationSpringXml.run());
        System.out.printf("[4. Spring annotations] Résultat = %.1f%n", PresentationSpringAnnotations.run());
    }
}
