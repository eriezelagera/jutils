package my.jutils.sms;

import java.io.*;
import java.util.concurrent.atomic.AtomicBoolean;
import org.slf4j.*;
import org.smslib.*;

/**
 * SMS Console module.
 * @author Erieze Lagera
 */
public class Console {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Console.class);
    
    /**
     * Execute the SMS console.
     */
    public static void execute() {
        try {
            final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String in;
            final AtomicBoolean quit = new AtomicBoolean();
            do {
                
                System.out.println("console> ");
                switch(in = br.readLine()) {
                    case "read all" :
                        ReadMessages.readOut(InboundMessage.MessageClasses.ALL);
                        break;
                    case "read unread" :
                        ReadMessages.readOut(InboundMessage.MessageClasses.UNREAD);
                        break;
                    case "read" :
                        ReadMessages.readOut(InboundMessage.MessageClasses.READ);
                        break;
                    case "send" :
                        LOGGER.info("Enter recipient (press Enter when done): ");
                        final String rec = br.readLine();
                        LOGGER.info("Enter message (press Enter when done): ");
                        final String msg = br.readLine();
                        final SendReadSMS sms = new SendReadSMS();
                        sms.sendMessage(rec, msg, false);
                        break;
                    case "info" :
                        SMSModule.modemInfo();
                        break;
                    case "quit" :
                        quit.set(true);
                        break;
                    default :
                        help();
                        
                }
                if (quit.get()) {
                    break;
                }
            } while (!in.equalsIgnoreCase("quit"));
            LOGGER.info("Console has now been closed...");
        } catch (IOException | GatewayException | InterruptedException | TimeoutException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
    
    /**
     * Display console commands.
     */
    private static void help() {
        LOGGER.info("-----------------------------------------------------------------------------");
        System.out.println("SMS Console Help");
        System.out.println("--> Reading message");
        System.out.println("\t\tread all    - read all messages, including read and unread.");
        System.out.println("\t\tread unread - read all unread messages.");
        System.out.println("\t\tread        - read messages, unread message not included.");
        System.out.println("\t\tsend        - send a message, executing this command will ask recipient and message");
        System.out.println("\t\tinfo        - display modem info.");
        System.out.println("\t\tquit        - quit SMS console.");
        LOGGER.info("-----------------------------------------------------------------------------");
    }
    
}
