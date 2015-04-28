package my.jutils.poi;

import java.io.*;
import java.util.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;

/**
 * Reference was from
 * {@code http://jxls.cvs.sourceforge.net/jxls/jxls/src/java/org/jxls/util/Util.java?revision=1.8&view=markup}
 * by Leonid Vysochyn and modified (adding styles copying) modified by {@code Philipp
 * Löpmeier} (replacing deprecated classes and methods, using generic types).
 *
 * @author jk
 */
public final class JXLSUtil {

    /**
     * Protect the constructor
     */
    private JXLSUtil() {
    }

    public static XSSFWorkbook mergeExcelFiles(XSSFWorkbook new_workbook, XSSFWorkbook workbook, String sheet_name, int sheet_index) throws IOException {
        copySheets(new_workbook.createSheet(sheet_name), workbook.getSheetAt(sheet_index));
        return new_workbook;
    }

    /**
     * @param newSheet the sheet to create from the copy.
     * @param sheet the sheet to copy.
     */
    public static void copySheets(XSSFSheet newSheet, XSSFSheet sheet) {
        copySheets(newSheet, sheet, true);
    }

    /**
     * @param newSheet the sheet to create from the copy.
     * @param sheet the sheet to copy.
     * @param copyStyle true copy the style.
     */
    public static void copySheets(XSSFSheet newSheet, XSSFSheet sheet, boolean copyStyle) {
        int maxColumnNum = 0;
        Map<Integer, XSSFCellStyle> styleMap = (copyStyle) ? new HashMap<Integer, XSSFCellStyle>() : null;
        for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
            XSSFRow srcRow = sheet.getRow(i);
            XSSFRow destRow = newSheet.createRow(i);
            if (srcRow != null) {
                JXLSUtil.copyRow(sheet, newSheet, srcRow, destRow, styleMap);
                if (srcRow.getLastCellNum() > maxColumnNum) {
                    maxColumnNum = srcRow.getLastCellNum();
                }
            }
        }
        for (int i = 0; i <= maxColumnNum; i++) {
            newSheet.setColumnWidth(i, sheet.getColumnWidth(i));
        }
    }

    /**
     * @param srcSheet the sheet to copy.
     * @param destSheet the sheet to create.
     * @param srcRow the row to copy.
     * @param destRow the row to create.
     * @param styleMap -
     */
    public static void copyRow(XSSFSheet srcSheet, XSSFSheet destSheet, XSSFRow srcRow, XSSFRow destRow, Map<Integer, XSSFCellStyle> styleMap) {
        // manage a list of merged zone in order to not insert two times a merged zone  
        final Set<CellRangeAddressWrapper> mergedRegions = new TreeSet<>();
        destRow.setHeight(srcRow.getHeight());
        // pour chaque row  
        for (int j = srcRow.getFirstCellNum(); j <= srcRow.getLastCellNum(); j++) {
            XSSFCell oldCell = srcRow.getCell(j);   // ancienne cell  
            XSSFCell newCell = destRow.getCell(j);  // new cell   
            if (oldCell != null) {
                if (newCell == null) {
                    newCell = destRow.createCell(j);
                }
                // copy chaque cell  
                copyCell(oldCell, newCell, styleMap);
                // copy les informations de fusion entre les cellules  
                //System.out.println("row num: " + srcRow.getRowNum() + " , col: " + (short)oldCell.getColumnIndex());  
                CellRangeAddress mergedRegion = getMergedRegion(srcSheet, srcRow.getRowNum(), (short) oldCell.getColumnIndex());

                if (mergedRegion != null) {
                    //System.out.println("Selected merged region: " + mergedRegion.toString());  
                    CellRangeAddress newMergedRegion = new CellRangeAddress(mergedRegion.getFirstRow(), mergedRegion.getLastRow(), mergedRegion.getFirstColumn(), mergedRegion.getLastColumn());
                    //System.out.println("New merged region: " + newMergedRegion.toString());  
                    CellRangeAddressWrapper wrapper = new CellRangeAddressWrapper(newMergedRegion);
                    if (isNewMergedRegion(wrapper, mergedRegions)) {
                        mergedRegions.add(wrapper);
                        destSheet.addMergedRegion(wrapper.range);
                    }
                }
            }
        }

    }

    /**
     * Copy the old cell into new cell.
     *
     * @param oldCell Old cell
     * @param newCell New cell
     * @param styleMap Style {@link HashMap}
     */
    public static void copyCell(XSSFCell oldCell, XSSFCell newCell, Map<Integer, XSSFCellStyle> styleMap) {
        if (styleMap != null) {
            if (oldCell.getSheet().getWorkbook() == newCell.getSheet().getWorkbook()) {
                newCell.setCellStyle(oldCell.getCellStyle());
            } else {
                int stHashCode = oldCell.getCellStyle().hashCode();
                XSSFCellStyle newCellStyle = styleMap.get(stHashCode);
                if (newCellStyle == null) {
                    newCellStyle = newCell.getSheet().getWorkbook().createCellStyle();
                    newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
                    styleMap.put(stHashCode, newCellStyle);
                }
                newCell.setCellStyle(newCellStyle);
            }
        }
        switch (oldCell.getCellType()) {
            case XSSFCell.CELL_TYPE_STRING:
                newCell.setCellValue(oldCell.getStringCellValue());
                break;
            case XSSFCell.CELL_TYPE_NUMERIC:
                newCell.setCellValue(oldCell.getNumericCellValue());
                break;
            case XSSFCell.CELL_TYPE_BLANK:
                newCell.setCellType(XSSFCell.CELL_TYPE_BLANK);
                break;
            case XSSFCell.CELL_TYPE_BOOLEAN:
                newCell.setCellValue(oldCell.getBooleanCellValue());
                break;
            case XSSFCell.CELL_TYPE_ERROR:
                newCell.setCellErrorValue(oldCell.getErrorCellValue());
                break;
            case XSSFCell.CELL_TYPE_FORMULA:
                newCell.setCellFormula(oldCell.getCellFormula());
                break;
            default:
                break;
        }

    }

    /**
     * Récupère les informations de fusion des cellules dans la sheet source
     * pour les appliquer à la sheet destination... Récupère toutes les zones
     * merged dans la sheet source et regarde pour chacune d'elle si elle se
     * trouve dans la current row que nous traitons. Si oui, retourne l'objet
     * CellRangeAddress.
     *
     * @param sheet the sheet containing the data.
     * @param rowNum the num of the row to copy.
     * @param cellNum the num of the cell to copy.
     * @return the CellRangeAddress created.
     */
    public static CellRangeAddress getMergedRegion(XSSFSheet sheet, int rowNum, short cellNum) {
        for (int i = 0; i < sheet.getNumMergedRegions(); i++) {
            CellRangeAddress merged = sheet.getMergedRegion(i);
            if (merged.isInRange(rowNum, cellNum)) {
                return merged;
            }
        }
        return null;
    }

    /**
     * Check that the merged region has been created in the destination sheet.
     *
     * @param newMergedRegion Merged region to copy or not in the
     * destination sheet.
     * @param mergedRegions List containing all the merged region.
     * @return True if the merged region is already in the list or not
     */
    private static boolean isNewMergedRegion(CellRangeAddressWrapper newMergedRegion, Set<CellRangeAddressWrapper> mergedRegions) {
        return !mergedRegions.contains(newMergedRegion);
    }

}
