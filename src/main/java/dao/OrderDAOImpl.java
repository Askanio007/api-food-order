package dao;

import dto.FoodTypeDto;
import entity.Food;
import entity.FoodType;
import entity.Order;
import entity.User;
import models.Product;
import models.reports.ReportByNameFood;
import models.reports.ReportByTypeFood;
import models.reports.ReportOrderPriceByDate;
import org.springframework.stereotype.Repository;
import utils.DateFilter;
import utils.PaginationFilter;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static utils.DateBuilder.endDay;
import static utils.DateBuilder.startDay;
import static utils.DateBuilder.today;

@Repository("orderDao")
public class OrderDAOImpl extends GenericDAOImpl<Order> implements OrderDao {

    @Override
    public void deleteTodayOrders(long id) {
        session().createNativeQuery("DELETE FROM order_foods WHERE order_id = :id").setParameter("id", id).executeUpdate();
        session().createNativeQuery("DELETE FROM order_user WHERE id = :id").setParameter("id", id).executeUpdate();
    }

    private CriteriaQuery findOrder(String name) {
        CriteriaBuilder builder = builder();
        CriteriaQuery<Order> q = criteriaQuery(builder);
        Root<Order> order = rootEntity(q);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(order.get("accept"), false));
        if (name != null && !"".equals(name)) {
            Join<Order, User> user = order.join("user");
            predicates.add(builder.equal(user.get("login"), name));
        }
        q.select(order).where(predicates.toArray(new Predicate[]{}));
        return q;
    }

    @Override
    public List<Order> findAllOrder() {
        CriteriaBuilder builder = builder();
        CriteriaQuery<Order> q = criteriaQuery(builder);
        Root<Order> order = rootEntity(q);
        q.select(order).where(builder.or(builder.between(order.get("dateOrder"), startDay(today()), endDay(today())),
                                        builder.between(order.get("dateDeliveredOrder"), startDay(today()), endDay(today()))));
        return createQuery(q).list();
    }

    @Override
    public Order findToday(String name) {
        Object ob = createQuery(findOrder(name)).uniqueResult();
        return ob != null ?  (Order) ob : null;
    }

    @Override
    public List<Order> findToday() {
        return createQuery(findOrder(null)).list();
    }

    private BigDecimal sumTodayOrder(Date date, List<FoodTypeDto> listType) {
        CriteriaBuilder builder = session().getCriteriaBuilder();
        CriteriaQuery<BigDecimal> q = builder.createQuery(BigDecimal.class);
        Root<Order> order = q.from(Order.class);
        List<Predicate> predicates = new ArrayList<>();
        Join<Order, Food> foods = order.join("foods");
        Join<Food, FoodType> types = foods.join("type");
        predicates.add(builder.equal(order.get("accept"), date != null));
        if (date != null)
            predicates.add(builder.between(order.get("dateDeliveredOrder"), startDay(date), endDay(date)));
        List<Predicate> predicatesFoodTypes = new ArrayList<>();
        listType.forEach(type -> predicatesFoodTypes.add(builder.equal(types.get("type"), type.getType())));
        predicates.add(builder.or(predicatesFoodTypes.toArray(new Predicate[]{})));
        q.select(builder.sum(foods.get("price"))).where(predicates.toArray(new Predicate[]{}));
        return getNumber(createQuery(q).uniqueResult());
    }

    @Override
    public BigDecimal amountMoneyTodayOrders(List<FoodTypeDto> listType) {
        return sumTodayOrder(null, listType);
    }

    private List<ReportByNameFood> todayByNameFood(Date date, List<FoodTypeDto> listType) {
        CriteriaBuilder builder = session().getCriteriaBuilder();
        CriteriaQuery<ReportByNameFood> q = builder.createQuery(ReportByNameFood.class);
        Root<Order> order = q.from(Order.class);
        List<Predicate> predicates = new ArrayList<>();
        Join<Order, Food> foods = order.join("foods");
        Join<Food, entity.FoodType> types = foods.join("type");
        predicates.add(builder.equal(order.get("accept"), date != null));
        if (date != null)
            predicates.add(builder.between(order.get("dateDeliveredOrder"), startDay(date), endDay(date)));
        List<Predicate> predicatesFoodTypes = new ArrayList<>();
        listType.forEach(type -> predicatesFoodTypes.add(builder.equal(types.get("type"), type.getType())));
        predicates.add(builder.or(predicatesFoodTypes.toArray(new Predicate[]{})));
        q.multiselect(foods.get("productCode"), foods.get("name"), builder.count(foods), foods.get("price"), builder.sum(foods.get("price")))
                .where(predicates.toArray(new Predicate[]{}))
                .groupBy(foods.get("name"), foods.get("price"), foods.get("productCode"))
                .orderBy(builder.desc(builder.sum(foods.get("price"))));
        return createQuery(q).list();
    }

    @Override
    public List<ReportByNameFood> getTodayReportByNameFood(List<FoodTypeDto> listType) {
        return todayByNameFood(null, listType);
    }

    @Override
    public BigDecimal amountMoneySumOrders(String login, Date from, Date to) {
        Object sum = createQuery(
                "select " +
                        "sum(food.price) " +
                        "from " +
                        "Order ord " +
                        "join ord.foods food " +
                        "join ord.user user " +
                        "where " +
                        "ord.dateDeliveredOrder between :startDate and :endDate and " +
                        "user.login = :login and " +
                        "ord.accept = true")
                .setParameter("login", login)
                .setParameter("startDate", from)
                .setParameter("endDate",to)
                .uniqueResult();
        return getNumber(sum);
    }

    @Override
    public BigDecimal sumTodayOrder(String name) {
        Object sum = createQuery(
                "select " +
                        "sum(food.price) " +
                        "from " +
                        "Order ord " +
                        "join ord.foods food " +
                        "join ord.user user " +
                        "where " +
                        "user.login = :login and " +
                        "ord.accept = false")
                .setParameter("login", name)
                .uniqueResult();
        return getNumber(sum);
    }

    @Override
    public boolean todayOrdersAccept() {
        long count = (long) createQuery("select count(*) from Order where accept = false")
                .uniqueResult();
        return count == 0;
    }

    @Override
    public boolean todayOrdersAccept(Date dateAcceptingOrder) {
        long count = (long) createQuery("select count(*) from Order where accept = false and dateOrder < :date")
                .setParameter("date", dateAcceptingOrder)
                .uniqueResult();
        return count == 0;
    }

    @Override
    public List<Product> getFullTodayOrder() {
        CriteriaBuilder builder = session().getCriteriaBuilder();
        CriteriaQuery<Product> q = builder.createQuery(Product.class);
        Root<Order> order = q.from(Order.class);
        List<Predicate> predicates = new ArrayList<>();
        Join<Order, Food> foods = order.join("foods");
        predicates.add(builder.equal(order.get("accept"), false));
        q.multiselect(foods.get("productCode"), builder.count(foods))
                .where(predicates.toArray(new Predicate[]{}))
                .groupBy(foods.get("productCode"), foods.get("price"))
                .orderBy(builder.desc(builder.sum(foods.get("price"))));
        return createQuery(q).list();
    }

    @Override
    public long countTodayOrders() {
        Object ob = createQuery("select count (*) from Order as ord where accept = false").uniqueResult();
        return ob != null ? (long) ob : 0;
    }

    @Override
    public List<Order> orderByFood(long id) {
        return createQuery("select ord from Order as ord join ord.foods as food where food.id = :id and ord.accept = false").setParameter("id", id).list();
    }
}
