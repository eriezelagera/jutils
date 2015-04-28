package my.jutils.sms.notifications;

import org.smslib.AGateway;
import org.smslib.IGatewayStatusNotification;

public class GatewayStatusNotification implements IGatewayStatusNotification {

    public void process(AGateway gateway, AGateway.GatewayStatuses oldStatus, AGateway.GatewayStatuses newStatus) {
        System.out.println(">>> Gateway Status change for " + gateway.getGatewayId() + ", OLD: " + oldStatus + " -> NEW: " + newStatus);
    }

}
