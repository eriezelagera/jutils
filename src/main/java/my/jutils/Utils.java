package my.jutils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.imageio.*;
import javax.print.*;
import javax.print.attribute.*;
import javax.print.attribute.standard.Copies;
import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.JTextComponent;
import my.jutils.poi.CreateExcel;
import my.jutils.services.PrintJobWatcher;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.slf4j.*;

/**
 * This contains methods that will help to optimize your programs at ease.
 * <p>
 * All <b>exceptions</b> are caught internally.
 *
 *
 * Update history: <i> Check doc at the root of this project. </i>
 *
 * If you want to handle the exception, just contact me and I will give the
 * source code.
 *
 *
 * Contact me at erieze.lagera@mail.com
 *
 * @author Erieze Lagera
 * @version 3.0
 */
public final class Utils {

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class.getSimpleName());

    /**
     * Causes the currently executing thread to sleep (temporarily cease
     * execution) for the specified number of milliseconds.
     *
     * @param delayTime The length of time to sleep in millis
     */
    public static void setDelay(long delayTime) {
        try {
            Thread.sleep(delayTime);
        } catch (InterruptedException e) {
            LOGGER.error("Cause: {}", e.toString());
        }
    }

    /**
     * Set JFrame location to the middle of your screen.
     * <p>
     * This will automatically adjusted depending on your resolution.
     *
     * @param width Width of your JFrame form
     * @param height Height of your JFrame form
     * @return Returns instance of Point, then use it with this.setLocation()
     * method.
     */
    public static Point setFrameMiddle(final int width, final int height) {
        final Point p = new Point();
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        p.setLocation(((Numbers.toInt(dim.getWidth()) - width) / 2), ((Numbers.toInt(dim.getHeight()) - height) / 2));
        return p;
    }

    /**
     * Set JFrame to the middle of your screen.
     * <p>
     * This will automatically adjusted depending on your resolution.
     *
     * @param frame Your JFrame form
     */
    public static void setFrameToMiddle(final Frame frame) {
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(((Numbers.toInt(dim.getWidth()) - frame.getWidth()) / 2), ((Numbers.toInt(dim.getHeight()) - frame.getHeight()) / 2));
    }

    /**
     * Set JFrame to the middle of your screen.
     * <p>
     * This will automatically adjusted depending on your resolution.
     *
     * @param dialog Your JDialog form
     */
    public static void setDialogToMiddle(final JDialog dialog) {
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        dialog.setLocation(((Numbers.toInt(dim.getWidth()) - dialog.getWidth()) / 2), ((Numbers.toInt(dim.getHeight()) - dialog.getHeight()) / 2));
    }

    /**
     * Set JFrame location based on the specified location.
     *
     * @param frame Your JFrame form
     * @param location Specified location
     * @return Specified point or location
     * @see Location
     */
    public static Point setFrameLocation(final JFrame frame, final Location location) {
        final Point p = frame.getLocation();
        final Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        switch (location) {
            case North:
                p.setLocation(((Numbers.toInt(dim.getWidth()) - frame.getWidth()) / 2), 0);
                break;
            case NorthEast:
                p.setLocation((Numbers.toInt(dim.getWidth()) - frame.getWidth()), 0);
                break;
            case NorthWest:
                p.setLocation(0, 0);
                break;
            case South:
                p.setLocation(((Numbers.toInt(dim.getWidth()) - frame.getWidth()) / 2), (Numbers.toInt(dim.getHeight()) - frame.getHeight()));
                break;
            case SouthEast:
                p.setLocation((Numbers.toInt(dim.getWidth()) - frame.getWidth()), (Numbers.toInt(dim.getHeight()) - frame.getHeight()));
                break;
            case SouthWest:
                p.setLocation(0, (Numbers.toInt(dim.getHeight()) - frame.getHeight()));
                break;
            case East:
                p.setLocation((Numbers.toInt(dim.getWidth()) - frame.getWidth()), ((Numbers.toInt(dim.getHeight()) - frame.getHeight()) / 2));
                break;
            case West:
                p.setLocation(0, ((Numbers.toInt(dim.getHeight()) - frame.getHeight()) / 2));
                break;
            case Middle:
                p.setLocation(((Numbers.toInt(dim.getWidth()) - frame.getWidth()) / 2), ((Numbers.toInt(dim.getHeight()) - frame.getHeight()) / 2));
                break;
        }
        return p;
    }

    /**
     * Location or Point where your form will open.
     *
     * @see Utils#setFrameLocation(javax.swing.JFrame, my.jutils.Utils.Location)
     */
    public static enum Location {

        /**
         * North Center of the screen.
         */
        North,
        /**
         * North East (Upper-right) of the screen.
         */
        NorthEast,
        /**
         * North West (Upper-right) of the screen. <br /> Note: This is the
         * default location.
         */
        NorthWest,
        /**
         * South Center of the screen.
         */
        South,
        /**
         * South East (Lower-right) of the screen.
         */
        SouthEast,
        /**
         * South West (Lower-left) of the screen.
         */
        SouthWest,
        /**
         * East Center (Middle-right) of the screen.
         */
        East,
        /**
         * West Center (Middle-left) of the screen.
         */
        West,
        /**
         * Middle of the screen. <br /> See Utils.setFrameToMiddle because it
         * was the same.
         */
        Middle
    }

    /**
     * Get the screen resolution of the current monitor.
     * <p>
     * Screen resolution is relative to the monitor, so every monitor has it's
     * different resolutions. This may return different values depending on the
     * monitor you are using.
     * <br />
     * Note: <i> You must design your Swing interface into relative, resizable
     * frame in order to maintain the interface. </i>
     *
     * @return Screen resolution of the currently using monitor
     */
    public static Dimension getScrResolution() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    /**
     * Replaces the string to the base on the given regex.
     *
     * @param string the String to be converted.
     * @param regex the regular expression.
     * @param replacement the replacement to the regular expression.
     * @return the converted String.
     */
    public static String replaceStringWithRegEx(String string, String regex, String replacement) {
        return string.replaceAll(regex, replacement);
    }

    /**
     * Remove all specified character in your String. <br />
     * <b>Ex.</b> <br />
     * removeCharFromStr("+", "Hel+lo") will return <br />
     * <b>"Hello"</b>
     *
     * @param foo String that you want to process
     * @param remove Character you want to remove to your String
     * @return Processed string
     */
    public static String removeCharFromStr(String foo, String remove) {
        //return foo.replaceAll("[" + remove + "]", "");
        return foo.replace(remove, "");
    }

    /**
     * This removes all specific character format in your String. <br />
     * <b>Ex.</b> <br />
     * foo = FEMALE removeSpecificCharFromStr("FE", "") will return <br />
     * <b>"MALE"</b>
     *
     * @param foo String that you want to process
     * @param remove Character you want to remove to your String
     * @return Processed string
     */
    public static String removeSpecificCharFromStr(String foo, String remove) {
        return foo.replaceAll(remove, "");
    }

    /**
     * Gets a value from an input dialog provided by native JOptionPane. It will
     * ask again if the user inputs an empty string.
     *
     * @param message the message to be ask to user
     * @return the value input by the user.
     */
    public static String getMessage(String message) {
        while (true) {
            final String input = JOptionPane.showInputDialog(null, message, "Input information", JOptionPane.QUESTION_MESSAGE);
            if (!input.isEmpty()) {
                return input;
            }
        }
    }

    /**
     * Convert char[] to String.
     *
     * @param charArr Some character array
     * @return Converted char[] -> String
     */
    public static String charArrToStr(char[] charArr) {
        String toString = "";
        for (int i = 0; i < charArr.length; i++) {
            toString += String.valueOf(charArr[i]);
        }
        return toString;
    }

    /**
     * This method converts a String to md5 format.
     *
     * @param password the password to be converted into md5 format
     * @return the md5 value of the password parameter
     */
    public static String convertStringToMD5(String password) {
        try {
            final MessageDigest mdEnc = MessageDigest.getInstance("MD5");
            mdEnc.update(password.getBytes(), 0, password.length());
            return new BigInteger(1, mdEnc.digest()).toString(16); // Encrypted 
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Cause: {}", e.toString());
            return "";
        }
    }

    /**
     * <i>Note: This was replace by <b>CreateExcel</b> class.</i>
     * <p>
     * Any caught exception such as <b>IOException</b> will interrupt the saving
     * of file.
     *
     * <i>
     * Warning: This will replace the old file if there's already exists! If you
     * want to avoid overwritten of file, please do a simple evaluation of file
     * existence first before calling this method.
     * </i>
     *
     * @param wb An instance of XSSFWorkbook, a class for MS Excel <b>2007</b>
     * support. This must contain the rows and cell that is ready for saving.
     * @param file_path The save path for the resulting file. This must include
     * the file extension.
     * @return True if no error occurs in process of saving, otherwise false.
     * @see CreateExcel#execute()
     * @deprecated
     */
    public static boolean doSaveExcelFile(XSSFWorkbook wb, String file_path) {
        try (FileOutputStream writeFile = new FileOutputStream(file_path)) {
            wb.write(writeFile);
            writeFile.flush();
            writeFile.close();
            return true;
        } catch (IOException e) {
            LOGGER.error("Cause: {}", e.toString());
            return false;
        }
    }

    /**
     * <i>Note: This was replace by <b>CreateExcel</b> class.</i>
     * <p>
     * Save/Write the resulting excel file to the specified file path. Any
     * caught exception such as <b>IOException</b> will interrupt the saving of
     * file.
     *
     * <i>
     * Warning: This will replace the old file if there's already exists! If you
     * want to avoid overwritten of file, please do a simple evaluation of file
     * existence first before calling this method.
     * </i>
     *
     * @param wb An instance of XSSFWorkbook, a class for MS Excel <b>2003</b>
     * support. This must contain the rows and cell that is ready for saving.
     * @param file_path The save path for the resulting file. This must include
     * the file extension.
     * @return True if no error occurs in process of saving, otherwise false.
     * @see CreateExcel#execute()
     * @deprecated
     */
    public static boolean doSaveExcelFile(HSSFWorkbook wb, String file_path) {
        try (FileOutputStream writeFile = new FileOutputStream(file_path)) {
            wb.write(writeFile);
            writeFile.flush();
            writeFile.close();
            return true;
        } catch (IOException e) {
            LOGGER.error("Cause: {}", e.toString());
            return false;
        }
    }

    /**
     * Manually invoke Garbage Collection.
     * <p>
     * Note that this was done by the JVM automatically but it was only
     * initiated when a specified object which allocated in memory can no longer
     * be accessed.
     *
     * @see System#gc()
     */
    public static void gc() {
        LOGGER.info("Running garbage collection...");
        System.gc();
        LOGGER.info("Garbage collected!");
    }

    /**
     * Convert String to Cyrillic Unicode.
     * <p>
     * If it is in this range it is Cyrillic. Just perform an if check. If it is
     * in the range use {@code Integer.toHexString()} and prepend the "\\u".
     *
     * @param str String
     * @return Cyrillic Unicode String
     */
    public static String toCyrillicUnicode(String str) {
        final StringBuilder b = new StringBuilder();
        for (char c : str.toCharArray()) {
            if ((1024 <= c && c <= 1279) || (1280 <= c && c <= 1327) || (11744 <= c && c <= 11775) || (42560 <= c && c <= 42655)) {
                b.append("\\u").append(Integer.toHexString(c));
            } else {
                b.append(c);
            }
        }
        return b.toString();
    }

    /**
     * Resize an image with hint.
     *
     * @param image Image in the form of byte array
     * @param label JLabel where this method called
     * @return Instance of BufferedImage
     */
    public static BufferedImage resizeImageWithHint(byte[] image, JLabel label) {
        BufferedImage resized = null;
        try {
            final BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
            final int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
            final BufferedImage resizedImage = new BufferedImage(label.getWidth(), label.getHeight(), type);
            final Graphics2D g = resizedImage.createGraphics();
            g.drawImage(bufferedImage, 0, 0, label.getWidth(), label.getHeight(), null);
            g.dispose();
            g.setComposite(AlphaComposite.Src);

            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            resized = resizedImage;
        } catch (IOException e) {
            LOGGER.error("Cause: {}", e.toString());
        }
        return resized;
    }

    /**
     * Resize an image with hint.
     *
     * @param image Image in the form of byte array
     * @param width width size of resized image
     * @param height height size of resized image
     * @return Instance of BufferedImage
     */
    public static BufferedImage resizeImageWithHint(byte[] image, int width, int height) {
        BufferedImage resized = null;
        try {
            final BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
            final int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
            final BufferedImage resizedImage = new BufferedImage(width, height, type);
            final Graphics2D g = resizedImage.createGraphics();
            g.drawImage(bufferedImage, 0, 0, width, height, null);
            g.dispose();
            g.setComposite(AlphaComposite.Src);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            resized = resizedImage;
        } catch (IOException e) {
            LOGGER.error("Cause: {}", e.toString());
        }
        return resized;
    }

    /**
     * Resize an image with hint.
     *
     * @param image Image in the form of byte array
     * @param label JLabel where this method called
     * @return Instance of BufferedImage
     */
    public static byte[] resizeImageWithHintBytes(byte[] image, JLabel label) {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            final BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(image));
            final int type = bufferedImage.getType() == 0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
            final BufferedImage resizedImage = new BufferedImage(label.getWidth(), label.getHeight(), type);
            final Graphics2D g = resizedImage.createGraphics();
            g.drawImage(bufferedImage, 0, 0, label.getWidth(), label.getHeight(), null);
            g.dispose();
            g.setComposite(AlphaComposite.Src);
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            ImageIO.write(resizedImage, "gif", baos);
            bytes = baos.toByteArray();
        } catch (IOException e) {
            LOGGER.error("Cause: {}", e.toString());
        } finally {
            closeOutputStream(baos);
        }
        return bytes;
    }

    /**
     * Extract bytes from BufferedImage.
     *
     * @param image Image in instance of BufferedImage
     * @param imageFormat Image format type (jpg, gif, png, etc)
     * @return Extracted bytes from BufferedImage with type of byte[]
     */
    public static byte[] extractBytes(BufferedImage image, String imageFormat) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] bytes = null;
        try {
            ImageIO.write(image, imageFormat, bos);
            bytes = bos.toByteArray();
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            closeOutputStream(bos);
        }
        return bytes;
    }

    /**
     * Closes any instance of a class underlying Writer class.
     * <p>
     * Upon closing the specified Writer, null value was also evaluated.
     *
     * @param writer Writer instance
     */
    public static void closeWriter(Writer writer) {
        try {
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * Closes any instance of a class underlying Reader class.
     * <p>
     * Upon closing the specified Reader, null value was also evaluated.
     *
     * @param reader Reader instance
     */
    public static void closeReader(Reader reader) {
        try {
            if (reader != null) {
                reader.close();
            }
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString());
        }
    }

    /**
     * Closes any instance of a class underlying OutputStream class.
     * <p>
     * Upon closing the specified OutputStream, null value was also evaluated.
     *
     * @param output OutputStream instance
     */
    public static void closeOutputStream(OutputStream output) {
        try {
            if (output != null) {
                output.close();
            }
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString());
        }
    }

    /**
     * Closes any instance of a class underlying InputStream class.
     * <p>
     * Upon closing the specified InputStream, null value was also evaluated.
     *
     * @param input InputStream instance
     */
    public static void closeInputStream(InputStream input) {
        try {
            if (input != null) {
                input.close();
            }
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString());
        }
    }

    /**
     * Execute shell command.
     * <p>
     * Shell commands are based to the platform you are currently using. So if
     * you plan to develop in different platforms, then use commands which is
     * appropriate in all type of platforms. If you're developing at Windows
     * platform, this will automatically add "cmd /c" at the beginning of the
     * specified command.
     *
     * @param command Shell command
     * @return Output from shell
     */
    public static String execCmd(String command) {
        if (OSValidator.isWindows()) {
            command = "cmd /c " + command;
        }
        final StringBuilder output = new StringBuilder();
        BufferedReader reader = null;
        try {
            final Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append("\n").append(line);
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.error("Cause: {}", e.toString());
        } finally {
            closeReader(reader);
        }

        return output.toString();
    }

    /**
     * Execute shell command.
     * <p>
     * Shell commands are based to the platform you are currently using. So if
     * you plan to develop in different platforms, then use commands which is
     * appropriate in all type of platforms.
     *
     * @param command Shell command
     */
    public static void execCmdOut(String command) {
        final StringBuilder output = new StringBuilder();
        BufferedReader reader = null;
        try {
            final Process p = Runtime.getRuntime().exec(command);
            p.waitFor();
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append("\n").append(line);
            }
        } catch (IOException | InterruptedException e) {
            LOGGER.error(e.getMessage(), e);
        } finally {
            closeReader(reader);
        }
        System.out.println(output.toString());
    }

    /**
     * Empty HashMap.
     * <p>
     * This does nothing special, just for shortening code.
     *
     * @return New instance of HashMap<String, Object>
     */
    public static HashMap<String, Object> emptyHash() {
        return new HashMap<>();
    }

    /**
     * Append to logs with date time stamp.
     *
     * @param logs the text are where the logs will be inserted
     * @param message Some message
     */
    public static void appendLog(JTextArea logs, String message) {
        logs.append("[" + Times.toDateFormat(new DateTime().toDate(), "MMM dd YYYY hh:mm:ssa") + "] " + message + "\n");
    }

    /**
     * Sets the User-interface based on currently using platform.
     * <p>
     * If you're using Windows platform, then Windows UI will be applied as the
     * default Look and Feel, and same for the other platform.
     */
    public static void setSystemLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            LOGGER.error("Cause: " + e.toString(), e);
        } catch (InstantiationException e) {
            LOGGER.error("Cause: " + e.toString(), e);
        } catch (IllegalAccessException e) {
            LOGGER.error("Cause: " + e.toString(), e);
        } catch (javax.swing.UnsupportedLookAndFeelException e) {
            LOGGER.error("Cause: " + e.toString(), e);
        }
    }

    /**
     * Get the total width of all columns.
     * <p>
     * Evaluates each columns and its row to get the widest width which will be
     * set to be the new width of a column. Getting the widest width will be
     * based on cell data length.
     * <br />
     * Note: <i>This may be time consuming based on the number of data stored in
     * your table because this will visit every row to evaluated each column's
     * width.</i>
     *
     * @param table JTable to be resized
     * @return The total width of all resized column
     */
    public static int autoResizeColumn(JTable table) {
        int totalWidth = 0;
        for (int column = 0; column < table.getColumnCount(); column++) {
            final TableColumn tableColumn = table.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            final int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < table.getRowCount(); row++) {
                final TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
                final Component c = table.prepareRenderer(cellRenderer, row, column);
                final int width = c.getPreferredSize().width + table.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                //  We've exceeded the maximum width, no need to check other rows
                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }
            LOGGER.info("Column/Width: {} / {}", tableColumn.getIdentifier(), preferredWidth);
            tableColumn.setPreferredWidth(preferredWidth);
            totalWidth += preferredWidth;
        }
        return totalWidth;
    }

    /**
     * Validates all fields from {@code JFrame} form.
     * <p>
     * This will check all fields value, if one is empty, then this will return
     * positive result. The following fields are included in validation:
     * <br />
     * <ul>
     * <li> {@code JTextField}
     * <li> {@code JTextArea}
     * <li> {@code JPasswordField}
     * </ul>
     * <br />
     * Other fields will not be evaluated, thus will be skipped from validation.
     * <br />
     * Usage: Considering you're on a {@code JFrame} extended class,
     * <i> boolean result =
     * validateFields(this.getContentPane().getComponents()); </i>
     * If the fields are enclosed in a {@code JPanel},
     * <i> boolean result = validateFields(jPanel.getComponents()); </i>
     *
     * @param components Array of components from your {@code JFrame} form
     * @return True if one field is empty, otherwise false
     */
    public static boolean validateFields(Component[] components) {
        for (Component c : components) {
            if (c instanceof JTextField || c instanceof JTextArea || c instanceof JPasswordField) {
                if (((JTextComponent) c).getText().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Clears all fields from the given components.
     * <p>
     * The following fields are included in validation:
     * <br />
     * <ul>
     * <li> {@code JTextField}
     * <li> {@code JTextArea}
     * <li> {@code JPasswordField}
     * </ul>
     * <br />
     * Other fields will not be evaluated, thus will be skipped from validation.
     * <br />
     * * Usage: Considering you're on a {@code JFrame} extended class,
     * <i> boolean result =
     * clearTextFields(this.getContentPane().getComponents()); </i>
     * If the fields are enclosed in a {@code JPanel},
     * <i> boolean result = clearTextFields(jPanel.getComponents()); </i>
     *
     * @param components Array of components from your {@code JFrame} form
     * @return True if one or more field was set to empty, otherwise false if
     * none
     */
    public static boolean clearTextFields(Component[] components) {
        final AtomicBoolean result = new AtomicBoolean();
        for (Component c : components) {
            if (c instanceof JTextField || c instanceof JTextArea || c instanceof JPasswordField) {
                ((JTextComponent) c).setText("");
                result.set(true);
            }
        }
        return result.get();
    }

    /**
     * Prints a file based on the specified file path.
     * <p>
     * This will print multiple copies based on the given number of copies.
     * <br />
     * Printing file will require a running <i> printer service </i> in able to
     * do a print job and a configured default printer service. If there's no
     * printer set to default, no print job will be done.
     *
     * @param filename Full path of the file, absolute or relative
     * @param copies Number of copies to be printed
     * @return True if printing was successful, otherwise false
     */
    public static boolean printFile(String filename, int copies) {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(filename));
            final DocFlavor flavor = DocFlavor.READER.TEXT_HTML;
            final PrintRequestAttributeSet attributeSet = new HashPrintRequestAttributeSet();
            attributeSet.add(new Copies(copies));
            final PrintService service = PrintServiceLookup.lookupDefaultPrintService();
            if (service != null) {
                LOGGER.info("Print Service Name is " + service.getName());
                final Doc doc = new SimpleDoc(in, flavor, null);
                final DocPrintJob job = service.createPrintJob();
                final PrintJobWatcher watcher = new PrintJobWatcher(job);
                LOGGER.info("Printing...");
                job.print(doc, attributeSet);
                watcher.waitForDone();
                return true;
            } else {
                LOGGER.warn("No default print service found");
                return false;
            }
        } catch (FileNotFoundException | PrintException e) {
            LOGGER.error("Cause: {}", e.toString());
            return false;
        } finally {
            Utils.closeInputStream(in);
        }
    }

    /**
     * Prints a file based on the specified file path.
     * <p>
     * This will print a single copy of the specified file. If you want to print
     * multiple copies, use {@link Utils#printFile(java.lang.String, int)}
     * instead.
     * <br />
     * Printing file will require a running <i> printer service </i> in able to
     * do a print job and a configured default printer service. If there's no
     * printer set to default, no print job will be done.
     *
     * @param filename Full path of the file, absolute or relative
     * @return True if printing was successful, otherwise false
     */
    public static boolean printFile(String filename) {
        return printFile(filename, 1);
    }

    /**
     * Prints a file based on the specified file path.
     * <p>
     * The difference between this method and
     * {@link Utils#printFile(java.lang.String)} is that this method uses the
     * native desktop printing facility rather than directly spool the file to
     * printer. But this method is more reliable.
     *
     * @param filename Full path of the file, absolute or relative
     * @return True if printing was successful, otherwise false
     */
    public static boolean printNative(String filename) {
        final AtomicBoolean result = new AtomicBoolean();
        try {
            //Desktop.getDesktop().print(new File(filename));
            Desktop desktop = null;
            if (!Desktop.isDesktopSupported()) {
                LOGGER.warn("Printing not supported on the current platform. [{}]", System.getProperty("os.name"));
                result.set(false);
            } else {
                desktop = Desktop.getDesktop();
                if (!desktop.isSupported(Desktop.Action.PRINT)) {
                    LOGGER.warn("Printing not supported on the current platform. [{}]", System.getProperty("os.name"));
                    result.set(false);
                } else {
                    LOGGER.info("Printing {}...", filename);
                    desktop.print(new File(filename));
                    result.set(true);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Cause: {}", e.toString());
            result.set(false);
        }
        return result.get();
    }

    /**
     * Prints a file based on the specified file path.
     * <p>
     * This will print multiple copies based on the given number of copies.
     * <br />
     * The difference between this method and
     * {@link Utils#printFile(java.lang.String)} is that this method uses the
     * native desktop printing facility rather than directly spool the file to
     * printer. But this method is more reliable.
     * <br />
     * Note: <i> Printing will stop and this method will immediately return if
     * an error occurred during the process. </i>
     *
     * @param filename Full path of the file, absolute or relative
     * @param copies Number of copies to be printed
     * @return True if printing was successful, otherwise false
     */
    public static boolean printNative(String filename, int copies) {
        if (copies <= 0) {
            throw new IllegalArgumentException("Value of copies must be greater than zero (0)");
        }
        final AtomicBoolean result = new AtomicBoolean();
        for (int i = 0; i < copies; i++) {
            result.set(printNative(filename));
            if (!result.get()) {
                LOGGER.warn("An error occured printing file {}!", filename);
                break;
            }
        }
        return result.get();
    }

    /**
     * Prints a multiple files based on the given {@code List} of filenames.
     * <p>
     * The difference between this method and
     * {@link Utils#printFile(java.lang.String)} is that this method uses the
     * native desktop printing facility rather than directly spool the file to
     * printer. But this method is more reliable.
     * <br />
     * Note: <i> Printing will stop and this method will immediately return if
     * an error occurred during the process. </i>
     *
     * @param filename List of full path of the files, absolute or relative
     * @return True if printing was successful, otherwise false
     */
    public static boolean printNative(java.util.List<String> filename) {
        final AtomicBoolean result = new AtomicBoolean();
        for (String file : filename) {
            result.set(printNative(file));
            if (!result.get()) {
                LOGGER.warn("An error occured printing file {}!", file);
                break;
            }
        }
        return result.get();
    }

}
