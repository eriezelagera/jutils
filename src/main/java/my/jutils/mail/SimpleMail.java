package my.jutils.mail;

import java.util.*;
import java.util.concurrent.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.JOptionPane;
import my.jutils.*;
import my.jutils.services.*;
import org.codemonkey.simplejavamail.*;
import org.slf4j.*;

/**
 * Simple utility for Sending Mail.
 * <p>
 * This utility also includes some useful tools such as validation and
 * informations of some Mail Providers to guide you to your specified mailing
 * server.
 *
 * @author Einar and Erieze Lagera
 */
abstract public class SimpleMail implements IMessage, Account {

    private static final String CLASS_NAME = SimpleMail.class.getSimpleName();
    private static final Logger LOGGER = LoggerFactory.getLogger(CLASS_NAME);

    private final Status status;
    private final ExecutorService simpleMailt;

    /**
     * Simple utility for sending mail.
     * <p>
     * All properties must be implemented to fill-in required informations.
     */
    public SimpleMail() {
        this.status = Status.Waiting;
        this.simpleMailt = Executors.newSingleThreadExecutor(new BackgroundThreadFactory(CLASS_NAME + "-MailSender"));
    }

    /**
     * Status of sending mail.
     */
    public static enum Status {

        Waiting,
        Sent,
        Failed
    }

    /**
     * Get the properties of the mail.
     *
     * @return Properties of the mail
     */
    abstract protected Properties getProperties();

    /**
     * Enable email debugging.
     *
     * @return Debugging
     */
    abstract protected boolean debug();

    /**
     * Property of the specified mail service provider.
     * <p>
     * This will return map with the following keys,
     * <ul>
     * <li> host
     * <li> port
     * </ul>
     * Get the value by invoking map.get("key").
     *
     * @param provider Your mail service provider
     * @return Properties of the selected provider
     * @see MailServiceProvider
     */
    public static Map<String, Object> getMailProperties(MailServiceProvider provider) {
        final Map<String, Object> map = new HashMap<>();
        switch (provider) {
            case GoogleSSL:
                map.put("host", "smtp.gmail.com");
                map.put("port", "465");
                map.put("trust", "smtp.gmail.com");
                break;
            case GoogleTLS:
                map.put("host", "smtp.gmail.com");
                map.put("port", "587");
                map.put("trust", "smtp.gmail.com");
                break;
            case GooglePop:
                map.put("host", "pop.gmail.com");
                map.put("port", "995");
                map.put("trust", "smtp.gmail.com");
                break;
            case OutlookTLS:
                map.put("host", "smtp.live.com");
                map.put("port", "587");
                map.put("trust", "smtp.gmail.com");
                break;
            case OutlookPop:
                map.put("host", "pop3.live.com");
                map.put("port", "995");
                map.put("trust", "smtp.gmail.com");
                break;
            case Office365TLS:
                map.put("host", "smtp.office365.com");
                map.put("port", "587");
                map.put("trust", "smtp.gmail.com");
                break;
            case Office365SSL:
                map.put("host", "outlook.office365.com");
                map.put("port", "995");
                map.put("trust", "smtp.gmail.com");
                break;
            case YahooSSL:
                map.put("host", "smtp.mail.yahoo.com");
                map.put("port", "465");
                map.put("trust", "smtp.gmail.com");
                break;
            case YahooPop:
                map.put("host", "pop.mail.yahoo.com");
                map.put("port", "995");
                map.put("trust", "smtp.gmail.com");
                break;
            case YahooPlusSSL:
                map.put("host", "plus.smtp.mail.yahoo.com");
                map.put("port", "465");
                map.put("trust", "smtp.gmail.com");
                break;
            case YahooPlusPop:
                map.put("host", "plus.pop.mail.yahoo.com");
                map.put("port", "995");
                map.put("trust", "smtp.gmail.com");
                break;
            case MailcomSSL:
                map.put("host", "smtp.mail.com");
                map.put("port", "465");
                map.put("trust", "smtp.gmail.com");
                break;
            case MailcomPop:
                map.put("host", "pop.mail.com");
                map.put("port", "995");
                map.put("trust", "smtp.gmail.com");
                break;
            default:
                return map;
        }
        return map;
    }

    /**
     * List of supported mail service provider.
     * <p>
     * If your email is not in the list, please refer to this site,
     * <a href="http://www.arclab.com/en/amlc/list-of-smtp-and-pop3-servers-mailserver-list.html">
     * <b>Mailing server lists</b>
     * </a>
     */
    public static enum MailServiceProvider {

        GoogleSSL,
        GoogleTLS,
        GooglePop,
        OutlookTLS,
        OutlookPop,
        Office365TLS,
        Office365SSL,
        YahooSSL,
        YahooPop,
        YahooPlusSSL,
        YahooPlusPop,
        MailcomSSL,
        MailcomPop
    }

    /**
     * Validate Email Address. This will check if the email address has the
     * correct format.
     *
     * @param email Email Address
     * @return True if the Email Address is correct and valid, otherwise false
     */
    public static boolean validateEmail(String email) {
        try {
            new InternetAddress(email).validate();
            return true;
        } catch (AddressException e) {
            LOGGER.error(e.getRef(), e);
            return false;
        }
    }

    /**
     * Get the mailing server from Email Address. Probably @google.com,
     *
     * @yahoo.com and other mailing services.
     *
     * @param email Email address
     * @return Mailing server
     */
    private String getMailingServer(String email) {
        final char[] strArr = email.toCharArray();
        for (int i = 0; i < strArr.length; i++) {
            if (strArr[i] == '@') {
                return email.substring(i + 1, email.length());
            }
        }
        return "";
    }

    /**
     * Check if there's recipient entered.
     *
     * @return True if there is a recipient, otherwise false
     */
    private boolean hasRecipient() {
        if (recipients().isEmpty()) {
            LOGGER.warn("No recipient found!");
            return false;
        }
        return true;
    }

    /**
     * Type of message to be sent.
     *
     * @return MessageType
     * @see MessageType
     */
    abstract public MessageType messageType();

    /**
     * Type of your content message.
     * <p>
     * <b>Text HTML</b> supports HTML tags,while <b>Text</b> just supports plain
     * text. No format and sizes. Format will only based on mailing application
     * or browser.
     */
    public static enum MessageType {

        Text,
        TextHTML
    }

    /**
     * Escapes Java to HTML.
     *
     * @param str String that may contains character to be escaped
     * @return Escaped HTML string
     */
    public static String escapeJavaToHTML(String str) {
        return str.replaceAll("\r?\n", "<br />").replaceAll("\t", "&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;");
    }

    /**
     * Verify if the account has a valid credentials.
     * <p>
     * This will be based on the specified abstract username and password. If
     * the account is valid, means that username and password is correct,
     * account verification is success. If verification is not success, there's
     * a possibility that either username or password is incorrect.
     *
     * @return True if account is valid, otherwise false
     */
    public boolean verify() {
        if (Strings.get(username(), null) == null || Strings.get(password(), null) == null) {
            LOGGER.warn("Did you implemented and supplied your account details?");
            LOGGER.warn("[{}][{}]", username(), password());
            return false;
        }
        final Session session = Session.getDefaultInstance(getProperties(), new AccountAuthenticator(username(), password()));
        return session != null;
    }

    /**
     * Process the workaround for sending email.
     * <p>
     * Abstracts must be implemented in order to load the parameters. This will
     * return boolean value stating the status of the send.
     *
     * @return True if the mail is sent successfully, otherwise false
     */
    public boolean send() {
        final Email email = new Email();
        email.setFromAddress(username(), username());
        email.setSubject(subject());

        // Collect recipients
        if (hasRecipient()) {
            for (String recipient : recipients()) {
                LOGGER.info(recipient);
                email.addRecipient(recipient.split("@")[0], recipient.trim(), Message.RecipientType.TO);
            }
        }

        // Check the content type
        switch (messageType()) {
            case Text:
                email.setText(content());
                break;
            case TextHTML:
                email.setTextHTML(content());
                break;
        }

        final Properties prop = getProperties();
        final Mailer mailer = new Mailer(prop.getProperty("mail.smtp.host"), Numbers.parseInt(prop.getProperty("mail.smtp.port")), username(), password(), TransportStrategy.SMTP_SSL);
        mailer.setDebug(debug());
        if (mailer.validate(email)) {
            final MailThread mailt = new MailThread(email, mailer);
            simpleMailt.submit(mailt);
            LOGGER.info("Sending mail {}...", async() ? "asynchronously" : "synchronously");
            if (!async()) {
                synchronized (mailt) {
                    while (mailt.getStatus() == Status.Waiting) {
                        try {
                            mailt.wait();
                        } catch (InterruptedException e) {
                            LOGGER.debug(e.getMessage(), e);
                        }
                    }
                }
            }
            simpleMailt.shutdownNow();
            LOGGER.info("Done sending Email...");
            return mailt.getStatus() != Status.Failed;
        } else {
            LOGGER.warn("Possibly missing recipient, subject or content.");
            LOGGER.warn("Summary: [Recipient={}, Subject={}, Content={}]", recipients().toString(), subject(), content());
            JOptionPane.showMessageDialog(null, 
                    "An error occured, possibly missing recipient, subject or content.", 
                    "Mail Sending Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * The result status in sending mail.
     *
     * @return Status result
     */
    public Status getStatus() {
        return status;
    }

}
