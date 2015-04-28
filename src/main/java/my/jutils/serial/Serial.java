package my.jutils.serial;

import gnu.io.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.*;

/**
 * Simple Utility for communicating with Serial Ports. <p>
 * This utility uses Java's native library for Serial Communication which is
 * from package {@code gnu.io}.
 * @author Erieze Lagera
 */
public class Serial {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Serial.class.getSimpleName());
    
    /**
     * Instance of SerialPort that can be used along runtime and across the
     * project.
     */
    public static SerialPort serialPort;
    
    /** Instance of CommPortIdentifier based on specified {@code commPort}. */
    private static CommPortIdentifier portIdentifier;
    /** InputStream of the current SerialPort instance. */
    private static InputStream input;
    /** OutputStream of the current SerialPort instance. */
    private static OutputStream output;

    /** Identify the status of serial port. */
    private static final AtomicBoolean open = new AtomicBoolean(false);
    /** @see Serial#getCurrentPortOnwer() */
    private static String currentPortOnwer;
    /** @see Serial#getPortName() */
    private static String portName;
    
    /***
     * ASCII Abbr and HEX value.
     */
    /** Start of Header. */
    public static final int SOH = 1;
    /** Start of Text. */
    public static final int STX = 2;
    /** End of Text. */
    public static final int ETX = 3;
    /** Enquiry. */
    public static final int ENQ = 5;
    /** Acknowledgment. */
    public static final int ACK = 6;
    /** Negative Acknowledgment. */
    public static final int NAK = 15;
    /** Cancel. */
    public static final int CAN = 18;
    /** NULL. */
    public static final int NULL = 0;

    /**
     * Open the port of a serial device based on COM Port. <p>
     * Opening port includes 500ms waiting time.
     * @param commPort COM Port of the serial device
     * @param portId COM Port identifier
     * @return True if the serial port opened successfully, otherwise false
     */
    public static boolean openPort(String commPort, String portId) {
        try {
            LOGGER.info("Available Ports: {}", getPortsString());
            portIdentifier = CommPortIdentifier.getPortIdentifier(commPort);
            if (portIdentifier.isCurrentlyOwned()) {
                currentPortOnwer = portIdentifier.getCurrentOwner();
                LOGGER.warn("Port in use by {}", currentPortOnwer);
            } else {
                portName = portIdentifier.getName();
                LOGGER.info("Opening port {}", portName);
                serialPort = (SerialPort) portIdentifier.open(portId, 500);
                serialPort.setSerialPortParams(19200,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, 
                    SerialPort.PARITY_NONE);
                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
                serialPort.notifyOnCarrierDetect(true);
                serialPort.notifyOnDataAvailable(true);
                serialPort.setOutputBufferSize(100);
                serialPort.setInputBufferSize(1000);
                
                input = serialPort.getInputStream();
                output = serialPort.getOutputStream();
            }
            open.set(true);
            return isOpen();
        } catch (NoSuchPortException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        } catch (PortInUseException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        } catch (UnsupportedCommOperationException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        } catch (IOException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        }
        open.set(false);
        return false;
    }

    /**
     * Open the port of a serial device based on COM Port. <p>
     * Opening port includes 500ms waiting time.
     * This also includes your own EventListenser for your Serial port.
     * 
     * @param commPort COM Port of the serial device
     * @param portId COM Port identifier
     * @param spe Your own SerialPortEventListenser
     * @return True if the serial port opened successfully, otherwise false
     */
    public static boolean openPort(String commPort, String portId, SerialPortEventListener spe) {
        try {
            LOGGER.info("Available Ports: {}", getPortsString());
            portIdentifier = CommPortIdentifier.getPortIdentifier(commPort);
            if (portIdentifier.isCurrentlyOwned()) {
                currentPortOnwer = portIdentifier.getCurrentOwner();
                LOGGER.warn("Port in use by {}", currentPortOnwer);
            } else {
                portName = portIdentifier.getName();
                LOGGER.info("Opening port {}", portName);
                serialPort = (SerialPort) portIdentifier.open(portId, 500);
                serialPort.setSerialPortParams(38400,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1, 
                    SerialPort.PARITY_NONE);
                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
                serialPort.notifyOnCarrierDetect(true);
                serialPort.notifyOnDataAvailable(true);
                serialPort.setOutputBufferSize(100);
                serialPort.setInputBufferSize(1000);
                serialPort.addEventListener(spe);
                
                input = serialPort.getInputStream();
                output = serialPort.getOutputStream();
            }
            open.set(true);
            return isOpen();
        } catch (NoSuchPortException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        } catch (PortInUseException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        } catch (UnsupportedCommOperationException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        } catch (IOException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        } catch (TooManyListenersException e) {
            LOGGER.error("Cause: {}", e.toString(), e);
        }
        open.set(false);
        return false;
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
                output.write(cmd.getBytes());
                output.flush();
                result.set(true);
            } catch (IOException e) {
                LOGGER.error("Cause: {}", e.toString(), e);
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
                output.write(bytes);
                output.flush();
                result.set(true);
            } catch (IOException e) {
                LOGGER.error("Cause: {}", e.toString(), e);
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
                output.write(cmd.getBytes());
                output.flush();
                LOGGER.info("Reading response");
                LOGGER.debug("No. of bytes: {}", input.available());
                byte bytesIn[] = new byte[20];
                input.read(bytesIn);
                LOGGER.info("Response: {}", new String(bytesIn));
                result.set(true);
            } catch (IOException e) {
                LOGGER.error("Cause: {}", e.toString(), e);
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
                output.write(bytes);
                output.flush();
                LOGGER.info("Reading response");
                LOGGER.debug("No. of bytes: {}", input.available());
                byte bytesIn[] = new byte[20];
                input.read(bytesIn);
                LOGGER.info("Response: {}", new String(bytesIn));
                result.set(true);
            } catch (IOException e) {
                LOGGER.error("Cause: {}", e.toString(), e);
                result.set(false);
            }
        }
        return result.get();
    }

    /**
     * Query/read the response from the serial port.
     *
     * @return Response of the serial port.
     * @deprecated Use {@code reads()}
     */
    public static String read() {
        if (isOpen()) {
            try {
                int bytes = input.available();
                if (bytes > 0) {
                    LOGGER.debug("No. of bytes: {}", bytes);
                    byte bytesIn[] = new byte[bytes];
                    input.read(bytesIn);
                    return new String(bytesIn);
                }
            } catch (IOException e) {
                LOGGER.error("Cause: {}", e.toString(), e);
                return "";
            }
        }
        return "";
    }
    
    /**
     * Query/read the response from the serial port. <p>
     * This will query serial port until input buffer is available.
     *
     * @return Response of the serial port.
     */
    public static String reads() {
        if (isOpen()) {
            try {
                String result = "";
                while (hasRead()) {
                    result += (char) input.read();
                }
                return result;
            } catch (IOException e) {
                LOGGER.error("Cause: {}", e.toString(), e);
                return "";
            }
        }
        return "";
    }
    
    /**
     * Query/read the response from the serial port. <p>
     * Indicates an estimated number of bytes to be read in the input buffer.
     * 
     * @return False if there has no available input buffer from serial,
     *  otherwise true
     */
    public static boolean hasRead() {
        final AtomicBoolean result = new AtomicBoolean();
        if (isOpen()) {
            try {
                result.set(input.available() > 0);
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
                result.set(false);
            }
        }
        return result.get();
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
                LOGGER.info("Closing port {}...", portName);
                input.close();
                output.close();
                serialPort.close();
                open.set(false);
                result.set(true);
            } catch (IOException e) {
                LOGGER.error("Cause: {}", e.toString(), e);
                result.set(false);
            }
        }
        return result.get();
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
        if (!open.get()) {
            LOGGER.warn("Serial port is closed or not open...");
        }
        return open.get();
    }

    /**
     * The current owner of the specified COM Port.
     * @return Current owner
     * @see Serial#openPort(java.lang.String, java.lang.String) 
     */
    public static String getCurrentPortOnwer() {
        return currentPortOnwer;
    }

    /**
     * Port name given from the specified COM Port.
     * @return Port name
     * @see Serial#openPort(java.lang.String, java.lang.String) 
     */
    public static String getPortName() {
        return portName;
    }

    /**
     * Get available ports.
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
     * Get available ports. <p>
     * Available ports are separated by commas (,) and enclosed to brackets ([])
     * since it was the format of {@code Collection.toString()}.
     * @return Available ports
     */
    public static String getPortsString() {
        return getPorts().toString();
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
     * Convert decimal value to String. <p>
     * Method was similar to {@code (char) ch}
     * 
     * @param ch ASCII decimal value
     * @return String value
     */
    public static String toString(int ch) {
        return String.valueOf((char) ch);
    }

    /**
     * Parses hex into integer. <p>
     * Method was similar to {@code Integer.parseInt(hex, 16)}
     * 
     * @param hex Hex as String
     * @return Parsed hex
     */
    public static int hexToInt(String hex) {
        return Integer.parseInt(hex, 16);
    }
    
}
