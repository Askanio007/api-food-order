package models.xmlOrder;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {

    //код (артикул) блюда позиции заказа;
    @XmlAttribute(name  = "Code")
    private String code;

    //количество блюда позиции заказа;
    @XmlAttribute(name  = "Qty")
    private long qty;

    @XmlAttribute(name  = "Remark")
    private String remark;

    //отдельный пакет гостя;
    @XmlAttribute(name  = "Guest")
    private String guest;

    @XmlValue
    private String value;

    public Product(String code, long qty) {
        this.code = code;
        this.qty = qty;
        this.value = "";
    }

    public static List<Product> convertToXMLForm(List<models.Product> foods) {
        List<Product> list = new ArrayList<>();
        foods.forEach(food -> list.add(new Product(food.getProductCode(), food.getCount())));
        return list;
    }

    public static List<Product> createXMLExample() {
        List<Product> list = new ArrayList<>();
        for (int i = 0; i< 10; i++)
            list.add(new Product("product code " + i, 5));
        return list;
    }

}
