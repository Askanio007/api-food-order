package models.xmlOrder;

import entity.Order;
import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.util.List;
import static models.xmlOrder.Product.convertToXMLForm;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Products {

    @XmlElement(name = "Product")
    private List<Product> products;

    public Products(List<models.Product> products) {
        this.products = convertToXMLForm(products);
    }

    public static Products createProducts() {
        Products pr = new Products();
        pr.setProducts(Product.createXMLExample());
        return pr;
    }

    public Products() {}
}
