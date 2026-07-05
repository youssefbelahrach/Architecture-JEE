package ma.enset.miniioc.demo.annotationcomponents;

import ma.enset.miniioc.annotations.Autowired;
import ma.enset.miniioc.annotations.Component;
import ma.enset.miniioc.demo.common.DataProvider;
import ma.enset.miniioc.demo.common.ReportService;

@Component
public class AnnotationFieldService implements ReportService {
    @Autowired
    private DataProvider dataProvider;

    @Override
    public String generateReport() {
        return "Annotations - injection par attribut : " + dataProvider.fetchData();
    }
}
