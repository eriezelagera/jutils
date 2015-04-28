package my.jutils.services;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import my.jutils.sms.*;
import my.jutils.sms.SMSModule.Status;
import org.slf4j.*;
import org.smslib.*;

/**
 * Create a new thread for Start and Shutdown of SMS gateway and service.
 * @author Erieze and Einar Lagera
 */
public class SMSServiceThread implements Runnable {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SMSServiceThread.class.getSimpleName());
    
    private final Actions action;
    private final AtomicBoolean success;
    private Status status;
    
    /**
     * Create thread for starting or shutting down of the SMS gateway and service.
     * @param action Action for this thread
     * @see Actions
     */
    public SMSServiceThread(Actions action) {
        this.action = action;
        this.success = new AtomicBoolean();
        this.status = SMSModule.Status.Waiting;
    }
    
    /**
     * Action to perform for the SMS gateway.
     */
    public static enum Actions {
        /**
         * Execute Startup Service Thread.
         */
        Start,
        /**
         * Execute Shutdown Service Thread.
         */
        Shutdown
    }
    
    @Override
    public void run() {
        synchronized(this) {
            switch (action) {
                case Start :
                    try {
                        SMSModule.SMS_SERVICE.startService();
                        success.set(true);
                        status = SMSModule.Status.Started;
                    } catch (IOException | InterruptedException | SMSLibException e) {
                        LOGGER.warn("Cannot start SMS Service and Gateway...");
                        success.set(false);
                        status = SMSModule.Status.Failed;
                    }
                    break;
                case Shutdown :
                    try {
                        SMSModule.GATEWAY.stopGateway();
                        SMSModule.SMS_SERVICE.stopService();
                        status = SMSModule.Status.Shutdown;
                        success.set(true);
                    } catch (IOException | InterruptedException | SMSLibException e) {
                        LOGGER.warn("Cannot stop SMS Service and Gateway...", e);
                        success.set(false);
                        status = SMSModule.Status.Failed;
                        
                    }
                    break;
            }
            notifyAll();
        }
    }
    
    /**
     * Check if the SMS service and gateway success successfully.
     * @return SMS Status
     */
    public boolean isSuccess() {
        return success.get();
    }
    
    /**
     * The result status in sending SMS.
     * @return Status result
     */
    public SMSModule.Status getStatus() {
        return status;
    }
    
}
