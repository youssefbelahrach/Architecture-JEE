package ma.enset.miniioc.core.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlField {
    @XmlAttribute(required = true)
    private String name;

    @XmlAttribute(required = true)
    private String ref;

    public String getName() {
        return name;
    }

    public String getRef() {
        return ref;
    }
}
