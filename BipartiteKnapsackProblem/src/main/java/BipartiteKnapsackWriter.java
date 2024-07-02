import java.io.FileOutputStream;
import java.util.*;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BipartiteKnapsackWriter {

    private final List<BipartiteKnapsackSolver.SolutionEntry> solutionEntries;

    public BipartiteKnapsackWriter(List<BipartiteKnapsackSolver.SolutionEntry> solutionEntries){
        this.solutionEntries = solutionEntries;
    }


    public void writeExcel(){

        if(solutionEntries == null){
            return;
        }

        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // For .xlsx files

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Sheet1");

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create Cells
        Cell cell0 = headerRow.createCell(0);
        cell0.setCellValue("Codice");

        Cell cell1 = headerRow.createCell(1);
        cell1.setCellValue("Desc");

        Cell cell2 = headerRow.createCell(2);
        cell2.setCellValue("Amount");


        int rowNum = 1;
        for (BipartiteKnapsackSolver.SolutionEntry rowData : solutionEntries) {
            Row row = sheet.createRow(rowNum++);

            Cell cell_0 = row.createCell(0);
            cell_0.setCellValue((String) rowData.code());

            Cell cell_1 = row.createCell(1);
            cell_1.setCellValue((String) rowData.description());

            Cell cell_2 = row.createCell(2);
            cell_2.setCellValue((Double) rowData.amount());
        }

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream("workbook.xlsx")) {
            workbook.write(fileOut);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Closing the workbook
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

