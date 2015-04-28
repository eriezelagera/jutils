package my.jutils.services;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import my.jutils.sms.*;
import my.jutils.sms.SMSModule.Status;
import org.slf4j.*;
import org.smslib.*;

/**
 * Create new thread for Sending SMS message.
 *
 * @author Erieze and Einar Lagera
 */
public class SMSThread extends OutboundMessage implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(SMSThread.class.getSimpleName());
    
    private String _recipient;
    private Collection<String> recipients;
    private String message;
    private int noOfSend;
    private boolean async;

    private InboundMessage.MessageClasses classes;
    private Collection<List<String>> inbox;
    private Collection<InboundMessage> inboxIM;

    private final AtomicBoolean success;
    private Status status;
    private final Actions action;
    private ReadAction readAction;

    /**
     * Create a new thread of SendMessage.
     *
     * @param recipient Recipient
     * @param message Message to be sent
     * @param async Send message asynchronously?
     */
    public SMSThread(String recipient, String message, boolean async) {
        this._recipient = recipient;
        this.message = message;
        this.noOfSend = 1;
        this.async = async;
        this.recipients = new ArrayList<>();
        this.success = new AtomicBoolean();
        this.action = Actions.Send;
        this.status = Status.Waiting;
    }

    /**
     * Create a new thread of SendMessage for Sending SMS messages.
     *
     * @param recipients Recipients
     * @param message Message to be sent
     * @param async Send message asynchronously?
     */
    public SMSThread(Collection<String> recipients, String message, boolean async) {
        this.recipients = recipients;
        this.message = message;
        this.async = async;
        this.success = new AtomicBoolean();
        this.action = Actions.Send;
        this.status = Status.Waiting;
    }

    /**
     * Create a new thread of SendMessage for SMS blaster.
     *
     * @param recipient Recipient
     * @param message Message to be sent
     * @param noOfSend No of message to be sent
     * @param async Send message asynchronously?
     */
    public SMSThread(String recipient, String message, int noOfSend, boolean async) {
        this._recipient = recipient;
        this.message = message;
        this.noOfSend = noOfSend;
        this.async = async;
        this.recipients = new ArrayList<>();
        this.success = new AtomicBoolean();
        this.action = Actions.Send;
        this.status = Status.Waiting;
    }

    /**
     * Create a new thread of ReadMessage.
     *
     * @param classes MessageClasses
     */
    public SMSThread(InboundMessage.MessageClasses classes) {
        this.success = new AtomicBoolean();
        this.action = Actions.Read;
        this.status = Status.Waiting;
        this.classes = classes;
    }

    /**
     * Create a new thread of Console SMS.
     */
    public SMSThread() {
        this.success = new AtomicBoolean();
        this.action = Actions.Console;
    }

    @Override
    public synchronized void run() {
        switch (action) {
            case Send:
                try {
                    LOGGER.info("Sending message {}", async ? "asynchronously..." : "synchronously...");
                    OutboundMessage.MessageStatuses msgStatus = MessageStatuses.UNSENT;
                    // Check if recipient is more than one (1)
                    if (recipients.isEmpty()) { // Send message to one (1) recipient
                        if (noOfSend == 1) {
                            msgStatus = SendMessage.send(_recipient, message, async);
                        } else if (noOfSend > 1) {
                            // Send message based on value of noOfSend
                            msgStatus = SendMessage.sendBlast(_recipient, message, noOfSend, async);
                        }
                    } else { // Otherwise, send to multiple recipient
                        msgStatus = SendMessage.sends(recipients, message, async);
                    }

                    // Check the Message Status
                    if (msgStatus == MessageStatuses.SENT) {
                        success.set(true);
                        status = Status.Sent;
                        LOGGER.info("{} message successfully sent...", async ? "Asynchronous" : "Synchronous");
                    } else if (msgStatus == MessageStatuses.UNSENT || msgStatus == MessageStatuses.FAILED) {
                        success.set(false);
                        status = Status.Failed;
                        LOGGER.info("{} message not sent...", async ? "Asynchronous" : "Synchronous");
                        success.set(false);
                        status = Status.Failed;
                    }
                } catch (GatewayException | IOException | TimeoutException | InterruptedException e) {
                    LOGGER.warn("Message not sent.", e);
                    success.set(false);
                    status = Status.Failed;
                }
                break;
            case Read:
                try {
                    switch (readAction) {
                        case Read:
                            setInbox(ReadMessages.read(classes));
                            success.set(true);
                            status = Status.Read;
                            break;
                        case ReadIM:
                            setInboxIM(ReadMessages.readIM(classes));
                            success.set(true);
                            status = Status.Read;
                            break;
                        case ReadOut:
                            ReadMessages.readOut(classes);
                            success.set(true);
                            status = Status.Read;
                            break;
                    }
                } catch (GatewayException | IOException | InterruptedException | TimeoutException e) {
                    LOGGER.warn("Message cannot be read.", e);
                    success.set(false);
                    status = Status.Failed;
                }
                break;
            case Console:
                Console.execute();
                LOGGER.info("Console successfully executed...");
                success.set(true);
                status = Status.Console;
                break;
        }
        notifyAll();
    }

    /**
     * List of Actions to do in this thread.
     *
     * @see SendMessage
     * @see ReadMessages
     */
    public static enum Actions {

        /**
         * Send a message action.
         */
        Send,
        /**
         * Read a message action.
         */
        Read,
        /**
         * Execute SMS console action.
         */
        Console
    }

    /**
     * Type of reading messages.
     *
     * @see ReadMessages#read(org.smslib.InboundMessage.MessageClasses)
     * @see ReadMessages#readIM(org.smslib.InboundMessage.MessageClasses)
     * @see ReadMessages#readOut(org.smslib.InboundMessage.MessageClasses)
     */
    public static enum ReadAction {

        /**
         * Read message from inbox, then returns Collection of Vector.
         *
         * @see ReadMessages#read(org.smslib.InboundMessage.MessageClasses)
         */
        Read,
        /**
         * Read message from inbox, then returns Collection of InboundMessage.
         *
         * @see ReadMessages#readIM(org.smslib.InboundMessage.MessageClasses)
         */
        ReadIM,
        /**
         * Read message from inbox, then print to console.
         *
         * @see ReadMessages#readOut(org.smslib.InboundMessage.MessageClasses)
         */
        ReadOut
    }

    /**
     * Action did in this thread.
     *
     * @return Action
     * @see Actions
     */
    public Actions getAction() {
        return action;
    }

    /**
     * How message read.
     *
     * @return Action how the message read
     * @see ReadAction
     */
    public ReadAction getReadAction() {
        return readAction;
    }

    /**
     * Set how will message be read.
     *
     * @param readAction Read action
     * @see ReadAction
     */
    public void setReadAction(ReadAction readAction) {
        this.readAction = readAction;
    }

    /**
     * Get the inbox.
     *
     * @return Collection of messages.
     * @see ReadMessages#MSG_DATE
     * @see ReadMessages#MSG_ORIGINATOR
     * @see ReadMessages#MSG_TEXT
     */
    public Collection<List<String>> getInbox() {
        return inbox;
    }

    /**
     * Set the inbox
     *
     * @param inbox Collection of inbox
     */
    public void setInbox(Collection<List<String>> inbox) {
        this.inbox = inbox;
    }

    /**
     * Get the inbox in instance of InboundMessage
     *
     * @return Collection of InboundMessage
     */
    public Collection<InboundMessage> getInboxIM() {
        return inboxIM;
    }

    /**
     * Set the inbox
     *
     * @param inboxIM Collection of InboundMessage
     */
    public void setInboxIM(Collection<InboundMessage> inboxIM) {
        this.inboxIM = inboxIM;
    }

    /**
     * Check if the message success successfully.
     *
     * @return True if success, otherwise false
     */
    public boolean isSuccess() {
        return success.get();
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
