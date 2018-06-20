package utils;
import converter.DateConverter;
import models.filters.ReportFilters;

import java.util.Date;

import static utils.DateBuilder.*;

public class DateFilter {

    private final Date from;
    private final Date to;
    private final String fromView;
    private final String toView;
    private final String fromViewRU;
    private final String toViewRU;

    public DateFilter(){
            this(null, null);
    }

    public DateFilter(Date from, Date to) {
        this.from = startDay(new Date(checkNullFrom(from).getTime()));
        this.to = startDay(new Date(checkNullTo(to).getTime()));
        this.fromView = formatView(this.from);
        this.toView = formatView(this.to);
        this.fromViewRU = formatViewRU(this.from);
        this.toViewRU = formatViewRU(this.to);
    }

    public DateFilter(Date day) {
        this.from = new Date(DateBuilder.startDay(day).getTime());
        this.to = new Date(DateBuilder.endDay(day).getTime());
        this.fromView = null;
        this.toView = null;
        this.fromViewRU = null;
        this.toViewRU = null;
    }

    private Date checkNullFrom(Date date) {
        return date == null ? new Date(1) : date;
    }
    private Date checkNullTo(Date date) {
        return date == null ? getTomorrow() : date;
    }

    private String formatView(Date date) {
        return DateConverter.getFormatView().format(date);
    }

    private String formatViewRU(Date date) {
        return DateConverter.getFormatViewRU().format(date);
    }

    public String getFromWithoutTime() {
        return fromView;
    }
    public String getToWithoutTime() {
        return toView;
    }
    public String getFromWithoutTimeRU() {
        return fromViewRU;
    }
    public String getToWithoutTimeRU() {
        return toViewRU;
    }
    public Date getFrom() {
        return (Date)from.clone();
    }
    public Date getTo() {
        return (Date)to.clone();
    }

    public static DateFilter currentCashPeriod() {
        return new DateFilter(firstDayCurrentCashPeriod(),lastDayCurrentCashPeriod());
    }

    public static DateFilter lastCashPeriod() {
        return new DateFilter(firstDayLastCashPeriod(),lastDayLastCashPeriod());
    }

    public static DateFilter generateDateFilter(ReportFilters reportFilters) {
        return new DateFilter(reportFilters.getFrom(), reportFilters.getTo());
    }

    // Joshua Bloch "Effective java 2rd edition" - chapter "Make defensive copies when needed"
}
