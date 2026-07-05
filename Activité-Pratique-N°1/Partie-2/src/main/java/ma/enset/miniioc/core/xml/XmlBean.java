package ma.enset.miniioc.core.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlElement;

import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class XmlBean {
    @XmlAttribute(required = true)
    private String id;

    @XmlAttribute(name = "class", required = true)
    private String className;

    @XmlElement(name = "constructor-arg")
    private List<XmlConstructorArg> constructorArgs = new ArrayList<>();

    @XmlElement(name = "property")
    private List<XmlProperty> properties = new ArrayList<>();

    @XmlElement(name = "field")
    private List<XmlField> fields = new ArrayList<>();

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public List<XmlConstructorArg> getConstructorArgs() {
        return constructorArgs;
    }

    public List<XmlProperty> getProperties() {
        return properties;
    }

    public List<XmlField> getFields() {
        return fields;
    }
}
