package my.jutils.sms;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import javax.crypto.spec.SecretKeySpec;
import org.slf4j.*;
import org.smslib.*;
import org.smslib.crypto.AESKey;

/**
 * Delete message.
 *
 * @author Erieze and Einar Lagera
 */
public class DeleteMessage {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteMessage.class.getSimpleName());

    /**
     * Delete a message.
     *
     * @param delete_index Message index no.
     * @throws TimeoutException Timeout error
     * @throws GatewayException Gateway error
     * @throws IOException Input/Output error
     * @throws InterruptedException Interrupted thread
     */
    public static void delete(int delete_index) throws TimeoutException, GatewayException, IOException, InterruptedException {
        if (SMSModule.isGatewayStarted()) {
            Service.getInstance().getKeyManager().registerKey("+639277054017", new AESKey(new SecretKeySpec("0011223344556677".getBytes(), "AES")));
            final Collection<InboundMessage> messageArr = ReadMessages.readIM(InboundMessage.MessageClasses.ALL);
            final AtomicInteger index = new AtomicInteger();
            for (InboundMessage msg : messageArr) {
                if (index.get() == delete_index) {
                    LOGGER.info("Message #{} is deleted", index.get());
                    SMSModule.SMS_SERVICE.deleteMessage(msg);
                    break;
                }
                if (index.get() == (messageArr.size() - 1)) {
                    LOGGER.warn("Message #{} does not exists!", delete_index);
                }
                index.getAndIncrement();
            }
            LOGGER.info("Message #{} deleted!", delete_index);
            ReadMessages.readOut(InboundMessage.MessageClasses.ALL);
        }
    }

    /**
     * Delete all messages from the SIM. <h1>HOORAY!</h1>
     *
     * @throws TimeoutException Timeout error
     * @throws GatewayException Gateway error
     * @throws IOException Input/Output error
     * @throws InterruptedException Interrupted thread
     */
    public static void deleteAll() throws TimeoutException, GatewayException, IOException, InterruptedException {
        if (SMSModule.isGatewayStarted()) {
            Service.getInstance().getKeyManager().registerKey("+639277054017", new AESKey(new SecretKeySpec("0011223344556677".getBytes(), "AES")));
            final Collection<InboundMessage> messageArr = ReadMessages.readIM(InboundMessage.MessageClasses.ALL);

            final AtomicInteger index = new AtomicInteger();
            for (InboundMessage msg : messageArr) {
                LOGGER.info("Message #{} is deleted!", index.getAndIncrement());
                SMSModule.SMS_SERVICE.deleteMessage(msg);
            }
            LOGGER.info("----------------------------------");
            LOGGER.info("Finished deleting all messages!");
            LOGGER.info("----------------------------------");
            ReadMessages.readOut(InboundMessage.MessageClasses.ALL);
            LOGGER.debug("Messages deleted! No of message: {}", index.get());
        }
    }

}
