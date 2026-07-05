package ma.enset.miniioc.demo.annotationcomponents;

import ma.enset.miniioc.annotations.Autowired;
import ma.enset.miniioc.annotations.Component;
import ma.enset.miniioc.demo.common.DataProvider;
import ma.enset.miniioc.demo.common.ReportService;

@Component
public class AnnotationSetterService implements ReportService {
    private DataProvider dataProvider;

    @Autowired
    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public String generateReport() {
        return "Annotations - injection par setter : " + dataProvider.fetchData();
    }
}
