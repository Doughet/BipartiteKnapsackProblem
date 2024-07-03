import java.util.*;

public class Main {
    public static void main(String[] args) {

        BipartiteDataProcessing bpDP = new BipartiteDataProcessing();

        bpDP.readExcelFile();
        bpDP.reprocessData();
        BipartiteDataProcessing.ResultBundle resultBundle = bpDP.getResultBundle();


        double P = resultBundle.P();
        double N = resultBundle.N();


        /*
        double[][] valuesArray = new double[5][2];
        valuesArray[0][0] = 300;
        valuesArray[0][1] = 6;
        valuesArray[1][0] = 250;
        valuesArray[1][1] = 8;
        valuesArray[2][0] = 150;
        valuesArray[2][1] = 15;
        valuesArray[3][0] = 200;
        valuesArray[3][1] = 9;
        valuesArray[4][0] = 100;
        valuesArray[4][1] = 12.5f;

        String[][] infoArray = new String[5][2];
        infoArray[0][0] = "p1";
        infoArray[0][1] = "";
        infoArray[1][0] = "p2";
        infoArray[1][1] = "";
        infoArray[2][0] = "p3";
        infoArray[2][1] = "";
        infoArray[3][0] = "p4";
        infoArray[3][1] = "";
        infoArray[4][0] = "p5";
        infoArray[4][1] = "";
        */



        BipartiteKnapsackSolver solver1 = new BipartiteKnapsackSolver(
                P,
                N,
                20500,
                64,
                2,
                5,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        BipartiteKnapsackSolver solver2 = new BipartiteKnapsackSolver(
                P,
                N,
                20500,
                64,
                1,
                6,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        BipartiteKnapsackSolver solver3 = new BipartiteKnapsackSolver(
                P,
                N,
                20500,
                64,
                1,
                6,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        solver1.useApproach1();
        solver2.useApproach2();
        solver3.useApproach3(0.2);

        List<BipartiteKnapsackSolver.SolutionEntry> solution1 = solver1.getSolution();
        List<BipartiteKnapsackSolver.SolutionEntry> solution2 = solver2.getSolution();
        List<BipartiteKnapsackSolver.SolutionEntry> solution3 = solver3.getSolution();

        BipartiteKnapsackWriter writer1 = new BipartiteKnapsackWriter(solution1);
        BipartiteKnapsackWriter writer2 = new BipartiteKnapsackWriter(solution2);

        writer2.writeExcel();
    }




}