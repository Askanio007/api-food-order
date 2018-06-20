package utils;

import java.util.Calendar;
import java.util.Date;

public class DateBuilder {

    private DateBuilder() {}

    private static Calendar getCurrentTime() {
        return (Calendar) Calendar.getInstance().clone();
    }

    public static Calendar getCalendar(Date date) {
        Calendar c = getCurrentTime();
        c.setTime(date);
        return c;
    }

    public static Date endDay(Date date) {
        Calendar calendar = getCurrentTime();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 1);
        calendar.setTime(startDay(calendar.getTime()));
        calendar.add(Calendar.MILLISECOND, -1);
        return calendar.getTime();
    }

    public static Date startDay(Date date) {
        Calendar calendar = getCurrentTime();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date timeAcceptOrders(String time) {
        String[] times = time.split(":");
        Calendar calendar = getCurrentTime();
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(times[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(times[1]));
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTime();
    }

    public static Date getTomorrow() {
        Calendar calendar = getCurrentTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return startDay(calendar.getTime());
    }

    public static Date firstDayCurrentMonth() {
        Calendar calendar = getCurrentTime();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    public static Date getTenMinutesBeforeOrder(Date dateAcceptOrder) {
        Calendar calendar = getCurrentTime();
        calendar.setTime(dateAcceptOrder);
        calendar.add(Calendar.MINUTE, -10);
        return calendar.getTime();
    }

    public static Date addOneMinutes(Date date) {
        Calendar calendar = getCurrentTime();
        calendar.setTime(date);
        calendar.add(Calendar.SECOND, 10);
        return calendar.getTime();
    }

    public static Date firstDayLastCashPeriod() {
        Calendar calendar = getCurrentTime();
        if (calendar.get(Calendar.DAY_OF_MONTH) < 21) {
            calendar.add(Calendar.MONTH, -2);
        } else {
            calendar.add(Calendar.MONTH, -1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 21);
        return startDay(calendar.getTime());
    }

    public static Date lastDayLastCashPeriod() {
        Calendar calendar = getCurrentTime();
        if (calendar.get(Calendar.DAY_OF_MONTH) < 21)
            calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DAY_OF_MONTH, 21);
        return startDay(calendar.getTime());
    }

    public static Date firstDayCurrentCashPeriod() {
        Calendar calendar = getCurrentTime();
        if (calendar.get(Calendar.DAY_OF_MONTH) < 21) {
            calendar.add(Calendar.MONTH, -1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 21);
        return startDay(calendar.getTime());
    }

    public static Date lastDayCurrentCashPeriod() {
        Calendar calendar = getCurrentTime();
        if (calendar.get(Calendar.DAY_OF_MONTH) >= 21) {
            calendar.add(Calendar.MONTH, 1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, 21);
        return startDay(calendar.getTime());
    }

    public static Date today() {
        return (Date)getCurrentTime().getTime().clone();
    }
}
