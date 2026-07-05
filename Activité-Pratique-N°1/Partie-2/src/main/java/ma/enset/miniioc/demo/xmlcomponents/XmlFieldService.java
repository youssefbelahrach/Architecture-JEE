package ma.enset.miniioc.demo.xmlcomponents;

import ma.enset.miniioc.demo.common.DataProvider;
import ma.enset.miniioc.demo.common.ReportService;

public class XmlFieldService implements ReportService {
    // Aucun setter : le framework affecte ce champ directement grâce à la réflexion.
    private DataProvider dataProvider;

    @Override
    public String generateReport() {
        return "XML - injection par attribut : " + dataProvider.fetchData();
    }
}
