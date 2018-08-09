package models.xmlProviderMenu;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Variant {

    @XmlAttribute(name = "title")
    private String title;

    @XmlAttribute(name = "price")
    private String price;

    @XmlAttribute(name = "id")
    private String id;
}
