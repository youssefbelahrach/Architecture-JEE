package ma.enset.miniioc.demo.main;

import ma.enset.miniioc.core.AnnotationApplicationContext;
import ma.enset.miniioc.demo.common.ReportService;

public class AnnotationDemoMain {
    public static void main(String[] args) {
        AnnotationApplicationContext context = new AnnotationApplicationContext(
                "ma.enset.miniioc.demo.annotationcomponents"
        );

        System.out.println("=== Démonstration annotations ===");
        System.out.println(context.getBean("annotationConstructorService", ReportService.class).generateReport());
        System.out.println(context.getBean("annotationSetterService", ReportService.class).generateReport());
        System.out.println(context.getBean("annotationFieldService", ReportService.class).generateReport());
    }
}
