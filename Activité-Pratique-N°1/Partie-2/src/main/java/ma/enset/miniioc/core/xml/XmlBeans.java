package ma.enset.miniioc.core.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "beans")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlBeans {
    @XmlElement(name = "bean")
    private List<XmlBean> beans = new ArrayList<>();

    public List<XmlBean> getBeans() {
        return beans;
    }

    public void setBeans(List<XmlBean> beans) {
        this.beans = beans;
    }
}
