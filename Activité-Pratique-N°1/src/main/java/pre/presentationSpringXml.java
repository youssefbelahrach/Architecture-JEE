package pre;

import metier.IMetier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class presentationSpringXml {
    public static void main(String[] args) {
        ApplicationContext context=new ClassPathXmlApplicationContext("config.xml");
        //IMetier metier=(IMetier) context.getBean("metier");
        IMetier metier=context.getBean(IMetier.class);
        System.out.println(metier.calcul());
        System.out.println("result with XML");
    }
}
