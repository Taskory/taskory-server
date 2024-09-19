package codeartist99.taskflower.common.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeUtil {

    private static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    /**
     * Converts an ISO 8601 formatted string to LocalDateTime in the specified timezone.
     *
     * @param isoString the ISO 8601 string to convert
     * @param zoneId    the ZoneId to apply to the conversion
     * @return the corresponding LocalDateTime
     * @throws DateTimeParseException if the string cannot be parsed
     */
    public static LocalDateTime isoStringToLocalDateTime(String isoString, ZoneId zoneId) throws DateTimeParseException {
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(isoString, ISO_DATE_TIME_FORMATTER.withZone(zoneId));
        return zonedDateTime.toLocalDateTime();
    }

    /**
     * Converts a LocalDateTime object to an ISO 8601 formatted string.
     *
     * @param localDateTime the LocalDateTime to convert
     * @return the corresponding ISO 8601 formatted string
     */
    public static String localDateTimeToIsoString(LocalDateTime localDateTime) {
        return localDateTime.format(ISO_DATE_TIME_FORMATTER);
    }
}
