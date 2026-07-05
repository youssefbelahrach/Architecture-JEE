package ma.enset.miniioc.demo.annotationcomponents;

import ma.enset.miniioc.annotations.Autowired;
import ma.enset.miniioc.annotations.Component;
import ma.enset.miniioc.demo.common.DataProvider;
import ma.enset.miniioc.demo.common.ReportService;

@Component
public class AnnotationConstructorService implements ReportService {
    private final DataProvider dataProvider;

    @Autowired
    public AnnotationConstructorService(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public String generateReport() {
        return "Annotations - injection par constructeur : " + dataProvider.fetchData();
    }
}
