package models.xmlProviderMenu;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement(name = "Item")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProviderItem {

    @XmlAttribute(name = "Code")
    private String code;

    @XmlAttribute(name = "ParentCode")
    private String parentCode;

    @XmlAttribute(name = "Name")
    private String name;

    @XmlAttribute(name = "NameEng")
    private String nameEng;

    @XmlAttribute(name = "Price")
    private String price;

    @XmlAttribute(name = "CodePicture")
    private String codePicture;

    @XmlAttribute(name = "Size")
    private String size;

    @XmlAttribute(name = "NameShort")
    private String nameShort;

    @XmlAttribute(name = "UnitName")
    private String unitName;

    @XmlAttribute(name = "Weight")
    private String weight;

    @XmlAttribute(name = "Energy")
    private String energy;

    @XmlAttribute(name = "Recommended")
    private String recommended;

    @XmlAttribute(name = "Description")
    private String description;

    @XmlAttribute(name = "DescriptionEng")
    private String descriptionEng;

    @XmlAttribute(name = "DiscountIgnore")
    private String discountIgnore;

    public ProviderItem() {
    }
}
