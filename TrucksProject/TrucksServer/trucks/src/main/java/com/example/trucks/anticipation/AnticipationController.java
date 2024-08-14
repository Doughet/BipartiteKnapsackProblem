package com.example.trucks.anticipation;

import com.example.trucks.anticipation.solver.TruckAnticipationBlock;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.http.HttpResponse;

@RestController
public class AnticipationController {

    Logger logger = LoggerFactory.getLogger(AnticipationController.class);

    public AnticipationController() {

    }

    @PostMapping("/api/anticipation/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        try {
            // Step 1: Read the uploaded Excel file
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

            // Step 2: Process the workbook (for example, add a new sheet)
            logger.info("THIS IS A LOG BECAUSE YOU DIDNT BREAK");

            /*
            TruckAnticipationBlock truckAnticipationBlock = new TruckAnticipationBlock(
                    workbook,
                    sheetName,
                    C1,
                    C2,
                    PLimit,
                    NLimit
            );

            truckAnticipationBlock.startAnticipation(0.0f, 0.0f);

             */


            // Define the path and filename for the Excel file
            String filePath = "input_anticipation.xlsx"; // This will save the file in the current working directory
            // Alternatively, you can specify an absolute path, e.g., "/path/to/directory/input_optimisation.xlsx"

            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                // Write the workbook to the file
                workbook.write(fileOut);
                // Close the workbook
                workbook.close();
                logger.info("Excel file 'input_anticipation.xlsx' created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Failed to save the Excel file.");
            }

            return "";

        }catch (IOException e){

            return "";
        }
    }

    @PostMapping("/api/anticipation/download")
    public ResponseEntity<InputStreamResource> downloadExcel(@RequestParam("sheetName") String sheetName,
                                                             @RequestParam("NLimit") float NLimit,
                                                             @RequestParam("PLimit") float PLimit,
                                                             @RequestParam("C1") int C1,
                                                             @RequestParam("C2") int C2) {
        try {

            XSSFWorkbook workbook = new XSSFWorkbook("input_anticipation.xlsx");

            TruckAnticipationBlock truckAnticipationBlock = new TruckAnticipationBlock(
                    workbook,
                    sheetName,
                    C1,
                    C2,
                    PLimit,
                    NLimit
            );

            truckAnticipationBlock.startAnticipation(0.0f, 0.0f);

            // Create a workbook (this could be your generated Excel file)
            XSSFWorkbook result = new XSSFWorkbook("truck_anticipation.xlsx");
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            result.write(out);
            result.close();

            // Convert the workbook to an InputStream
            ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());

            // Set headers to indicate file download
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=truck_anticipation.xlsx");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(new InputStreamResource(in));

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }




    @PostMapping("/api/thisistest")
    public String test() {
        logger.info("THIS IS A LOG BECAUSE YOU TESTED OUT");
        return "HI";
    }


}
