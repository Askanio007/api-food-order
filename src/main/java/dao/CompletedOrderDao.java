package dao;

import dto.FoodTypeDto;
import entity.CompletedOrder;
import models.reports.ReportByNameFood;
import models.reports.ReportByTypeFood;
import models.reports.ReportOrderPriceByDate;
import utils.DateFilter;
import utils.PaginationFilter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CompletedOrderDao extends GenericDao<CompletedOrder> {

    List<ReportOrderPriceByDate> getOrderPriceByDate(Date from, Date to, PaginationFilter paginationFilter);

    BigDecimal amountMoneySumOrders(Date from, Date to);

    long getCountOrderPriceByDate(Date from, Date to);

    long countOrderByDate(Date from, Date to);

    List<CompletedOrder> findByDate(Date from, Date to, PaginationFilter paginationFilter);

    List<ReportByTypeFood> getReportByTypeFood(String userLogin, DateFilter cashPeriod);

    BigDecimal amountSumReportByTypeFood(DateFilter cashPeriod);

    BigDecimal amountSumByTypeFood(String userLogin, DateFilter cashPeriod);

    List<ReportByNameFood> getReportByNameFood(Date date, List<FoodTypeDto> listType);

    BigDecimal amountMoneyOrders(Date date, List<FoodTypeDto> listType);
}
