package my.jutils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import javax.swing.*;
import org.slf4j.*;

/**
 * Utility for primitive numerical data types and BigDecimal.
 *
 * @author Erieze Lagera
 */
public class Numbers {

    private static final Logger LOGGER = LoggerFactory.getLogger(Numbers.class.getSimpleName());

    /**
     * Numbers String to native integer.
     * <p>
     * This will avoid the top-level program with any Exception when parsing
     * String.
     *
     * @param foo String to be parsed
     * @return Parsed String to int, if parsing is not successful, value of zero
     * (0)
     */
    public static int parseInt(String foo) {
        return parseInt(foo, 0);
    }
    
    /**
     * Numbers String to native integer.
     * <p>
     * This will avoid the top-level program with any Exception when parsing
     * String.
     *
     * @param foo String to be parsed
     * @param dflt Default value if parsing is not successful
     * @return Parsed String to int
     */
    public static int parseInt(String foo, int dflt) {
        try {
            return Integer.parseInt(foo);
        } catch (NumberFormatException e) {
            LOGGER.error("Cause: {} - Returning dflt value ({})", e.toString(), dflt);
            return dflt;
        }
    }
    
    /**
     * Numbers String to native long.
     * <p>
     * This will avoid the top-level program with any Exception when parsing
     * String.
     *
     * @param foo String to be parsed
     * @return Parsed String to long, if parsing is not successful, value of
     * zero (0)
     */
    public static long parseLong(String foo) {
        return parseLong(foo, 0);
    }

    /**
     * Numbers String to native long.
     * <p>
     * This will avoid the top-level program with any Exception when parsing
     * String.
     *
     * @param foo String to be parsed
     * @param dflt Default value if parsing is not successful
     * @return Parsed String
     */
    public static long parseLong(String foo, long dflt) {
        try {
            return Long.parseLong(foo);
        } catch (NumberFormatException e) {
            LOGGER.error("Cause: {} - Returning dflt value ({})", e.toString(), dflt);
            return dflt;
        }
    }

    /**
     * Numbers String to native double.
     * <p>
     * This will avoid the top-level program with any Exception when parsing
     * String.
     *
     * @param foo String to be parsed
     * @return Parsed String to long, if parsing is not successful, value of
     * zero (0)
     */
    public static double parseDouble(String foo) {
        return parseDouble(foo, 0.0);
    }
    
    /**
     * Numbers String to native double.
     * <p>
     * This will avoid the top-level program with any Exception when parsing
     * String.
     *
     * @param foo String to be parsed
     * @param dflt Default value if parsing is not successful
     * @return Parsed String
     */
    public static double parseDouble(String foo, double dflt) {
        try {
            return Double.parseDouble(foo);
        } catch (NumberFormatException e) {
            LOGGER.error("Cause: {} - Returning dflt value ({})", e.toString(), dflt);
            return dflt;
        }
    }
    
    /**
     * String to native byte.
     * <p>
     * This will avoid the top-level program with any Exception when parsing
     * String.
     *
     * @param foo String to be parsed
     * @return Parsed String
     */
    public static byte parseByte(String foo) {
        return parseByte(foo, (byte) 0);
    }
    
    /**
     * String to native byte.
     * <p>
     * This will avoid the top-level program with any Exception when parsing
     * String.
     *
     * @param foo String to be parsed
     * @param dflt Default value if parsing is not successful
     * @return Parsed String
     */
    public static byte parseByte(String foo, byte dflt) {
        try {
            return Byte.parseByte(foo);
        } catch (NumberFormatException e) {
            LOGGER.error("Cause: {} - Returning dflt value ({})", e.toString(), dflt);
            return dflt;
        }
    }

    /**
     * Numbers String to BigDecimal with two (2) scales.
     * <p>
     * This will avoid the top-level program with any Exception when parsing
     * String.
     *
     * @param foo String to be translated
     * @param dflt Default value if String is not successfully translated to
     * BigDecimal
     * @return Parsed String
     */
    public static BigDecimal toBigDecimal(String foo, BigDecimal dflt) {
        try {
            return new BigDecimal(foo).setScale(2);
        } catch (Exception e) {
            LOGGER.error("Cause: {} - Returning dflt value ({})", e.toString(), dflt);
            return dflt;
        }
    }

    /**
     * Numbers String to BigDecimal with two (2) scales.
     * <p>
     * This will avoid the top-level program with any Exception when parsing
     * String.
     *
     * @param foo String to be translated
     * @return Translated String to BigDecimal, if translation is not
     * successful, value of zero (0)
     */
    public static BigDecimal toBigDecimal(String foo) {
        return toBigDecimal(foo, zeroBigDecimal());
    }

    /**
     * A zero BigDecimal with two (2) scales.
     *
     * @return (BigDecimal) 0.00
     */
    public static BigDecimal zeroBigDecimal() {
        return BigDecimal.ZERO.setScale(2);
    }

    /**
     * Cast native long to native int.
     *
     * @param num A number in instance of double
     *
     * @return Casted int
     */
    public static int toInt(long num) {
        return (int) num;
    }

    /**
     * Cast native double to native int.
     *
     * @param num A number in instance of double
     *
     * @return Casted int
     */
    public static int toInt(double num) {
        return (int) num;
    }

    /**
     * Turn your BigDecimal to Currency format. (<b>eg. Php[Philippine
     * currency</b>).
     * <p>
     * <blockquote>
     * Note: <i>In Accounting principle, negative money was enclosed with
     * parenthesis, not with negative sign.</i>
     * </blockquote>
     *
     * @param money Your BigDecimal
     * @return Your BigDecimal with currency format.
     */
    public static String toCurrencyFormat(final BigDecimal money) {
        final ThreadLocal<String> tl = new ThreadLocal<String>() {
            @Override
            protected String initialValue() {
                return NumberFormat.getCurrencyInstance().format(money.setScale(2).doubleValue());
            }
        };
        final String result = tl.get();
        tl.remove();
        return result;
    }

    /**
     * Evaluate value if it is less than zero (0).
     * <p>
     * If value is less than or equal to zero then default
     * value will be used.
     *
     * @param val Value of {@code int}
     * @param dflt Default value
     * @return {@code val} if greater than or equal to zero, 
     * otherwise {@code dflt}
     */
    public static int get(int val, int dflt) {
        if (val <= 0) {
            LOGGER.warn("val is less than 1 - Returning dflt value ({})", dflt);
            return dflt;
        }
        return val;
    }

    /**
     * Evaluate value if it is less than zero (0).
     * <p>
     * If value is less than or equal to zero then default
     * value will be used.
     *
     * @param val Value of {@code long}
     * @param dflt Default value
     * @return {@code val} if greater than or equal to zero, 
     * otherwise {@code dflt}
     */
    public static long get(long val, long dflt) {
        if (val <= 0) {
            LOGGER.error("val is less than 0 - Returning dflt value ({})", dflt);
            return dflt;
        }
        return val;
    }

    /**
     * Evaluate value if it is less than zero (0.00, decimal is based on scale).
     * <p>
     * If value is less than or equal to zero or equal to null then default
     * value will be used.
     *
     * @param val Value of {@link BigDecimal}
     * @param dflt Default value
     * @return {@code val} if greater than or equal to zero or null, 
     * otherwise {@code dflt}
     */
    public static BigDecimal get(BigDecimal val, BigDecimal dflt) {
        if (val == null) {
            LOGGER.error("val is null - Returning dflt value ({})", dflt);
            return dflt;
        }
        if (val.compareTo(BigDecimal.ZERO) <= 0) {
            LOGGER.error("val is less than 1 - Returning dflt value ({})", dflt);
            return dflt;
        }
        return val;
    }
    
    /**
     * Evaluate value if the given String is a number.
     * <p>
     * If the value is non-numeric, empty or null then default
     * value will be used.
     *
     * @param val Value of {@link BigDecimal}
     * @param dflt Default value
     * @return {@code val} if non-numeric, empty or null, 
     * otherwise {@code dflt}
     */
    public static BigDecimal get(String val, BigDecimal dflt) {
        BigDecimal result = dflt;
        try {
            if (val != null && !val.isEmpty()) {
                result = new BigDecimal(val);
            }
        } catch (Exception e) {
            LOGGER.error(e.toString(), e);
        }
        return result;
    }

    /**
     * Check if number is a natural number.
     *
     * @param num Number
     * @return True if number is positive natural number, otherwise false
     */
    public static boolean isNaturalNumber(int num) {
        return num > 0;
    }

    /**
     * Check if number is a natural integer. This means that it won't allow any
     * decimal point. The String number will be parsed to be able to evaluate.
     *
     * @param num Number in instance of String
     * @return True if number is positive natural number, otherwise false
     */
    public static boolean isNaturalInteger(String num) {
        try {
            return Numbers.parseInt(num) > 0;
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString());
            return false;
        }
    }

    /**
     * Parse the string into integer.
     *
     * @param num1 the string to be parse
     * @param num2 the value if there's an exception thrown
     * @return the parsed string. Otherwise, will return the value of num2
     */
    public static int parseToInt(String num1, int num2) {
        try {
            return Integer.parseInt(num1);
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString());
            return num2;
        }
    }

    /**
     * Check if number is a natural double value. Unlike natural integer, this
     * accepts any decimal point. The String number will be parsed to be able to
     * evaluate.
     *
     * @param num
     * @return True if the given number is a natural {@code double}, otherwise
     * false
     */
    public static boolean isNaturalDouble(String num) {
        try {
            return Double.parseDouble(num) > 0;
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString());
            return false;
        }
    }

    /**
     * Checks if String contains only number.
     *
     * @param foo String you want to be checked.
     * @return True if your String contains only number, otherwise false.
     */
    public static boolean isOnlyNum(String foo) {
        try {
            Integer.parseInt(foo);
            Long.parseLong(foo);
            Double.parseDouble(foo);
            Float.parseFloat(foo);
            return true;
        } catch (NumberFormatException e) {
            LOGGER.error("Cause: {}", e.toString());
            return false;
        }
    }

    /**
     * Convert BigDecimal to a nice number format. This format inserts comma (,)
     * when reaches thousands and so on.
     *
     * @param decimal Your BigDecimal
     * @return Number format in instance of String
     */
    public static String toNumberFormat(BigDecimal decimal) {
        return NumberFormat.getNumberInstance().format(decimal);
    }

    /**
     * Convert integer to nice number format.
     *
     * @param integer Your integer
     * @return Number format in instance of String
     */
    public static String toNumberFormat(int integer) {
        return NumberFormat.getNumberInstance().format(integer);
    }

    /**
     * Gets the int value from the showInputDialog of JOptionPane.
     * <p>
     * This will get input until the input is not an integer.
     *
     * @param message the message to appear in JOptionPane
     * @return the integer from the input.
     */
    public static int getIntValue(String message) {
        while (true) {
            try {
                return Integer.parseInt(JOptionPane.showInputDialog(null, message, "Input a natural number"));
            } catch (Exception e) {
                LOGGER.error("Cause: {}", e.toString());
            }
        }
    }

    /**
     * Gets the double value from the showInputDialog of JOptionPane.
     * <p>
     * This will get input until the input is not an double.
     *
     * @param message the message to appear in JOptionPane
     * @param frame Your JFrame
     * @return the integer from the input.
     */
    public static double getDoubleValue(String message, JFrame frame) {
        while (true) {
            try {
                final Double result = Double.parseDouble(JOptionPane.showInputDialog(null, message, "Input a natural number"));
                if (result == JOptionPane.CANCEL_OPTION) {
                    frame.dispose();
                }
                return result;
            } catch (Exception e) {
                LOGGER.error("Cause: {}", e.toString());
            }
        }
    }
    
    /**
     * String simple math addition.
     * <p>
     * Add a {@code String} value to an {@code int} value.
     * 
     * @param foo A String to be added
     * @param addTo Value to be added to String
     * @return Sum of the String and integer, , if a String has non-integer
     * value, it will be equivalent to zero (0)
     * @see Strings#add(java.lang.String, int) String value
     */
    public static int add(String foo, int addTo) {
        return Numbers.parseInt(foo, 0) + addTo;
    }
    
    /**
     * String simple math addition.
     * <p>
     * Adds two {@code String} value which sums to {@code int}.
     * 
     * @param foo A String to be added
     * @param addTo Value to be added to another String
     * @return Sum of two Strings, if a String has non-integer
     * value, it will be equivalent to zero (0)
     * @see Strings#add(java.lang.String, java.lang.String) String value
     */
    public static int add(String foo, String addTo) {
        return add(foo, Numbers.parseInt(addTo, 0));
    }
    
    /**
     * Format number.
     * <p>
     * Formatting will insert commas (,) to thousands, millions, 
     * and so on.
     * <br />
     * Note: <i> Object such as {@link Double}, {@link Integer}, 
     * {@link BigDecimal} and other Numerical objects are the only 
     * objects supported by this method. Use {@link Numbers#formatStr(java.lang.String)}
     * for String. </i>
     * 
     * @param num A number in type of {@code Object}
     * @return Formatted number
     * @see Numbers#formatStr(java.lang.String)
     */
    public static String format(Object num) {
        return NumberFormat.getNumberInstance().format(num);
    }
    
    /**
     * Format number with scale.
     * <p>
     * Formatting will insert commas (,) to thousands, millions, 
     * and so on and a decimal scale at the end based on the given
     * {@code scale} value.
     * <br />
     * Note: <i> Object such as {@link Double}, {@link Integer}, 
     * {@link BigDecimal} and other Numerical objects are the only 
     * objects supported by this method. Use {@link Numbers#formatStr(java.lang.String)}
     * for String. </i>
     * 
     * @param num A number in type of {@code Object}
     * @param scale Scale of the value to be returned
     * @return Formatted number
     * @see Numbers#formatStr(java.lang.String)
     */
    public static String format(Object num, int scale) {
        BigDecimal result;
        if (num instanceof Double) {
            result = new BigDecimal((Double) num);
        } else if (num instanceof Integer) {
            result = new BigDecimal((Integer) num);
        } else if (num instanceof BigDecimal) {
            result = (BigDecimal) num;
        } else {
            LOGGER.error("Unexpected instance of object [{}] - Returning null...", num);
            return null;
        }
        return toCurrencyFormat(result.setScale(scale)).replaceAll("[A-Za-z]", "");
    }
    
    /**
     * Format number.
     * <p>
     * Formatting will insert commas (,) to thousands, millions, 
     * and so on.
     * <br />
     * 
     * @param num A number in type of {@code Object}
     * @return Formatted number
     */
    public static String formatStr(String num) {
        return NumberFormat.getNumberInstance().format(new BigDecimal(num));
    }
    
    /**
     * Format number with scale.
     * <p>
     * Formatting will insert commas (,) to thousands, millions, 
     * and so on and a decimal scale at the end based on the given
     * {@code scale} value.
     * <br />
     * 
     * @param num A number in type of {@code Object}
     * @param scale Scale of the value to be returned
     * @return Formatted number
     */
    public static String formatStr(String num, int scale) {
        return toCurrencyFormat(get(num, BigDecimal.ZERO).setScale(scale)).replaceAll("[A-Za-z]", "");
    }
    
    /**
     * Safe BigDecimal return value.
     * <p>
     * Useful for returning BigDecimal from a method. This avoids 
     * {@code NullPointerException} from runtime.
     * 
     * @param num A {@link BigDecimal} number value
     * @return If the given argument was null, this will return
     * {@link BigDecimal#ZERO}, otherwise the given argument
     * itself
     */
    public static BigDecimal safeReturn(BigDecimal num) {
        return (num == null ? BigDecimal.ZERO : num);
    }

}
