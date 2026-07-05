package ma.enset.miniioc.demo.main;

import ma.enset.miniioc.core.XmlApplicationContext;
import ma.enset.miniioc.demo.common.ReportService;

public class XmlDemoMain {
    public static void main(String[] args) {
        XmlApplicationContext context = new XmlApplicationContext("beans.xml");

        System.out.println("=== Démonstration XML + JAXB ===");
        System.out.println(context.getBean("xmlConstructorService", ReportService.class).generateReport());
        System.out.println(context.getBean("xmlSetterService", ReportService.class).generateReport());
        System.out.println(context.getBean("xmlFieldService", ReportService.class).generateReport());
    }
}
