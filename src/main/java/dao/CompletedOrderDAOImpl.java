package dao;

import dto.FoodTypeDto;
import entity.*;
import entity.Order;
import models.reports.ReportByNameFood;
import models.reports.ReportByTypeFood;
import models.reports.ReportOrderPriceByDate;
import org.springframework.stereotype.Repository;
import utils.DateFilter;
import utils.PaginationFilter;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static utils.DateBuilder.endDay;
import static utils.DateBuilder.startDay;

@Repository("completedOrder")
public class CompletedOrderDAOImpl extends GenericDAOImpl<CompletedOrder> implements CompletedOrderDao {

    @Override
    public List<ReportOrderPriceByDate> getOrderPriceByDate(Date from, Date to, PaginationFilter paginationFilter) {
        CriteriaBuilder builder = session().getCriteriaBuilder();
        CriteriaQuery<ReportOrderPriceByDate> q = builder.createQuery(ReportOrderPriceByDate.class);
        Root<CompletedOrder> order = q.from(CompletedOrder.class);
        List<Predicate> predicates = new ArrayList<>();
        Join<CompletedOrder, CompletedOrderItem> foods = order.join("items");
        predicates.add(builder.between(order.get("dateDeliveredOrder"), from, to));
        q.multiselect(order.get("dateDeliveredOrder").as(java.sql.Date.class), builder.sum(foods.get("price")))
                .where(predicates.toArray(new Predicate[]{}))
                .groupBy(order.get("dateDeliveredOrder").as(java.sql.Date.class));
        return createQuery(q).setFirstResult(paginationFilter.getOffset()).setMaxResults(paginationFilter.getLimit()).list();
    }

    @Override
    public BigDecimal amountMoneySumOrders(Date from, Date to) {
        Object sum = createQuery(
                "select " +
                        "sum(food.price) " +
                        "from " +
                        "CompletedOrder ord " +
                        "join ord.items food " +
                        "where " +
                        "ord.dateDeliveredOrder between :startDate and :endDate")
                .setParameter("startDate", from)
                .setParameter("endDate",to)
                .uniqueResult();
        return getNumber(sum);
    }

    @Override
    public long getCountOrderPriceByDate(Date from, Date to) {
        Object count = session().createNativeQuery(
                "SELECT count(*) FROM (SELECT count(date_delivered_order), CAST(date_delivered_order AS DATE) as fate " +
                        "FROM order_completed as ord " +
                        "WHERE date_delivered_order BETWEEN :from AND :to " +
                        "GROUP BY CAST(date_delivered_order AS DATE)) as my")
                .setParameter("from", from)
                .setParameter("to", to)
                .getSingleResult();
        return count != null ? ((BigInteger)count).longValue() : 0;
    }

    @Override
    public long countOrderByDate(Date from, Date to) {
        CriteriaBuilder builder = builder();
        CriteriaQuery<Long> q = builder.createQuery(Long.class);
        Root<CompletedOrder> order = q.from(CompletedOrder.class);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.between(order.get("dateDeliveredOrder"), from, to));
        q.select(builder.count(order)).where(predicates.toArray(new Predicate[]{}));
        Object count = createQuery(q).getSingleResult();
        return count != null ? (long)count : 0;
    }

    @Override
    public List<CompletedOrder> findByDate(Date from, Date to, PaginationFilter paginationFilter) {
        CriteriaBuilder builder = builder();
        CriteriaQuery<CompletedOrder> q = criteriaQuery(builder);
        Root<CompletedOrder> order = rootEntity(q);
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(builder.between(order.get("dateDeliveredOrder"), from, to));
        q.select(order).where(predicates.toArray(new Predicate[]{}));
        return createQuery(q).setMaxResults(paginationFilter.getLimit()).setFirstResult(paginationFilter.getOffset()).list();
    }

    @Override
    public List<ReportByTypeFood> getReportByTypeFood(String userLogin, DateFilter cashPeriod) {
        CriteriaBuilder builder = session().getCriteriaBuilder();
        CriteriaQuery<ReportByTypeFood> q = builder.createQuery(ReportByTypeFood.class);
        Root<CompletedOrder> order = q.from(CompletedOrder.class);
        List<Predicate> predicates = new ArrayList<>();
        Join<CompletedOrder, CompletedOrderItem> foods = order.join("items");
        predicates.add(builder.between(order.get("dateDeliveredOrder"), cashPeriod.getFrom(),cashPeriod.getTo()));
        if (userLogin != null && !"".equals(userLogin)) {
            Join<CompletedOrder, User> user = order.join("user");
            predicates.add(builder.equal(user.get("login"), userLogin));
        }
        q.multiselect(foods.get("type"), builder.count(foods), builder.sum(foods.get("price")))
                .where(predicates.toArray(new Predicate[]{}))
                .groupBy(foods.get("type"));
        return createQuery(q).list();
    }

    @Override
    public BigDecimal amountSumReportByTypeFood(DateFilter cashPeriod) {
        Object sum = createQuery(
                "select " +
                        "sum(food.price) " +
                        "from " +
                        "CompletedOrder ord " +
                        "join ord.items food " +
                        "where " +
                        "ord.dateDeliveredOrder between :startDate and :endDate")
                .setParameter("startDate", cashPeriod.getFrom())
                .setParameter("endDate",cashPeriod.getTo())
                .uniqueResult();
        return getNumber(sum);
    }

    @Override
    public BigDecimal amountSumByTypeFood(String userLogin, DateFilter cashPeriod) {
        Object sum = createQuery(
                "select " +
                        "sum(food.price) " +
                        "from " +
                        "CompletedOrder ord " +
                        "join ord.items food " +
                        "join ord.user user " +
                        "where " +
                        "user.login = :userLogin and " +
                        "ord.dateDeliveredOrder between :startDate and :endDate")
                .setParameter("startDate", cashPeriod.getFrom())
                .setParameter("endDate", cashPeriod.getTo())
                .setParameter("userLogin", userLogin)
                .uniqueResult();
        return getNumber(sum);
    }

    public List<ReportByNameFood> getReportByNameFood(Date date, List<FoodTypeDto> listType) {
        CriteriaBuilder builder = session().getCriteriaBuilder();
        CriteriaQuery<ReportByNameFood> q = builder.createQuery(ReportByNameFood.class);
        Root<CompletedOrder> order = q.from(CompletedOrder.class);
        List<Predicate> predicates = new ArrayList<>();
        Join<CompletedOrder, CompletedOrderItem> foods = order.join("items");
        if (date != null)
            predicates.add(builder.between(order.get("dateDeliveredOrder"), startDay(date), endDay(date)));
        List<Predicate> predicatesFoodTypes = new ArrayList<>();
        listType.forEach(type -> predicatesFoodTypes.add(builder.equal(foods.get("type"), type.getType())));
        predicates.add(builder.or(predicatesFoodTypes.toArray(new Predicate[]{})));
        q.multiselect(foods.get("productCode"), foods.get("name"), builder.count(foods), foods.get("price"), builder.sum(foods.get("price")))
                .where(predicates.toArray(new Predicate[]{}))
                .groupBy(foods.get("name"), foods.get("price"), foods.get("productCode"))
                .orderBy(builder.desc(builder.sum(foods.get("price"))));
        return createQuery(q).list();
    }

    public BigDecimal amountMoneyOrders(Date date, List<FoodTypeDto> listType) {
        CriteriaBuilder builder = session().getCriteriaBuilder();
        CriteriaQuery<BigDecimal> q = builder.createQuery(BigDecimal.class);
        Root<CompletedOrder> order = q.from(CompletedOrder.class);
        List<Predicate> predicates = new ArrayList<>();
        Join<CompletedOrder, CompletedOrderItem> foods = order.join("items");
        if (date != null)
            predicates.add(builder.between(order.get("dateDeliveredOrder"), startDay(date), endDay(date)));
        List<Predicate> predicatesFoodTypes = new ArrayList<>();
        listType.forEach(type -> predicatesFoodTypes.add(builder.equal(foods.get("type"), type.getType())));
        predicates.add(builder.or(predicatesFoodTypes.toArray(new Predicate[]{})));
        q.select(builder.sum(foods.get("price"))).where(predicates.toArray(new Predicate[]{}));
        return getNumber(createQuery(q).uniqueResult());
    }
}
