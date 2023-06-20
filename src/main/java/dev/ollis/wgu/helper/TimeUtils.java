package dev.ollis.wgu.helper;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;

/**
 * Utility class for working with time.
 */
public abstract class TimeUtils {

    /**
     * Converts a timestamp from UTC to the target zone.
     * @param utc The timestamp in UTC
     * @param targetZone The target zone
     * @return The timestamp in the target zone
     */
    public static Timestamp fromUTC(Timestamp utc, ZoneId targetZone) {
        return convert(utc, ZoneId.of("UTC"), targetZone);
    }

    /**
     * Converts a timestamp from the local zone to UTC.
     * @param local The timestamp in the local zone
     * @param sourceZone The local zone
     * @return The timestamp in UTC
     */
    public static Timestamp toUTC(Timestamp local, ZoneId sourceZone) {
        return convert(local, sourceZone, ZoneId.of("UTC"));
    }

    /**
     * Converts a timestamp from EST to a local zone.
     * @param est The timestamp in EST
     * @param targetZone The local zone
     * @return The timestamp in the local zone
     */
    public static Timestamp fromEST(Timestamp est, ZoneId targetZone) {
        return convert(est, ZoneId.of("America/New_York"), targetZone);
    }

    /**
     * Converts a timestamp from the local zone to EST.
     * @param local The timestamp in the local zone
     * @param sourceZone The local zone
     * @return The timestamp in EST
     */
    public static Timestamp toEST(Timestamp local, ZoneId sourceZone) {
        return convert(local, sourceZone, ZoneId.of("America/New_York"));
    }

    /**
     * Converts a timestamp from a zone to the target zone.
     * @param time The timestamp to convert
     * @param fromZone The zone of the timestamp
     * @param toZone The target zone
     * @return The timestamp in the target zone
     */
    private static Timestamp convert(Timestamp time, ZoneId fromZone, ZoneId toZone) {
        LocalDateTime timeAsDateTime = time.toLocalDateTime();
        ZonedDateTime fromZonedDataTime = ZonedDateTime.of(timeAsDateTime, fromZone);
        ZonedDateTime toZonedDataTime = fromZonedDataTime.withZoneSameInstant(toZone);
        return Timestamp.valueOf(toZonedDataTime.toLocalDateTime());
    }

    /**
     * Checks if a timestamp is on a weekend day.
     * @param time The timestamp to check
     * @return True if the timestamp is on a weekend, false otherwise
     */
    public static boolean isWeekend(Timestamp time) {
        LocalDateTime startAsDateTime = time.toLocalDateTime();
        DayOfWeek day =  startAsDateTime.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    /**
     * Checks if a timestamp range is during the weekend
     * @param start The start timestamp
     * @param end The end timestamp
     * @return True if the timestamp is during the weekend, false otherwise
     */
    public static boolean isWeekend(Timestamp start, Timestamp end) {
        return isWeekend(start) || isWeekend(end);
    }

    /**
     * Checks if a timestamp is outside of business hours.
     * @param time The timestamp to check
     * @param startHour The start hour of business hours
     * @param endHour The end hour of business hours
     * @return True if the timestamp is outside of business hours, false otherwise
     */
    public static boolean isOutsideBusinessHours(Timestamp time, int startHour, int endHour) {
        LocalDateTime startAsDateTime = time.toLocalDateTime();
        int hour = startAsDateTime.getHour();
        int minute = startAsDateTime.getMinute();
        return hour < startHour || hour > endHour || (hour == endHour && minute > 0);
    }

    /**
     * Checks if a timestamp range is outside business hours.
     * @param start The start timestamp
     * @param end The end timestamp
     * @param startHour The start hour of business hours
     * @param endHour The end hour of business hours
     * @return True if the timestamp range is outside of business hours, false otherwise
     */
    public static boolean isOutsideBusinessHours(Timestamp start, Timestamp end, int startHour, int endHour) {
        return isOutsideBusinessHours(start, startHour, endHour) || isOutsideBusinessHours(end, startHour, endHour);
    }

    /**
     * Checks if a timestamp is within the next x minutes.
     * @param minutes The number of minutes to check
     * @param time The timestamp to check
     * @return True if the timestamp is within the next x minutes, false otherwise
     */
    public static boolean isWithinTheNext(int minutes, Timestamp time) {
        LocalDateTime startAsDateTime = time.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        return startAsDateTime.isAfter(now) && startAsDateTime.isBefore(now.plusMinutes(minutes));
    }

    /**
     * Returns the date of a timestamp in the format MM/DD/YYYY
     * @param time The timestamp to get the date of
     * @return The date of the timestamp
     */
    public static String getDate(Timestamp time) {
        LocalDateTime startAsDateTime = time.toLocalDateTime();
        return startAsDateTime.getMonthValue() + "/" + startAsDateTime.getDayOfMonth() + "/" + startAsDateTime.getYear();
    }

    /**
     * Returns the time of a timestamp in the format HH:MM
     * @param time The timestamp to get the time of
     * @return The time of the timestamp
     */
    public static String getTime(Timestamp time) {
        LocalDateTime startAsDateTime = time.toLocalDateTime();
        return startAsDateTime.getHour() + ":" + startAsDateTime.getMinute();
    }

    /**
     * Returns the current year.
     * @return The current year
     */
    public static int getCurrentYear() {
        return LocalDateTime.now().getYear();
    }

    /**
     * Returns a string representation of a week range.
     * @param year The year
     * @param week The week
     * @return The string representation of the week range: May 1 - May 7 2023
     */
    public static String getWeekRangeString(int year, int week){
        LocalDate startOfYear = LocalDate.of(year, Month.JANUARY, 1);
        LocalDate firstDay = startOfYear.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        if (firstDay.get(ChronoField.ALIGNED_WEEK_OF_YEAR) == 2) {
            firstDay = firstDay.minusDays(7);
        }
        firstDay = firstDay.plusWeeks(week - 1);

        LocalDate lastDay = firstDay.plusDays(6);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d");
        String firstDayString = firstDay.format(formatter);
        String lastDayString = lastDay.format(formatter);
        return firstDayString + " - " + lastDayString + " " + year;
    }

    /**
     * Returns the current timestamp as a string.
     * @return The current timestamp as a string
     */
    public static String now() {
        return LocalDateTime.now().toString();
    }

    /**
     * Returns the current date as a string.
     * @return The current date as a string
     */
    public static String today() {
        return LocalDate.now().toString();
    }
}
