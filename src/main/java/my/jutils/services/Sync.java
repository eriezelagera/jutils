package my.jutils.services;

import java.io.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import my.jutils.Utils;
import org.slf4j.*;

/**
 * Utilities that has to wait.
 * <p>
 * This contains utilities that needs to be wait as they may have longer time of
 * execution.
 * <br />
 * In order to synchronize in this utility, you'll need to synchronize the
 * object with instance of this class, then invoke {@code wait()} <b>UNTIL</b>
 * the {@code getStatus()} returns {@code ThreadStatus.Executed}. The code might
 * look like this: <br />
 * <i>
 * Sync s = new Sync(); <br />
 * synchronized(s) { <br />
 * <blockquote>
 * while (s.getStatus == ThreadStatus.Waiting) {
 * <blockquote>s.wait(); </blockquote>
 * }
 * </blockquote>
 * } </br />
 * </i>
 * <br />
 * Note: <i> This uses {@code Executors.newCachedThreadPool()} in handling
 * threads. ThreadFactory used was included in this package. </i>
 *
 * @author Erieze Lagera
 * @see BackgroundThreadFactory
 *
 */
public class Sync {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sync.class.getSimpleName());
    
    private ThreadStatus status;
    private final ExecutorService thread;

    /**
     * Snychronized an action.
     */
    public Sync() {
        this.thread = Executors.newCachedThreadPool(new BackgroundThreadFactory(Sync.class.getSimpleName()));
        this.status = ThreadStatus.Waiting;
    }

    /**
     * Create new file.
     * <p>
     * This uses {@code PrintWriter} in writing the file and wrapped and
     * buffered by {@code BufferedWriter} and {@code FileWriter}.
     *
     * @param filePath Full file path, this includes the absolute path with
     * filename
     * @param append Append the file if exists?
     * @return True if file was created, otherwise false
     * @throws java.lang.InterruptedException Snychronization Error
     * @throws java.util.concurrent.ExecutionException Callable Error
     */
    public boolean newFile(final String filePath, final boolean append) throws InterruptedException, ExecutionException {
        return this.thread.submit(new Callable<Boolean>() {
            @Override
            public synchronized Boolean call() throws Exception {
                PrintWriter writer = null;
                final AtomicBoolean success = new AtomicBoolean();
                try {
                    writer = new PrintWriter(new BufferedWriter(new FileWriter(filePath, append)));
                    writer.println();
                    writer.flush();
                    success.set(true);
                } catch (IOException e) {
                    LOGGER.error(e.getMessage(), e);
                    success.set(false);
                } finally {
                    if (writer != null) {
                        Utils.closeWriter(writer);
                    }
                }
                notifyAll();
                status = ThreadStatus.Executed;
                return success.get();
            }
        }).get();
    }

    /**
     * Get the current status of the thread.
     *
     * @return Status
     * @see ThreadStatus
     */
    public ThreadStatus getStatus() {
        return status;
    }

}
