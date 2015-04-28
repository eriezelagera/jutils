package my.jutils.services;

import java.util.concurrent.atomic.AtomicBoolean;
import my.jutils.mail.*;
import my.jutils.mail.SimpleMail.Status;
import org.codemonkey.simplejavamail.*;
import org.slf4j.*;

/**
 * Create a new thread when sending mail.
 *
 * @author Erieze and Einar Lagera
 */
public class MailThread implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(MailThread.class.getSimpleName());
    
    private final Email email;
    private final Mailer mailer;

    private Status status;
    private final AtomicBoolean success;

    /**
     * Create new thread for sending mail.
     *
     * @param email Email
     * @param mailer Mailer
     */
    public MailThread(Email email, Mailer mailer) {
        this.email = email;
        this.mailer = mailer;
        this.status = SimpleMail.Status.Waiting;
        this.success = new AtomicBoolean();
    }

    @Override
    public synchronized void run() {
        try {
            mailer.sendMail(email);
            status = SimpleMail.Status.Sent;
            success.set(true);
        } catch (MailException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
            status = SimpleMail.Status.Failed;
            success.set(false);
        }
        notifyAll();
    }

    /**
     * The result status in sending Mail.
     *
     * @return Status result
     */
    public SimpleMail.Status getStatus() {
        return status;
    }

    /**
     * Mail sent successfully
     *
     * @return Successful?
     */
    public boolean isSuccess() {
        return success.get();
    }

}
