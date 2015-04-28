package my.jutils.poi;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import my.jutils.OSValidator;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.*;

/**
 * Process all data that will be written to the Excel file.
 * <p>
 * The process starts from reading the template Excel files. This will process
 * the replacement of <b>{@code placeholders}</b> from the template file. After
 * the process from template, the Excel file will be written to the specified
 * <b>save path</b>.
 *
 *
 * <i>
 * Version history:
 * <ul>
 * <li> Version 1.0 - This has to be the simplest solution, but cell style was
 * the problem.
 * <li> Version 1.5 - No methods added, just some logic. Cell style problem was
 * fixed, but doesn't support the multiple detail placeholder.
 * <li>Version 2.0 - This version supports the detail placeholder, which works
 * almost the same as the JasperReports. But the current version only supports
 * fixed column size, which means you can't exceed to your template's number of
 * columns, but row is capable of dynamic count. Methods were added to support
 * the new placeholder. Moved the old logic to the bottom most this class. See
 * Detail Placeholder sample {@code http://pastebin.com/ZMLLGAMp, and
 * https://www.dropbox.com/s/kiluqafq8ofmxo8/template.xlsx} for sample template
 * file.
 * </ul>
 * </i>
 *
 * Note: <i> Please see the JXLSUtil.java {@code http://pastebin.com/Rf4FW9ir},
 * this was for the
 * {@link JXLSUtil#mergeExcelFiles(org.apache.poi.xssf.usermodel.XSSFWorkbook, org.apache.poi.xssf.usermodel.XSSFWorkbook, java.lang.String, int)}
 * method.</i>
 *
 * If you have questions or feedback, please don't hesitate to contact me. This
 * will help us and others a lot! Contact me at
 * <i>{@code erieze.lagera@gmail.com}</i>
 *
 * @author Erieze Lagera
 */
public class CreateExcel {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateExcel.class.getSimpleName());

    /**
     * Path of the template Excel file.
     */
    private final String filePath;
    /**
     * Sheet index of your template.
     */
    private final int sheetIndex;
    /**
     * Path for the resulting Excel file.
     */
    private final String savePath;
    /**
     * Sheet name for the resulting Excel workbook.
     */
    private final String sheetName;
    /**
     * Contains the placeholders together with their value.
     * <i> Note that the placeholder from the template should starts with dollar
     * sign (<b>$</b>). But dollar sign when putting placeholder in HashMap is
     * not necessary.
     */
    private final Map<String, Object> placeholders;

    /**
     * Create an Excel file that will contains the process data.
     * <p>
     * You must have the template Excel file before invoking this method,
     * because this is an dependent processing of Excel file. This needs a
     * template file to create a resulting Excel file.
     * <i>
     * Note: If save path is null or empty, it will be moved to your
     * <b>Desktop</b> directory. And for file path, null or empty value will be
     * handled by the IOException. Also the placeholder from the template should
     * starts with dollar sign (<b>$</b>), but not necessary when putting in the
     * {@link HashMap}. Ex. map.put("replaceMe", "Hello");
     * </i>
     *
     * @param file_path Path of the template Excel file
     * @param sheet_index Sheet index of your template
     * @param save_path Path for the resulting Excel file
     * @param sheet_name Sheet name for the resulting Excel workbook
     * @param placeholders Contains the placeholders together with their value.
     */
    public CreateExcel(String file_path, int sheet_index, String save_path, String sheet_name, Map<String, Object> placeholders) {
        this.filePath = file_path;
        this.sheetIndex = sheet_index;
        this.savePath = checkSavePath(save_path);
        this.sheetName = sheet_name;
        this.placeholders = placeholders;
    }

    /**
     * Process the creation of the Excel report.
     * <p>
     * @return True of no error occurs in process, otherwise false.
     * @since 1.0
     */
    public boolean execute() {
        try (FileInputStream file = new FileInputStream(new File(filePath))) {
            // Load the template file
            final XSSFWorkbook wb = new XSSFWorkbook(file);
            XSSFSheet sheet = wb.getSheetAt(sheetIndex);
            XSSFRow row;
            XSSFCell cell;
            // For resulting Excel file
            XSSFWorkbook wbNew = new XSSFWorkbook();

            final AtomicInteger rowIndex = new AtomicInteger();
            final AtomicInteger cellIndex = new AtomicInteger();
            String cellStr = "";

            /**
             * * New: Cell index of the detail placeholder **
             */
            final AtomicInteger tempCellIndex = new AtomicInteger();

            /**
             * * New: User must place #end to tell the program that it is the
             * last row **
             */
            // Get row until it reaches the #end
            while (true) {
                row = sheet.getRow(rowIndex.get());

                if (row == null) {
                    row = sheet.createRow(rowIndex.getAndIncrement());
                }

                // We only support fixed column length and expanding row.
                for (; cellIndex.get() < row.getPhysicalNumberOfCells(); cellIndex.getAndIncrement()) {
                    cell = row.getCell(cellIndex.get());
                    cellStr = cell.toString();

                    if (isPlaceholder(cellStr)) {
                        cell.setCellValue(getValue(cellStr));
                    } else if (isDetailPlaceholder(cellStr)) {
                        /* 
                         * Replace the detail placeholder with anything you want, 
                         * for now let's replace it with blank.
                         */
                        cell.setCellValue("");

                        /*
                         * This will give a free row for the detail placeholder, 
                         * moving the existing rows based on detail row count.
                         */
                        final int detailCount = getValueArr(cellStr).size();
                        sheet.shiftRows(rowIndex.get(), rowIndex.get() + detailCount, detailCount);

                        tempCellIndex.set(cellIndex.get());
                        for (List<String> valuesArr : getValueArr(cellStr)) {
                            row = sheet.createRow(rowIndex.getAndIncrement());
                            for (String value : valuesArr) {
                                cell = row.createCell(cellIndex.getAndIncrement());
                                cell.setCellValue(value);
                            }
                            cellIndex.set(tempCellIndex.get());
                        }
                    }
                    // To check if next row is the end
                    cellStr = sheet.getRow(rowIndex.get()).getCell(0).toString();
                }
                // End the iteration
                if (isEndingCell(cellStr)) {
                    /*
                     * Replace the ending cell placeholder with anything you want, 
                     * for now let's replace it with blank.
                     */
                    row.getCell(0).setCellValue("");
                    break;
                }
                rowIndex.getAndIncrement();
            }

            wbNew = JXLSUtil.mergeExcelFiles(wbNew, wb, sheetName, 0);
            wb.close();
            file.close();
            return doSaveExcelFile(wbNew);
        } catch (IOException e) {
            LOGGER.error("Cause: {}", e.toString());
            return false;
        } catch (Exception e) {
            LOGGER.error("Cause: {}", e.toString());
            System.exit(0);
            return false;
        }
    }

    /**
     * Checks if the cell is a placeholder. A placeholder always starts with a
     * <i>dollar sign (<b>$</b>)</i>.
     *
     * @param cell A cell in instance of String
     * @return True if the cell is a placeholder
     * @since 1.0
     */
    private boolean isPlaceholder(String cell) {
        return !cell.isEmpty() && cell.charAt(0) == '$';
    }

    /**
     * Check if the cell is a detail placeholder. A detail placeholder always
     * starts with <i>percent sign (<b>%</b>)</i>.
     *
     * @param cell A cell in instance of String
     * @return True if the cell is a detail placeholder
     * @since 2.0
     */
    private boolean isDetailPlaceholder(String cell) {
        return !cell.isEmpty() && cell.charAt(0) == '%';
    }

    /**
     * Check if the cell is the ending cell (<b>#end</b>).
     *
     * @param cell A cell in instance of String
     * @return True if the cell is the ending cell
     * @since 2.0
     */
    private boolean isEndingCell(String cell) {
        return cell.equalsIgnoreCase("#end");
    }

    /**
     * Get the value from the {@link HashMap} by key. The <b>key</b> is the
     * placeholder from your template Excel file.
     *
     * @param cell A cell in instance of String
     * @return The specified value for the specified placeholder
     * @since 1.0
     */
    private String getValue(String cell) {
        if (placeholders.containsKey(cell.substring(1))) {
            return (String) placeholders.get(cell.substring(1));
        } else {
            return cell;
        }
    }

    /**
     * Get the value in ArrayList from the {@link HashMap} by key. The
     * <b>key</b> is the placeholder from your template Excel file.
     *
     * @param cell A cell in instance of String
     * @return The specified value for the specified placeholder
     * @since 2.0
     */
    private List<List<String>> getValueArr(String cell) {
        if (placeholders.containsKey(cell.substring(1))) {
            return (List<List<String>>) placeholders.get(cell.substring(1));
        } else {
            // Just create an empty ArrayList to avoid NullPointerException
            final List<List<String>> a = new ArrayList<>();
            final List<String> b = new ArrayList<>();
            b.add("");
            a.add(b);
            return a;
        }
    }

    /**
     * Populate a multiple record which will based on the cell location of the
     * placeholder from your template Excel file.
     * <p>
     * This will occupy number of columns based on your ArrayList's size, and
     * also the same with the number of rows.
     */
    private int doPopulateDetail(XSSFSheet sheet, XSSFRow row, int rowIndex, int cellIndex, ArrayList<ArrayList> values) {
        for (ArrayList valuesArr : values) {
            for (Object value : valuesArr) {
                XSSFCell cell = row.createCell(cellIndex);
            }

        }
        return 0;
    }

    /**
     * Save/Write the resulting excel file to the specified file path.
     * <p>
     * Any caught exception such as <b>IOException</b> will interrupt the saving
     * of file.
     * <i>
     * Warning: This will replace the old file if there's already exists! If you
     * want to avoid overwritten of file, please do a simple evaluation first
     * before calling this method.
     * </i>
     *
     * @param wb An instance of XSSFWorkbook, a class for MS Excel <b>2007</b>
     * support. This must contain the rows and cell that is ready for saving.
     * @return True if no error occurs in process of saving, otherwise false.
     * @since 1.0
     */
    private boolean doSaveExcelFile(XSSFWorkbook wb) {
        try (FileOutputStream writeFile = new FileOutputStream(savePath)) {
            wb.write(writeFile);
            writeFile.flush();
            writeFile.close();
            wb.close();
            return true;
        } catch (IOException e) {
            LOGGER.error("Encountered an error while saving the file");
            LOGGER.error("Cause: {}", e.toString());
            return false;
        }
    }

    /**
     * Check the nullity or emptiness value of save path.
     * <p>
     * <i>
     * Note: File name will be out.xlsx by default.
     * </i>
     *
     * @return If null or empty, the directory will be moved to your Desktop,
     * otherwise return the user-specified path.
     * @since 1.0
     */
    private String checkSavePath(String save_path) {
        if (save_path == null || save_path.isEmpty()) {
            String path;
            if (OSValidator.isWindows()) {
                path = System.getenv("userprofile") + "/Desktop/out.xlsx";
            } else {
                path = System.getenv("HOME") + "/Desktop/out.xlsx";
            }
            LOGGER.warn("save_path has null or empty value! This will be moved to {}", new File(path).getAbsolutePath());
            return path;
        } else {
            return save_path;
        }
    }

}

/**
 * * OLD VERSIONS **
 */
/* Version 1.5
 // Iterate the row from template
 for (int row_i = 0; row_i < sheet.getPhysicalNumberOfRows(); row_i++) {
 row = sheet.getRow(row_i);
 // Iterate the cell of the current row from the template
 for (int cell_i = 0; cell_i < row.getPhysicalNumberOfCells(); cell_i++) {
 cell = row.getCell(cell_i);
 String cell_str = cell.toString();
 if (isPlaceholder(cell_str)) {
 cell.setCellValue(getValue(cell_str));
 }
 }
 }
 */

/* Version 1.0
 // Iterate the row
 while (row_iter.hasNext()) { 
 XSSFRow xrow = (XSSFRow) row_iter.next(); 
 Iterator cell_iter = xrow.cellIterator();
 row = sheet_new.createRow(row_index++);

 // Iterate the cell from current row
 while (cell_iter.hasNext()) { 
 XSSFCell xcell = (XSSFCell) cell_iter.next();
 String cell_str = xcell.toString();
 cell = row.createCell(cell_index);

 // Search for available placeholder
 if (isPlaceholder(cell_str)) {
 cell.setCellValue(getValue(cell_str));
 }
 else {
 cell.setCellValue(cell_str);
 }
 cell_index++;
 }
 cell_index = 0;
 }
 */
