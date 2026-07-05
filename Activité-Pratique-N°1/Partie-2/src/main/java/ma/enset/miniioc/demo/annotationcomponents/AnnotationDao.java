package ma.enset.miniioc.demo.annotationcomponents;

import ma.enset.miniioc.annotations.Component;
import ma.enset.miniioc.demo.common.DataProvider;

@Component("annotationDao")
public class AnnotationDao implements DataProvider {
    @Override
    public String fetchData() {
        return "données provenant de AnnotationDao";
    }
}
