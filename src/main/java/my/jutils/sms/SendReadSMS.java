package my.jutils.sms;

import java.awt.HeadlessException;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import javax.swing.JOptionPane;
import my.jutils.*;
import org.slf4j.*;
import org.smslib.*;

/**
 * Sending SMS utility.
 *
 * @author Erieze and Einar Lagera
 * @see SMSModule
 */
public class SendReadSMS extends SMSModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendReadSMS.class);

    /**
     * Create an instance of SMS Module.
     *
     * @see SMSModule
     */
    public SendReadSMS() {
    }

    /**
     * Send SMS message synchronously/asynchronously.
     * <p>
     * This just invoked the {@code send()} from SMSModule, but this method
     * already caught the exceptions.
     *
     * @param recipients Recipients
     * @param message Message to be sent
     * @param async Send message asynchronously?
     * @return True if one or more message sent successfully, false if no
     * message sent
     */
    public boolean sendMessage(Collection<String> recipients, String message, boolean async) {
        try {
            super.send(recipients, message, async);
            return true;
        } catch (GatewayException | IOException | InterruptedException | TimeoutException e) {
            LOGGER.debug(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, "An error occured, SMS not sent.", "SMS Sending Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Send SMS message synchronously/asynchronously.
     * <p>
     * This just invoked the {@code send()} from SMSModule, but this method
     * already caught the exceptions.
     *
     * @param recipient Recipient
     * @param message Message to be sent
     * @param async Send message asynchronously?
     * @return True if message sent successfully, otherwise false
     */
    public boolean sendMessage(String recipient, String message, boolean async) {
        try {
            super.send(recipient, message, async);
            return super.getSent() > 0;
        } catch (GatewayException | IOException | TimeoutException e) {
            LOGGER.debug(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, "An error occured, SMS not sent.", "SMS Sending Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (InterruptedException e) {
            LOGGER.error("Cause: " + e.toString(), e);
            return false;
        }
    }

    /**
     * Send SMS message synchronously/asynchronously with the given retry count.
     * <p>
     * This was just invoking {@code send()} from SMSModule, but this method
     * already caught the exceptions. If sending message was failed, this will
     * keep sending until the given retry count.
     *
     * @param recipients Recipients
     * @param message Message to be sent
     * @param async Send message asynchronously?
     * @param retryCount Maximum retry count
     * @return True if one or more message sent successfully, false if no
     * message sent
     */
    public boolean sendMessage(Collection<String> recipients, String message, boolean async, int retryCount) {
        try {
            super.send(recipients, message, async);
            if (super.getSent() > 0) {
                return true;
            } else {
                final AtomicInteger retry = new AtomicInteger();
                do {
                    if (retry.get() >= retryCount) {
                        JOptionPane.showMessageDialog(null, "An error occured, SMS not sent.", "SMS Sending Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    super.send(recipients, message, async);
                } while (super.getSent() == 0);
                return true;
            }
        } catch (GatewayException | IOException | InterruptedException | TimeoutException e) {
            LOGGER.debug(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, "An error occured, SMS not sent.", "SMS Sending Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Send SMS message synchronously/asynchronously.
     * <p>
     * This just invoked the {@code send()} from SMSModule, but this method
     * already caught the exceptions. If sending message was failed, this will
     * keep sending until the given retry count.
     *
     * @param recipient Recipient
     * @param message Message to be sent
     * @param async Send message asynchronously?
     * @param retryCount Maximum retry count
     * @return True if message sent successfully, otherwise false
     */
    public boolean sendMessage(String recipient, String message, boolean async, int retryCount) {
        try {
            super.send(recipient, message, async);
            if (super.getSent() > 0) {
                return true;
            } else {
                final AtomicInteger retry = new AtomicInteger();
                do {
                    if (retry.get() >= retryCount) {
                        JOptionPane.showMessageDialog(null, "An error occured, SMS not sent.", "SMS Sending Error", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }
                    super.send(recipient, message, async);
                } while (super.getSent() == 0);
                return true;
            }
        } catch (GatewayException | IOException | InterruptedException | TimeoutException e) {
            LOGGER.debug(e.getMessage(), e);
            JOptionPane.showMessageDialog(null, "An error occured, SMS not sent.", "SMS Sending Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public boolean startGateway() {
        try {
            return super.start();
        } catch (GatewayException | HeadlessException | IOException | InterruptedException e) {
            LOGGER.error("Cause: " + e.toString(), e);
            return false;
        }
    }

    @Override
    public String gatewayId() {
        return "GSM-Globe";
    }

    /**
     * COMM Port of the GSM modem. <br />
     * Eg. <i>COM1, /dev/tty.HUAWEIMobile-Modem</i>
     *
     * @return COMM Port
     */
    @Override
    public String commPort() {
        return "COM1";
    }

    @Override
    public int baudRate() {
        return 38400;
    }

    @Override
    public String manufacturer() {
        return "Huawei";
    }

    @Override
    public String model() {
        return "E610";
    }

    @Override
    public String smscNumber() {
        return Strings.getRandomString(new String[]{"+6391702", "+6391703", "+6391708"});
    }

    @Override
    public AGateway.Protocols gatewayProtocol() {
        return AGateway.Protocols.PDU;
    }

    @Override
    public boolean autoGC() {
        return true;
    }

    @Override
    public int autGCInterval() {
        return 10;
    }

}
