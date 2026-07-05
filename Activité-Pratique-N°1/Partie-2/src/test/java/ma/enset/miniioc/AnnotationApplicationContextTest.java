package ma.enset.miniioc;

import ma.enset.miniioc.core.AnnotationApplicationContext;
import ma.enset.miniioc.demo.common.ReportService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class AnnotationApplicationContextTest {
    @Test
    void shouldInjectByConstructorSetterAndFieldWithAnnotations() {
        AnnotationApplicationContext context = new AnnotationApplicationContext(
                "ma.enset.miniioc.demo.annotationcomponents"
        );

        assertTrue(context.getBean("annotationConstructorService", ReportService.class)
                .generateReport().contains("constructeur"));
        assertTrue(context.getBean("annotationSetterService", ReportService.class)
                .generateReport().contains("setter"));
        assertTrue(context.getBean("annotationFieldService", ReportService.class)
                .generateReport().contains("attribut"));
    }
}
