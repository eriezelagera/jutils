package my.jutils.config;

import java.io.*;
import my.jutils.*;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.ini4j.*;
import org.slf4j.*;

/**
 * Load the config file which contains the system parameters.
 * <p>
 * JINI4j is useful for Config with sections, since this strongly requires the
 * Sections in every config file.
 * If you want to retrieve a value which is not String or Integer, use
 * {@code getInstance()} to have generic type.
 * <br />
 * This small config tool uses ini4j internally. Then for logging, slf4j was
 * used since it was a great and simple logger for Java.
 * <br /><br />
 * References of the following frameworks used:
 * <li> <a href="http://ini4j.sourceforge.net/index.html"><b>ini4j</b></a>
 * <li> <a href="http://www.slf4j.org"><b>SLF4J</b></a>
 *
 * @author Erieze and Einar Lagera
 */
public class JINI4j {

    /**
     * A singleton instance of Ini.
     */
    private static Ini INI = null;
    /**
     * Store the filename for necessary usage.
     */
    private static String FILENAME = "";
    private static final Logger LOGGER = LoggerFactory.getLogger(JINI4j.class.getSimpleName());

    /**
     * This has nothing to do since JINI4j class does not have non-static
     * methods.
     * <p>
     * Instead use {@code verify()} to open the Config file.
     *
     * @see JINI4j#verify(java.lang.String)
     */
    private JINI4j() {
        LOGGER.debug("Incorrect usage of JINI4j!");
    }

    /**
     * Get the instance of Ini.
     * <p>
     * Getting the instance of Ini will give you access in getting different
     * data types of properties. You can also use some useful methods under Ini.
     * <br />
     * Note: You'll need to verify this config before invoking this method to
     * avoid Configuration error. Eg. <i> {@code getString()},
     * {@code getInteger()}, and other data types.</i>
     *
     * @return Instance of Ini
     * @see PropertiesConfiguration
     * @see JINI4j#verify(java.lang.String) 
     * @see JINI4j#verify(java.lang.String, java.lang.String) 
     */
    public static Ini getInstance() {
        LOGGER.debug("Getting the instance of Ini based on given filename parameterized on verify().");
        if (INI == null) {
            LOGGER.warn("Did you already verify the JINI4j?");
        }
        return INI;
    }

    /**
     * Verify if the config file is present or has contents.
     * <p>
     * Eg. <i>verify("conf/config.properties");</i>
     * After this method invoked, file <i>config.properties</i> under <i> conf
     * </i>
     * directory will be used by methods under this class such as <i> 
     * {@code getString} and {@code setProperty}.</i>
     * <br />
     * If you want to access another Config file, you may invoke this method
     * again and with different parameter.
     * <br />
     * Note: <i> This is a Singleton class, {@code verify} is only called once
     * if you only have one config file. If you want to sync config at runtime,
     * just invoke {@code verify} before getting value fro config. </i>
     * 
     * @param filename Filename of the config file including it's path
     * @return True if Config file was successfully verified and ready for use,
     * otherwise false
     */
    public static boolean verify(String filename) {
        FILENAME = filename;
        try {
            final File f = new File(filename);
            INI = new Ini(new FileInputStream(f));
            if (INI.isEmpty()) {
                LOGGER.warn("Config file is empty!");
                return false;
            }
            INI.setFile(f);
            LOGGER.info("Config verification success!");
            return true;
        } catch (FileNotFoundException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
            return false;
        } catch (IOException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
            return false;
        }
    }

    /**
     * Verify if the config file is present or has contents.
     * <p>
     * Eg. <i>verify("somecontent", "conf/config.properties");</i>
     * After this method invoked, file <i>config.properties</i> under <i> conf
     * </i> directory will be used by methods under this class such as <i> 
     * {@code getString} and {@code setProperty}.</i> 
     * However, the content will serve as your backup, whenever it was deleted 
     * or altered.
     * <br />
     * If you want to access another Config file, you may invoke 
     * this method again and with different parameter.
     * <br />
     * Note: <i> This is a Singleton class, {@code verify} is only called once
     * if you only have one config file. If you want to sync config at runtime,
     * just invoke {@code verify} before getting value for config. </i>
     * 
     * @param content Default content, template or current content
     * @param filename Filename of the config file including it's path
     * @return True if Config file was successfully verified and ready for use,
     * otherwise false
     */
    public synchronized static boolean verify(String content, String filename) {
        if (JFile.mkfile(filename)) {
            new File(filename).delete();
            JFile.createFile(content, filename);
        }
        if (JFile.mkfile(filename + ".bak")) {
            new File(filename + ".bak").delete();
            JFile.createFile(content, filename + ".bak");
        }
        return verify(filename);
    }

    /**
     * Get the String value of the specified key under the specified section 
     * within this property list.
     * <p>
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns null
     * if the property is not found.
     * <br />
     * Note: You'll need to verify this config before invoking this method to
     * avoid Configuration error.
     *
     * @param section Section of the key
     * @param key The property key
     * @param dflt Default value
     * @return The value in this property list with the specified key value,
     * otherwise the specified default value
     * @see PropertiesConfiguration#getString(java.lang.String)
     * @see JINI4j#verify(java.lang.String)
     */
    public static String getString(String section, String key, String dflt) {
        LOGGER.debug("Retrieving value for key [" + key + "]");
        return Strings.get(INI.get(section, key), dflt);
    }
    
    /**
     * Get the String value of the specified key under the specified section 
     * within this property list.
     * <p>
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns null
     * if the property is not found.
     * <br />
     * Note: You'll need to verify this config before invoking this method to
     * avoid Configuration error.
     *
     * @param section Section of the key
     * @param key The property key
     * @return The value in this property list with the specified key value,
     * otherwise null
     * @see PropertiesConfiguration#getString(java.lang.String)
     * @see JINI4j#verify(java.lang.String)
     */
    public static String getString(String section, String key) {
        return getString(section, key, null);
    }

    /**
     * Get the integer value of the specified key under the specified section 
     * within this property list.
     * <p>
     * If the key is not found in the given config property list, the default
     * property list, and its defaults, recursively, are then checked. The
     * method returns null if the property is not found.
     * <br />
     * You'll need to verify this config before invoking this method to avoid
     * Configuration error.
     *
     * @param section Section of the key
     * @param key The property key
     * @param dflt Default value
     * @return The value in this property list with the specified key value,
     * otherwise the default value
     * @see JINI4j#verify(java.lang.String)
     */
    public static Integer getInteger(String section, String key, int dflt) {
        LOGGER.debug("Retrieving value for key [" + key + "]");
        return Numbers.parseInt(INI.get(section, key), dflt);
    }
    
    /**
     * Get the integer value of the specified key under the specified section 
     * within this property list.
     * <p>
     * If the key is not found in the given config property list, the default
     * property list, and its defaults, recursively, are then checked. The
     * method returns null if the property is not found.
     * <br />
     * You'll need to verify this config before invoking this method to avoid
     * Configuration error.
     *
     * @param section Section of the key
     * @param key The property key
     * @return The value in this property list with the specified key value,
     * otherwise zero (0)
     * @see JINI4j#verify(java.lang.String)
     */
    public static Integer getInteger(String section, String key) {
        return getInteger(section, key, 0);
    }
    
    /**
     * Get the boolean value of the specified key under the specified section 
     * within this property list.
     * <p>
     * If the key is not found in the given config property list, the default
     * property list, and its defaults, recursively, are then checked. The
     * method returns null if the property is not found.
     * <br />
     * Example of boolean values which is supported: <br />
     * <ul>
     * <li> True
     * <li> False
     * <li> Yes
     * <li> No
     * </ul>
     * Values are not case-sensitive so you may use either "Yes" or "yes" and
     * so on.
     * <br />
     * You'll need to verify this config before invoking this method to avoid
     * Configuration error.
     *
     * @param section Section of the key
     * @param key The property key
     * @param dflt Default value
     * @return The value in this property list with the specified key value,
     * otherwise the default value
     * @see JINI4j#verify(java.lang.String)
     */
    public static boolean getBoolean(String section, String key, boolean dflt) {
        LOGGER.debug("Retrieving value for key [" + key + "]");
        final String result = INI.get(section, key);
        if (result == null || result.isEmpty())
            return dflt;
        return result.equalsIgnoreCase("Yes") || result.equalsIgnoreCase("True");
    }
    
    /**
     * Get the boolean value of the specified key under the specified section 
     * within this property list.
     * <p>
     * If the key is not found in the given config property list, the default
     * property list, and its defaults, recursively, are then checked. The
     * method returns null if the property is not found.
     * <br />
     * Example of boolean values which is supported: <br />
     * <ul>
     * <li> True
     * <li> False
     * <li> Yes
     * <li> No
     * </ul>
     * Values are not case-sensitive so you may use either "Yes" or "yes" and
     * so on.
     * <br />
     * You'll need to verify this config before invoking this method to avoid
     * Configuration error.
     *
     * @param section Section of the key
     * @param key The property key
     * @return The value in this property list with the specified key value,
     * otherwise zero (0)
     * @see JINI4j#verify(java.lang.String)
     */
    public static boolean getBoolean(String section, String key) {
        return getBoolean(section, key, false);
    }

    /**
     * Set new value for the specified property.
     * <p>
     * Possible instance of object are the following, <br />
     * <ul>
     * <li> String
     * <li> Boolean
     * <li> Integer
     * </ul>
     * <br />
     * Note: You'll need to verify this config before invoking this method to
     * avoid Configuration error.
     *
     * @param section Section of the key
     * @param key The property key
     * @param o New value
     * @see JINI4j#verify(java.lang.String)
     */
    public static void setProperty(String section, String key, Object o) {
        LOGGER.debug("Setting property for key [" + key + "]");
        try {
            final Wini w = new Wini(new FileInputStream(INI.getFile()));
            if (o instanceof String) {
                w.put(section, key, (String) o);
            } else if (o instanceof Boolean) {
                w.put(section, key, (Boolean) o);
            } else if (o instanceof Integer) {
                w.put(section, key, (Integer) o);
            }
            w.store(new FileOutputStream(INI.getFile()));
            // Get new instance of JINI4j
            verify(INI.getFile().getAbsolutePath());
        } catch (IOException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        }
    }
    
    /**
     * Get the filename of the current session.
     * 
     * @return Filename given from {@code verify(String)}
     */
    public static String getFilename() {
        return FILENAME;
    }

}
