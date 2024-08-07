import java.util.*;

public class Main {
    public static void main(String[] args) {
        //DEFINITIONS

        //This is the definition of the truck anticipator
        //file path is the name of the Excel file
        //sheet name is the name of the sheet in the Excel file
        //C1 is the number of trucks to anticipate
        //C2 is the number of remaining trucks
        //p per c is the maximum weight per truck
        //n per c is the maximum amount of palettes per truck
        TruckAnticipationBlock truckAnticipationBlock = new TruckAnticipationBlock(
                "inputs/Algoritmo.xlsx",
                "Avance de camion",
                2,
                5,
                20500,
                64
        );

        //This is the definition of the truck optimisator
        //file path is the name of the Excel file
        //sheet name is the name of the sheet in the Excel file
        //PSalumis is the weight of the fixed Salumis
        //NSalumis is the number of palettes of the fixed Salumis
        //C1 is the number of trucks to anticipate
        //C2 is the number of remaining trucks
        //p per c is the maximum weight per truck
        //n per c is the maximum amount of palettes per truck
        TruckOptimizationBlock truckOptimizationBlock = new TruckOptimizationBlock(
                "inputs/Algoritmo 4.xlsx",
                "Foglio1",
                2112,
                7.111,
                1,
                20500,
                64
        );

        //EXECUTION
        //truckAnticipationBlock.startAnticipation(0.0, 0.0);
        truckOptimizationBlock.startOtpimisation(0.0, 0.0);
    }
}