package my.jutils.sms.notifications;

import org.smslib.AGateway;
import org.smslib.IOrphanedMessageNotification;
import org.smslib.InboundMessage;

public class OrphanedMessageNotification implements IOrphanedMessageNotification {

    public boolean process(AGateway gateway, InboundMessage msg) {
        System.out.println(">>> Orphaned message part detected from " + gateway.getGatewayId());
        System.out.println(msg);
        // Since we are just testing, return FALSE and keep the orphaned message part.
        // Update: This will now return FALSE to keep memory in shape
        return true;
    }

}
