package pre;

import metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class presentationSpringAnnotation {
    public static void main(String[] args) {
        ApplicationContext context= new AnnotationConfigApplicationContext("ext","metier","dao");
        IMetier metier=context.getBean(IMetier.class);
        System.out.println("Res: "+metier.calcul());
        System.out.println("result with annotation");
    }
}
