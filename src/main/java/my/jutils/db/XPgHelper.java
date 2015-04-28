package my.jutils.db;

import java.sql.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.persistence.EntityManager;
import org.postgresql.*;
import org.slf4j.*;

/**
 * Simple Utility for PgNotification. <p>
 * This utility serves as helper for handling Postgres triggers.
 * @author Erieze Lagera
 */
public class XPgHelper {

    private static PGConnection pgconn;

    private static final Logger LOGGER = LoggerFactory.getLogger(XPgHelper.class.getSimpleName());

    /**
     * Execute Postgres Listener. <p>
     * Executing query Listen will invoke your Triggers. Usage of this method is
     * to invoke this at your initialization class to start Listening to your
     * triggers. Once this was invoked, you will be notified thru {@code hasNotified}
     * or {@code hasNotification} method under this class.
     * <br />
     * If you have multiple triggers, then you may invoke this more than once.
     * <br />
     * This will assume that XPersist is already open.
     *
     * @param listener
     * @return True if the system successfully listens to the
     * given listener, otherwise false
     */
    public static boolean execListener(String listener) {
        if (XPersist.isOpen()) {
            final EntityManager em = XPersist.createManager();
            em.getTransaction().begin();
            final Connection conn = em.unwrap(Connection.class);
            XPgHelper.pgconn = (org.postgresql.PGConnection) conn;
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("LISTEN " + listener);
            } catch (SQLException e) {
                LOGGER.error(e.getSQLState(), e);
                return false;
            }
            em.getTransaction().commit();
            LOGGER.info("System is now listening to " + listener + "...");
            return true;
        }
        else {
            LOGGER.warn("XPerist is currently close. Did you forget to invoke XPersist.open()?");
            return false;
        }
    }

    /**
     * Check if there is new notification from your trigger.
     *
     * @return True if there has been modification based on your trigger, otherwise
     *  false
     */
    public static boolean hasNotified() {
        try {
            // Fetch notification
            final PGNotification notifications[] = pgconn.getNotifications();
            if (notifications != null) {
                for (PGNotification notification : notifications) {
                    LOGGER.info("New notification from: {} {}", notification.getName(), notification.getParameter());
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            LOGGER.error("Cause: {}-{}", e.getSQLState(), e.toString(), e);
            return false;
        }
    }
    
    /**
     * Check if there is new notification from your trigger. <p>
     * This is useful for multiple row modification.
     *
     * @return The total number of notification, zero (0) if none
     */
    public static int hasNotificationCount() {
        final AtomicInteger count = new AtomicInteger();
        try {
            // Fetch notification
            final PGNotification notifications[] = pgconn.getNotifications();
            if (notifications != null) {
                for (PGNotification notification : notifications) {
                    LOGGER.info("New notification from: {} {}", notification.getName(), notification.getParameter());
                    count.getAndIncrement();
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Cause: {}-{}", e.getSQLState(), e.toString(), e);
        }
        return count.get();
    }
    
    /**
     * Check if there is new notification from your trigger. <p>
     * This is useful for multiple triggers. With this method, you may identify
     * the listener of the current notification.
     *
     * @return Name of trigger
     */
    public static String hasNotifications() {
        String result = "";
        try {
            // Fetch notification
            final PGNotification notifications[] = pgconn.getNotifications();
            if (notifications != null) {
                for (PGNotification notification : notifications) {
                    LOGGER.info("New notification from: {} {}", notification.getName(), notification.getParameter());
                    result = notification.getName();
                    break;
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Cause: {}-{}", e.getSQLState(), e.toString(), e);
        }
        return result;
    }
    
    /**
     * Check if there is new notification from your trigger. <p>
     * This is useful for multiple row modification.
     * 
     * @return Notifications notified by the trigger
     */
    public static PGNotification[] getNotifications() {
        PGNotification[] result = null;
        try {
            // Fetch notification
            final PGNotification notifications[] = pgconn.getNotifications();
            if (notifications != null) {
                for (PGNotification notification : notifications) {
                    LOGGER.info("New notification from: {} {}", notification.getName(), notification.getParameter());
                }
                result = notifications;
            }
        } catch (SQLException e) {
            LOGGER.error("Cause: {}-{}", e.getSQLState(), e.toString(), e);
        }
        return result;
    }

}
