package my.jutils.sms.notifications;

import my.jutils.*;
import org.smslib.AGateway;
import org.smslib.IOutboundMessageNotification;
import org.smslib.OutboundMessage;

public class OutboundNotification implements IOutboundMessageNotification {

    public void process(AGateway gateway, OutboundMessage msg) {
        System.out.println("Outbound handler called from Gateway: " + gateway.getGatewayId());

        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Date: " + Times.toDateFormat(msg.getDate(), "EEE MMM dd yyyy hh:mm:ss a"));
        System.out.println("Status: " + msg.getMessageStatus());
        System.out.println("To: " + msg.getRecipient());
        System.out.println("Text: " + msg.getText());
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println();
    }
}
