package models.xmlOrder;

import lombok.Getter;
import lombok.Setter;

import javax.xml.bind.annotation.*;
import java.util.Date;

@Getter
@Setter
@XmlRootElement(name = "Order")
@XmlAccessorType(XmlAccessType.FIELD)
public class XMLOrder {

    //способ оплаты (код или наименование из справочника Способы оплаты: Наличные/Безнал/Yandex Money/…);
    @XmlAttribute(name = "PayMethod")
    private String payMethod;

    //количество персон
    @XmlAttribute(name = "QtyPerson")
    private long qtyPerson;

    //тип заказа (1/2/3 = Доставка/С собой/Зал);
    @XmlAttribute(name = "Type")
    private long type;

    //флаг предварительной оплаты  заказа (0/1 = нет/да);
    @XmlAttribute(name = "PayStateID")
    private long payStateID;

    @XmlAttribute(name = "Remark")
    private String remark;

    //подготовить сдачу с…;
    @XmlAttribute(name = "RemarkMoney")
    private long remarkMoney;

    //доставить к…;
    @XmlAttribute(name = "TimePlan")
    private Date timePlan;

    //код бренда (для мультибрендовых конфигураций);
    @XmlAttribute(name = "Brand")
    private long brand;

    //код конкретного подразделения для выполнения заказа (для заказов «С собой»);
    @XmlAttribute(name = "Department")
    private long department;

    //процент скидки (указывается или для всего заказа (тег Order), или для каждого блюда (тег Product));
    @XmlAttribute(name = "DiscountPercent")
    private long discountPercent;

    //фиксированная сумма скидки;
    @XmlAttribute(name = "DiscountAmount")
    private long discountAmount;

    //сумма, списываемая с бонусного счета;
    @XmlAttribute(name = "Debt")
    private long debt;

    //промокод
    @XmlAttribute(name = "Promo")
    private String promo;

    @XmlElement(name = "Customer")
    private Customer customer;

    @XmlElement(name = "Address")
    private Address address;

    @XmlElement(name = "Phone")
    private Phone phone;

    @XmlElement(name = "Products")
    private Products products = new Products();

    public XMLOrder() {}

    public XMLOrder(long qtyPerson, Products products) {
        this.payMethod = "100001";
        this.type = 1;
        this.payStateID = 1;
        this.qtyPerson = qtyPerson;
        this.products = products;
        this.customer = new Customer();
    }

    public static XMLOrder exampleXML() {
        Products products = Products.createProducts();
        XMLOrder xml = new XMLOrder(5, products);
        return xml;
    }

    // http://91.193.237.45:2000/FastOperator.asmx?op=AddOrder
    /* <Order PayMethod="100000001" QtyPerson="1" Type="1" PayStateID="0" Remark="Меньше соли" RemarkMoney="5000" TimePlan="14.12.2015 15:00" Brand="" Department="100000001" DiscountPercent="10"
   DiscountAmount="100" Debt="100" Promo="215667">
       <Customer AuthData="login@mail.ru" FIO="Иванов Иван Иванович"/>
       <Address CityName = "" StationName="" StreetName="" House="" Corpus="" Building="" Flat="" Porch="" Floor="" DoorCode=""/>
       <Phone Number="1234567890"/>
       <Products>
              <Product Code="" Qty="" Remark ="не пережаривать" Guest="Вася">
                     <Modificators>
                            <Product Code=""/>
                            <Product Code=""/>
                     </Modificators>
                     <Combo>
                            <Product Code=""/>
                            <Product Code=""/>
                     </Combo>
              </Product>
              <Product Code="" Qty=""/>
       </Products>
</Order>*/


}
