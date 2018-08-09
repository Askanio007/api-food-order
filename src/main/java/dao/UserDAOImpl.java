package dao;

import entity.AutoOrder;
import entity.Role;
import entity.User;
import enums.RoleType;
import models.reports.ReportByTodayOrder;
import models.reports.ReportUserByMoney;
import org.hibernate.query.Query;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;
import utils.PaginationFilter;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static utils.DateBuilder.*;

@Repository("userDao")
public class UserDAOImpl extends GenericDAOImpl<User> implements UserDao {

    @Override
    public boolean isActive(String login) {
        Object ob = createQuery("select enabled from User where login = :login").setParameter("login", login).uniqueResult();
        return ob != null && (boolean)ob;
    }

    @Override
    public List<AutoOrder> findAutoOrders() {
        return createQuery("select ao from User as user join user.autoOrder as ao where ao.active = true").list();
    }

    @Override
    public AutoOrder findAutoOrder(String name) {
        Object ob = createQuery("select ao from User as user join user.autoOrder as ao where user.login = :name").setParameter("name", name).uniqueResult();
        return ob != null ? (AutoOrder) ob : null;
    }

    public User find(String login) {
        return (User) createQuery("from User where login = :login").setParameter("login", login).uniqueResult();
    }

    @Override
    public List<ReportByTodayOrder> findTodayOrder(Date dateAcceptMenu) {
        List<ReportByTodayOrder> list = createQuery(
                "select u.login as login, u.name as name, orders as foodName " +
                        "from User u " +
                        "join u.orders as orders " +
                        "where " +
                        "(orders.dateOrder < :date and orders.dateDeliveredOrder is null) or " +
                        "orders.dateDeliveredOrder between :from and :to" )
                .setParameter("date", dateAcceptMenu)
                .setParameter("from", startDay(today()))
                .setParameter("to", endDay(today()))
                .setResultTransformer(Transformers.aliasToBean(ReportByTodayOrder.class))
                .list();
        return list;
    }

    private String USER_BY_MONEY_SUBQUERY = "select sum(o.price) from u.completedOrders as o where u = o.user and o.dateDeliveredOrder between :from and :to";
    private String USER_BY_MONEY =
            "select " +
                "u.id as id, u.login as login, u.name as name, (" + USER_BY_MONEY_SUBQUERY + " ) as money " +
            "from " +
                "User as u " +
                "join u.role as r " +
            "where " +
                "r.id = :role";

    @Override
    public List<ReportUserByMoney> find(Date from, Date to, PaginationFilter paginationFilter, String name) {
        String query = USER_BY_MONEY;
        if (name != null && !"".equals(name)) {
            query = USER_BY_MONEY + " and u.name like :name";
        }
        Query q = createQuery(query);
        if (name != null && !"".equals(name)) {
            q.setParameter("name",  "%" + name + "%");
        }
        List<ReportUserByMoney> list = (List<ReportUserByMoney>) q
                .setParameter("from", from)
                .setParameter("to", to)
                .setParameter("role", RoleType.ROLE_USER.getId())
                .setMaxResults(paginationFilter.getLimit())
                .setFirstResult(paginationFilter.getOffset())
                .setResultTransformer(Transformers.aliasToBean(ReportUserByMoney.class))
                .list();
        return list;
    }

    @Override
    public BigDecimal amountSumCustomerByMoney(Date from, Date to, String name) {
        String SUM_MONEY_BY_CUSTOMER =
                "select " +
                    "sum(o.price) " +
                "from " +
                    "User as u " +
                    "join u.role as r " +
                    "join u.completedOrders as o " +
                "where " +
                    "r.id = :role and " +
                    "o.dateDeliveredOrder between :from and :to";
        if (name != null && !"".equals(name))
            SUM_MONEY_BY_CUSTOMER = SUM_MONEY_BY_CUSTOMER + " and u.name like :name";
        Query q = createQuery(SUM_MONEY_BY_CUSTOMER);
        if (name != null && !"".equals(name))
            q.setParameter("name",  "%" + name + "%");
        Object sum = q
                .setParameter("from", from)
                .setParameter("to", to)
                .setParameter("role", RoleType.ROLE_USER.getId())
                .uniqueResult();
        return getNumber(sum);
    }

    @Override
    public List<User> find(PaginationFilter paginationFilter, long roleId, String login) {
        CriteriaBuilder builder = session().getCriteriaBuilder();
        CriteriaQuery<User> q = builder.createQuery(User.class);
        Root<User> user = q.from(User.class);
        List<Predicate> predicates = new ArrayList<>();
        Join<User, Role> role = user.join("role");
        if (login != null)
            predicates.add(builder.like(user.get("login"), "%" + login + "%"));
        if (roleId > 0)
            predicates.add(builder.equal(role.get("id"), roleId));
        q.select(user)
                .where(predicates.toArray(new Predicate[]{}))
                .orderBy(builder.desc(user.get("id")));
        return createQuery(q)
                .setFirstResult(paginationFilter.getOffset())
                .setMaxResults(paginationFilter.getLimit())
                .list();
    }

    @Override
    public long countAllCustomer(String name) {
        String query = "select count(u) from User as u join u.role as role where role.id = :role";
        if (name != null && !"".equals(name))
            query = query + " and u.name like :name";
        Query q = createQuery(query);
        if (name != null && !"".equals(name))
            q.setParameter("name", "%" + name + "%");
        Object count = q
                .setParameter("role", RoleType.ROLE_USER.getId())
                .getSingleResult();
        return count != null ? (long)count : 0;
    }

    @Override
    public long countAll(String login, long roleId) {
        CriteriaBuilder builder = session().getCriteriaBuilder();
        CriteriaQuery<Long> q = builder.createQuery(Long.class);
        Root<User> user = q.from(User.class);
        List<Predicate> predicates = new ArrayList<>();
        Join<User, Role> role = user.join("role");
        if (login != null)
            predicates.add(builder.like(user.get("login"), "%" + login + "%"));
        if (roleId > 0)
            predicates.add(builder.equal(role.get("id"), roleId));
        q.select(builder.count(user))
                .where(predicates.toArray(new Predicate[]{}));
        Object count = createQuery(q).getSingleResult();
        return count != null ? (long)count : 0;
    }

    @Override
    public void updateUserBalance(BigDecimal balance, Role role) {
        createQuery("update User set balance = :balance where role = :role")
                .setParameter("balance", balance)
                .setParameter("role", role)
                .executeUpdate();
    }
}