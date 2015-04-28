package my.jutils.services;

import java.util.concurrent.ThreadFactory;
import org.slf4j.*;

/**
 * Custom ThreadFactory.
 * <p>
 * This ThreadFactory will be use across this framework in able to give threads
 * a proper name and to encapsulate the creation of threads thru Executors.
 *
 * @author Erieze Lagera
 */
public class BackgroundThreadFactory implements ThreadFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackgroundThreadFactory.class.getSimpleName());

    private final String name;

    /**
     * Create new ThreadFactory.
     * <p>
     * To give thread a proper name, invoked the non-static method
     * {@code setName} and supply the name as parameter.
     *
     * @param name Thread name
     * @see BackgroundThreadFactory#setName(java.lang.String)
     */
    public BackgroundThreadFactory(String name) {
        this.name = name;
    }

    /**
     * Set custom thread name.
     * <p>
     * This will appear like "custom_name-thread_info" Custom name can be like
     * "ClassName-ServiceName" or your call. This will add dashes (-) at the end
     * of the specified name.
     *
     * @param name Thread name
     * @return Instance of BackgroundThreadFactory
     * @deprecated
     */
    public BackgroundThreadFactory setName(String name) {
        return this;
    }

    @Override
    public Thread newThread(Runnable r) {
        final Thread t = new Thread(r, name);
        LOGGER.debug("{} created...", name);
        t.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                LOGGER.error("Cause: {}", e.toString(), e);
            }
        });
        return t;
    }

}
