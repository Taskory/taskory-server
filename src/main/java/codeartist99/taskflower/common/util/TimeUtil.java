package codeartist99.taskflower.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for handling common date-time conversions between
 * String and LocalDateTime objects using a predefined format.
 * The format used is "yyyy-MM-dd'T'HH:mm".
 */
public class TimeUtil {

    // Define the common date-time pattern
    private static final String PATTERN = "yyyy-MM-dd'T'HH:mm";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    /**
     * Converts a date-time string into a LocalDateTime object.
     * The string must follow the format "yyyy-MM-dd'T'HH:mm".
     *
     * @param dateTimeString the date-time string to be converted
     * @return the corresponding LocalDateTime object
     * @throws IllegalArgumentException if the string is not in the correct format
     */
    public static LocalDateTime stringToLocalDateTime(String dateTimeString) {
        try {
            return LocalDateTime.parse(dateTimeString, FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid dateTime format: " + dateTimeString);
        }
    }

    /**
     * Converts a LocalDateTime object into a formatted date-time string.
     * The output format will be "yyyy-MM-dd'T'HH:mm".
     *
     * @param dateTime the LocalDateTime object to be converted
     * @return the corresponding date-time string
     */
    public static String localDateTimeToString(LocalDateTime dateTime) {
        return dateTime.format(FORMATTER);
    }
}
