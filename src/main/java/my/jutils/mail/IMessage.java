package my.jutils.mail;

import java.util.Collection;

/**
 * The content of the mail.
 * @author Erieze and Einar Lagera
 */
public interface IMessage {
    
    /** 
     * List of recipient where the message will send to.
     * @return Recipients
     */
    public Collection<String> recipients();
    
    /** 
     * Subject of the message. 
     * @return Subject
     */
    public String subject();
    
    /** 
     * Content of the message. 
     * @return Content
     */
    public String content();
    
    /**
     * Send message asynchronously?
     * @return Asynchronously?
     */
    public boolean async();
    
}
