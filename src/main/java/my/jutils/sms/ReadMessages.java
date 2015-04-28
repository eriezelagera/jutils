package my.jutils.sms;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.crypto.spec.SecretKeySpec;
import my.jutils.*;
import org.slf4j.*;
import org.smslib.*;
import org.smslib.InboundMessage.MessageClasses;
import org.smslib.crypto.AESKey;

/**
 * SMS message reader/receiver utility.
 *
 * @author Erieze and Einar Lagera
 */
public class ReadMessages {

    private static final Logger LOGGER = LoggerFactory.getLogger(ReadMessages.class.getSimpleName());

    /**
     * Date of the message.
     */
    public static final int MSG_DATE = 0;
    /**
     * Originator of the message.
     */
    public static final int MSG_ORIGINATOR = 1;
    /**
     * Text message.
     */
    public static final int MSG_TEXT = 2;

    /**
     * Read the messages.
     *
     * @param classes Class of the message to be read
     * @return Collection of the read messages
     * @throws TimeoutException Timeout error
     * @throws GatewayException Gateway error
     * @throws IOException Input/Output error
     * @throws InterruptedException Interrupted thread
     * @see MessageClasses
     */
    public static Collection<List<String>> read(MessageClasses classes) throws TimeoutException, GatewayException, IOException, InterruptedException {
        final Collection<List<String>> messageArr = new ArrayList<>();
        if (SMSModule.isGatewayStarted()) {
            Service.getInstance().getKeyManager().registerKey("+639367404260", new AESKey(new SecretKeySpec("0011223344556677".getBytes(), "AES")));
            /* 
             * Read Messages. The reading is done via the Service object and
             * affects all Gateway objects defined. This can also be more directed to a specific
             * Gateway - look JavaDocs for information on the Service method calls.
             */
            final List<InboundMessage> msgList = new ArrayList<>();
            LOGGER.debug("Retrieving messages...");
            final int messages = SMSModule.SMS_SERVICE.readMessages(msgList, classes);
            if (messages == 0) {
                System.out.println("No message. Hooray!");
                System.out.println();
            } else {
                for (InboundMessage msg : msgList) {
                    final List<String> l = new ArrayList<>();
                    l.add(MSG_DATE, Times.toDateFormat(msg.getDate(), "EEE MMM dd yyyy hh:mm:ss a"));
                    l.add(MSG_ORIGINATOR, msg.getOriginator());
                    l.add(MSG_TEXT, msg.getText());
                    messageArr.add(l);
                }
                LOGGER.info("Total messages: {}", messages);
            }
            return messageArr;
        }
        LOGGER.debug("Messages retrieved!");
        return messageArr;
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
    public static Collection<InboundMessage> readIM(MessageClasses classes) throws TimeoutException, GatewayException, IOException, InterruptedException {
        final Collection<InboundMessage> messageArr = new ArrayList<>();
        if (SMSModule.isGatewayStarted()) {
            Service.getInstance().getKeyManager().registerKey("+639367404260", new AESKey(new SecretKeySpec("0011223344556677".getBytes(), "AES")));
            /* 
             * Read Messages. The reading is done via the Service object and
             * affects all Gateway objects defined. This can also be more directed to a specific
             * Gateway - look JavaDocs for information on the Service method calls.
             */
            LOGGER.debug("Retrieving messages...");
            final int messages = SMSModule.SMS_SERVICE.readMessages(messageArr, classes);
            LOGGER.info("Total messages: {}", messages);
            return messageArr;
        }
        LOGGER.debug("Messages retrieved!");
        return messageArr;
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
    public static void readOut(MessageClasses classes) throws TimeoutException, GatewayException, IOException, InterruptedException {
        if (SMSModule.isGatewayStarted()) {
            Service.getInstance().getKeyManager().registerKey("+639367404260", new AESKey(new SecretKeySpec("0011223344556677".getBytes(), "AES")));
            /* 
             * Read Messages. The reading is done via the Service object and
             * affects all Gateway objects defined. This can also be more directed to a specific
             * Gateway - look JavaDocs for information on the Service method calls.
             */
            final List<InboundMessage> msgList = new ArrayList<>();
            LOGGER.debug("Retrieving messages...");
            final int messages = SMSModule.SMS_SERVICE.readMessages(msgList, classes);
            if (messages == 0) {
                LOGGER.info("\nNo message. Hooray!\n");
            } else {
                AtomicInteger index = new AtomicInteger(1);
                for (InboundMessage msg : msgList) {
                    System.out.println("Message #:" + index.getAndIncrement());
                    System.out.println("Date: " + Times.toDateFormat(msg.getDate(), "EEE MMM dd yyyy hh:mm:ss a"));
                    System.out.println("From: " + msg.getOriginator());
                    System.out.println("Text: " + msg.getText());
                    System.out.println();
                }
                LOGGER.info("Total messages: {}", messages);
            }
        }
        LOGGER.debug("Messages retrieved!");
    }

}
