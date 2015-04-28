package my.jutils.sms;

import java.util.Collection;

/**
 * The content of the SMS.
 *
 * @author Erieze and Einar Lagera
 */
public interface IMessage {

    /**
     * List of recipient where the message will send to.
     *
     * @return Recipients
     */
    public Collection<String> recipients();

    /**
     * The content of the SMS.
     *
     * @return Message
     */
    public String message();

}
