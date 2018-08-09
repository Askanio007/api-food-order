package models.xmlProviderMenu;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;

@Getter
@Setter
@XmlRootElement(name = "dc_catalog")
@XmlAccessorType(XmlAccessType.FIELD)
public class XmlMenu {

    @XmlAttribute(name = "last_update")
    private String last_update;

    @XmlElement(name = "delivery_service")
    private DeliveryService delivery_service;

    public XmlMenu() {
    }
}
