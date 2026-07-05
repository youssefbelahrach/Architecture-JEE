package ma.enset.iocdi.presentation;

import ma.enset.iocdi.metier.IMetier;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Injection des dépendances par Spring IoC avec un fichier XML.
 */
public final class PresentationSpringXml {
    private PresentationSpringXml() {
    }

    public static double run() {
        try (ClassPathXmlApplicationContext context =
                     new ClassPathXmlApplicationContext("spring-context.xml")) {
            IMetier metier = context.getBean("metier", IMetier.class);
            return metier.calcul();
        }
    }
}
