package dao;

import entity.Food;
import entity.Order;
import entity.User;
import enums.FoodType;
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

import static enums.FoodType.fullBusinessLunch;
import static enums.FoodType.otherType;
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

    @Override
    public List<Order> findByDate(Date from, Date to, String login, PaginationFilter paginationFilter) {
        CriteriaBuilder builder = builder();
        CriteriaQuery<Order> q = criteriaQuery(builder);
        Root<Order> order = rootEntity(q);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(order.get("accept"), true));
        predicates.add(builder.between(order.get("dateDeliveredOrder"), from, to));
        if (login != null && !"".equals(login)) {
            Join<Order, User> user = order.join("user");
            predicates.add(builder.equal(user.get("login"), login));
        }
        q.select(order).where(predicates.toArray(new Predicate[]{}));
        return createQuery(q).setMaxResults(paginationFilter.getLimit()).setFirstResult(paginationFilter.getOffset()).list();
    }

    @Override
    public long countOrderByDate(Date from, Date to, String login) {
        CriteriaBuilder builder = builder();
        CriteriaQuery<Long> q = builder.createQuery(Long.class);
        Root<Order> order = q.from(Order.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.equal(order.get("accept"), true));
        predicates.add(builder.between(order.get("dateDeliveredOrder"), from, to));
        if (login != null && !"".equals(login)) {
            Join<Order, User> user = order.join("user");
            predicates.add(builder.equal(user.get("login"), login));
        }
        q.select(builder.count(order)).where(predicates.toArray(new Predicate[]{}));
        Object count = createQuery(q).getSingleResult();
        return count != null ? (long)count : 0;
    }

    @Override
    public List<ReportByTypeFood> getReportByTypeFood(String userLogin) {
        DateFilter f = DateFilter.currentCashPeriod();
        CriteriaBuilder builder = session().getCriteriaBuilder();
        CriteriaQuery<ReportByTypeFood> q = builder.createQuery(ReportByTypeFood.class);
        Root<Order> order = q.from(Order.class);
        List<Predicate> predicates = new ArrayList<>();
        Join<Order, Food> foods = order.join("foods");
        Join<Food, entity.FoodType> types = foods.join("type");
        predicates.add(builder.between(order.get("dateDeliveredOrder"), f.getFrom(),f.getTo()));
        predicates.add(builder.equal(order.get("accept"), true));
        if (userLogin != null && !"".equals(userLogin)) {
            Join<Order, User> user = order.join("user");
            predicates.add(builder.equal(user.get("login"), userLogin));
        }
        q.multiselect(types.get("type"), builder.count(foods), builder.sum(foods.get("price")))
                .where(predicates.toArray(new Predicate[]{}))
                .groupBy(types.get("type"));
        return createQuery(q).list();
    }

    private BigDecimal sumTodayOrder(Date date, List<FoodType> listType) {
        CriteriaBuilder builder = session().getCriteriaBuilder();
        CriteriaQuery<BigDecimal> q = builder.createQuery(BigDecimal.class);
        Root<Order> order = q.from(Order.class);
        List<Predicate> predicates = new ArrayList<>();
        Join<Order, Food> foods = order.join("foods");
        Join<Food, entity.FoodType> types = foods.join("type");
        if (date != null)
            predicates.add(builder.equal(order.get("accept"), true));
        else
            predicates.add(builder.equal(order.get("accept"), false));
        if (date != null)
            predicates.add(builder.between(order.get("dateDeliveredOrder"), startDay(date), endDay(date)));
        if (listType.size() > 1)
            predicates.add(builder.notEqual(types.get("type"), FoodType.OTHER));
        if (listType.size() == 1)
            predicates.add(builder.equal(types.get("type"), FoodType.OTHER));
        q.select(builder.sum(foods.get("price"))).where(predicates.toArray(new Predicate[]{}));
        return getNumber(createQuery(q).uniqueResult());
    }

    @Override
    public BigDecimal amountMoneyTodayOrders() {
        return sumTodayOrder(null, fullBusinessLunch());
    }

    @Override
    public BigDecimal amountMoneyTodayOtherOrders() {
        return sumTodayOrder(null, otherType());
    }

    private List<ReportByNameFood> todayByNameFood(Date date, List<FoodType> listType) {
        CriteriaBuilder builder = session().getCriteriaBuilder();
        CriteriaQuery<ReportByNameFood> q = builder.createQuery(ReportByNameFood.class);
        Root<Order> order = q.from(Order.class);
        List<Predicate> predicates = new ArrayList<>();
        Join<Order, Food> foods = order.join("foods");
        Join<Food, entity.FoodType> types = foods.join("type");
        if (date != null)
            predicates.add(builder.equal(order.get("accept"), true));
        else
            predicates.add(builder.equal(order.get("accept"), false));
        if (date != null)
            predicates.add(builder.between(order.get("dateDeliveredOrder"), startDay(date), endDay(date)));
        if (listType.size() > 1)
            predicates.add(builder.notEqual(types.get("type"), FoodType.OTHER));
        if (listType.size() == 1)
            predicates.add(builder.equal(types.get("type"), FoodType.OTHER));
        q.multiselect(foods.get("productCode"), foods.get("name"), builder.count(foods), foods.get("price"), builder.sum(foods.get("price")))
                .where(predicates.toArray(new Predicate[]{}))
                .groupBy(foods.get("name"), foods.get("price"), foods.get("productCode"))
                .orderBy(builder.desc(builder.sum(foods.get("price"))));
        return createQuery(q).list();
    }

    @Override
    public List<ReportByNameFood> getTodayReportByNameFood() {
        return todayByNameFood(null, fullBusinessLunch());
    }

    @Override
    public List<ReportByNameFood> getTodayReportByTypeOtherFood() {
        return todayByNameFood(null, otherType());
    }

    @Override
    public List<ReportOrderPriceByDate> getOrderPriceByDate(String userLogin, Date from, Date to, PaginationFilter paginationFilter) {
        CriteriaBuilder builder = session().getCriteriaBuilder();
        CriteriaQuery<ReportOrderPriceByDate> q = builder.createQuery(ReportOrderPriceByDate.class);
        Root<Order> order = q.from(Order.class);
        List<Predicate> predicates = new ArrayList<>();
        Join<Order, Food> foods = order.join("foods");
        predicates.add(builder.between(order.get("dateDeliveredOrder"), from, to));
        predicates.add(builder.equal(order.get("accept"), true));
        if (userLogin != null && !"".equals(userLogin)) {
            Join<Order, User> user = order.join("user");
            predicates.add(builder.equal(user.get("login"), userLogin));
        }
        q.multiselect(order.get("dateDeliveredOrder").as(java.sql.Date.class), builder.sum(foods.get("price")))
                .where(predicates.toArray(new Predicate[]{}))
                .groupBy(order.get("dateDeliveredOrder").as(java.sql.Date.class));
        return createQuery(q).setFirstResult(paginationFilter.getOffset()).setMaxResults(paginationFilter.getLimit()).list();
    }

    @Override
    public long getCountOrderPriceByDate(String userLogin, Date from, Date to) {
        Object count = session().createNativeQuery(
                "SELECT count(*) FROM (SELECT count(date_delivered_order), CAST(date_delivered_order AS DATE) as fate " +
                "FROM order_user as ord " +
                "WHERE accept = TRUE AND " +
                "date_delivered_order BETWEEN :from AND :to " +
                "GROUP BY CAST(date_delivered_order AS DATE)) as my")
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();
        return count != null ? ((BigInteger)count).longValue() : 0;
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
    public BigDecimal amountMoneySumOrders(Date from, Date to) {
        Object sum = createQuery(
                "select " +
                        "sum(food.price) " +
                        "from " +
                        "Order ord " +
                        "join ord.foods food " +
                        "where " +
                        "ord.dateDeliveredOrder between :startDate and :endDate and " +
                        "ord.accept = true")
                .setParameter("startDate", from)
                .setParameter("endDate",to)
                .uniqueResult();
        return getNumber(sum);
    }



    @Override
    public BigDecimal amountSumReportByTypeFood() {
        DateFilter filter = DateFilter.currentCashPeriod();
        Object sum = createQuery(
                "select " +
                        "sum(food.price) " +
                        "from " +
                        "Order ord " +
                        "join ord.foods food " +
                        "join food.type type " +
                        "where " +
                        "ord.dateDeliveredOrder between :startDate and :endDate and " +
                        "ord.accept = true")
                .setParameter("startDate", filter.getFrom())
                .setParameter("endDate",filter.getTo())
                .uniqueResult();
        return getNumber(sum);
    }

    @Override
    public BigDecimal amountSumOrderPriceByDate() {
        DateFilter filter = DateFilter.currentCashPeriod();
        Object sum = createQuery(
                "select " +
                        "sum(food.price) " +
                        "from " +
                        "Order ord " +
                        "join ord.foods food " +
                        "where " +
                        "ord.dateDeliveredOrder between :startDate and :endDate and " +
                        "ord.accept = true")
                .setParameter("startDate", filter.getFrom())
                .setParameter("endDate",filter.getTo())
                .uniqueResult();
        return getNumber(sum);
    }

    @Override
    public BigDecimal amountSumByTypeFood(String userLogin) {
        DateFilter filter = DateFilter.currentCashPeriod();
        Object sum = createQuery(
                "select " +
                        "sum(food.price) " +
                        "from " +
                        "Order ord " +
                        "join ord.foods food " +
                        "join food.type type " +
                        "join ord.user user " +
                        "where " +
                        "user.login = :userLogin and " +
                        "ord.dateDeliveredOrder between :startDate and :endDate and " +
                        "ord.accept = true")
                .setParameter("startDate", filter.getFrom())
                .setParameter("endDate", filter.getTo())
                .setParameter("userLogin", userLogin)
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
    public List<ReportByNameFood> getReportByNameFood(Date date) {
        return todayByNameFood(date, fullBusinessLunch());
    }

    @Override
    public List<ReportByNameFood> getReportByTypeOtherFood(Date date) {
        return todayByNameFood(date, otherType());
    }

    @Override
    public BigDecimal amountMoneyOrders(Date date) {
        return sumTodayOrder(date, fullBusinessLunch());
    }

    @Override
    public BigDecimal amountMoneyOtherOrders(Date date) {
        return sumTodayOrder(date, otherType());
    }

    @Override
    public List<Order> orderByFood(long id) {
        return createQuery("select ord from Order as ord join ord.foods as food where food.id = :id and ord.accept = false").setParameter("id", id).list();
    }
}
