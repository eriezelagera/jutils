package my.jutils;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.*;
import javax.swing.JOptionPane;
import org.slf4j.*;

/**
 * Utility for String and character processing.
 *
 * @author Erieze Lagera and Einar Lagera
 */
public class Strings {

    private static final Logger LOGGER = LoggerFactory.getLogger(Strings.class.getSimpleName());

    /**
     * Set your first letter of String to <b>Uppercase</b>.
     *
     * @param upWord Any word you want to make right.
     * @return String with first letter uppercased.
     */
    public static String toUpCaseFirst(String upWord) {
        return Character.toUpperCase(upWord.charAt(0)) + upWord.substring(1);
    }

    /**
     * Set every first letter of String to <b>Uppercase</b>.
     *
     * @param upWord Any word you want to make right.
     * @return String with every first letter uppercased.
     */
    public static String toUpCaseFirstEveryWord(String upWord) {
        final char[] c_word = upWord.toCharArray();
        String s_word = "";

        try {
            for (int i = 0; i < c_word.length; i++) {
                if (i == 0 && !Character.isWhitespace(c_word[0])) {
                    c_word[0] = Character.toUpperCase(c_word[0]);
                }

                if (!Character.isLetter(c_word[i])) {
                    c_word[i + 1] = Character.toUpperCase(c_word[i + 1]);
                }

                s_word += c_word[i];
            }
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString());
        }
        return s_word;
    }

    /**
     * Set every other letter to <b>Uppercase</b>.
     *
     * @param altWord Any word you want to dance.
     * @return String with every other letter uppercased.
     */
    public static String toAltCaseUpFirst(String altWord) {
        char[] c_word = altWord.toCharArray();
        String s_word = "";

        for (int i = 0; i < c_word.length; i++) {
            if (i % 2 == 0) {
                s_word += Character.toUpperCase(c_word[i]);
            } else {
                s_word += Character.toLowerCase(c_word[i]);
            }
        }
        return s_word;
    }

    /**
     * Set every other letter to <b>Lowercase</b>.
     *
     * @param altWord Any word you want to dance.
     * @return String with every other letter to lowercased.
     */
    public static String toAltCaseLowFirst(String altWord) {
        char[] c_word = altWord.toCharArray();
        String s_word = "";

        for (int i = 0; i < c_word.length; i++) {
            if (i % 2 != 0) {
                s_word += Character.toUpperCase(c_word[i]);
            } else {
                s_word += Character.toLowerCase(c_word[i]);
            }
        }
        return s_word;
    }

    /**
     * Convert Character Array (<b>char[]</b>) to String.
     *
     * @param charArr Some character array.
     * @return Converted char[] to String.
     */
    public static String charArrToStr(char[] charArr) {
        String toString = "";
        for (int i = 0; i < charArr.length; i++) {
            toString += String.valueOf(charArr[i]);
        }
        return toString;
    }

    /**
     * Reverse a String.
     *
     * @param toReverse Your String you want to reverse.
     * @return Reversed String.
     */
    public static String strReverse(String toReverse) {
        return new StringBuffer(toReverse).reverse().toString();
    }

    /**
     * This was the opposite of subString() method in some other way.
     * <p>
     * This method allow you to start your sub-Stringing from the end of your
     * String until your specified index.
     *
     * <blockquote>
     * <b>Example:</b>
     * "unhappy".substring(5) returns "happy" "Harbison".substring(5) returns
     * "bison"
     * </blockquote>
     *
     * @param str Your String.
     * @param index Starting from end of string until the specified index.
     * @return The specified String.
     */
    public static String reSubstring(String str, int index) {
        String substr = "";
        for (int i = str.length() - 1, a = index; a > 0; i--, a--) {
            substr += str.charAt(i);
        }
        return strReverse(substr);
    }

    /**
     * This was the opposite of subString() method in some other way.
     * <p>
     * This method allow you to start your sub-Stringing from the end of your
     * String.
     *
     * <blockquote>
     * <b> Example: </b>
     * "unhappy".substring(6, 1) returns "happy" "Harbison".substring(7, 2)
     * returns "bison"
     * </blockquote>
     *
     * @param str Your String.
     * @param beginIndex Beginning index. Starts from 0 until end of String.
     * @param endIndex Ending index. Start from 0 until end of String.
     * @return The specified String.
     */
    public static String reSubstring(String str, int beginIndex, int endIndex) {
        String substr = "";
        for (int i = endIndex; i <= beginIndex; i++) {
            substr += str.charAt(i);
        }
        return substr;
    }

    /**
     * Get String from specified index.
     * <p>
     * Useful when using {@code split()} under String class. This will also help
     * you to avoid errors like
     * <b>ArrayOutOfBoundsException</b>.
     *
     * @param split_str String[] from split() method.
     * @param index Index of String you want to get.
     * @return The specified String.
     */
    public static String split(String[] split_str, int index) {
        try {
            return split_str[index];
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString());
            return "";
        }
    }
    
    /**
     * Type of naming format for your Full name.
     *
     * @see Strings#normalizeName(java.lang.String, java.lang.String, java.lang.String, my.jutils.Strings.NormalizeType) 
     */
    public static enum NormalizeType {

        /**
         * Standard name format.
         * <p>
         * <b>"first_name" "middle_name" "last_name"</b>
         */
        FirstMiddleLast,
        /**
         * Last name's first format.
         * <p>
         * <b>"last_name", "first_name" "middle_name"</b>
         */
        LastFirstMiddle,
        /**
         * First and last name format.
         * <p>
         * <b>"first_name" "last_name"</b>
         */
        FirstLast,
        /**
         * Last and first name format.
         * <p>
         * <b>"last_name" "first_name"</b>
         */
        LastFirst
    }

    /**
     * Normalize person's full name.
     * <p>
     *
     * @param firstName First name
     * @param middleName Middle name
     * @param lastName Last name
     * @param type Type of normalization
     * @return Normalized name
     * @see NormalizeType
     */
    public static String normalizeName(String firstName, String middleName, String lastName, NormalizeType type) {
        switch (type) {
            case FirstMiddleLast:
                return get(firstName + " ", "") + get(middleName + " ", "") + get(lastName, "");
            case LastFirstMiddle:
                return get(lastName + ", ", "") + get(firstName + " ", "") + get(middleName, "");
            case FirstLast:
                return get(firstName + " ", "") + get(lastName, "");
            case LastFirst:
                return get(lastName + " ", "") + get(firstName, "");
            default:
                return get(firstName + " ", "") + get(middleName + " ", "") + get(lastName, "");
        }
    }

    /**
     * Possible position of the character.
     */
    public static enum FillPosition {
        /**
         * Place the character at the start of the String.
         */
        Start,
        /**
         * Place the character at the middle of the String based on the
         * given String length.
         */
        Middle,
        /**
         * Place the character at the end of the String.
         */
        End
    }

    /**
     * Fill a String with specified character.
     *
     * @param str A String to be processed
     * @param fill Character that will be filled with String
     * @param length Maximum length of the final String
     * @param position Possible position of the character
     * @return Specified String filled with character
     * @see FillPosition
     */
    public static String fill(String str, String fill, int length, FillPosition position) {
        String character = "";
        String result = "";
        for (int i = 0; i < length - str.length(); i++) {
            character += fill;
        }
        switch (position) {
            case Start:
                result = character + str;
                break;
            case Middle:
                result += str.substring(0, str.length() / 2);
                result += character;
                result += str.substring(str.length() / 2);
                break;
            case End:
                result = str + character;
                break;
        }
        return result;
    }
    
    /**
     * Fill a String with specified character.
     *
     * @param num An integer to be processed
     * @param fill Character that will be filled with String
     * @param length Maximum length of the final String
     * @param position Possible position of the character
     * @return Specified String filled with character
     * @see FillPosition
     */
    public static String fill(int num, int fill, int length, FillPosition position) {
        String str = String.valueOf(num);
        String character = "";
        String result = "";
        for (int i = 0; i < length - str.length(); i++) {
            character += fill;
        }
        switch (position) {
            case Start:
                result = character + str;
                break;
            case Middle:
                result += str.substring(0, str.length() / 2);
                result += character;
                result += str.substring(str.length() / 2);
                break;
            case End:
                result = str + character;
                break;
        }
        return result;
    }

    /**
     * Concatenate to Character Array to String.
     *
     * @param charArr1 1st Character Array
     * @param charArr2 2nd Character Array
     * @return Concatenated String
     */
    public static String concatenateArray(char charArr1, char charArr2) {
        return String.valueOf(charArr1) + String.valueOf(charArr2);
    }

    /**
     * Create an empty space based on the specified number of spaces.
     *
     * @param spaces Number of spaces
     * @return Spaces
     */
    public static String space(int spaces) {
        final AtomicInteger idx = new AtomicInteger();
        String space = "";
        while (idx.getAndIncrement() < spaces) {
            space += " ";
        }
        return space;
    }
    
    public static String space(String str, int length, FillPosition pos) {
        return fill(str, " ", length, pos);
    }

    /**
     * Checks if String contains only number.
     *
     * @param foo_string String you want to be checked.
     * @return True if your String contains only number, otherwise false.
     */
    public static boolean isOnlyNum(String foo_string) {
        try {
            Integer.parseInt(foo_string);
            Long.parseLong(foo_string);
            Double.parseDouble(foo_string);
            Float.parseFloat(foo_string);
            return true;
        } catch (NumberFormatException e) {
            LOGGER.error("Cause: {}", e.toString());
            return false;
        }
    }

    /**
     * Checks if String contains only number.
     *
     * @param foo_string String you want to be checked.
     * @param errMsg Generate your own error message in the form of
     * showMessageDialog()
     * @return True if your String contains only number, otherwise false.
     */
    public static boolean isOnlyNum(String foo_string, String errMsg) {
        try {
            Integer.parseInt(foo_string);
            return true;
        } catch (NumberFormatException nfe) {
            System.out.println(nfe.getLocalizedMessage());
            JOptionPane.showMessageDialog(null, errMsg);
            return false;
        }
    }

    /**
     * Map all the placeholders to a text.
     * <p>
     * This will replace all placeholder present in the text. Placeholder key
     * must contain the character that will tell that it is a placeholder.
     *
     * <b>Example.</b>
     * <blockquote>
     * From placeholders, [map.put("$name", "Juan");] From text, ["My name is
     * $name. Nice to meet you!"]
     * </blockquote>
     *
     * @param text Text
     * @param placeholders Placeholder map
     * @return Processed text, replaced all placeholder
     */
    public static String mapParams(String text, Map<String, Object> placeholders) {
        for (String word : text.split("\\s")) {
            if (isPlaceholder(word)) {
                text = text.replaceAll(word, getMapValue(word, placeholders));
            }
        }
        return text;
    }

    /**
     * Checks if the cell is a placeholder.
     * <p>
     * A placeholder should always starts with a <i>dollar sign (<b>$</b>)</i>.
     *
     * @param text Text
     * @return True if the text is a placeholder, otherwise false
     */
    public static boolean isPlaceholder(String text) {
        return !text.isEmpty() && text.charAt(0) == '$';
    }

    /**
     * Checks if the cell is a placeholder.
     * <p>
     * A placeholder should starts with the given character.
     *
     * @param text Text
     * @param placeholder Character that tells it is placeholder
     * @return True if the text is a placeholder, otherwise false
     */
    public static boolean isPlaceholder(String text, char placeholder) {
        if (placeholder == ' ') {
            return !text.isEmpty() && text.charAt(0) == '$';
        }
        return !text.isEmpty() && text.charAt(0) == placeholder;
    }

    /**
     * Get the value from the Hashmap by key. 
     * <p> 
     * The <b>key</b> is the placeholder from your template Excel file.
     *
     * @param text Text
     * @param placeholders Placeholder map
     * @return The specified value for the specified placeholder
     */
    public static String getMapValue(String text, Map<String, Object> placeholders) {
        if (placeholders.containsKey(text.substring(1))) {
            return (String) placeholders.get(text.substring(1));
        } else {
            return text;
        }
    }

    /**
     * Get a random value from String array.
     *
     * @param strArr A String array
     * @return Random value from String array
     */
    public static String getRandomString(String strArr[]) {
        final int index = new Random().nextInt(strArr.length);
        return strArr[index];
    }

    /**
     * Evaluate value if it is null or empty.
     * <p>
     * Emptiness of String was evaluated thru {@code isEmpty()} under String
     * class. If value is null or empty, then the default value will be used.
     *
     * @param val Value of String
     * @param dflt Default value
     * @return {@code dflt} if equal to {@code null} or {@link String#isEmpty()},
     * otherwise {@code val}
     */
    public static String get(String val, String dflt) {
        if (val == null || val.isEmpty()) {
            LOGGER.error("val is null or empty - Returning dflt value ({})", dflt);
            return dflt;
        }
        return val;
    }

    /**
     * Evaluate value if object is null.
     * <p>
     * If value is null, then the default value will be used.
     *
     * @param val Value of String
     * @param dflt Default value
     * @return {@code dflt} if equal to {@code null}, otherwise {@code val}
     */
    public static String get(Object val, String dflt) {
        if (val == null) {
            LOGGER.error("val is null - Returning dflt value ({})", dflt);
            return dflt;
        }
        return String.valueOf(val);
    }

    /**
     * Extract IPv4 address from a String.
     * <p>
     * Ex. "<i>jdbc:postgresql://192.168.1.100:5432/jdatabase</i>" will return
     * "<b>192.168.1.100</b>".
     *
     * @param str String that probably contains valid IPv4 address, including
     *  localhost
     * @return Extracted IPv4 address, 0.0.0.0 if not valid
     */
    public static String extractIPv4(String str) {
        final String IPADDRESS_PATTERN = "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|localhost";
        final Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
        final Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "0.0.0.0";
        }
    }

    /**
     * Place an {@code HTML tag} on the first and last part of the given
     * String.
     * <p>
     * Eg. <i> &lt;html&gt; Hello &lt;/html&gt; </i>
     * <br />
     * Note: If the given String does not contains {@code HTML} tag,
     * this will automatically place on the first and last place of the
     * given String, otherwise it no {@code HTML} tag will be placed.
     * 
     * @param foo A String
     * @return String between {@code HTML tag}
     */
    public static String html(String foo) {
        final String str = "<html> " + foo + " </html>";
        if (foo.contains("<html>") && foo.contains("</html>")) {
            return foo;
        }
        return str;
    }
    
    /**
     * Place an {@code HTML} and {@code italic} tags
     * on the first and last part of the given
     * String.
     * <p>
     * Eg. <i> &lt;html&gt; Hello &lt;/html&gt; </i>
     * <br />
     * Note: If the given String does not contains {@code HTML} tag,
     * this will automatically place on the first and last place of the
     * given String, otherwise it no {@code HTML} tag will be placed.
     * 
     * @param foo A String
     * @return String between {@code HTML} and {@code italic} tag
     */
    public static String htmlItalic(String foo) {
        final String italic = "<i> " + foo + " </i>";
        if (foo.contains("<html>") && foo.contains("</html>")) {
            return italic;
        }
        return html(italic);
    }
    
    /**
     * Place an {@code HTML} and {@code bold} tags
     * on the first and last part of the given
     * String.
     * <p>
     * Eg. <i> &lt;html&gt; Hello &lt;/html&gt; </i>
     * <br />
     * Note: If the given String does not contains {@code HTML} tag,
     * this will automatically place on the first and last place of the
     * given String, otherwise it no {@code HTML} tag will be placed.
     * 
     * @param foo A String
     * @return String between {@code HTML} and {@code bold} tag
     */
    public static String htmlBold(String foo) {
        final String bold = "<b> " + foo + " </b>";
        if (foo.contains("<html>") && foo.contains("</html>")) {
            return bold;
        }
        return html(bold);
    }
    
    /**
     * Place an {@code HTML} and {@code underline} tags
     * on the first and last part of the given
     * String.
     * <p>
     * Eg. <i> &lt;html&gt; Hello &lt;/html&gt; </i>
     * <br />
     * Note: If the given String does not contains {@code HTML} tag,
     * this will automatically place on the first and last place of the
     * given String, otherwise it no {@code HTML} tag will be placed.
     * 
     * @param foo A String
     * @return String between {@code HTML} and {@code underline} tag
     */
    public static String htmlUnderline(String foo) {
        final String underline = "<u>" + foo + "</u>";
        if (foo.contains("<html>") && foo.contains("</html>")) {
            return underline;
        }
        return html(underline);
    }
    
    /**
     * Get {@code boolean} value of a String.
     * <p>
     * Strings such as "Yes", "No", "True", "False" are the values evaluated.
     * Given string is not case-sensitive which means you may 
     * get the {@code boolean} value of string "Yes" or "yes".
     * 
     * @param foo Possible {@code boolean} value in type of {@link String}
     * @return True if the given value is positive value of type of {@code boolean}
     * ("yes", "true"), then false for negative value ("false", "no")
     */
    public static boolean getBoolean(String foo) {
        return foo.equalsIgnoreCase("Yes") || foo.equalsIgnoreCase("True");
    }
    
    /**
     * Get {@code boolean} value of a String.
     * <p>
     * Strings such as "Yes", "No", "True", "False" are the values evaluated.
     * Given string is not case-sensitive which means you may 
     * get the {@code boolean} value of string "Yes" or "yes".
     * <br />
     * If value is null or empty, then the default value will be used.
     * @param val Possible {@code boolean} value in type of {@link String}
     * @param dflt Default value
     * @return True if the given value is positive value of type of {@code boolean}
     * ("yes", "true"), then false for negative value ("false", "no")
     */
    public static boolean getBoolean(String val, boolean dflt) {
        if (val == null || val.isEmpty()) {
            LOGGER.error("val is null or empty - Returning dflt value ({})", dflt);
            return dflt;
        }
        return getBoolean(val);
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
     * @see Numbers#add(java.lang.String, int) Integer value
     */
    public static String add(String foo, int addTo) {
        return String.valueOf(Numbers.parseInt(foo, 0) + addTo);
    }
    
    /**
     * String simple math addition.
     * <p>
     * Adds two {@code String} value which sums to another
     * {@code String} value.
     * 
     * @param foo A String to be added
     * @param addTo Value to be added to another String
     * @return Sum of two Strings, if a String has non-integer
     * value, it will be equivalent to zero (0)
     * @see Numbers#add(java.lang.String, java.lang.String) Integer value
     */
    public static String add(String foo, String addTo) {
        return add(foo, Numbers.parseInt(addTo, 0));
    }
    
}
