package src.service;

/**
 * Enumerator of days in the week and the day index in the Enum.
 * Order starts with Sunday.
 */
public enum DayOfTheWeekEnum {
    SUNDAY(0),
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(3),
    THURSDAY(4),
    FRIDAY(5),
    SATURDAY(6);

    public final int dayIndex;

    DayOfTheWeekEnum(int dayIndex) {
        this.dayIndex = dayIndex;
    }
}
