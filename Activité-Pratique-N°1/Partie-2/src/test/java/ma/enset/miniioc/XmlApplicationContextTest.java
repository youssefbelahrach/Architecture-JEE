package ma.enset.miniioc;

import ma.enset.miniioc.core.XmlApplicationContext;
import ma.enset.miniioc.demo.common.ReportService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class XmlApplicationContextTest {
    @Test
    void shouldInjectByConstructorSetterAndFieldFromXml() {
        XmlApplicationContext context = new XmlApplicationContext("beans.xml");

        assertTrue(context.getBean("xmlConstructorService", ReportService.class)
                .generateReport().contains("constructeur"));
        assertTrue(context.getBean("xmlSetterService", ReportService.class)
                .generateReport().contains("setter"));
        assertTrue(context.getBean("xmlFieldService", ReportService.class)
                .generateReport().contains("attribut"));
    }
}
