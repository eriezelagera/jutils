package my.jutils.serial;

import gnu.io.CommPortIdentifier;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import jssc.*;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.*;

/**
 * Simple Utility for communicating with Serial Ports.
 * <p>
 * This utility uses Java Simple Serial Connector (JSSC), which is also an easy
 * and small utility for Java Serial Communication.
 * <br /> <br />
 * Reference link:
 * <a href="https://code.google.com/p/java-simple-serial-connector/">https://code.google.com/p/java-simple-serial-connector/</a>
 *
 * @author Erieze Lagera
 */
public class SerialJSSC {

    private static final Logger LOGGER = LoggerFactory.getLogger(SerialJSSC.class.getSimpleName());
    
    /**
     * Instance of SerialPort that can be used along runtime and across the
     * project.
     */
    public static SerialPort serialPort;

    /**
     * Identify the status of serial port.
     */
    private static final AtomicBoolean open = new AtomicBoolean(false);
    /**
     * @see Serial#getPortName()
     */
    private static String portName;

    /**
     * *
     * ASCII Abbr and HEX value.
     */
    /**
     * Start of Header.
     */
    public static final int SOH = 1;
    /**
     * Start of Text.
     */
    public static final int STX = 2;
    /**
     * End of Text.
     */
    public static final int ETX = 3;
    /**
     * Enquiry.
     */
    public static final int ENQ = 5;
    /**
     * Acknowledgment.
     */
    public static final int ACK = 6;
    /**
     * Negative Acknowledgment.
     */
    public static final int NAK = 15;
    /**
     * Cancel.
     */
    public static final int CAN = 18;
    /**
     * NULL.
     */
    public static final int NULL = 0;

    /**
     * Open the port of a serial device based on COM Port.
     * <p>
     * @param commPort COM Port of the serial device
     * @return True if the serial port opened successfully, otherwise false
     */
    public static boolean openPort(String commPort) {
        try {
            serialPort = new SerialPort(commPort);
            portName = serialPort.getPortName();
            LOGGER.info("Available Ports: {}", Arrays.toString(SerialPortList.getPortNames()));
            LOGGER.info("Opening port {}", portName);
            open.set(serialPort.openPort());
            open.set(serialPort.setParams(
                    SerialPort.BAUDRATE_19200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE));
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            serialPort.setRTS(false);
            serialPort.setDTR(true);
            return isOpen();
        } catch (SerialPortException e) {
            LOGGER.error("{} at {}", e.toString(), e.getMethodName(), e);
            open.set(false);
            return false;
        }
    }

    /**
     * Open the port of a serial device based on COM Port.
     * <p>
     * This also includes your own EventListenser for your Serial port.
     *
     * @param commPort COM Port of the serial device
     * @param spe Your own SerialPortEventListenser
     * @return True if the serial port opened successfully, otherwise false
     */
    public static boolean openPort(String commPort, SerialPortEventListener spe) {
        try {
            serialPort = new SerialPort(commPort);
            portName = serialPort.getPortName();
            LOGGER.info("Available Ports: {}", Arrays.toString(SerialPortList.getPortNames()));
            LOGGER.info("Opening port {}", portName);
            open.set(serialPort.openPort());
            open.set(serialPort.setParams(
                    SerialPort.BAUDRATE_9600,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE));
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            serialPort.setRTS(false);
            serialPort.setDTR(true);
            final int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR; // Prepare mask
            serialPort.setEventsMask(mask);
            serialPort.addEventListener(spe);
            return isOpen();
        } catch (SerialPortException e) {
            LOGGER.error("{} at {}", e.toString(), e.getMethodName(), e);
            open.set(false);
            return false;
        }
    }

    /**
     * Write/Send command to serial port. After writing or sending command to
     * the serial port, a 100ms break signal will be invoked to ensure the
     * result.
     *
     * @param cmd Command to be written/send
     * @return True if command was successfully written/sent, otherwise false
     */
    public static boolean write(String cmd) {
        final AtomicBoolean result = new AtomicBoolean();
        if (isOpen()) {
            try {
                result.set(serialPort.writeString(cmd));
            } catch (SerialPortException e) {
                LOGGER.error("{} at {}", e.toString(), e.getMethodName(), e);
                result.set(false);
            }
        }
        return result.get();
    }

    /**
     * Write/Send command to serial port. After writing or sending command to
     * the serial port, a 100ms break signal will be invoked to ensure the
     * result.
     *
     * @param bytes Command to be written/send with type of byte[]
     * @return True if command was successfully written/sent, otherwise false
     */
    public static boolean write(byte[] bytes) {
        final AtomicBoolean result = new AtomicBoolean();
        if (isOpen()) {
            try {
                LOGGER.info("Writing {}...", Arrays.toString(bytes));
                result.set(serialPort.writeBytes(bytes));
            } catch (SerialPortException e) {
                LOGGER.error("{} at {}", e.toString(), e.getMethodName(), e);
                result.set(false);
            }
        }
        return result.get();
    }

    /**
     * Write/Send command to serial port and print the response. After writing
     * or sending command to the serial port, a 100ms break signal will be
     * invoked to ensure the result and then the response will be immediately
     * printed.
     *
     * @param cmd Command to be written/send
     * @return True if command was successfully written/sent, otherwise false
     */
    public static boolean writeAndRead(String cmd) {
        final AtomicBoolean result = new AtomicBoolean();
        if (isOpen()) {
            try {
                serialPort.writeString(cmd);
                LOGGER.info("Response: {}", serialPort.readString());
                result.set(true);
            } catch (SerialPortException e) {
                LOGGER.error("{} at {}", e.toString(), e.getMethodName(), e);
                result.set(false);
            }
        }
        return result.get();
    }

    /**
     * Write/Send command to serial port and print the response. After writing
     * or sending command to the serial port, a 100ms break signal will be
     * invoked to ensure the result and then the response will be immediately
     * printed.
     *
     * @param bytes Command to be written/send with type of byte[]
     * @return True if command was successfully written/sent, otherwise false
     */
    public static boolean writeAndRead(byte[] bytes) {
        final AtomicBoolean result = new AtomicBoolean();
        if (isOpen()) {
            try {
                LOGGER.info("Writing {}...", Arrays.toString(bytes));
                result.set(serialPort.writeBytes(bytes));
                LOGGER.info("Response: {}", serialPort.readHexString());
            } catch (SerialPortException e) {
                LOGGER.error("{} at {}", e.toString(), e.getMethodName(), e);
                result.set(false);
            }
        }
        return result.get();
    }

    /**
     * Query/read the response from the serial port.
     *
     * @return Response of the serial port.
     */
    public static String read() {
        String read = "";
        try {
            final byte[] in = SerialJSSC.serialPort.readBytes();
            if (in != null) {
                for (byte b : in) {
                    read += (char) b;
                }
            }
        } catch (SerialPortException e) {
            LOGGER.error("{} at {}", e.toString(), e.getMethodName(), e);
        }
        return read;
    }

    /**
     * Query/read the response from the serial port.
     *
     * @param type Type of reading response
     * @return Response of the serial port.
     * @see ReadType
     * @deprecated Use the variety of SerialJSSC.read() for more options
     */
    public static String read(ReadType type) {
        String result = "";
        if (isOpen()) {
            try {
                serialPort.sendBreak(100);
                switch (type) {
                    case String:
                        result = serialPort.readHexString();
                        break;
                    case StringArr:
                        for (String str : serialPort.readHexStringArray()) {
                            result += str;
                        }
                        break;
                    case ByteArr:
                        for (byte b : serialPort.readBytes()) {
                            result += (char) b;
                        }
                        break;
                    case IntArr:
                        for (int i : serialPort.readIntArray()) {
                            result += String.valueOf((char) i) + " ";
                        }
                        result = result.trim();
                        break;
                }
            } catch (SerialPortException e) {
                LOGGER.error("{} at {}", e.toString(), e.getMethodName(), e);
            } catch (NullPointerException e) {
                LOGGER.debug("Response: {}", e.getMessage());
            }
        }
        return result.trim();
    }

    /**
     * Type of data in reading response from serial port.
     *
     * @deprecated Use the variety of SerialJSSC.read() for more options
     */
    public static enum ReadType {

        /**
         * Read response as type of String.
         */
        String,
        /**
         * Read response as type of String array.
         * <p>
         * Separated by spaces.
         */
        StringArr,
        /**
         * Read response as type of bytes.
         * <p>
         * Each byte will be casted to character value and will return as
         * String.
         */
        ByteArr,
        /**
         * Read response as type of native Integer array.
         * <p>
         * Result will be casted to character value and will return as String.
         */
        IntArr
    }

    /**
     * Closes the COM port.
     * <p>
     * Upon invoking this method, status will be changed to closed if port was
     * successfully closed. Otherwise it will remain open if
     * {@code SerialPortException} was caught.
     *
     * @return True if port was successfully closed, otherwise true and it will
     * remain open
     */
    public static boolean closePort() {
        final AtomicBoolean result = new AtomicBoolean();
        if (isOpen()) {
            try {
                LOGGER.info("Closing port {}...", serialPort.getPortName());
                open.set(!serialPort.closePort());
                result.set(true);
            } catch (SerialPortException e) {
                LOGGER.error("{} at {}", e.toString(), e.getMethodName(), e);
                result.set(false);
            }
        }
        return result.get();
    }

    /**
     * Port name given from the specified COM Port.
     *
     * @return Port name
     * @see Serial#openPort(java.lang.String, java.lang.String)
     */
    public static String getPortName() {
        return portName;
    }

    /**
     * Get available ports.
     *
     * @return Collection of available ports
     */
    public static Collection<String> getPorts() {
        final Collection<String> portsAvailable = new ArrayList<String>();
        final Enumeration portList = CommPortIdentifier.getPortIdentifiers();
        while (portList.hasMoreElements()) {
            final CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
            if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                portsAvailable.add(portId.getName());
            }
        }
        return portsAvailable;
    }

    /**
     * Get available ports.
     * <p>
     * Available ports are separated by commas (,) and enclosed to brackets ([])
     * since it was the format of {@code Collection.toString()}.
     *
     * @return Available ports
     */
    public static String getPortsString() {
        return getPorts().toString();
    }

    /**
     * Check Serial Port status.
     * <p>
     * Initially, status is closed. If {@code openPort(String)} was invoked,
     * status may changed to open or close if port cannot be open.
     * {@code closePort()} can also change status to close and this method will
     * always return false until you open the serial port again.
     *
     * @return True if serial port is currently open, otherwise false if it was
     * or already closed
     */
    public static boolean isOpen() {
        if (open.get()) {
            return open.get();
        }
        LOGGER.warn("Serial port is closed or not open...");
        return open.get();
    }

    /**
     * Convert decimal value to Escaped String.
     *
     * @param ch ASCII decimal value
     * @return Escaped String
     */
    public static String escapeString(int ch) {
        return StringEscapeUtils.escapeJava(String.valueOf((char) ch));
    }

    /**
     * Convert decimal value to String.
     * <p>
     * Method was similar to {@code (char) ch}
     *
     * @param ch ASCII decimal value
     * @return String value
     */
    public static String toString(int ch) {
        return String.valueOf((char) ch);
    }

    /**
     * Parses hex into integer.
     * <p>
     * Method was similar to {@code Integer.parseInt(hex, 16)}
     *
     * @param hex Hex as String
     * @return Parsed hex
     */
    public static int hexToInt(String hex) {
        return Integer.parseInt(hex, 16);
    }

}
