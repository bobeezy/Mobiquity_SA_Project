package main.java.utils;

/**
 * @author lionel.mangoua
 * date: 04/08/22
 */

import main.java.engine.DriverFactory;
import main.java.exceptions.ReadFileException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;

public class ExcelUtility extends DriverFactory {

    //region <Method reads data from the spreadsheet and store it in a 2D array>
    public static String[][] readExcelFile (String sheetName) {

        try {
            /*
             * Creating a Workbook from an Excel file (.xls or .xlsx)
             * And make a temporary copy to prevent changes to the one under
             * version control.
             */
            log(" *** Read the excel file *** ", "INFO", "text");

            String originalName;
            originalName = xlsxFilePath();
            Workbook workbook = WorkbookFactory.create(new File(originalName)); // Create Excel Instance

            //Get the data from the sheet you want to read
            Sheet sheet = workbook.getSheet(sheetName); // Get Sheet named as sheetName value

            //Find number of rows in excel file
            int rowCount = sheet.getLastRowNum() - sheet.getFirstRowNum();
            int numRows = sheet.getLastRowNum() + 1;
            int numCols = sheet.getRow(0).getLastCellNum();
            excelData = new String[numRows][numCols];

            //Create a loop over all the rows of excel file to read it
            for (int i = 0; i < rowCount + 1; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    DataFormatter fmt = new DataFormatter();
                    String valueAsSeenInExcel = fmt.formatCellValue(row.getCell(j));
                    excelData[i][j] = valueAsSeenInExcel;
                }
            }

            workbook.close();
            return excelData;
        }
        catch(IOException e) {
            throw new ReadFileException("[ERROR] Something went wrong reading the excel sheet --- " + e.getMessage());
        }
    }
    //endregion
}
