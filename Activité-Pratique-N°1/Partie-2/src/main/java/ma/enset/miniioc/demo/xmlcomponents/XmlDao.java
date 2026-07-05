package ma.enset.miniioc.demo.xmlcomponents;

import ma.enset.miniioc.demo.common.DataProvider;

public class XmlDao implements DataProvider {
    @Override
    public String fetchData() {
        return "données provenant de XmlDao";
    }
}
