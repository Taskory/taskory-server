package codeartist99.taskflower.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;

/**
 * Utility class for handling LocalDateTime to String conversions and vice versa in UTC timezone.
 */
public class TimeUtil {

    // DateTimeFormatter for formatting and parsing the string
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    /**
     * Converts a LocalDateTime object to a string formatted as "yyyy-MM-dd'T'HH:mm" in UTC.
     *
     * @param dateTime the LocalDateTime object to be converted (assumed to be in UTC).
     * @return a formatted string representing the LocalDateTime in UTC.
     */
    public static String LocalDateTimeToString(LocalDateTime dateTime) {
        ZonedDateTime utcDateTime = dateTime.atZone(ZoneId.of("UTC"));
        return utcDateTime.format(formatter);
    }

    /**
     * Converts a string formatted as "yyyy-MM-dd'T'HH:mm" to a LocalDateTime object in UTC.
     *
     * @param dateTimeString the string representing the date and time in "yyyy-MM-dd'T'HH:mm" format.
     * @return a LocalDateTime object corresponding to the parsed string in UTC.
     */
    public static LocalDateTime StringToLocalDateTime(String dateTimeString) {
        LocalDateTime localDateTime = LocalDateTime.parse(dateTimeString, formatter);
        return localDateTime.atZone(ZoneId.of("UTC")).toLocalDateTime();
    }
}
