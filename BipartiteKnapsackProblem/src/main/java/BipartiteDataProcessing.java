import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class BipartiteDataProcessing {

    String filePath;
    String sheetName;

    private String[][] infoArray;
    private double[][] valuesArray;
    double P;
    double N;


    public void readExcelFile(){
        String excelFilePath = "C:/Users/adria/Downloads/Cartel2.xlsx"; // Update this path

        try (FileInputStream fis = new FileInputStream(new File(excelFilePath));
             Workbook workbook = new XSSFWorkbook(fis)) {

            // Get the first sheet from the workbook
            Sheet sheet = workbook.getSheet("Feuil2");

            int rowNumber = sheet.getLastRowNum();

            infoArray = new String[rowNumber][2];
            valuesArray = new double[rowNumber][2];

            P = 0;
            N = 0;

            iterateAColumnInfo(0, sheet, 0);
            iterateAColumnInfo(1, sheet, 1);
            iterateAColumnValue(3, sheet, 0);
            iterateAColumnValue(7, sheet, 1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void reprocessData(){
        for (int i = 0; i < valuesArray.length; i++) {
            N += valuesArray[i][0];
            P += valuesArray[i][1];

            valuesArray[i][1] = valuesArray[i][1] / valuesArray[i][0];

        }
    }

    ResultBundle getResultBundle(){
        return new ResultBundle(infoArray, valuesArray, P, N);
    }

    void iterateAColumnInfo(int column, Sheet sheet, int index){

        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            Row row  = sheet.getRow(i);
            Cell cell = row.getCell(column);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case STRING:
                        infoArray[i-1][index] = cell.getStringCellValue();
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                        } else {
                            infoArray[i-1][index] = Integer.toString((int)cell.getNumericCellValue());
                        }
                        break;
                    case BOOLEAN:
                        break;
                    case FORMULA:
                        break;
                    case BLANK:
                        break;
                    default:
                        break;
                }
            }
        }

    }

    void iterateAColumnValue(int column, Sheet sheet, int index){

        for (int i = 1; i < sheet.getLastRowNum(); i++) {
            Row row  = sheet.getRow(i);
            Cell cell = row.getCell(column);
            if (cell != null) {
                switch (cell.getCellType()) {
                    case STRING:
                        break;
                    case NUMERIC:
                        if (DateUtil.isCellDateFormatted(cell)) {
                        } else {
                            valuesArray[i-1][index] = cell.getNumericCellValue();
                        }
                        break;
                    case BOOLEAN:
                        break;
                    case FORMULA:
                        break;
                    case BLANK:
                        break;
                    default:
                        break;
                }
            }
        }

    }


    public record ParsedInput(String[] objectCodes, int[][] objectDescriptions){}
    public record ResultBundle(String[][] infoArray, double[][] valuesArray, double P, double N){}
}
