package my.jutils.sms;

import org.smslib.AGateway;

/**
 * Properties for GSM modem.
 *
 * @author Erieze and Einar Lagera
 */
public interface GSMProperties {

    /**
     * Your own ID for addressing this gateway.
     *
     * @return Gateway Identifier
     */
    public String gatewayId();

    /**
     * The COMM port to which this modem is connected. <i>For example, COM1 or
     * /dev/ttyS1.</i>
     *
     * @return Serial COMM Port
     */
    public String commPort();

    /**
     * The baud rate of the serial connection.
     *
     * @return Serial baud rate
     */
    public int baudRate();

    /**
     * The manufacturer, for example "Huawei".
     *
     * @return GSM Modem manufacturer
     */
    public String manufacturer();

    /**
     * The model, for example "Globe Tattoo".
     *
     * @return GSM Modem model
     */
    public String model();

    /**
     * The SMSC number used from now on.
     *
     * @return SMSC Number
     */
    public String smscNumber();

    /**
     * The protocol to be used.
     *
     * @return Gateway Protocol
     */
    public AGateway.Protocols gatewayProtocol();

    /**
     * Automatically initiate garbage collection.
     *
     * @return Auto-GC?
     */
    public boolean autoGC();

    /**
     * Interval in each manual garbage collection.
     *
     * @return Interval in minutes
     */
    public int autGCInterval();

}
