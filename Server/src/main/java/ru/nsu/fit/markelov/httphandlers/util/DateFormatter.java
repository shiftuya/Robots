package ru.nsu.fit.markelov.httphandlers.util;

import java.util.Date;

public class DateFormatter {

    public static String formatLastActive(long millis) {
        millis = new Date().getTime() - millis;

        long inSecond = 1000;
        long inMinute = inSecond * 60;
        long inHour = inMinute * 60;
        long inDay = inHour * 24;
        long inWeek = inDay * 7;
        long inMonth = inDay * 31;
        long inYear = inDay * 366;

        long value;
        String name;

        if (millis < inMinute) {
            if (millis < 1) {
                return "Just now";
            }

            value = inSecond;
            name = "second";
        } else if (millis < inHour) {
            value = inMinute;
            name = "minute";
        } else if (millis < inDay) {
            value = inHour;
            name = "hour";
        } else if (millis < inWeek) {
            value = inDay;
            name = "day";
        } else if (millis < inMonth) {
            value = inWeek;
            name = "week";
        } else if (millis < inYear) {
            value = inMonth;
            name = "month";
        } else {
            value = inYear;
            name = "year";
        }

        long number = millis / value;

        return number + " " + name + (number == 1 ? "" : "s") + " ago";
    }
}
