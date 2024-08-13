package com.example.trucks.anticipation.solver;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class TruckAnticipationBlock {

    XSSFWorkbook workbook;
    String sheetName;

    double P;
    double N;

    int C1 = 2;
    int C2 = 5;

    double p_per_c = 20500;
    double n_per_c = 64;

    public TruckAnticipationBlock(XSSFWorkbook workbook, String sheetName, int C1, int C2,
                                  double p_per_c, double n_per_c){
        this.workbook = workbook;
        this.sheetName = sheetName;

        this.C1 = C1;
        this.C2 = C2;

        this.p_per_c = p_per_c;
        this.n_per_c = n_per_c;
    }

    public void startAnticipation(double NModifier, double PModifier){
        TruckAnticipationDP bpDP = new TruckAnticipationDP(workbook, sheetName);

        bpDP.readExcelFile();
        bpDP.reprocessData();
        TruckAnticipationDP.ResultBundle resultBundle = bpDP.getResultBundle();

        P = resultBundle.P();
        N = resultBundle.N();

        TruckAnticipationSolver solver1 = new TruckAnticipationSolver(
                P,
                N,
                p_per_c + PModifier,
                n_per_c + NModifier,
                C1,
                C2,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        TruckAnticipationSolver solver2 = new TruckAnticipationSolver(
                P,
                N,
                p_per_c + PModifier,
                n_per_c + NModifier,
                C1,
                C2,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        TruckAnticipationSolver solver3 = new TruckAnticipationSolver(
                P,
                N,
                p_per_c + PModifier,
                n_per_c + NModifier,
                C1,
                C2,
                resultBundle.infoArray(),
                resultBundle.valuesArray()
        );

        TruckAnticipationSolver.SolutionStats stats1 = solver1.useApproach1();
        TruckAnticipationSolver.SolutionStats stats2 = solver2.useApproach2();
        TruckAnticipationSolver.SolutionStats stats3 = solver3.useApproach3(0.2);

        /*
        if(stats1.result() == -1 && stats2.result() == -1 && stats3.result() == -1 && stats2.NperC() - (n_per_c + NModifier) > 0){
            startAnticipation(NModifier + 0.1, PModifier);
            return;
        } else if (stats1.result() == -1 && stats2.result() == -1 && stats3.result() == -1 && stats2.PperC() - (p_per_c + PModifier) > 0) {
            startAnticipation(NModifier, PModifier + 5);
            return;
        }*/

        TruckAnticipationSolver.SolutionStats [] stats = {stats1, stats2, stats3};
        TruckAnticipationSolver [] solvers = {solver1, solver2, solver3};
        TruckAnticipationSolver selectedSolver = selectSolution(stats, solvers);

        List<TruckAnticipationSolver.SolutionEntry> solution = selectedSolver.getSolution();

        TruckAnticipationWriter writer = new TruckAnticipationWriter(solution, resultBundle.infoArray(), resultBundle.valuesArray(), N, P, C1, C2);

        writer.writeExcelSolution();
    }

    private TruckAnticipationSolver selectSolution(TruckAnticipationSolver.SolutionStats [] stats, TruckAnticipationSolver [] solvers){
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
