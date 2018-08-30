package models.xmlProviderMenu;

import lombok.Getter;
import lombok.Setter;
import models.xmlOrder.Product;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
@XmlRootElement(name = "Items")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProviderItems {

    @XmlElement(name = "Item")
    private List<ProviderItem> items;

    public ProviderItems() {
    }
}
