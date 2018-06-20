package models.xmlProviderResponse;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@XmlRootElement(name = "string", namespace = "www.fast-operator.ru")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProviderResponse {

    @XmlElement(name = "Order", namespace = "www.fast-operator.ru")
    private InformationOrder order;


}
