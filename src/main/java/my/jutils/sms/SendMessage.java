package my.jutils.sms;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.*;
import my.jutils.*;
import org.joda.time.DateTime;
import org.slf4j.*;
import org.smslib.*;

/**
 * SMS message sender utility.
 *
 * @author Erieze and Einar Lagera
 */
public class SendMessage extends OutboundMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMessage.class);

    /**
     * Send SMS message synchronously.
     *
     * @param recipient Recipient
     * @param message Message to be sent
     * @param async Send message asynchronously?
     * @return Status of the message
     * @throws TimeoutException Timeout error
     * @throws GatewayException Gateway error
     * @throws IOException Input/Output error
     * @throws InterruptedException Interrupted thread
     */
    public synchronized static MessageStatuses send(String recipient, String message, boolean async) throws TimeoutException, GatewayException, IOException, InterruptedException {
        if (!SMSModule.isGatewayStarted()) {
            return MessageStatuses.FAILED;
        }

        final OutboundMessage msg = new OutboundMessage(recipient, message);
        OutboundMessage.MessageStatuses status = msg.getMessageStatus();
        if (async) { // Send a message asynchronously.
            LOGGER.debug("Sending message asynchronously...");
            status = SMSModule.SMS_SERVICE.queueMessage(msg) ? MessageStatuses.SENT : status;
        } else { // Send a message synchronously.
            LOGGER.debug("Sending message synchronously...");
            status = SMSModule.SMS_SERVICE.sendMessage(msg) ? MessageStatuses.SENT : status;
        }
        System.out.println();
        System.out.println("------------------------------- SENDING MESSAGE -------------------------------");
        System.out.println("Date: " + Times.toDateFormat(msg.getDate(), "EEE MMM dd yyyy hh:mm:ss a"));
        System.out.println("Status: " + status.name());
        System.out.println("To: " + msg.getRecipient());
        System.out.println("Text: " + msg.getText());
        System.out.println("-------------------------------------------------------------------------------");
        LOGGER.debug("Sending message status: {}", status.name());
        return status;
    }

    /**
     * Send SMS messages asynchronously/synchronously.
     *
     * @param recipients Recipients
     * @param message Message to be sent
     * @param async Send messages asynchronously?
     * @return Status of the message
     * @throws TimeoutException Timeout error
     * @throws GatewayException Gateway error
     * @throws IOException Input/Output error
     * @throws InterruptedException Interrupted thread
     */
    public synchronized static MessageStatuses sends(Collection<String> recipients, String message, boolean async) throws TimeoutException, GatewayException, IOException, InterruptedException {
        if (!SMSModule.isGatewayStarted()) {
            return MessageStatuses.FAILED;
        }

        OutboundMessage.MessageStatuses status = MessageStatuses.UNSENT;
        final Collection<OutboundMessage> queues = new ArrayList<>();
        for (String recipient : recipients) {
            final OutboundMessage msg = new OutboundMessage(recipient, message);
            status = msg.getMessageStatus();

            System.out.println();
            System.out.println("------------------------------- SENDING MESSAGE -------------------------------");
            System.out.println("Date: " + Times.toDateFormat(new DateTime().toDate(), "EEE MMM dd yyyy hh:mm:ss a"));
            System.out.println("To: " + recipients.toString());
            System.out.println("Text: " + message);
            System.out.println("-------------------------------------------------------------------------------");
            queues.add(msg);
        }
        if (async) { // Send a message asynchronously.
            LOGGER.debug("Sending messages asynchronously...");
            status = SMSModule.SMS_SERVICE.queueMessages(queues) > 0 ? MessageStatuses.SENT : status;
        } else { // Send a message synchronously.
            LOGGER.debug("Sending messages synchronously...");
            status = SMSModule.SMS_SERVICE.sendMessages(queues) > 0 ? MessageStatuses.SENT : status;
        }
        LOGGER.debug("Sending SMS messages status: {}", status.name());
        return status;
    }

    /**
     * Blast SMS message asynchronously/synchronously.
     *
     * @param recipient Recipient
     * @param message Message to be sent
     * @param noOfSend No of times message to be sent
     * @param async Send messages asynchronously?
     * @return Status of the message
     * @throws TimeoutException Timeout error
     * @throws GatewayException Gateway error
     * @throws IOException Input/Output error
     * @throws InterruptedException Interrupted thread
     */
    public synchronized static MessageStatuses sendBlast(String recipient, String message, int noOfSend, boolean async) throws TimeoutException, GatewayException, IOException, InterruptedException {
        final AtomicInteger ctr = new AtomicInteger();
        final AtomicBoolean sent = new AtomicBoolean();
        MessageStatuses status;
        do {
            sent.set((status = send(recipient, message, async)) == MessageStatuses.SENT);
            LOGGER.info("[#{}] message sent...", ctr.get());
        } while (sent.get() && ctr.getAndIncrement() < noOfSend);
        if (!sent.get()) {
            LOGGER.warn("Sending message stoppped at sequence [#{}]", ctr.get());
        }
        return status;
    }

}
