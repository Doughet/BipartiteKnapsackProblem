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
                "inputs/Algoritmo 2.xlsx",
                "Feuil2",
                7634,
                23,
                2,
                20500,
                64
        );

        //EXECUTION
        truckAnticipationBlock.startAnticipation(0.0, 0.0);
        //truckOptimizationBlock.startOtpimisation(0.0, 0.0);
    }
}