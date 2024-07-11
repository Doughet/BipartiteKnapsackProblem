import java.util.*;

public class Main {
    public static void main(String[] args) {

        TruckAnticipationDP bpDP = new TruckAnticipationDP("inputs/Algoritmo.xlsx", "Avance de camion");

        bpDP.readExcelFile();
        bpDP.reprocessData();
        TruckAnticipationDP.ResultBundle resultBundle = bpDP.getResultBundle();


        double P = resultBundle.P();
        double N = resultBundle.N();

        int C1 = 2;
        int C2 = 5;

        TruckAnticipationSolver solver1 = new TruckAnticipationSolver(
                P,
                N,
                20500,
                64,
                C1,
                C2,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        TruckAnticipationSolver solver2 = new TruckAnticipationSolver(
                P,
                N,
                20500,
                64,
                C1,
                C2,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        TruckAnticipationSolver solver3 = new TruckAnticipationSolver(
                P,
                N,
                20500,
                64,
                C1,
                C2,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        //solver1.useApproach1();
        //solver2.useApproach2();
        //solver3.useApproach3(0.2);

        List<TruckAnticipationSolver.SolutionEntry> solution1 = solver1.getSolution();
        List<TruckAnticipationSolver.SolutionEntry> solution2 = solver2.getSolution();
        List<TruckAnticipationSolver.SolutionEntry> solution3 = solver3.getSolution();

        TruckAnticipationWriter writer1 = new TruckAnticipationWriter(solution1, resultBundle.infoArray(), resultBundle.valuesArray(), N, P, C1, C2);
        TruckAnticipationWriter writer2 = new TruckAnticipationWriter(solution2, resultBundle.infoArray(), resultBundle.valuesArray(), N, P, C1, C2);

        //writer2.writeExcelSolution();

        System.out.println(N);
        System.out.println(P);



        TruckAnticipationDP salumisDp = new TruckAnticipationDP("inputs/Algoritmo.xlsx", "Optimisation salumi");

        salumisDp.readExcelFile();
        salumisDp.reprocessData();
        TruckAnticipationDP.ResultBundle resultSalumis = salumisDp.getResultBundle();


        double PS = resultSalumis.P();
        double NS = resultSalumis.N();

        int C2S = 3;

        double PSalumis = 5000;
        double NSalumis = 19;

        TruckOptimizationSolver salumiSolver1 = new TruckOptimizationSolver(
                PS + PSalumis,
                NS + NSalumis,
                20500,
                64,
                C2S,
                PSalumis,
                NSalumis,
                resultSalumis.infoArray(),
                resultSalumis.valuesArray()
        );

        salumiSolver1.useApproach1();

        List<TruckOptimizationSolver.SolutionEntry> solution1S = salumiSolver1.getSolution();
        TruckOptimizationWriter writer1S = new TruckOptimizationWriter(solution1S, resultSalumis.infoArray(), resultSalumis.valuesArray(), NS + NSalumis, PS + PSalumis, 1, C2S);

        writer1S.writeExcelSolution();
    }




}