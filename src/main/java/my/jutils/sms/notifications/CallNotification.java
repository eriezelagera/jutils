package my.jutils.sms.notifications;

import org.smslib.AGateway;
import org.smslib.ICallNotification;

public class CallNotification implements ICallNotification {

    public void process(AGateway gateway, String callerId) {
        System.out.println(">>> New call detected from Gateway: " + gateway.getGatewayId() + " : " + callerId);
    }

}
