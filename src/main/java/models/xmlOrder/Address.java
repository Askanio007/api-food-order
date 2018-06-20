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
public class Address {

    @XmlAttribute(name = "CityName")
    private String cityName;

    @XmlAttribute(name = "StationName")
    private String stationName;

    @XmlAttribute(name = "StreetName")
    private String streetName;

    @XmlAttribute(name = "House")
    private String house;

    @XmlAttribute(name = "Corpus")
    private String corpus;

    @XmlAttribute(name = "Building")
    private String building;

    @XmlAttribute(name = "Flat")
    private String flat;

    @XmlAttribute(name = "Porch")
    private String porch;

    @XmlAttribute(name = "Floor")
    private String floor;

    @XmlAttribute(name = "DoorCode")
    private String doorCode;

    @XmlValue
    private String value;

    public Address() {
        this.cityName = "Тула";
        this.stationName = "";
        this.streetName = "Николая Руднева";
        this.house = "28а";
        this.corpus = "";
        this.building = "";
        this.flat = "31";
        this.porch = "";
        this.floor = "3";
        this.doorCode = "";
        this.value = "";
    }

    public Address(String city, String street, String house, String flat, String floor) {
        this.cityName = city;
        this.stationName = "";
        this.streetName = street;
        this.house = house;
        this.corpus = "";
        this.building = "";
        this.flat = flat;
        this.porch = "";
        this.floor = floor;
        this.doorCode = "";
        this.value = "";
    }
}
