package my.jutils.sms;

import my.jutils.services.SMSThread;
import my.jutils.services.SMSServiceThread;
import java.awt.HeadlessException;
import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.*;
import javax.swing.JOptionPane;
import my.jutils.Utils;
import my.jutils.services.*;
import my.jutils.sms.notifications.*;
import org.slf4j.*;
import org.smslib.*;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.modem.*;

/**
 * <b>SMS Module Framework.</b>
 * <p>
 * This package consist of useful utility in sending and reading SMS.
 *
 * Since this is an abstract class, you should extend this class in able to
 * access fields and methods. If you're developing on a JFrame form, create a
 * Java class, extend it to this class, and from your form instantiate your
 * newly create Java class. You may refer to SendReadSMS class as an example.
 *
 * This module uses SMSLib internally for sending, receiving and other SMS
 * related stuff that this module does. Then for logging, slf4j was used since
 * it was the best than other available logger for Java. Other utilities comes
 * from the handy JUtils package which includes tools for String, Numbers, and
 * others.
 *
 * <b>Important!</b>
 * <i>Note that you'll need to configure the necessary driver for <u>RXTX</u>
 * before using this module. So that this module works perfectly. Please refer
 * to the documentation located at the root of this project.</i>
 *
 * @author Various programmers
 */
abstract public class SMSModule implements GSMProperties {

    private static final String CLASS_NAME = SMSModule.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(CLASS_NAME);

    /**
     * Gateway SMS_SERVICE.
     */
    public static Service SMS_SERVICE;

    /**
     * Gateway for your GSM modem.
     */
    public static SerialModemGateway GATEWAY;

    private final Collection<List<String>> unsentMessage;
    private final AtomicInteger sent;
    private final AtomicInteger failed;
    private final ExecutorService thread;

    /**
     * Service for manual Garbage Collection.
     */
    private ScheduledExecutorService gcd;

    private Status status;

    /**
     * Create an instance of SMS Module.
     * <p>
     * This must be protected!
     */
    public SMSModule() {
        // This should be re-initialized every time this module was called
        this.unsentMessage = new ArrayList<>();
        this.sent = new AtomicInteger();
        this.failed = new AtomicInteger();
        this.status = Status.Waiting;
        this.thread = Executors.newCachedThreadPool(new BackgroundThreadFactory(CLASS_NAME + "-SMSThread"));
    }

    /**
     * Start the SMS Gateway.
     *
     * @return True if the gateway started successfully, otherwise false
     * @throws GatewayException Gateway error
     * @throws IOException Input/Output error
     * @throws InterruptedException Interrupted thread
     */
    synchronized public boolean start() throws GatewayException, HeadlessException, IOException, InterruptedException {
        final AtomicBoolean result = new AtomicBoolean();
        if (autoGC()) {
            gcd = Executors.newSingleThreadScheduledExecutor(new BackgroundThreadFactory(CLASS_NAME + "-GarbageCollector"));
        }

        final InboundNotification inboundNotification = new InboundNotification();
        final OutboundNotification outboundNotification = new OutboundNotification();
        final CallNotification callNotification = new CallNotification();
        final GatewayStatusNotification statusNotification = new GatewayStatusNotification();
        final OrphanedMessageNotification orphanedMessageNotification = new OrphanedMessageNotification();

        try {
            LOGGER.info("Initializing SMS Module...");

            SMSModule.GATEWAY = new SerialModemGateway(gatewayId(), commPort(), baudRate(), manufacturer(), model());
            SMSModule.GATEWAY.setProtocol(AGateway.Protocols.PDU);
            SMSModule.GATEWAY.setSimPin("0000");
            SMSModule.GATEWAY.setSmscNumber(smscNumber());
            SMSModule.GATEWAY.setInbound(true);
            SMSModule.GATEWAY.setOutbound(true);

            /**
             * Note: According to this article,
             * https://code.google.com/p/smslib/issues/detail?id=486 some modem
             * should set Storage Location manually to avoid
             * IndexOutOfBoundExceptions.
             */
            SMSModule.GATEWAY.getATHandler().setStorageLocations("SM");

            // Setup the notification methods.
            SMSModule.SMS_SERVICE = Service.getInstance();
            //SMSModule.SMS_SERVICE.setInboundMessageNotification(inboundNotification);
            SMSModule.SMS_SERVICE.setOutboundMessageNotification(outboundNotification);
            SMSModule.SMS_SERVICE.setCallNotification(callNotification);
            SMSModule.SMS_SERVICE.setGatewayStatusNotification(statusNotification);
            SMSModule.SMS_SERVICE.setOrphanedMessageNotification(orphanedMessageNotification);

            SMSModule.SMS_SERVICE.addGateway(SMSModule.GATEWAY);

            final SMSServiceThread servicet = new SMSServiceThread(SMSServiceThread.Actions.Start);
            thread.submit(servicet);
            synchronized (servicet) {
                while (servicet.getStatus() == Status.Waiting) {
                    try {
                        servicet.wait();
                    } catch (InterruptedException e) {
                        LOGGER.debug(e.getMessage(), e);
                    }
                }
                notifyAll();
            }

            if (SMSModule.isGatewayStarted()) {
                modemInfo();
                LOGGER.info("Gateway started! Send, Read and Console module is now available...");
                status = Status.Started;
                if (autoGC()) {
                    gcd.scheduleWithFixedDelay(new Runnable() {
                        @Override
                        public void run() {
                            Utils.gc();
                        }
                    }, 0, autGCInterval(), TimeUnit.MINUTES);
                }
            } else {
                LOGGER.warn("Cannot start SMS gateway.");
                JOptionPane.showMessageDialog(null,
                        "Cannot start SMS gateway.\nPress okay to continue",
                        "SMS not loaded", JOptionPane.WARNING_MESSAGE);
                status = Status.Failed;
            }
            result.set(servicet.isSuccess());
        } catch (SMSLibException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
            result.set(false);
        }
        return result.get();
    }

    /**
     * Shutdown the SMS Gateway.
     *
     * @return True if the gateway has shutdown successfully, false if gateway
     * is already shutdown.
     * @throws InterruptedException Interrupted thread
     */
    synchronized public boolean shutdown() throws InterruptedException {
        if (isGatewayStarted()) {
            LOGGER.info("Shutting down SMS gateway...");
            final SMSServiceThread servicet = new SMSServiceThread(SMSServiceThread.Actions.Shutdown);
            thread.submit(servicet);
            synchronized (servicet) {
                while (servicet.getStatus() == Status.Waiting) {
                    servicet.wait();
                }
                notifyAll();
            }
            status = Status.Shutdown;
            return servicet.isSuccess();
        }
        return false;
    }

    /**
     * Restart the currently active SMS Gateway.
     * <p>
     * Any caught exception considered as failed action.
     *
     * @return True if the gateway was successfully stopped and successfully
     * started, otherwise false if one of the action failed
     */
    synchronized public boolean restart() {
        final AtomicBoolean isStopped = new AtomicBoolean();
        final AtomicBoolean isStarted = new AtomicBoolean();
        try {
            if (shutdown()) {
                LOGGER.info("Gateway shutdown successfully");
                isStopped.set(true);
            } else {
                LOGGER.warn("Gateway shutdown unsuccessfully");
                isStopped.set(false);
            }
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString());
            isStopped.set(false);
        }
        try {
            if (start()) {
                LOGGER.info("Gateway started successfully");
                isStarted.set(true);
            } else {
                LOGGER.warn("Gateway not started. An error occurred!");
                isStarted.set(false);
            }
        } catch (GatewayException | HeadlessException | IOException | InterruptedException e) {
            LOGGER.error("Cause: {}", e.toString());
            isStarted.set(false);
        }
        return isStopped.get() && isStarted.get();
    }

    /**
     * Check if the gateway is already started. If the gateway is not yet
     * started, invoke start()
     *
     * @return True if the gateway is started, otherwise false
     * @see SMSModule#start()
     */
    public static boolean isGatewayStarted() {
        if (SMSModule.GATEWAY.getStatus() != AGateway.GatewayStatuses.STARTED) {
            LOGGER.warn("Gateway is not yet started.");
            return false;
        }
        return true;
    }

    /**
     * Send SMS message asynchronously.
     * <p>
     * To confirm if message is sent, get the total sent number from
     * {@code getSent()} or {@code getFailed()} for total failed. If sent is
     * greater than zero (0), therefore message is sent, otherwise number of
     * failed is greater than zero. You can also get the list of unset messages
     * from {@code getUnsentMessage()}.
     *
     * @param recipient Recipient
     * @param message Message to be sent
     * @param async Send message asynchronously?
     * @throws TimeoutException Timeout error
     * @throws GatewayException Gateway error
     * @throws IOException Input/Output error
     * @throws java.lang.InterruptedException Interrupted thread
     * @see SMSModule#getSent()
     * @see SMSModule#getFailed()
     * @see SMSModule#getUnsentMessage()
     */
    public void send(String recipient, String message, boolean async) throws TimeoutException, GatewayException, IOException, InterruptedException {
        final SMSThread smst = new SMSThread(recipient, message, async);
        thread.submit(smst);
        synchronized (smst) {
            while (smst.getStatus() == Status.Waiting) {
                smst.wait();
            }
        }
        if (!smst.isSuccess()) {
            final List<String> l = new ArrayList<>();
            l.add(recipient);
            l.add(message);
            setUnsentMessage(l);
            addFailed();
        } else {
            addSent();
        }
    }

    /**
     * Send SMS message to more than one recipients asynchronously.
     * <p>
     * This action was only invoking {@code send(String, String, boolean}
     * multiple times based on the size of the Collection.
     *
     * @param recipients Recipients
     * @param message Message to be sent
     * @param async Send message asynchronously?
     * @throws TimeoutException Timeout error
     * @throws GatewayException Gateway error
     * @throws IOException Input/Output error
     * @throws InterruptedException Interrupted thread
     * @see SMSModule#send(java.lang.String, java.lang.String, boolean)
     */
    public void send(Collection<String> recipients, String message, boolean async) throws TimeoutException, GatewayException, IOException, InterruptedException {
        for (String recipient : recipients) {
            send(recipient, message, async);
        }
        /*final SMSThread smst = new SMSThread(recipients, message, async);
         smst.setAction(SMSThread.Actions.Send);
         thread.submit(smst);*/
    }

    /**
     * Read the messages.
     *
     * @param classes Class of the messages to be read
     * @return Collection of the read messages
     * @throws TimeoutException Timeout error
     * @throws GatewayException Gateway error
     * @throws IOException Input/Output error
     * @throws InterruptedException Interrupted thread
     * @see InboundMessage
     */
    public Collection<List<String>> read(MessageClasses classes) throws TimeoutException, GatewayException, IOException, InterruptedException {
        final SMSThread smst = new SMSThread(classes);
        smst.setReadAction(SMSThread.ReadAction.Read);
        thread.submit(smst);
        synchronized (smst) {
            while (smst.getStatus() == Status.Waiting) {
                smst.wait();
            }
        }
        return smst.getInbox();
    }

    /**
     * Read the messages.
     *
     * @param classes Class of the message to be read
     * @return Collection of the read messages in instance of InboundMessage
     * @throws TimeoutException Timeout error
     * @throws GatewayException Gateway error
     * @throws IOException Input/Output error
     * @throws InterruptedException Interrupted thread
     * @see MessageClasses
     */
    public Collection<InboundMessage> readIM(InboundMessage.MessageClasses classes) throws TimeoutException, GatewayException, IOException, InterruptedException {
        final SMSThread smst = new SMSThread(classes);
        smst.setReadAction(SMSThread.ReadAction.ReadIM);
        thread.submit(smst);
        synchronized (smst) {
            while (smst.getStatus() == Status.Waiting) {
                smst.wait();
            }
        }
        return smst.getInboxIM();
    }

    /**
     * Read the messages and print the information.
     *
     * @param classes Class of the message to be read
     * @throws TimeoutException Timeout error
     * @throws GatewayException Gateway error
     * @throws IOException Input/Output error
     * @throws InterruptedException Interrupted thread
     * @see MessageClasses
     */
    public void readOut(InboundMessage.MessageClasses classes) throws TimeoutException, GatewayException, IOException, InterruptedException {
        final SMSThread smst = new SMSThread(classes);
        smst.setReadAction(SMSThread.ReadAction.ReadOut);
        thread.submit(smst);
        synchronized (smst) {
            while (smst.getStatus() == Status.Waiting) {
                smst.wait();
            }
            if (smst.isSuccess()) {
                LOGGER.warn("Messages successfully read out...");
            }
        }
    }

    /**
     * Execute SMS Console.
     */
    public void console() {
        final SMSThread smst = new SMSThread();
        Executors.newSingleThreadExecutor(new BackgroundThreadFactory(getClass().getSimpleName() + "-Console")).submit(smst);
    }

    /**
     * Display modem information.
     *
     * @throws TimeoutException Timeout error
     * @throws GatewayException Gateway error
     * @throws IOException Input/Output error
     * @throws InterruptedException Interrupted thread
     */
    public static void modemInfo() throws GatewayException, HeadlessException, IOException, InterruptedException, TimeoutException {
        LOGGER.info("---------------------------");
        System.out.println("Modem Information:");
        System.out.println("  Manufacturer: " + SMSModule.GATEWAY.getManufacturer());
        System.out.println("  Model: " + SMSModule.GATEWAY.getModel());
        System.out.println("  Serial No: " + SMSModule.GATEWAY.getSerialNo());
        System.out.println("  SIM IMSI: " + SMSModule.GATEWAY.getImsi());
        System.out.println("  Signal Level: " + SMSModule.GATEWAY.getSignalLevel() + " dBm");
        System.out.println("  Battery Level: " + SMSModule.GATEWAY.getBatteryLevel() + "%");
        LOGGER.info("---------------------------");
    }

    /**
     * List of Status/Actions in SMS threading.
     */
    public static enum Status {

        /**
         * Thread is still in progress.
         */
        Waiting,
        /**
         * Service Thread started successfully.
         */
        Started,
        /**
         * Service Thread shutdown successfully.
         */
        Shutdown,
        /**
         * Thread caught an exception.
         */
        Failed,
        /**
         * Sending SMS thread is done and executed successfully.
         */
        Sent,
        /**
         * Reading SMS thread is done and executed successfully.
         */
        Read,
        /**
         * SMS Console thread is up and running.
         */
        Console
    }

    /**
     * List of unsent message.
     * <p>
     * Index of Vector:
     * <blockquote>
     * 0 = recipient <br />
     * 1 = message
     * </blockquote>
     *
     * @return Unsent messages
     */
    public Collection<List<String>> getUnsentMessage() {
        return unsentMessage;
    }

    /**
     * Populate the unsent messages.
     *
     * @param l Recipient and message
     */
    private void setUnsentMessage(List<String> l) {
        this.unsentMessage.add(l);
    }

    /**
     * Get the number of sent messages.
     *
     * @return Sent messages
     */
    public int getSent() {
        return sent.get();
    }

    /**
     * Add sent message counter.
     *
     * @param sent Number of sent messages
     */
    private void addSent() {
        this.sent.getAndIncrement();
    }

    /**
     * Get the number of failed messages.
     *
     * @return Failed messages
     */
    public int getFailed() {
        return failed.get();
    }

    /**
     * Add failed message counter.
     *
     * @param failed Number of failed messages
     */
    private void addFailed() {
        this.failed.getAndIncrement();
    }

    /**
     * The result status in sending SMS.
     *
     * @return Status result
     */
    public Status getStatus() {
        return status;
    }

}
