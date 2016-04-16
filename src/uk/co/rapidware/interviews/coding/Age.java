package uk.co.rapidware.interviews.coding;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Logger;

/**
 * Created by Sonny on 28/04/2014.
 */
public class Age {

    private static final Logger LOGGER = Logger.getLogger(Age.class.getName());

    public static Logger getLogger() {
        return LOGGER;
    }

    public int getAgeInYears(final Date dob) {

        final Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        final int nowYear = utcCalendar.get(Calendar.YEAR);
        final int nowMonthOfYear = utcCalendar.get(Calendar.MONTH) + 1;
        final int nowDayOfMonth = utcCalendar.get(Calendar.DAY_OF_MONTH);

        getLogger().info(String.format("Date today is %d/%d/%d", nowDayOfMonth, nowMonthOfYear, nowYear));

        utcCalendar.setTime(dob);

        final int year = utcCalendar.get(Calendar.YEAR);
        final int monthOfYear = utcCalendar.get(Calendar.MONTH) + 1;
        final int dayOfMonth = utcCalendar.get(Calendar.DAY_OF_MONTH);

        getLogger().info(String.format("Date of birth is %d/%d/%d", dayOfMonth, monthOfYear, year));

        final int age = (nowYear - year) - (
                (nowMonthOfYear > monthOfYear || (nowDayOfMonth == monthOfYear && nowDayOfMonth > dayOfMonth)) ?
                        0 : 1);
        return age;
    }

    public boolean isOlderThan(final Date dob, int age) {
        return getAgeInYears(dob) >= age;
    }

    public static void main(String[] args) {

        final Calendar utcCalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

        utcCalendar.set(Calendar.YEAR, 1977);
        utcCalendar.set(Calendar.MONTH, 11);
        utcCalendar.set(Calendar.DAY_OF_MONTH, 26);

        final Date sonnyDob = utcCalendar.getTime();
        Age ageLib = new Age();
        getLogger().info(String.format("Sonny was born on [%s] and is [%d] Years old", sonnyDob, ageLib.getAgeInYears(sonnyDob)));

        utcCalendar.set(Calendar.YEAR, 2007);
        utcCalendar.set(Calendar.MONTH, 0);
        utcCalendar.set(Calendar.DAY_OF_MONTH, 20);

        final Date nehaDob = utcCalendar.getTime();
        getLogger().info(String.format("Neha was born on [%s] and is [%d] Years old", nehaDob, ageLib.getAgeInYears(nehaDob)));

        utcCalendar.set(Calendar.YEAR, 2009);
        utcCalendar.set(Calendar.MONTH, 5);
        utcCalendar.set(Calendar.DAY_OF_MONTH, 13);

        final Date shaanDob = utcCalendar.getTime();
        final int olderThan = 4;
        getLogger().info(String.format("Is Shaan older than [%d] Years old?  [%s]", olderThan, ageLib.isOlderThan(shaanDob, olderThan)));
    }
}
