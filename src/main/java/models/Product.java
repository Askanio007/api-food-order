package models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Product {

    private String productCode;

    private long count;

    public Product(String productCode, long count) {
        this.productCode = productCode;
        this.count = count;
    }
}
