package my.jutils.db;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.persistence.*;
import javax.swing.JOptionPane;
import javax.xml.parsers.*;
import my.jutils.Strings;
import org.eclipse.persistence.exceptions.*;
import org.slf4j.*;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

/**
 * Simple application-managed EntityManager.
 * <p>
 * To get started, {@code XPersist} must be initialize. You must invoke {@link XPersist#open(java.lang.String, java.lang.String, java.lang.String)}
 * to open the connection. It verifies the database connection and user credentials.
 * If the verification succeeds, it will now create new {@link EntityManagerFactory}
 * which will be use across the runtime. If there is already an active 
 * {@code EntityManagerFactory}, it will be closed explicitly. When the
 * {@code EntityManagerFactory} is open, you may now create new {@link EntityManager}
 * which will be use for SQL queries. Invoking {@link XPersist#createManager()} 
 * will create new {@code EntityManager} under the currently active 
 * {@code EntityManagerFactory}.
 * 
 * Saving and updating record was also made easy with {@code XPersist}. Just
 * use {@link XPersist#create(java.lang.Object)} to create new record, and
 * {@link XPersist#update(java.lang.Object)} to update update existing record.
 * Both methods uses the currently active {@code EntityManagerFactory} so
 * before invoking both methods, you must invoke the {@code open()}.
 * 
 * <br /><br />
 * Note: <i> You must close the {@code EntityManager} after using or after
 * executing an SQL query. </i> <br />
 * Here's a snippet on how to do so: <br />
 * <table>
 * <tr> <td>
 * final EntityManager em = XPersist.createManager(); <br />
 * // Some query here <br />
 * if (em.isOpen()) <br />
 * <blockquote>
 * em.close();<br />
 * </blockquote>
 * </td> </tr>
 * </table>
 *
 * @author Erieze and Einar Lagera
 */
public class XPersist {

    /**
     * EntityManagerFactory created by XPersist.
     * <p>
     * Access with variable made possible, so that you can open and close
     * EntityManagerFactory explicity.
     */
    public static EntityManagerFactory EMF;

    /**
     * Persistence Unit Name.
     */
    private static String PU;
    /**
     * Database server host address.
     */
    private static String SERVER;
    /**
     * Logging Level.
     */
    private static String LOGGING;
    /**
     * Singleton instance of EntityManager.
     */
    private static EntityManager EM;

    private static final AtomicBoolean open = new AtomicBoolean();

    private static final Logger LOGGER = LoggerFactory.getLogger(XPersist.class.getSimpleName());

    /**
     * This has nothing to do since XPersist class does not have non-static
     * methods.
     * <p>
     * Instead use ${@code open()} to open a new connection.
     *
     * @see XPersist#verify(java.lang.String, java.lang.String,
     * java.lang.String, boolean)
     */
    private XPersist() {
        LOGGER.debug("Illegal XPersist invocation! Please read the class' javadoc.");
    }

    /**
     * Open, Verify and Create new Entity Manager Factory.
     * <p>
     * After creating new EntityManagerFactory, a test on connection will be
     * evaluated. Invoking this method will also store the given parameters
     * statically and will be used along the runtime.
     *
     * Eg. <i>open("somepu", "localhost", "FINE");</i>
     * After this method invoked, <i>"somepu", "localhost"</i> and <i>"FINE"</i>
     * will be used by methods under this class.
     *
     * Note: <i>If there is currently open EntityManagerFactory, it will be
     * close before creating new. </i> Also, use "<i>localhost</i>" as default
     * host from
     * <b>persistence.xml</b> to replace it by the specified server.
     *
     * @param pu Persistence unit used for this project
     * @param server IP address of the server.
     * @param logging Eclipselink level of logging
     * @return True if EntityManagerFactory has been created successfully and
     * connection has been tested successfully, otherwise false
     *
     */
    public static boolean open(String pu, String server, String logging) {
        PU = pu;
        SERVER = server;
        LOGGING = logging;
        LOGGER.info("XPersist verification running...");

        if (XPersist.EMF == null || !XPersist.EMF.isOpen()) {
            XPersist.EMF = createEntityManagerFactory();
        } else if (XPersist.EMF.isOpen()) {
            XPersist.EMF.close();
            XPersist.EMF = createEntityManagerFactory();
        }
        if (test()) {
            open.set(true);
            return true;
        } else {
            open.set(false);
            return false;
        }
    }

    /**
     * Close current EntityManagerFactory explicitly.
     */
    public static void closeFactory() {
        if (!XPersist.isOpen()) {
            throw new IllegalStateException("XPersist -> [ERROR] EntityManagerFactory has been already closed or null.");
        } else {
            XPersist.EMF.close();
            open.set(false);
            LOGGER.info("EntityManagerFactory is now closed...");
        }
    }

    /**
     * Test the connection.
     * <p>
     * This has to be invoked from initialization to have initial testing on
     * database connection.
     *
     * @return True if connection passed the test, otherwise false
     */
    public static boolean test() {
        try {
            XPersist.createManager().close();
            LOGGER.info("XPersist tested successfully!");
            LOGGER.info("Summary: [host={}, pu={}]", SERVER, PU);
            return true;
        } catch (DatabaseException e) {
            if (e.getErrorCode() == 40000) {
                LOGGER.error("Error connecting to database! Connection refused!");
                JOptionPane.showMessageDialog(null, "Error connecting to database!", "Connection refused.", JOptionPane.ERROR_MESSAGE);
            } else {
                LOGGER.error("Server connection error!");
                JOptionPane.showMessageDialog(null, "Server connection error!", "Connection error!", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(1);
            return false;
        } catch (Exception e) {
            LOGGER.error("Server connection error!", e);
            JOptionPane.showMessageDialog(null, "Server connection error!", "Connection error!", JOptionPane.ERROR_MESSAGE);
            System.exit(2);
            return false;
        }
    }

    /**
     * Create an Entity Manager Factory.
     * <p>
     * Create an instance of this class invoke this method automatically.
     *
     * @return Application-managed Entity Manager Factory
     * @see XPersist
     */
    public static EntityManagerFactory createEntityManagerFactory() {
        return Persistence.createEntityManagerFactory(PU, getProperties());
    }

    /**
     * Create new EntityManager from the current EntityManagerFactory. This
     * EntityManager have to be closed explicity.
     *
     * @return New EntityManager
     */
    public static EntityManager createManager() {
        if (XPersist.isOpen()) {
            /*if (EM.isOpen()) {
             LOGGER.info("Current insntace of EntityManager is open, closing...");
             EM.close();
             }
             XPersist.EM = XPersist.EMF.createEntityManager();
             return XPersist.EM;*/
            return XPersist.EMF.createEntityManager();
        } else {
            LOGGER.warn("EntityManagerFactory is already closed! Did you verify XPersist?");
            throw new IllegalStateException("XPersist -> [ERROR] EntityManagerFactory is close.");
        }
    }

    /**
     * Close the current instance of EntityManager.
     * <p>
     * Using this method also verifies the status of the current
     * EntityManagerFactory, causing {@code IllegalStateException} if it is
     * already closed or not open. EntityManager instance is also verified by
     * this method.
     *
     * @deprecated BETA: at its early stage
     */
    public static void close() {
        if (!XPersist.isOpen()) {
            throw new IllegalStateException("XPersist -> [ERROR] EntityManagerFactory has been already closed or null.");
        }
        if (XPersist.EM == null) {
            LOGGER.error("EntityManager is not initialized properly. Did you create Entity Manager?");
            throw new IllegalStateException("XPersist -> [ERROR] EntityManager is not initialized properly. Did you create Entity Manager?");
        } else {
            if (!XPersist.EM.isOpen()) {
                LOGGER.error("EntityManager has been already closed or not open.");
                throw new IllegalStateException("XPersist -> [ERROR] EntityManager has been already closed or not open.");
            } else {
                XPersist.EM.close();
                LOGGER.debug("EntityManager is now closed...");
            }
        }
    }

    /**
     * Properties for the EntityManager.
     * <p>
     * This consists of driver, url, user, pass, and other possible properties
     * based on your Persistence library.
     *
     * @return Properties of the EntiyManager
     */
    private static Map<String, String> getProperties() {
        final Map<String, String> properties = getPersistenceProp();
        final String dbURL = properties.get("javax.persistence.jdbc.url");
        properties.put("javax.persistence.jdbc.url", dbURL.replaceFirst(Strings.extractIPv4(dbURL), SERVER));

        /**
         * * Optional properties for logging. **
         */
        if (LOGGING != null && !LOGGING.isEmpty()) {
            properties.put("eclipselink.logging.level", LOGGING);
            properties.put("eclipselink.logging.timestamp", "true");
            properties.put("eclipselink.logging.session", "true");
        }
        return properties;
    }

    /**
     * Parses the current project's <b>persistence.xml</b>.
     * <p>
     * This will retrieves the element's attributes under <i>property</i> node.
     * This method was used by {@code getProperties()} privately to retrieve and
     * alter the URL's server.
     * <br />
     * Note: persistence.xml must use <b>localhost</b> as default value to work
     * alter it properly.
     *
     * @return Map of the persistence properties
     */
    public static Map<String, String> getPersistenceProp() {
        final Map<String, String> prop = new HashMap<>();
        try {
            final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().
                    parse(Thread.currentThread().
                            getContextClassLoader().
                            getResource("META-INF/persistence.xml").openStream());
            doc.getDocumentElement().normalize();
            LOGGER.debug("Root element: {}", doc.getDocumentElement().getNodeName());
            Element elem = (Element) doc.getElementsByTagName("properties").item(0);
            final NodeList child = elem.getChildNodes();
            for (int i = 0; i < child.getLength(); i++) {
                if (child.item(i).getNodeName().equals("property")) {
                    elem = (Element) child.item(i);
                    prop.put(elem.getAttribute("name"), elem.getAttribute("value"));
                    LOGGER.debug("value={}", elem.getAttribute("value"));
                }
            }
            return prop;
        } catch (ParserConfigurationException | IOException | SAXException e) {
            LOGGER.error(e.getMessage(), e);
            return prop;
        }
    }

    /**
     * Check whether EntityManagerFactory is open or closed.
     * <p>
     * Nullity of EntityManagerFactory is also verified when using this method.
     *
     * @return True if EntityManagerFactory is currently open, otherwise false
     */
    public static boolean isOpen() {
        if (XPersist.EMF == null || !XPersist.EMF.isOpen()) {
            LOGGER.error("EntityManagerFactory has been already closed or null.");
            open.set(false);
        } else {
            open.set(true);
        }
        return open.get();
    }
    
    /**
     * Create new record based on given instance of this EntityClass.
     * <p>
     * * Object instance is implicit which may cause Exception if the given
     * {@code entity} value is not a type of Entity class.
     *
     * @param entity Instance of an Entity class that contains the fields values
     * which will be used in creating new record
     * @return True if record created successfully, otherwise false
     */
    public static boolean create(Object entity) {
        final EntityManager em = XPersist.createManager();
        final AtomicBoolean result = new AtomicBoolean();
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.close();
            em.getTransaction().commit();
            result.set(true);
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString(), e);
            result.set(false);
            em.getTransaction().rollback();
        }
        if (em.isOpen()) {
            em.close();
        }
        return result.get();
    }
    
    /**
     * Update record based on given instance of this EntityClass.
     * <p>
     * Object instance is implicit which may cause Exception if the given
     * {@code entity} value is not a type of Entity class.
     *
     * @param entity Instance of an Entity Class that contains the updated
     * fields
     * @return True if given entity updated successfully, otherwise false
     */
    public static boolean update(Object entity) {
        final EntityManager em = XPersist.createManager();
        final AtomicBoolean result = new AtomicBoolean();
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.close();
            em.getTransaction().commit();
            result.set(true);
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString(), e);
            result.set(false);
            em.getTransaction().rollback();
        }
        if (em.isOpen()) {
            em.close();
        }
        return result.get();
    }

}
