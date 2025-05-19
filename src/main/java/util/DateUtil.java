package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.Date;

public class DateUtil {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static Date from(String dateStr) {
        try {
            java.util.Date utilDate = DATE_FORMAT.parse(dateStr);
            return new Date(utilDate.getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format, expected yyyy-MM-dd: " + dateStr);
        }
    }

    public static String toString(Date date) {
        return DATE_FORMAT.format(date);
    }
}
