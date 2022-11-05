package com.github.cargoclean.core.model;

import com.github.cargoclean.core.validator.InvalidDomainObjectError;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.Optional;

/**
 * Wrapper around {@link ZonedDateTime} fixed to the {@code UTC} timezone.
 * This is a real immutable value-object encapsulating all date-time related
 * operations.
 */
public class UtcDateTime implements Comparable<UtcDateTime> {
    private static final ZoneId UTC = ZoneId.from(ZoneOffset.UTC);

    /*
        Converted from "se.citerus.dddsample.domain.model.cargo.Itinerary#END_OF_DAYS".
     */
    public static UtcDateTime END_OF_DAYS = new UtcDateTime(Instant.ofEpochMilli(Long.MAX_VALUE).atZone(UTC));

    /**
     * Creates an instance of {@link UtcDateTime} from the short date string. The start
     * of the day will be used for the time.
     *
     * @param date string in the form "dd-MM-yyyy"
     * @return new instance of {@code UtcDateTime}
     */
    public static UtcDateTime of(String date) {
        return new UtcDateTime(date);
    }

    public static UtcDateTime of(Date fromDate) {
        return new UtcDateTime(fromDate);
    }

    public static UtcDateTime of(Instant fromInstant) {
        return new UtcDateTime(fromInstant);
    }

    public static UtcDateTime now() {
        return new UtcDateTime(Instant.now());
    }

    private final ZonedDateTime dateTimeAtUtc;

    public UtcDateTime(Instant instant) {
        this.dateTimeAtUtc = Optional.ofNullable(instant)
                .orElseThrow(() -> new InvalidDomainObjectError("Source instant cannot be null"))
                .atZone(UTC);
    }

    public UtcDateTime(ZonedDateTime fromDateTime) {
        this.dateTimeAtUtc = Optional.ofNullable(fromDateTime)
                .orElseThrow(() -> new InvalidDomainObjectError("Source date-time cannot be null"))
                .withZoneSameInstant(UTC);
    }

    public UtcDateTime(Date fromDate) {
        this.dateTimeAtUtc = Instant.ofEpochMilli(Optional.ofNullable(fromDate)
                .orElseThrow(() -> new InvalidDomainObjectError("Source date cannot be null"))
                .getTime()).atZone(UTC);
    }

    public UtcDateTime(String fromString) {
        try {
            this.dateTimeAtUtc = ZonedDateTime.of(LocalDate.parse(fromString, DateTimeFormatter.ofPattern("dd-MM-yyyy")).atStartOfDay(),
                    UTC);
        } catch (NullPointerException | DateTimeParseException e) {
            throw new InvalidDomainObjectError("String must not be null and must be in the format: \"dd-MM-yyyy\"");
        }
    }

    public ZonedDateTime toDateTimeAtUtc(){
        return dateTimeAtUtc;
    }

    public boolean isUnknown() {
        return dateTimeAtUtc.toInstant().toEpochMilli() == Long.MAX_VALUE;
    }

    @Override
    public String toString() {
        return dateTimeAtUtc.format(DateTimeFormatter.BASIC_ISO_DATE);
    }

    public Instant toInstant() {
        return dateTimeAtUtc.toInstant();
    }

    public boolean isAfter(UtcDateTime anotherUtcDateTime) {
        return dateTimeAtUtc.isAfter(anotherUtcDateTime.toDateTimeAtUtc());
    }

    @Override
    public int compareTo(UtcDateTime anotherUtcDateTime) {
        return dateTimeAtUtc.compareTo(anotherUtcDateTime.toDateTimeAtUtc());
    }

    public String format(DateTimeFormatter pattern) {
        return dateTimeAtUtc.format(pattern);
    }

    public UtcDateTime plusSeconds(long seconds) {
        return new UtcDateTime(dateTimeAtUtc.plusSeconds(seconds));
    }

    public UtcDateTime plusMonths(long months) {
        return new UtcDateTime(dateTimeAtUtc.plusMonths(months));
    }

    public UtcDateTime atStartOfDay() {
        return new UtcDateTime(dateTimeAtUtc.withHour(0).withMinute(0).withSecond(0).withNano(0));
    }

    public UtcDateTime plusDays(long days) {
        return new UtcDateTime(dateTimeAtUtc.plusDays(days));
    }
}
