package my.jutils.config;

import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;
import my.jutils.JFile;
import my.jutils.Strings;
import org.apache.commons.configuration.*;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.commons.configuration.reloading.InvariantReloadingStrategy;
import org.slf4j.*;

/**
 * Load the config file which contains the system parameters.
 * <p>
 * JConfig is useful for complex Config. Apache Commons Configurations offers
 * useful tools for manipulating the config file. If you want to retrieve a
 * value which is not String or Integer, use {@code getInstance()} to have
 * generic type.
 * <br/ >
 * This small config tool uses Apache Commons Configuration internally. Then for
 * logging, slf4j was used since it was a great and simple logger for Java.
 * <br /><br />
 * References of the following frameworks used:
 * <li>
 * <a href="http://commons.apache.org/proper/commons-configuration/index.html"><b>Commons
 * Configuration</b></a>
 * <li> <a href="http://www.slf4j.org"><b>SLF4J</b></a>
 *
 * @author Erieze and Einar Lagera
 */
public final class JConfig {

    /**
     * Singleton instance of Configuration.
     */
    private static PropertiesConfiguration CONFIG = null;
    /**
     * Store the filename for necessary usage.
     */
    private static String FILENAME = "";
    private static final Logger LOGGER = LoggerFactory.getLogger(JConfig.class.getSimpleName());

    /**
     * This has nothing to do since Config class does not have non-static
     * methods.
     * <p>
     * Instead use {@code verify()} to open the Config file.
     *
     * @see JConfig#verify(java.lang.String)
     */
    private JConfig() {
        LOGGER.debug("Incorrect usage of JConfig!");
    }

    /**
     * Get the instance of PropertiesConfiguration.
     * <p>
     * Getting the instance of PropertiesConfiguration will give you access in
     * getting different data types of properties. You can also use some useful
     * methods under PropertiesConfiguration.
     * <br />
     * Note: You'll need to verify this config before invoking this method to
     * avoid Configuration error. Eg. <i>
     * {@code getString()}, {@code getInteger()}, and other data types.</i>
     *
     * @return Instance of PropertiesConfiguration
     * @see PropertiesConfiguration
     * @see JConfig#verify(java.lang.String, boolean)
     * @see JConfig#verify(java.lang.String, java.lang.String, boolean)
     */
    public static Configuration getInstance() {
        LOGGER.debug("Getting the instance of PropertiesConfiguration based on given filename parameterized on verify().");
        if (CONFIG == null) {
            LOGGER.warn("Did you already verify the JConfig?");
        }
        return CONFIG;
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
     * set {@code autoReload} to {@code true} </i>
     *
     * @param filename Filename of the config file including it's path
     * @param autoReload Automatically reload whenever config file was modified?
     * @return True if Config file was successfully verified and ready for use,
     * otherwise false
     */
    public static boolean verify(String filename, boolean autoReload) {
        FILENAME = filename; // Store the filename
        try {
            CONFIG = new PropertiesConfiguration();
            if (autoReload) {
                CONFIG.setReloadingStrategy(new FileChangedReloadingStrategy());
            } else {
                CONFIG.setReloadingStrategy(new InvariantReloadingStrategy());
            }
            CONFIG.setIOFactory(new WhitespaceIOFactory());
            CONFIG.setFile(new File(filename));
            CONFIG.setAutoSave(true);
            CONFIG.load();

            if (CONFIG.isEmpty()) {
                LOGGER.warn("Config file is empty!");
                return false;
            }
            LOGGER.info("Config verification success!");
            return true;
        } catch (ConfigurationException e) {
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
     * If you want to access another Config file, you may invoke this method
     * again and with different parameter.
     * <br />
     * Note: <i> This is a Singleton class, {@code verify} is only called once
     * if you only have one config file. If you want to sync config at runtime,
     * set {@code autoReload} to {@code true} </i>
     *
     * @param content Default content, template or current content
     * @param filename Filename of the config file including it's path
     * @param autoReload Automatically reload whenever config file was modified?
     * @return True if Config file was successfully verified and ready for use,
     * otherwise false
     */
    public synchronized static boolean verify(String content, String filename, boolean autoReload) {
        if (JFile.mkfile(filename)) {
            new File(filename).delete();
            JFile.createFile(content, filename);
        }
        if (JFile.mkfile(filename + ".bak")) {
            new File(filename + ".bak").delete();
            JFile.createFile(content, filename + ".bak");
        }
        return verify(filename, autoReload);
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
     * @param key The property key
     * @param dflt Default value
     * @return The value in this property list with the specified key value,
     *  otherwise the default value
     * @see PropertiesConfiguration#getString(java.lang.String)
     * @see JConfig#verify(java.lang.String, boolean)
     * @see JConfig#verify(java.lang.String, java.lang.String, boolean)
     */
    public static String getString(String key, String dflt) {
        LOGGER.debug("Retrieving value for key [" + key + "]");
        return CONFIG.getString(key, dflt);
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
     * @param key The property key
     * @return The value in this property list with the specified key value,
     *  otherwise null
     * @see PropertiesConfiguration#getString(java.lang.String)
     * @see JConfig#verify(java.lang.String, boolean)
     * @see JConfig#verify(java.lang.String, java.lang.String, boolean)
     */
    public static String getString(String key) {
        return getString(key, null);
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
     * @param key The property key
     * @param dflt Default value
     * @return The value in this property list with the specified key value,
     *  otherwise the default value
     * @see PropertiesConfiguration#getInteger(java.lang.String,
     * java.lang.Integer)
     * @see JConfig#verify(java.lang.String, boolean)
     * @see JConfig#verify(java.lang.String, java.lang.String, boolean)
     */
    public static Integer getInteger(String key, int dflt) {
        LOGGER.debug("Retrieving value for key [" + key + "]");
        return CONFIG.getInteger(key, dflt);
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
     * @param key The property key
     * @return The value in this property list with the specified key value,
     *  otherwise zero (0)
     * @see PropertiesConfiguration#getInteger(java.lang.String,
     * java.lang.Integer)
     * @see JConfig#verify(java.lang.String, boolean)
     * @see JConfig#verify(java.lang.String, java.lang.String, boolean)
     */
    public static Integer getInteger(String key) {
        return getInteger(key, 0);
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
     * @param key The property key
     * @param dflt Default value
     * @return The value in this property list with the specified key value,
     * otherwise the default value
     * @see JINI4j#verify(java.lang.String)
     */
    public static boolean getBoolean(String key, boolean dflt) {
        LOGGER.debug("Retrieving value for key [" + key + "]");
        final AtomicBoolean result = new AtomicBoolean();
        try {
            result.set(Strings.getBoolean(CONFIG.getString(key), dflt));
        } catch (ConversionException e) {
            LOGGER.error("Cause: {} - Default value will be returned", e.toString());
            result.set(dflt);
        }
        return result.get();
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
     * @param key The property key
     * @return The value in this property list with the specified key value,
     * otherwise zero (0)
     * @see JConfig#verify(java.lang.String, boolean)
     * @see JConfig#verify(java.lang.String, java.lang.String, boolean)
     */
    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    /**
     * Set new value for the specified property.
     * <p>
     * If the key is not found in the given config property list, the default
     * property list, and its defaults, recursively, are then checked.
     * <br />
     * Note: You'll need to verify this config before invoking this method to
     * avoid Configuration error.
     *
     * @param key The property key
     * @param o New value
     * @see PropertiesConfiguration#setProperty(java.lang.String,
     * java.lang.Object)
     * @see JConfig#verify(java.lang.String, boolean)
     * @see JConfig#verify(java.lang.String, java.lang.String, boolean)
     */
    public static void setProperty(String key, Object o) {
        LOGGER.debug("Setting property for key [" + key + "]");
        CONFIG.setProperty(key, o);
    }

    static class WhitespaceIOFactory extends PropertiesConfiguration.DefaultIOFactory {

        /**
         * Return our special properties reader.
         *
         * @param in
         * @param delimiter
         * @return
         */
        @Override
        public PropertiesConfiguration.PropertiesReader createPropertiesReader(Reader in, char delimiter) {
            return new WhitespacePropertiesReader(in, delimiter);
        }
    }

    static class WhitespacePropertiesReader extends PropertiesConfiguration.PropertiesReader {

        public WhitespacePropertiesReader(Reader in, char delimiter) {
            super(in, delimiter);
        }

        /**
         * Special algorithm for parsing properties keys with whitespace. This
         * method is called for each non-comment line read from the properties
         * file.
         */
        @Override
        protected void parseProperty(String line) {
                // simply split the line at the first '=' character
            // (this should be more robust in production code)
            if (line.charAt(0) == '[' && line.charAt(line.length() - 1) == ']') {
                return;
            }
            int pos;
            if ((pos = line.indexOf('=')) == -1 && (pos = line.indexOf(':')) == -1) {
                throw new StringIndexOutOfBoundsException("Delimiter equal (=) pr colon (:) not found between key and value.");
            }
            String key = line.substring(0, pos).trim();
            String value = line.substring(pos + 1).trim();
            // Now store the key and the value of the property
            initPropertyName(key);
            initPropertyValue(value);
        }
    }

    /**
     * Get the filename of the current session.
     * 
     * @return Filename given from {@code verify(String, boolean)}
     */
    public static String getFilename() {
        return FILENAME;
    }
    
}
