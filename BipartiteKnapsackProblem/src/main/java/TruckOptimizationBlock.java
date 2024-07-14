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

    public void startOtpimisation(double NModifier, double PModifier) {
        TruckAnticipationDP bpDP = new TruckAnticipationDP(filePath, sheetName);

        bpDP.readExcelFile();
        bpDP.reprocessData();
        TruckAnticipationDP.ResultBundle resultBundle = bpDP.getResultBundle();

        P = resultBundle.P() + PSalumis;
        N = resultBundle.N() + NSalumis;

        TruckOptimizationSolver solver1 = new TruckOptimizationSolver(
                P,
                N,
                p_per_c + PModifier,
                n_per_c + NModifier,
                C2,
                PSalumis,
                NSalumis,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        TruckOptimizationSolver solver2 = new TruckOptimizationSolver(
                P,
                N,
                p_per_c + PModifier,
                n_per_c + NModifier,
                C2,
                PSalumis,
                NSalumis,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        TruckOptimizationSolver solver3 = new TruckOptimizationSolver(
                P,
                N,
                p_per_c + PModifier,
                n_per_c + NModifier,
                C2,
                PSalumis,
                NSalumis,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        TruckOptimizationSolver.SolutionStats stats1 = solver1.useApproach1();
        TruckOptimizationSolver.SolutionStats stats2 = solver2.useApproach2();
        TruckOptimizationSolver.SolutionStats stats3 = solver3.useApproach3(0.2);

        if(stats1.result() == -1 && stats2.result() == -1 && stats3.result() == -1 && stats2.NperC() - (n_per_c + NModifier) > 0){
            startOtpimisation(NModifier + 0.1, PModifier);
            return;
        } else if (stats1.result() == -1 && stats2.result() == -1 && stats3.result() == -1 && stats2.PperC() - (p_per_c + PModifier) > 0) {
            startOtpimisation(NModifier, PModifier + 5);
            return;
        }

        TruckOptimizationSolver.SolutionStats [] stats = {stats1, stats2, stats3};
        TruckOptimizationSolver [] solvers = {solver1, solver2, solver3};
        TruckOptimizationSolver selectedSolver = selectSolution(stats, solvers);

        List<TruckOptimizationSolver.SolutionEntry> solution = selectedSolver.getSolution();

        TruckOptimizationWriter writer = new TruckOptimizationWriter(solution, resultBundle.infoArray(), resultBundle.valuesArray(), N, P, C2, NSalumis, PSalumis);

        writer.writeExcelSolution();
    }

    private TruckOptimizationSolver selectSolution(TruckOptimizationSolver.SolutionStats [] stats, TruckOptimizationSolver [] solvers){
        int selected = 0;
        double best = computeHeuristic(stats[0].NperC(), stats[0].PperC(), stats[0].PtheoC(), stats[0].NtheoC());
        for (int i = 0; i < stats.length; i++) {
            double candidate = computeHeuristic(stats[0].NperC(), stats[0].PperC(), stats[0].PtheoC(), stats[0].NtheoC());

            if(candidate <= best){
                best = candidate;
                selected = i;
            }
        }

        return solvers[selected];
    }

    private double computeHeuristic(double nMoyenPalette, double selectedWeight,
                                     double pMoyenCamion, double selectedPalettes){
        double heuristicWeight = (selectedWeight - pMoyenCamion) / pMoyenCamion;

        double heuristicPalette = (selectedPalettes - pMoyenCamion) / pMoyenCamion;

        return Math.abs(heuristicWeight) + Math.abs(heuristicPalette);
    }

}
