package dev.ollis.wgu.helper;

import java.sql.Timestamp;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;

// the general idea is that we convert the timestamp to a LocalDateTime
// then we convert that to a ZonedDateTime, shit the time to the expect zone
// and finally convert back into a timestamp
public abstract class TimeUtils {
    public static Timestamp fromUTC(Timestamp utc, ZoneId targetZone) {
        return convert(utc, ZoneId.of("UTC"), targetZone);
    }

    public static Timestamp toUTC(Timestamp local, ZoneId sourceZone) {
        return convert(local, sourceZone, ZoneId.of("UTC"));
    }

    public static Timestamp fromEST(Timestamp est, ZoneId targetZone) {
        return convert(est, ZoneId.of("America/New_York"), targetZone);
    }

    public static Timestamp toEST(Timestamp local, ZoneId sourceZone) {
        return convert(local, sourceZone, ZoneId.of("America/New_York"));
    }

    private static Timestamp convert(Timestamp time, ZoneId fromZone, ZoneId toZone) {
        LocalDateTime timeAsDateTime = time.toLocalDateTime();
        ZonedDateTime fromZonedDataTime = ZonedDateTime.of(timeAsDateTime, fromZone);
        ZonedDateTime toZonedDataTime = fromZonedDataTime.withZoneSameInstant(toZone);
        return Timestamp.valueOf(toZonedDataTime.toLocalDateTime());
    }

    public static boolean isWeekend(Timestamp time) {
        LocalDateTime startAsDateTime = time.toLocalDateTime();
        DayOfWeek day =  startAsDateTime.getDayOfWeek();
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }

    public static boolean isWeekend(Timestamp start, Timestamp end) {
        return isWeekend(start) || isWeekend(end);
    }

    public static boolean isOutsideBusinessHours(Timestamp time, int startHour, int endHour) {
        LocalDateTime startAsDateTime = time.toLocalDateTime();
        int hour = startAsDateTime.getHour();
        int minute = startAsDateTime.getMinute();
        return hour < startHour || hour > endHour || (hour == endHour && minute > 0);
    }

    public static boolean isOutsideBusinessHours(Timestamp start, Timestamp end, int startHour, int endHour) {
        return isOutsideBusinessHours(start, startHour, endHour) || isOutsideBusinessHours(end, startHour, endHour);
    }

    public static boolean isWithinTheNext(int minutes, Timestamp time) {
        LocalDateTime startAsDateTime = time.toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        return startAsDateTime.isAfter(now) && startAsDateTime.isBefore(now.plusMinutes(minutes));
    }

    public static String getDate(Timestamp time) {
        LocalDateTime startAsDateTime = time.toLocalDateTime();
        return startAsDateTime.getMonthValue() + "/" + startAsDateTime.getDayOfMonth() + "/" + startAsDateTime.getYear();
    }

    public static String getTime(Timestamp time) {
        LocalDateTime startAsDateTime = time.toLocalDateTime();
        return startAsDateTime.getHour() + ":" + startAsDateTime.getMinute();
    }

    public static int getCurrentYear() {
        return LocalDateTime.now().getYear();
    }

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
}
