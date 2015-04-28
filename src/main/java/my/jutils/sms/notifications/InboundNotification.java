package my.jutils.sms.notifications;

import my.jutils.*;
import org.smslib.AGateway;
import org.smslib.IInboundMessageNotification;
import org.smslib.InboundMessage;
import org.smslib.Message;

public class InboundNotification implements IInboundMessageNotification {

    public void process(AGateway gateway, Message.MessageTypes msgType, InboundMessage msg) {
        if (msgType == Message.MessageTypes.INBOUND) {
            System.out.println(">>> New message from Gateway: " + gateway.getGatewayId());
        }
        else if (msgType == Message.MessageTypes.STATUSREPORT) {
            System.out.println(">>> New Inbound Status Report message detected from Gateway: " + gateway.getGatewayId());
        }
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println("Date: " + Times.toDateFormat(msg.getDate(), "EEE MMM dd yyyy hh:mm:ss a"));
        System.out.println("From: " + msg.getOriginator());
        System.out.println("Text: " + msg.getText());
        System.out.println("-------------------------------------------------------------------------------");
        System.out.println();

    }
}
