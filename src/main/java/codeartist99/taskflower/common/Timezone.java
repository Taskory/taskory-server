package codeartist99.taskflower.common;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

/**
 * Enum representing timezones for various cities with their respective ZoneId.
 */
public enum Timezone {

    UTC("UTC"),
    LONDON("Europe/London"),
    BERLIN("Europe/Berlin"),
    HELSINKI("Europe/Helsinki"),
    NEW_YORK("America/New_York"),
    CHICAGO("America/Chicago"),
    DENVER("America/Denver"),
    LOS_ANGELES("America/Los_Angeles"),
    SEOUL("Asia/Seoul"),
    TOKYO("Asia/Tokyo"),
    SYDNEY("Australia/Sydney");

    private final String zoneId;
    private static final Map<String, Timezone> ZONE_MAP = new HashMap<>();

    // Static block to initialize the map for faster lookup
    static {
        for (Timezone tz : Timezone.values()) {
            ZONE_MAP.put(tz.zoneId, tz);  // Maps zoneId (e.g., "Asia/Seoul") to the enum value
        }
    }

    /**
     * Constructor to associate ZoneId string with each timezone.
     *
     * @param zoneId the string representing the ZoneId.
     */
    Timezone(String zoneId) {
        this.zoneId = zoneId;
    }

    /**
     * Get the ZoneId associated with the timezone.
     *
     * @return ZoneId corresponding to the timezone.
     */
    public ZoneId getZoneId() {
        return ZoneId.of(zoneId);
    }

    /**
     * Converts a string to the corresponding Timezone enum by matching the ZoneId.
     S
     * @param timezoneString the input string representing a timezone (e.g., "Asia/Seoul").
     * @return the corresponding Timezone enum.
     * @throws IllegalArgumentException if the input string doesn't match any known timezone.
     */
    public static Timezone fromString(String timezoneString) {
        Timezone timezone = ZONE_MAP.get(timezoneString);
        if (timezone == null) {
            throw new IllegalArgumentException("Invalid timezone provided: " + timezoneString);
        }
        return timezone;
    }
}
