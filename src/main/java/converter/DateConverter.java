package converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateConverter {

    private static ThreadLocal<DateFormat> viewFormat = new ThreadLocal<>();
    private static ThreadLocal<DateFormat> dataBaseFormat = new ThreadLocal<>();
    private static ThreadLocal<DateFormat> viewFormatRU = new ThreadLocal<>();
    private static ThreadLocal<DateFormat> time = new ThreadLocal<>();

    public static DateFormat getFormatView() {
        return getFormat(viewFormat, "yyyy-MM-dd");
    }

    public static DateFormat getFormatDatabase() {
        return getFormat(dataBaseFormat, "yyyy-MM-dd'T'hh:mm");
    }

    public static DateFormat getFormatViewRU() {
        return getFormat(viewFormatRU, "dd.MM.yyyy");
    }

    public static DateFormat getFormatTime() {
        return getFormat(time, "HH:mm");
    }

    private static DateFormat getFormat(ThreadLocal<DateFormat> thread, String pattern) {
        DateFormat format = thread.get();
        if (format == null) {
            format = new SimpleDateFormat(pattern);
            thread.set(format);
        }
        return format;
    }
}
