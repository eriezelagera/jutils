package my.jutils;

import java.math.BigDecimal;
import java.sql.Time;
import java.text.*;
import java.util.*;
import org.joda.time.*;
import org.joda.time.format.*;
import org.slf4j.*;

/**
 * Utility for Date and Time processing.
 * <p>
 * We've use Joda-Time framework on some methods under this utility.
 *
 * @author Erieze Lagera and Einar Lagera
 */
public class Times {

    private static final Logger LOGGER = LoggerFactory.getLogger(Times.class.getSimpleName());

    /**
     * Get temporal date instance.
     * <p>
     * This will truncate seconds from the given date.
     *
     * @param date A date
     * @return Temporal date
     */
    public static Date getTemporalYMDHM(Date date) {
        final DateTime dt = new DateTime(date);
        return new DateTime(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), dt.getHourOfDay(), dt.getMinuteOfHour()).toDate();
    }

    /**
     * Get temporal date instance.
     * <p>
     * This will truncate hour and minute from the given date.
     *
     * @param date A date
     * @return Temporal date
     */
    public static Date getTemporalYMD(Date date) {
        final DateTime dt = new DateTime(date);
        return new DateTime(dt.getYear(), dt.getMonthOfYear(), dt.getDayOfMonth(), 0, 0).toDate();
    }

    /**
     * Check if the given date is Weekend.
     *
     * @param date An instance of Joda's DateTime
     * @return True if the given date is Weekend
     */
    public static boolean isWeekend(DateTime date) {
        return date.getDayOfWeek() == DateTimeConstants.SATURDAY || date.getDayOfWeek() == DateTimeConstants.SUNDAY;
    }

    /**
     * Check if the given date is Weekend.
     *
     * @param date A date
     * @return True if the given date is Weekend
     */
    public static boolean isWeekend(Date date) {
        final DateTime dt = new DateTime(date);
        return dt.getDayOfWeek() == DateTimeConstants.SATURDAY || dt.getDayOfWeek() == DateTimeConstants.SUNDAY;
    }

    /**
     * Check if the given date is Weekday.
     *
     * @param date An instance of Joda's DateTime
     * @return True if the given date is Weekday
     */
    public static boolean isWeekday(DateTime date) {
        return !isWeekend(date);
    }

    /**
     * Check if the given date is Weekday.
     *
     * @param date A date
     * @return True if the given date is Weekday
     */
    public static boolean isWeekday(Date date) {
        return !isWeekend(date);
    }

    /**
     * Get day today in instance of String.
     * <p>
     *
     * <blockquote>
     * <b>Example:</b>
     * "Monday", "Friday"
     * </blockquote>
     *
     * @return Day today.
     */
    public static String getDayTodayString() {
        return toDateFormat(new DateTime(), "EEEE");
    }

    /**
     * Evaluate two time in millis.
     * <p>
     * If the difference of two millis (now - past) is greater than or equal
     * (>=) to timeout, then it is considered timeout.
     *
     * @param now Time now in millis
     * @param past Time earlier in millis
     * @param timeout Timeout in seconds
     * @return True if the difference is greater than or equal to timeout,
     * otherwise false
     */
    public static boolean isTimeout(final long now, final long past, final int timeout) {
        return (now - past) >= secondsToMillis(timeout);
    }

    /**
     * Convert duration to hours.
     *
     * @param duration Duration of time
     * @return Hours of time
     */
    public static BigDecimal durationToHourBigDecimal(final BigDecimal duration) {
        final ThreadLocal<BigDecimal> tl = new ThreadLocal<BigDecimal>() {
            @Override
            protected BigDecimal initialValue() {
                return duration.divide(new BigDecimal("3600000"), BigDecimal.ROUND_HALF_UP);
            }
        };
        final BigDecimal result = tl.get();
        tl.remove();
        return result.setScale(1);
    }

    /**
     * Checks if the input string is in date format.
     *
     * @param date the date to be check
     * @param format the format to be use for validation
     * @return true if its in date format, otherwise false.
     */
    public static boolean isStringInDateFormat(String date, String format) {
        final SimpleDateFormat df = new SimpleDateFormat(format);
        try {
            df.parse(date);
            return true;
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString());
            return false;
        }
    }

    /**
     * Convert duration to minutes.
     *
     * @param duration Duration of time
     * @return Minutes of time
     */
    public static Integer durationToMinuteInt(BigDecimal duration) {
        return duration.divide(new BigDecimal("60000"), BigDecimal.ROUND_DOWN).remainder(new BigDecimal("60")).intValue();
    }

    /**
     * Simple unit conversion.
     * <p>
     * <blockquote>
     * Note: <i>fraction is currently not supported for now.</i>
     * </blockquote>
     *
     * @param millis Millis in time
     * @return Converted seconds from millis (millis->seconds)
     */
    public static int millisToSeconds(final int millis) {
        return millis / 1000;
    }

    /**
     * Simple unit conversion.
     * <p>
     * <blockquote>
     * Note: <i>fraction is currently not supported for now.</i>
     * </blockquote>
     *
     * @param millis Millis in time
     * @return Converted seconds from millis (millis->seconds)
     */
    public static int millisToSeconds(final long millis) {
        return Numbers.toInt(millis / 1000);
    }

    /**
     * Simple unit conversion.
     * <p>
     * <blockquote>
     * Note: <i>fraction is currently not supported for now.</i>
     * </blockquote>
     *
     * @param seconds Seconds in time
     * @return Converted millis from seconds (seconds->millis)
     */
    public static long secondsToMillis(final int seconds) {
        return seconds * 1000;
    }
    
    /**
     * Simple unit conversion.
     * <p>
     * <blockquote>
     * Note: <i>fraction is currently not supported for now.</i>
     * </blockquote>
     *
     * @param seconds Seconds in time
     * @return Converted minute from seconds (seconds->minute)
     */
    public static long secondsToMinute(final int seconds) {
        return seconds * 60;
    }
    
    /**
     * Simple unit conversion.
     * <p>
     * <blockquote>
     * Note: <i>fraction is currently not supported for now.</i>
     * </blockquote>
     *
     * @param seconds Seconds in time
     * @return Converted hour from seconds (seconds->hour)
     */
    public static long secondsToHour(final int seconds) {
        return secondsToMinute(seconds) * 60;
    }

    /**
     * Simple unit conversion.
     * <p>
     * <blockquote>
     * Note: <i>fraction is currently not supported for now.</i>
     * </blockquote>
     *
     * @param minute Minutes in time
     * @return Converted millis from minute (minute->millis)
     */
    public static int minuteToMillis(final int minute) {
        return (minute * 60) * 1000;
    }

    /**
     * Simple unit conversion.
     * <p>
     * <blockquote>
     * Note: <i>fraction is currently not supported for now.</i>
     * </blockquote>
     *
     * @param minute Minutes in time
     * @return Converted seconds from minutes (minutes->seconds)
     */
    public static int minuteToSeconds(final int minute) {
        return minute * 60;
    }

    /**
     * Simple unit conversion.
     * <p>
     * <blockquote>
     * Note: <i>fraction is currently not supported for now.</i>
     * </blockquote>
     *
     * @param hour Hour in time
     * @return Converted millis from hour (hours->millis)
     */
    public static long hourToMillis(final int hour) {
        return hour * 3600000;
    }

    /**
     * Simple unit conversion.
     * <p>
     * <blockquote>
     * Note: <i>fraction is currently not supported for now.</i>
     * </blockquote>
     *
     * @param hour Hour in time
     * @return Converted seconds from hour (hours->seconds)
     */
    public static long hourToSeconds(final int hour) {
        return hour * 3600;
    }

    /**
     * Get current time in instance of Time.
     *
     * @return Current time in instance of Time.
     */
    public static Time getTimeToTime() {
        Time result = new Time(System.currentTimeMillis());
        try {
            final SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
            result = new Time(currentTime.parse(currentTime.format(new DateTime().toDate())).getTime());
        } catch (ParseException e) {
            LOGGER.error("Cause: {}", e.toString());
        }
        return result;
    }

    /**
     * Format date to String format.
     * <p>
     * Customize the format of your Date instance based on your specified date
     * format. This will
     *
     * <blockquote>
     * <b>Example:</b>
     * dateFormat = (mm/dd/yyyy hh:mm:ss a) will result to "10/23/1992 09:09:09
     * AM"
     * </blockquote>
     * <br /><br />
     * <i> Note: Any caught exception will return null. </i>
     *
     * @param date Date you want to changed format
     * @param dateFormat Your own date format
     * @return Formatted date based on the specified date
     */
    public static String toDateFormat(final Date date, final String dateFormat) {
        try {
            final ThreadLocal<SimpleDateFormat> tlSdf = new ThreadLocal<SimpleDateFormat>() {
                @Override
                protected SimpleDateFormat initialValue() {
                    return new SimpleDateFormat(dateFormat, Locale.US);
                }
            };
            final String result = tlSdf.get().format(date);
            tlSdf.remove();
            return result;
        } catch (NullPointerException e) {
            LOGGER.error("Cause: {}", e.toString());
            return null;
        } catch (IllegalArgumentException e) {
            LOGGER.error("Incorrect date format!\n{}", e.toString());
            return null;
        }
    }
    
    /**
     * Format date to String format.
     * <p>
     * Customize the format of your Date instance based on your specified date
     * format. This will
     *
     * <blockquote>
     * <b>Example:</b>
     * dateFormat = (mm/dd/yyyy hh:mm:ss a) will result to "10/23/1992 09:09:09
     * AM"
     * </blockquote>
     * <br /><br />
     * <i> Note: Any caught exception will return null. </i>
     *
     * @param date Date you want to changed format
     * @param dateFormat Your own date format
     * @return Formatted date based on the specified date
     */
    public static String toDateFormat(final DateTime date, final String dateFormat) {
        return toDateFormat(date.toDate(), dateFormat);
    }
    
    /**
     * Format the current date (today) to String format.
     * <p>
     * Customize the format of the current Date instance based on your specified date
     * format. This will
     *
     * <blockquote>
     * <b>Example:</b>
     * dateFormat = (mm/dd/yyyy hh:mm:ss a) will result to "10/23/1992 09:09:09
     * AM"
     * </blockquote>
     * <br /><br />
     * <i> Note: Any caught exception will return null. </i>
     *
     * @param dateFormat Your own date format
     * @return Formatted date based on the specified date
     */
    public static String toDateFormat(final String dateFormat) {
        return toDateFormat(new Date(), dateFormat);
    }

    /**
     * Parses text from the beginning of the given string to produce a date.
     * <p>
     * The method may not use the entire text of the given string.
     *
     * <blockquote>
     * <b>Example:</b>
     * dateFormat = (mm/dd/yyyy hh:mm:ss a) will result to "10/23/1992 09:09:09
     * AM"
     * </blockquote>
     * <br /><br />
     * <i> Note: Any caught exception will return null. </i>
     *
     * @param date Date you want to changed format
     * @param dateFormat Your own date format
     * @param dflt Default value
     * @return Parsed Date instance, dflt if ParseExceptin was caught
     */
    public static Date toDateFormat(Date date, String dateFormat, Date dflt) {
        try {
            //return DateFormat.getInstance().parse(toDateFormat(date, dateFormat));
            return new SimpleDateFormat(dateFormat).parse(toDateFormat(date, dateFormat));
        } catch (ParseException e) {
            LOGGER.error("Cause: {} - Returning dflt value ({})", e.toString(), dflt);
            return dflt;
        }
    }

    /**
     * Convert duration to hours.
     *
     * @param duration Duration of time
     * @return Hours of time
     */
    public static Integer durationToHourInt(BigDecimal duration) {
        return duration.divide(new BigDecimal("3600000"), BigDecimal.ROUND_DOWN).intValue();
    }

    /**
     * Set the time of the given date to the given time-format string. For
     * Example, the date you've given is (1992-10-23) and the time you've given
     * is (11:40am), the date you'll get is 1992-10-23 11:40.
     *
     * @param date any valid date
     * @param time any valid time format
     * @return the date with the time you've given. Will return the same date if
     * there's any exception caught.
     */
    public static Date getDateWithTime(Date date, String time) {
        try {
            final DateTime datetime = new DateTime(date);
            int hour = Integer.parseInt(time.substring(0, 1));
            int minute = Integer.parseInt(time.substring(3, 4));
            if (time.substring(5).equalsIgnoreCase("pm")) {
                hour += 12;
            }
            return datetime.withTime(hour, minute, 0, 0).toDate();
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString());
            return date;
        }
    }

    /**
     * Gets the day of week name.
     *
     * @param date the basis date.
     * @return the name of the day of week of the date parameter.
     */
    public static String getDayOfWeekName(Date date) {
        final int dayOfWeek = getDayOfWeek(date);
        return (dayOfWeek == 1) ? "Monday" : (dayOfWeek == 2) ? "Tuesday" : (dayOfWeek == 3) ? "Wednesday" : (dayOfWeek == 4) ? "Thursday" : (dayOfWeek == 5) ? "Friday" : (dayOfWeek == 6) ? "Saturday" : (dayOfWeek == 7) ? "Sunday" : "";
    }

    /**
     * Gets the day of week in integer.
     *
     * @param date the date basis to get the day of week.
     * @return the nth position of the date's day of week
     */
    public static int getDayOfWeek(Date date) {
        return new DateTime(date).getDayOfWeek();
    }

    /**
     * Compares DateTime's month, day and year.
     * <p>
     * Seconds and milliseconds are ignored in this comparator.
     *
     * @param dt1 A DateTime
     * @param dt2 Another DateTime
     * @return True if both DateTime has equal month, day and year, otherwise
     * false
     */
    public static boolean compareDateTime(DateTime dt1, DateTime dt2) {
        return (dt1.getMonthOfYear() == dt2.getMonthOfYear()) && (dt1.getDayOfMonth() == dt2.getDayOfMonth()) && (dt1.getYear() == dt2.getYear());
    }

    /**
     * This puts zero in from of the time-format string. It will return the same
     * string if there's already a zero in front.
     *
     * @param time
     * @return the time-format string
     */
    public static String putZeroInTime(String time) {
        if (time.startsWith("0")) {
            time = "0" + time;
        }
        return time;
    }

    /**
     * Get date today in instance of String according to your own format.
     * <p>
     * <blockquote>
     * <b>Example:</b>
     * dateFormat = (mm/dd/yyyy) will result to "10231992"
     * </blockquote>
     * <i>
     * Note: Any caught exception such as NumberFormatException will return 0,
     * so you may know from your top-level class that there was caught
     * exception.
     * </i>
     *
     * @param dateFormat Your own date format.
     * @return Date today according to your format, then 0 if there's a caught
     * exception.
     */
    public static Integer getDateTodayInt(String dateFormat) {
        try {
            return Integer.parseInt(Times.toDateFormat(new DateTime(), dateFormat).replaceAll("\\D", ""));
        } catch (NumberFormatException e) {
            LOGGER.error("Cause: {}", e.toString());
            return 0;
        }
    }

    /**
     * Convert milliseconds to date format.
     * <p>
     * Example: <i> millis = <b> 123456789 </b> will result to <b> 1 day, 10 hrs
     * 17 mins, 36 sec </b> </i>
     *
     * @param millis Your datetime in millis
     * @return Formatted date and time.
     */
    public static String toDateFormat(long millis) {
        final long SECOND = 1000;
        final long MINUTE = 60 * SECOND;
        final long HOUR = 60 * MINUTE;
        final long DAY = 24 * HOUR;
        final StringBuilder text = new StringBuilder("");
        if (millis > DAY) {
            final long _day = millis / DAY;
            text.append(_day).append(_day > 1 ? " days, " : " day, ");
            millis %= DAY;
        }
        if (millis > HOUR) {
            final long _hour = millis / HOUR;
            text.append(_hour).append(_hour > 1 ? " hrs, " : " hrs, ");
            millis %= HOUR;
        }
        if (millis > MINUTE) {
            final long _minute = millis / MINUTE;
            text.append(_minute).append(_minute > 1 ? " mins, " : " min, ");
            millis %= MINUTE;
        }
        if (millis > SECOND) {
            text.append(millis / SECOND).append("sec");
            millis %= SECOND;
        }
        return text.toString();
    }

    /**
     * *
     *
     * Added by Einar Lagera January 1, 2015
     */
    /**
     * Extracts the hour in time.
     * <p>
     * Checks and compute if format of your time is 12-hour format or 24-hour
     * format.
     *
     * @param time Time where the hour will be taken. The time should be in
     * correct format and correct value to have an accurate value. It could be
     * hh:mma or hh:mm
     * @return Hour from the time, always returns a 24-hour time format.
     * @deprecated Use {@link Times#getHrFromDate(java.lang.String, java.lang.String, boolean)}
     */
    public static String getHourInTime(String time) {
        String hour = "";
        /*for (int i = 0; i < time.length(); i++) {
         if (time.charAt(i) == ':') {
         return hour;
         }
         hour += String.valueOf(time.charAt(i));
         }
         if (!isTimeTwelveHourFormat(time)) {
         hour = String.valueOf(Numbers.parseInt(hour) + 12);
         }*/
        hour = hour.substring(0, 2);
        return !isTimeTwelveHourFormat(time) ? Strings.add(hour, 12)
                : hour;
    }

    /**
     * Gets the hour from the time parameter. The time should be in this way:
     * hh:mm or otherwise, it will return a wrong value.
     *
     * @param time the time basis.
     * @return the hour from the time parameter.
     * @deprecated Use {@link Times#getHrFromDate(java.lang.String, java.lang.String, boolean)}
     */
    public static int getHourFromTime(String time) {
        return Numbers.parseInt(String.valueOf(time).substring(0, 2));
    }

    /**
     * Get the hour time from the given {@code dateTime}.
     * <p>
     * Datetime format will based on the given {@code dateFormat}. Format or
     * pattern should have "hh" or "HH" to get the hour time.
     *
     * @param dateTime A {@code Date} in type of {@code String}
     * @param dateFormat Date format or pattern
     * @param hour24Format 24-hour base {@code dateTime} (eg. 23:59)?
     * @return If hour24Format is true, returns hour in 24-hour based time
     * format, otherwise 12-hour based format
     * @see Times#getHrFromDateStr(java.lang.String, java.lang.String, boolean)
     * 
     */
    public static int getHrFromDate(String dateTime, String dateFormat, boolean hour24Format) {
        return Numbers.parseInt(
                getHrFromDateStr(dateTime, dateFormat, hour24Format),
                0);
    }

    /**
     * Get the hour time from the given {@code dateTime}.
     * <p>
     * Datetime format will based on the given {@code dateFormat}. Format or
     * pattern should have "hh" or "HH" to get the hour time.
     *
     * @param dateTime A {@code Date} in type of {@code String}
     * @param dateFormat Date format or pattern
     * @param hour24Format 24-hour base {@code dateTime} (eg. 23:59)?
     * @return If hour24Format is true, returns hour in 24-hour based time
     * format, otherwise 12-hour based format
     * @see Times#getHrFromDate(java.lang.String, java.lang.String, boolean)
     */
    public static String getHrFromDateStr(String dateTime, String dateFormat, boolean hour24Format) {
        return DateTimeFormat.forPattern(dateFormat).
                parseDateTime(dateTime).
                toString(hour24Format ? "HH" : "hh");
    }

    /**
     * Extracts minute from time.
     *
     * @param time Time where the hour will be taken. The time should be in
     * correct format and correct value to have an accurate value. It could be
     * hh:mma or hh:mm
     * @return Hour value from the given time
     * @deprecated Use {@link Times#getMinFromDate(java.lang.String, java.lang.String)}
     */
    public static String getMinutesInTime(String time) {
        /*for (int i = 0; i < time.length(); i++) {
         if (time.charAt(i) == ':') {
         return time.substring(i + 1, i + 3);
         }
         }
         return "";*/
        final int colonLoc = time.indexOf(":");
        return colonLoc == -1 ? time.substring(2, 4)
                : time.substring(3, 5);
    }

    /**
     * Gets the minutes from the time parameter. The time should be in this way:
     * hh:mm or otherwise, it will return a wrong value.
     *
     * @param time the time basis.
     * @return the minutes from the time parameter.
     * @deprecated Use {@link Times#getMinFromDate(java.lang.String, java.lang.String)}
     */
    public static int getMinutesFromTime(String time) {
        //return Numbers.parseInt(String.valueOf(time).substring(2, 4));
        final int colonLoc = time.indexOf(":");
        return Numbers.parseInt(colonLoc == -1 ? time.substring(2, 4)
                : time.substring(3, 5));
    }

    /**
     * Get the minute time from the given {@code dateTime}.
     * <p>
     * Datetime format will based on the given {@code dateFormat}. Format or
     * pattern should have "mm" to get the minute time. Don't get confuse to
     * format "MM" and "mm" as the uppercase format is for month not minute.
     *
     * @param dateTime A {@code Date} in type of {@code String}
     * @param dateFormat Date format or pattern
     * @return Minute value
     * @see Times#getHrFromDate(java.lang.String, java.lang.String, boolean)
     */
    public static int getMinFromDate(String dateTime, String dateFormat) {
        return Numbers.parseInt(
                getMinFromDateStr(dateTime, dateFormat),
                0);
    }

    /**
     * Get the minute time from the given {@code dateTime}.
     * <p>
     * Datetime format will based on the given {@code dateFormat}. Format or
     * pattern should have "mm" to get the minute time. Don't get confuse to
     * format "MM" and "mm" as the uppercase format is for month not minute.
     *
     *
     * @param dateTime A {@code Date} in type of {@code String}
     * @param dateFormat Date format or pattern
     * @return Minute value
     * @see Times#getHrFromDate(java.lang.String, java.lang.String, boolean)
     * value
     */
    public static String getMinFromDateStr(String dateTime, String dateFormat) {
        return DateTimeFormat.forPattern(dateFormat).
                parseDateTime(dateTime).
                toString("mm");
    }

    /**
     * Converts the given {@code dateTime} with type of {@code String} to
     * instance of {@link DateTime}.
     * <p>
     * The given {@code dateTime} should be appropriate to the given
     * {@code dateFormat}.
     * <br />
     * Note: <i> Format will not be retain if {@link DateTime#toString()} was
     * invoke, use {@link DateTime#toString(java.lang.String)} instead to have
     * your own date format. </i>
     *
     * @param dateTime A {@code Date} in type of {@code String}
     * @param dateFormat Date format or pattern
     * @return Instance of {@link DateTime} from the given {@code dateTime}
     */
    public static DateTime toDateTime(String dateTime, String dateFormat) {
        return DateTimeFormat.forPattern(dateFormat).
                parseDateTime(dateTime);
    }

    /**
     * Converts the given {@code dateTime} with type of {@code String} to
     * instance of {@link Date}.
     * <p>
     * The given {@code dateTime} should be appropriate to the given
     * {@code dateFormat}.
     * <br />
     * Note: <i> Format will not be retain if {@link Date#toString()} was
     * invoke, use {@link DateTime#toString(java.lang.String)} instead to have
     * your own date format. </i>
     *
     * @param dateTime A {@code Date} in type of {@code String}
     * @param dateFormat Date format or pattern
     * @return Instance of {@link Date} from the given {@code dateTime}
     */
    public static Date toDate(String dateTime, String dateFormat) {
        return toDateTime(dateTime, dateFormat).toDate();
    }

    /**
     * Checks if the time is 12-hour format.
     *
     * @param time Time which will be evaluated. It should have the correct
     * format and value. The date format should be hh:mma
     * @return True if it has AM/PM, otherwise false
     */
    public static boolean isTimeTwelveHourFormat(String time) {
        return time.toLowerCase().contains("am")
                || time.toLowerCase().contains("pm");
    }

    /**
     * Converts the time in type of {@code String} to type of {@code int}.
     *
     * @param time Time to be converted, it could be 12-hour format or 24-hour
     * format.
     * @return it always returns a 24-hour format. Example, 9:59pm returns 2195
     */
    public static int convertTimeToInt(String time) {
        return (Numbers.parseInt(getHourInTime(time)) * 100)
                + Numbers.parseInt(getMinutesInTime(time));
    }

    /**
     * Set the time of date given.
     * <p>
     * The given {@code time} should be in format of "HH:mm" or "hh:mm".
     *
     * @param date Date to be modified
     * @param time Time to be evaluated
     * @param hour24Format 24-hour base {@code dateTime} (eg. 23:59)?
     * @return Instance of {@code java.util.Date} with the given time
     */
    public static Date setTimeFromDate(Date date, String time, boolean hour24Format) {
        final String format = (hour24Format) ? "HH:mm" : "hh:mm";
        return new DateTime(date).
                withHourOfDay(Times.getHrFromDate(time, format, hour24Format)).
                withMinuteOfHour(Times.getMinFromDate(time, format)).
                toDate();
    }

    /**
     * Gets the hour in the given date.
     *
     * @param date Date where the hour will be come from
     * @return Hour of the given date
     */
    public static int getHrFromDate(Date date) {
        return new DateTime(date).getHourOfDay();
    }

    /**
     * Adds days in the given date.
     *
     * @param date Date to be modified
     * @param days Number of days to be added
     * @return Instance of {@code java.util.Date} with days added.
     */
    public static Date plusDay(Date date, int days) {
        return new DateTime(date).plusDays(days).toDate();
    }

    /**
     * Subtracts days in the given date.
     *
     * @param date Date to be modified
     * @param days Number of days to be subtracted
     * @return Instance of {@code java.util.Date} with days subtracted
     */
    public static Date minusDay(Date date, int days) {
        return new DateTime(date).minusDays(days).toDate();
    }

    /**
     * Adds hours in the given date.
     *
     * @param date Date to be modified
     * @param hours Number of hours to be added
     * @return Instance of {@code java.util.Date} with days added
     */
    public static Date plusHour(Date date, int hours) {
        return new DateTime(date).plusHours(hours).toDate();
    }

    /**
     * Subtracts hours in the given date.
     *
     * @param date Date to be modified
     * @param hours Number of hours to be subtracted
     * @return Instance of {@code java.util.Date} with days subtracted
     */
    public static Date minusHour(Date date, int hours) {
        return new DateTime(date).minusHours(hours).toDate();
    }

    /**
     * Adds minutes in the time of the date.
     *
     * @param date Date to be modified
     * @param minutes Number of minutes to be added
     * @return Instance of {@code java.util.Date} with minutes added
     */
    public static Date plusMinute(Date date, int minutes) {
        return new DateTime(date).plusMinutes(minutes).toDate();
    }

    /**
     * Subtracts minutes in the time of the date.
     *
     * @param date Date to be modified
     * @param minutes Number of minutes to be subtracted
     * @return Instance of {@code java.util.Date} with minutes subtracted
     */
    public static Date minusMinute(Date date, int minutes) {
        return new DateTime(date).minusMinutes(minutes).toDate();
    }

    /**
     * Converts the millis to date.
     * 
     * @param millis Millis of the date. Should always be positive
     * @return Date of the given millis
     * @deprecated Use {@code new java.util.Date(long millis)}
     */
    public static Date convertMillisToDate(long millis) {
        return new DateTime().withMillis(millis).toDate();
    }

    /**
     * Gets the seconds difference of the two dates.
     * <p>
     * The second date must always be the larger to obtain a positive value.
     *
     * @param firstDate First date or in subtraction, the subtrahend
     * @param secondDate Second date or in subtraction, the minuend
     * @return Seconds difference of the two dates
     */
    public static int getSecondsDifference(Date firstDate, Date secondDate) {
        return millisToSeconds(secondDate.getTime() - firstDate.getTime());
    }

    /**
     * Gets the integer time from date.
     * <p>
     * Example: Date is 19930116 12:30:00, it will get first the 12:30 from the
     * time and then replaces all the non-integer value into blank to extract
     * the number only which will be 1230.
     *
     * @param date Instance of Date
     * @return Time integer.
     */
    public static int getIntTimeFromDate(Date date) {
        return Numbers.parseInt(toDateFormat(date, "hh:mm").replaceAll("\\D", ""));
    }

    /**
     * Converts the String formatted time to integer.
     * <p>
     * The time should be 24-hour format only. Example: 12:30 -> 1230
     *
     * @param time The time to be converted.
     * @return Integer of the given time.
     */
    public static int convertStringTimeToInt(String time) {
        return Numbers.parseInt(time.replaceAll("\\D", ""));
    }

    /**
     * Evaluate value if {@code Date} is null.
     * <p>
     * If value is null, then the default value will be used.
     *
     * @param val Value of {@link Date}
     * @param dflt Default value
     * @return {@code dflt} if equal to {@code null}, otherwise {@code val}
     */
    public static Date get(Date val, Date dflt) {
        if (val == null) {
            LOGGER.error("val is null - Returning dflt value ({})", dflt);
            return dflt;
        }
        return val;
    }
    
    /**
     * Evaluate value if {@code Date} is null.
     * <p>
     * If value is null, then the default value will be used.
     *
     * @param val Value of {@link Date}
     * @param dflt Default value
     * @return {@code dflt} if not instance of {@link Date} or 
     * equal to {@code null}, otherwise {@code val}
     */
    public static Date get(Object val, Date dflt) {
        if (val == null) {
            LOGGER.error("val is null - Returning dflt value ({})", dflt);
            return dflt;
        }
        if (val instanceof Date) {
            return (Date) val;
        } else {
            LOGGER.error("val is not instance of Date - Returning dflt value ({})", dflt);
            return dflt;
        }
    }
    
}
