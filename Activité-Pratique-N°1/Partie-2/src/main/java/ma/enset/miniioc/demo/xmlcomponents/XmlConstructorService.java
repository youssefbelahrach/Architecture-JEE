package ma.enset.miniioc.demo.xmlcomponents;

import ma.enset.miniioc.demo.common.DataProvider;
import ma.enset.miniioc.demo.common.ReportService;

public class XmlConstructorService implements ReportService {
    private final DataProvider dataProvider;

    public XmlConstructorService(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public String generateReport() {
        return "XML - injection par constructeur : " + dataProvider.fetchData();
    }
}
