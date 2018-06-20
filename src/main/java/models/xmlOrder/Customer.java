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
public class Customer {

    @XmlAttribute(name = "AuthData")
    private String login;

    @XmlAttribute(name = "FIO")
    private String fio;

    @XmlValue
    private String value;

    public Customer() {
        this.login = "";
        this.fio = "";
        this.value = "";
    }

}
