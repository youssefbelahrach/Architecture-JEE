package ma.enset.miniioc.demo.xmlcomponents;

import ma.enset.miniioc.demo.common.DataProvider;
import ma.enset.miniioc.demo.common.ReportService;

public class XmlSetterService implements ReportService {
    private DataProvider dataProvider;

    public void setDataProvider(DataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    @Override
    public String generateReport() {
        return "XML - injection par setter : " + dataProvider.fetchData();
    }
}
