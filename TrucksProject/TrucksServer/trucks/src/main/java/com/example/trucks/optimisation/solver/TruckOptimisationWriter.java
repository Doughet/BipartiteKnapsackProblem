package com.example.trucks.optimisation.solver;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class TruckOptimisationWriter {

    private final List<TruckOptimisationSolver.SolutionEntry> solutionEntries;
    private String infoArray[][];
    private double valuesArray[][];

    private double N;
    private double P;

    private int C2;

    private double NSalumis;
    private double PSalumis;

    public TruckOptimisationWriter(List<TruckOptimisationSolver.SolutionEntry> solutionEntries, String[][] infoArray, double[][] valuesArray, double N, double P, int C2, double NSalumis, double PSalumis){
        this.solutionEntries = solutionEntries;

        this.infoArray = new String[infoArray.length][infoArray[0].length];
        this.valuesArray = new double[infoArray.length][infoArray[0].length];

        for (int i = 0; i < infoArray.length; i++) {
            this.infoArray[i] = infoArray[i].clone();
            this.valuesArray[i] = valuesArray[i].clone();
        }

        this.N = N;
        this.P = P;

        this.C2 = C2;

        this.NSalumis = NSalumis;
        this.PSalumis = PSalumis;
    }


    public void writeExcelSolution(){

        if(solutionEntries == null){
            return;
        }

        // Create a Workbook
        Workbook workbook = new XSSFWorkbook(); // For .xlsx files

        WriteAdvancedTrucks(workbook);
        WriteRemainingTrucks(workbook);

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream("truck_optimization.xlsx")) {
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

    private void WriteAdvancedTrucks(Workbook workbook) {
        double PAdvance = PSalumis;
        double NAdvance = NSalumis;

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Salumis Truck");

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create Cells
        Cell cell0 = headerRow.createCell(0);
        cell0.setCellValue("Materiale");

        Cell cell1 = headerRow.createCell(1);
        cell1.setCellValue("Codice materiale");

        Cell cell2 = headerRow.createCell(2);
        cell2.setCellValue("Num di CT");

        Cell cell3 = headerRow.createCell(3);
        cell3.setCellValue("CT per PAL");

        Cell cell4 = headerRow.createCell(4);
        cell4.setCellValue("Num di pallet");

        Cell cell5 = headerRow.createCell(5);
        cell5.setCellValue("Poids brut du total");

        Cell cell6 = headerRow.createCell(6);
        cell6.setCellValue("Poids par palette");

        Cell cell7 = headerRow.createCell(7);
        cell7.setCellValue("Categorie");


        int rowNum = 1;
        for (TruckOptimisationSolver.SolutionEntry rowData : solutionEntries) {
            Row row = sheet.createRow(rowNum++);

            Cell cell_0 = row.createCell(0);
            cell_0.setCellValue((String) rowData.code());

            Cell cell_1 = row.createCell(1);
            cell_1.setCellValue((String) rowData.description());

            Cell cell_2 = row.createCell(2);
            cell_2.setCellValue((int) (rowData.values()[2] * rowData.amount()));

            Cell cell_3 = row.createCell(3);
            cell_3.setCellValue((int) (rowData.values()[2]));

            Cell cell_4 = row.createCell(4);
            cell_4.setCellValue((Double) rowData.amount());
            NAdvance += rowData.amount();

            Cell cell_5 = row.createCell(5);
            cell_5.setCellValue((Double) (rowData.values()[1] * rowData.amount()));
            PAdvance += rowData.values()[1] * rowData.amount();

            Cell cell_6 = row.createCell(6);
            cell_6.setCellValue(rowData.values()[1]);

            Cell cell_7 = row.createCell(7);
            cell_7.setCellValue(rowData.values()[3]);
        }
        WriteInsights(sheet, rowNum, NAdvance, PAdvance, 1);
    }

    private void WriteRemainingTrucks(Workbook workbook) {
        double PRemain = 0;
        double NRemain = 0;

        // Create a Sheet
        Sheet sheet = workbook.createSheet("Remaining Trucks");

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create Cells
        Cell cell0 = headerRow.createCell(0);
        cell0.setCellValue("Materiale");

        Cell cell1 = headerRow.createCell(1);
        cell1.setCellValue("Codice materiale");

        Cell cell2 = headerRow.createCell(2);
        cell2.setCellValue("Num di CT");

        Cell cell3 = headerRow.createCell(3);
        cell3.setCellValue("CT per PAL");

        Cell cell4 = headerRow.createCell(4);
        cell4.setCellValue("Num di pallet");

        Cell cell5 = headerRow.createCell(5);
        cell5.setCellValue("Poids brut du total");

        Cell cell6 = headerRow.createCell(6);
        cell6.setCellValue("Poids par palette");

        Cell cell7 = headerRow.createCell(7);
        cell7.setCellValue("Categorie");


        int rowNum = 1;
        for (int i = 0; i < infoArray.length; i++) {
            int finalI = i;
            Optional<TruckOptimisationSolver.SolutionEntry> entry = solutionEntries.stream().filter(x -> x.code() == infoArray[finalI][0]).findFirst();

            if(!entry.isEmpty() && entry.get().amount() == valuesArray[i][0]) {
                continue;
            }
            else if(entry.isEmpty() && valuesArray[i][0] == 0){
                continue;
            }
            else if(entry.isEmpty()){
                entry = Optional.of(new TruckOptimisationSolver.SolutionEntry("", "", 0, null));
            }

            Row row = sheet.createRow(rowNum++);

            Cell cell_0 = row.createCell(0);
            cell_0.setCellValue(infoArray[i][0]);

            Cell cell_1 = row.createCell(1);
            cell_1.setCellValue(infoArray[i][1]);

            Cell cell_2 = row.createCell(2);
            cell_2.setCellValue((int) (valuesArray[i][2] * (valuesArray[i][0] - entry.get().amount())));

            Cell cell_3 = row.createCell(3);
            cell_3.setCellValue((int) (valuesArray[i][2]));

            Cell cell_4 = row.createCell(4);
            cell_4.setCellValue((Double) valuesArray[i][0] - entry.get().amount());
            NRemain += valuesArray[i][0] - entry.get().amount();

            Cell cell_5 = row.createCell(5);
            cell_5.setCellValue((Double) (valuesArray[i][1] * (valuesArray[i][0] - entry.get().amount())));
            PRemain += valuesArray[i][1] * (valuesArray[i][0] - entry.get().amount());

            Cell cell_6 = row.createCell(6);
            cell_6.setCellValue(valuesArray[i][1]);

            Cell cell_7 = row.createCell(7);
            cell_7.setCellValue(valuesArray[i][3]);
        }

        WriteInsights(sheet, rowNum, NRemain, PRemain, C2);
    }

    private void WriteInsights(Sheet sheet, int rowNum, double NAdvance, double PAdvance, int C) {
        Row totalRow = sheet.createRow(++rowNum);

        Cell cellTotalTitle = totalRow.createCell(0);
        cellTotalTitle.setCellValue("Total:");

        Cell cellTotalN = totalRow.createCell(4);
        cellTotalN.setCellValue(NAdvance);

        Cell cellTotalP = totalRow.createCell(5);
        cellTotalP.setCellValue(PAdvance);

        Row theoreticalRow = sheet.createRow(++rowNum);

        Cell cellTheoTitle = theoreticalRow.createCell(0);
        cellTheoTitle.setCellValue("Ideal per truck:");

        Cell cellTheoN = theoreticalRow.createCell(4);
        cellTheoN.setCellValue(N/(1 + C2));

        Cell cellTheoP = theoreticalRow.createCell(5);
        cellTheoP.setCellValue(P/(1 + C2));

        Row realRow = sheet.createRow(++rowNum);

        Cell cellRealTitle = realRow.createCell(0);
        cellRealTitle.setCellValue("Real per truck:");

        Cell cellRealN = realRow.createCell(4);
        cellRealN.setCellValue(NAdvance / C);

        Cell cellRealP = realRow.createCell(5);
        cellRealP.setCellValue(PAdvance / C);

        Row percentsRow = sheet.createRow(++rowNum);

        Cell cellPercentsTitle = percentsRow.createCell(0);
        cellPercentsTitle.setCellValue("Closeness Percentage:");

        Cell cellPercentsN = percentsRow.createCell(4);
        double percentageN = Math.min((NAdvance / C), (N / (1 + C2))) / Math.max((NAdvance / C), (N / (1 + C2))) * 100;
        cellPercentsN.setCellValue(percentageN);

        Cell cellPercentsP = percentsRow.createCell(5);
        double percentageP = Math.min((PAdvance / C), (P / (1 + C2))) / Math.max((PAdvance / C), (P / (1 + C2))) * 100;
        cellPercentsP.setCellValue(percentageP);
    }


}

