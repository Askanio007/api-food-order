package models.xmlProviderMenu;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

    @XmlAttribute(name  = "id")
    private String id;

    @XmlElement(name = "category_id")
    private CategoryId category_id;

    @XmlElement(name = "name")
    private Name name;

    @XmlElement(name = "price")
    private Price price;

    @XmlElement(name = "picture")
    private Picture picture;

    @XmlElement(name = "variants")
    private Variants variants;

    @XmlElement(name = "ingredients")
    private Ingredients ingredients;
}
