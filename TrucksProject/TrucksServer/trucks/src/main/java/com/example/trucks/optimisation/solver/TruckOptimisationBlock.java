package com.example.trucks.optimisation.solver;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class TruckOptimisationBlock {
    XSSFWorkbook workbook;
    String sheetName;

    double P;
    double N;

    double PSalumis;
    double NSalumis;

    int C1 = 1;
    int C2 = 5;

    double p_per_c = 20500;
    double n_per_c = 64;

    public TruckOptimisationBlock(XSSFWorkbook workbook, String sheetName, double PSalumis, double NSalumis, int C2,
                                  double p_per_c, double n_per_c) {
        this.workbook = workbook;
        this.sheetName = sheetName;

        this.PSalumis = PSalumis;
        this.NSalumis = NSalumis;

        this.C2 = C2;

        this.p_per_c = p_per_c;
        this.n_per_c = n_per_c;
    }

    public void startOtpimisation(double NModifier, double PModifier) {
        TruckOptimisationDP bpDP = new TruckOptimisationDP(workbook, sheetName);

        bpDP.readExcelFile();
        bpDP.reprocessData();
        TruckOptimisationDP.ResultBundle resultBundle = bpDP.getResultBundle();

        P = resultBundle.P() + PSalumis;
        N = resultBundle.N() + NSalumis;

        TruckOptimisationSolver solver1 = new TruckOptimisationSolver(
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

        TruckOptimisationSolver solver2 = new TruckOptimisationSolver(
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

        TruckOptimisationSolver solver3 = new TruckOptimisationSolver(
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

        TruckOptimisationSolver.SolutionStats stats1 = solver1.useApproach1();
        TruckOptimisationSolver.SolutionStats stats2 = solver2.useApproach2();
        TruckOptimisationSolver.SolutionStats stats3 = solver3.useApproach3(0.2);

        if(stats1.result() == -1 && stats2.result() == -1 && stats3.result() == -1 && stats2.NperC() - (n_per_c + NModifier) > 0){
            startOtpimisation(NModifier + 0.1, PModifier);
            return;
        } else if (stats1.result() == -1 && stats2.result() == -1 && stats3.result() == -1 && stats2.PperC() - (p_per_c + PModifier) > 0) {
            startOtpimisation(NModifier, PModifier + 5);
            return;
        }

        TruckOptimisationSolver.SolutionStats [] stats = {stats1, stats2, stats3};
        TruckOptimisationSolver [] solvers = {solver1, solver2, solver3};
        TruckOptimisationSolver selectedSolver = selectSolution(stats, solvers);

        List<TruckOptimisationSolver.SolutionEntry> solution = selectedSolver.getSolution();

        TruckOptimisationWriter writer = new TruckOptimisationWriter(solution, resultBundle.infoArray(), resultBundle.valuesArray(), N, P, C2, NSalumis, PSalumis);

        writer.writeExcelSolution();
    }

    private TruckOptimisationSolver selectSolution(TruckOptimisationSolver.SolutionStats [] stats, TruckOptimisationSolver [] solvers){
        int selected = 0;
        double best = computeHeuristic(stats[0].NperC(), stats[0].PperC(), stats[0].PtheoC(), stats[0].NtheoC());
        for (int i = 0; i < stats.length; i++) {
            double candidate = computeHeuristic(stats[i].NperC(), stats[i].PperC(), stats[i].PtheoC(), stats[i].NtheoC());

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
