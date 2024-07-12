import java.util.List;

public class TruckOptimizationBlock {
    String filePath;
    String sheetName;

    double P;
    double N;

    double PSalumis;
    double NSalumis;

    int C1 = 1;
    int C2 = 5;

    double p_per_c = 20500;
    double n_per_c = 64;

    public TruckOptimizationBlock(String filePath, String sheetName, double PSalumis, double NSalumis, int C2,
                                  double p_per_c, double n_per_c) {
        this.filePath = filePath;
        this.sheetName = sheetName;

        this.PSalumis = PSalumis;
        this.NSalumis = NSalumis;

        this.C2 = C2;

        this.p_per_c = p_per_c;
        this.n_per_c = n_per_c;
    }

    public void startOtpimisation() {
        TruckAnticipationDP bpDP = new TruckAnticipationDP(filePath, sheetName);

        bpDP.readExcelFile();
        bpDP.reprocessData();
        TruckAnticipationDP.ResultBundle resultBundle = bpDP.getResultBundle();

        P = resultBundle.P() + PSalumis;
        N = resultBundle.N() + NSalumis;

        TruckOptimizationSolver solver1 = new TruckOptimizationSolver(
                P,
                N,
                p_per_c,
                n_per_c,
                C2,
                PSalumis,
                NSalumis,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        TruckOptimizationSolver solver2 = new TruckOptimizationSolver(
                P,
                N,
                p_per_c,
                n_per_c,
                C2,
                PSalumis,
                NSalumis,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        TruckOptimizationSolver solver3 = new TruckOptimizationSolver(
                P,
                N,
                p_per_c,
                n_per_c,
                C2,
                PSalumis,
                NSalumis,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        solver1.useApproach1();
        solver2.useApproach2();
        solver3.useApproach3(0.2);

        List<TruckOptimizationSolver.SolutionEntry> solution1 = solver1.getSolution();
        List<TruckOptimizationSolver.SolutionEntry> solution2 = solver2.getSolution();
        List<TruckOptimizationSolver.SolutionEntry> solution3 = solver3.getSolution();

        TruckOptimizationWriter writer1 = new TruckOptimizationWriter(solution1, resultBundle.infoArray(), resultBundle.valuesArray(), N, P, C2, NSalumis, PSalumis);
        TruckOptimizationWriter writer2 = new TruckOptimizationWriter(solution2, resultBundle.infoArray(), resultBundle.valuesArray(), N, P, C2, NSalumis, PSalumis);
        TruckOptimizationWriter writer3 = new TruckOptimizationWriter(solution3, resultBundle.infoArray(), resultBundle.valuesArray(), N, P, C2, NSalumis, PSalumis);

        writer2.writeExcelSolution();
    }

}
