package models.xmlProviderResponse;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement(name = "Order")@XmlAccessorType(XmlAccessType.FIELD)
public class InformationOrder {

    @XmlAttribute(name = "ID")
    private String id;

    @XmlAttribute(name = "Code")
    private String code;
}
