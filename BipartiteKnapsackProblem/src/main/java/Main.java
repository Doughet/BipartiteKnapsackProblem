import java.util.*;

public class Main {
    public static void main(String[] args) {
        //DEFINITIONS
        TruckAnticipationBlock truckAnticipationBlock = new TruckAnticipationBlock(
                "inputs/Algoritmo.xlsx",
                "Avance de camion",
                2,
                5,
                20500,
                64
        );

        TruckOptimizationBlock truckOptimizationBlock = new TruckOptimizationBlock(
                "inputs/Algoritmo.xlsx",
                "Optimisation salumi",
                5000,
                19,
                2,
                20500,
                65
        );

        //EXECUTION
        truckAnticipationBlock.startAnticipation();
        truckOptimizationBlock.startOtpimisation();
    }
}