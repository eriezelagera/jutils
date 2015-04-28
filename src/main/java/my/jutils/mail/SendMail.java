package my.jutils.mail;

import java.util.*;
import org.slf4j.*;

/**
 * Sending Email utility.
 * <p>
 * This is a Singleton Java Mail utility, means that you may have only one email
 * account for sending Email. If you've want to support multiple account, you
 * may need to create multiple instance of this class.
 *
 * @author Erieze and Einar Lagera
 */
public class SendMail extends SimpleMail {

    private static final Logger LOGGER = LoggerFactory.getLogger(SendMail.class.getSimpleName());
    
    private final Collection<String> recipients;
    private final String recipient;
    private final String subject;
    private final String message;
    private final boolean async;

    private String invalidEmail;

    /**
     * Send an email to single recipients. We used ArrayList to hold multiple
     * recipients.
     *
     * @param recipient Recipient
     * @param subject Subject of the message
     * @param message Message of the email
     * @param async Send message asynchronously?
     */
    public SendMail(String recipient, String subject, String message, boolean async) {
        this.recipients = new ArrayList<>();
        this.recipient = recipient;
        this.subject = subject;
        this.message = message;
        this.async = async;
        this.invalidEmail = "";
    }

    /**
     * Send an email to multiple recipients. We used ArrayList to hold multiple
     * recipients.
     *
     * @param recipients List of recipients
     * @param subject Subject of the message
     * @param message Message of the email
     * @param async Send message asynchronously?
     */
    public SendMail(Collection<String> recipients, String subject, String message, boolean async) {
        this.recipients = recipients;
        this.recipient = "";
        this.subject = subject;
        this.message = message;
        this.async = async;
        this.invalidEmail = "";
    }

    /**
     * Send an email to multiple recipients. We used ArrayList to hold multiple
     * recipients.
     *
     * @param recipients List of recipients
     * @param subject Subject of the message
     * @param message Message of the email
     * @param async Send message asynchronously?
     */
    public SendMail(String[] recipients, String subject, String message, boolean async) {
        this.recipients = Arrays.asList(recipients);
        this.recipient = "";
        this.subject = subject;
        this.message = message;
        this.async = async;
        this.invalidEmail = "";
    }

    /**
     * Validate the Email Address.
     *
     * @return True if all Email Address are valid, otherwise false
     */
    public boolean validate() {
        for (String rec : recipients) {
            if (!SimpleMail.validateEmail(rec)) {
                LOGGER.warn("Email address {} not valid!", rec.trim());
                this.invalidEmail += "[" + rec + "] ";
                return false;
            }
        }
        return true;
    }

    /**
     * Validate the Email Addresses.
     *
     * @return True if all Email Addresses are valid, otherwise false
     */
    public boolean validates() {
        for (String rec : recipients) {
            if (!SimpleMail.validateEmail(rec)) {
                LOGGER.warn("Email address {} not valid!", rec.trim());
                this.invalidEmail += "[" + rec + "] ";
                return false;
            }
        }
        return true;
    }

    @Override
    public Collection<String> recipients() {
        Collection<String> c = new ArrayList<>();
        if (this.recipients.isEmpty()) {
            c.add(this.recipient);
            return c;
        }
        return this.recipients;
    }

    @Override
    public String subject() {
        return this.subject;
    }

    @Override
    public String content() {
        return this.message;
    }

    @Override
    public boolean async() {
        return this.async;
    }

    @Override
    public MessageType messageType() {
        return SimpleMail.MessageType.Text;
    }

    @Override
    public Properties getProperties() {
        Map<String, Object> map = SimpleMail.getMailProperties(MailServiceProvider.GoogleTLS);
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", map.get("host"));
        props.put("mail.smtp.port", map.get("port"));
        props.put("mail.smtp.socketFactory.port", map.get("port"));
        props.put("mail.smtp.starttls.enable", "true");
        return props;
    }

    /**
     * The invalid Email Address
     *
     * @return Email Address
     */
    public String getInvalidEmail() {
        return invalidEmail;
    }

    @Override
    public String username() {
        return "username@email.com";
    }

    @Override
    public String password() {
        return "p@ssw0rd";
    }

    @Override
    public boolean debug() {
        return true;
    }

}
