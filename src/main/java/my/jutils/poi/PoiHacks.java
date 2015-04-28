package my.jutils.poi;

import java.util.*;
import org.apache.poi.xssf.usermodel.*;

public class PoiHacks
{
    // Fix of XSSFSheet.createRow(int index)
    public static XSSFRow createRow(XSSFSheet sheet, int index) {
        XSSFRow row = sheet.getRow(index);
        
        if (row == null)
            return sheet.createRow(index);

        Iterator it = row.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
        
        return row;
    }
}