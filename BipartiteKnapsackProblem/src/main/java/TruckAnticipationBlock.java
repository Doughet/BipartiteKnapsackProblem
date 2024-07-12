import java.util.List;

public class TruckAnticipationBlock {

    String filePath;
    String sheetName;

    double P;
    double N;

    int C1 = 2;
    int C2 = 5;

    double p_per_c = 20500;
    double n_per_c = 64;

    public TruckAnticipationBlock(String filePath, String sheetName, int C1, int C2,
                                  double p_per_c, double n_per_c){
        this.filePath = filePath;
        this.sheetName = sheetName;

        this.C1 = C1;
        this.C2 = C2;

        this.p_per_c = p_per_c;
        this.n_per_c = n_per_c;
    }

    public void startAnticipation(){
        TruckAnticipationDP bpDP = new TruckAnticipationDP(filePath, sheetName);

        bpDP.readExcelFile();
        bpDP.reprocessData();
        TruckAnticipationDP.ResultBundle resultBundle = bpDP.getResultBundle();

        P = resultBundle.P();
        N = resultBundle.N();

        TruckAnticipationSolver solver1 = new TruckAnticipationSolver(
                P,
                N,
                p_per_c,
                n_per_c,
                C1,
                C2,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        TruckAnticipationSolver solver2 = new TruckAnticipationSolver(
                P,
                N,
                p_per_c,
                n_per_c,
                C1,
                C2,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        TruckAnticipationSolver solver3 = new TruckAnticipationSolver(
                P,
                N,
                p_per_c,
                n_per_c,
                C1,
                C2,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        solver1.useApproach1();
        solver2.useApproach2();
        solver3.useApproach3(0.2);

        List<TruckAnticipationSolver.SolutionEntry> solution1 = solver1.getSolution();
        List<TruckAnticipationSolver.SolutionEntry> solution2 = solver2.getSolution();
        List<TruckAnticipationSolver.SolutionEntry> solution3 = solver3.getSolution();

        TruckAnticipationWriter writer1 = new TruckAnticipationWriter(solution1, resultBundle.infoArray(), resultBundle.valuesArray(), N, P, C1, C2);
        TruckAnticipationWriter writer2 = new TruckAnticipationWriter(solution2, resultBundle.infoArray(), resultBundle.valuesArray(), N, P, C1, C2);
        TruckAnticipationWriter writer3 = new TruckAnticipationWriter(solution3, resultBundle.infoArray(), resultBundle.valuesArray(), N, P, C1, C2);

        writer2.writeExcelSolution();
    }

}
