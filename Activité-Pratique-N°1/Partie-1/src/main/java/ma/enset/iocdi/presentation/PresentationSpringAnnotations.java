package ma.enset.iocdi.presentation;

import ma.enset.iocdi.config.AppConfig;
import ma.enset.iocdi.metier.IMetier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * Injection des dépendances par Spring IoC avec annotations.
 */
public final class PresentationSpringAnnotations {
    private PresentationSpringAnnotations() {
    }

    public static double run() {
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(AppConfig.class)) {
            IMetier metier = context.getBean(IMetier.class);
            return metier.calcul();
        }
    }
}
