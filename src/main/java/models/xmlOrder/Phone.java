package models.xmlOrder;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlValue;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Phone {

    //номер телефона передается десятью цифрами.
    @XmlAttribute(name = "Number")
    private String number;

    @XmlValue
    private String value;

    public Phone() {
        this.number = "9207471983";
        this.value = "";
    }

    public Phone(String phone) {
        this.number = phone;
        this.value = "";

    }
}
