import java.util.*;

public class TruckAnticipationSolver {

    //Total Weight
    private double P;
    //Total Palettes
    private double N;
    //Poids par camion
    private double P_PER_C;
    //Palettes par camion
    private double N_PER_C;

    //Nb Camions 1
    private int C1;
    //Poids max C1
    private double P1;
    //Palettes max C1
    private double N1;

    //Nb Camions 2
    private int C2;
    //Poids max C2
    private double P2;
    //Palettes max C2
    private double N2;

    //Real Arrays
    //Data info array
    private final String[][] infoArray;
    //Values array
    private final double[][] valuesArray;

    //Edited Arrays
    //Data info array
    private String[][] infoArrayE;
    //Values array
    private double[][] valuesArrayE;
    //List of the selected objects
    private HashMap<String, Double> codesSelected;

    private List<SolutionEntry> solutionEntries;

    //Real values
    private double Pr1;
    private double Nr1;

    private boolean isFeasible = false;

    public TruckAnticipationSolver(double p, double n, double p_PER_C, double n_PER_C,
                                   int c1, int c2,
                                   String[][] infoArray, double[][] valuesArray) {
        P = p;
        N = n;
        P_PER_C = p_PER_C;
        N_PER_C = n_PER_C;

        if(c1 < c2){
            C1 = c1;
            P1 = P_PER_C * c1;
            N1 = N_PER_C * c1;
            C2 = c2;
            P2 = P_PER_C * c2;
            N2 = N_PER_C * c2;
        }else{
            C1 = c2;
            P1 = P_PER_C * c2;
            N1 = N_PER_C * c2;
            C2 = c1;
            P2 = P_PER_C * c1;
            N2 = N_PER_C * c1;
        }

        this.infoArray = new String[infoArray.length][infoArray[0].length];
        this.valuesArray = new double[infoArray.length][infoArray[0].length];

        this.infoArrayE = new String[infoArray.length][infoArray[0].length];
        this.valuesArrayE = new double[infoArray.length][infoArray[0].length];


        for (int i = 0; i < infoArray.length; i++) {
            this.infoArray[i] = infoArray[i].clone();
            this.valuesArray[i] = valuesArray[i].clone();
            this.infoArrayE[i] = infoArray[i].clone();
            this.valuesArrayE[i] = valuesArray[i].clone();
        }

        codesSelected = new HashMap<>();
        solutionEntries = new ArrayList<>();

        Pr1 = 0;
        Nr1 = 0;

        isFeasible = false;
    }


    /**
     * Launches the first approach.
     * It fills in the list of codes selected.
     * @return It returns -1 if no solution was found. It returns 0 if a solution was found.
     */
    private SolutionStats launchApproach1(){
        double selectedPalettes = 0;
        double selectedWeight = 0;

        double nMoyenCamion = N / (C1 + C2);
        double pMoyenCamion = P / (C1 + C2);
        double pMoyenPalette = pMoyenCamion / nMoyenCamion;

        double weightPerPaletteLeft = pMoyenPalette;

        while(testMustContinue(selectedWeight, selectedPalettes)){

            int selectedIndex = findClosestObject(valuesArrayE, weightPerPaletteLeft);
            int selectedIndexUnder = findClosestObjectUnder(valuesArrayE, weightPerPaletteLeft);

            if(selectedIndex == -1 && selectedIndexUnder == -1){
                break;
            }else if(selectedIndex == -1){
                selectedIndex = selectedIndexUnder;
            }

            //ADD TO LIST
            String code = infoArray[selectedIndex][0];
            codesSelected.put(code, codesSelected.getOrDefault(code, 0.0) + 1);

            //Decrease the palettes in the table
            valuesArrayE[selectedIndex][0] -= 1;
            //UPDATE THE SELECTED PALETTES
            selectedPalettes += 1;
            //UPDATE THE SELECTED WEIGHT
            selectedWeight += valuesArray[selectedIndex][1];
            //UPDATE THE WEIGHT PER PALETTE
            weightPerPaletteLeft = (pMoyenCamion - selectedWeight) / (nMoyenCamion - selectedPalettes);
        }

        //In case it is now finished we have to:
        //test if it's alright now

        if(testFinalConstraint(selectedWeight, selectedPalettes)){

            optimizeAfterFinish(selectedPalettes, selectedWeight);

            return new SolutionStats(0, P, N, selectedWeight / C1, selectedPalettes / C1, pMoyenCamion, nMoyenCamion);
        }

        while(testUnderMax(selectedWeight, selectedPalettes)){
            int selectedIndex = findClosestObject(valuesArrayE, weightPerPaletteLeft);

            if(selectedIndex == -1 || valuesArrayE[selectedIndex][1] + selectedWeight > P1){
                selectedIndex = findClosestObjectUnder(valuesArrayE, weightPerPaletteLeft);

                if(selectedIndex == -1 || valuesArrayE[selectedIndex][1] + selectedWeight > P1){

                    break;
                }
            }

            //ADD TO LIST
            String code = infoArray[selectedIndex][0];
            codesSelected.put(code, codesSelected.getOrDefault(code, 0.0) + 1);

            //Decrease the palettes in the table
            valuesArrayE[selectedIndex][0] -= 1;
            //UPDATE THE SELECTED PALETTES
            selectedPalettes += 1;
            //UPDATE THE SELECTED WEIGHT
            selectedWeight += valuesArray[selectedIndex][1];
            //UPDATE THE WEIGHT PER PALETTE
            weightPerPaletteLeft = (P_PER_C - selectedWeight) / (N_PER_C - selectedPalettes);

            if(testFinalConstraint(selectedWeight, selectedPalettes)){
                return new SolutionStats(0, P, N, selectedWeight / C1, selectedPalettes / C1, pMoyenCamion, nMoyenCamion);
            }
        }

        return new SolutionStats(-1, P, N, selectedWeight / C1, selectedPalettes / C1, pMoyenCamion, nMoyenCamion);
    }


    private SolutionStats launchApproach2(){
        double selectedPalettes = 0;
        double selectedWeight = 0;

        double nMoyenCamion = N / (C1 + C2);
        double pMoyenCamion = P / (C1 + C2);
        double pMoyenPalette = pMoyenCamion / nMoyenCamion;

        double weightPerPaletteLeft = pMoyenPalette;

        while(testMustContinue(selectedWeight, selectedPalettes)){

            double currentMoyenne = selectedWeight / selectedPalettes;

            int selectedIndex = -1;

            if(currentMoyenne <= pMoyenPalette){
                selectedIndex = findClosestObjectAbove(valuesArrayE, weightPerPaletteLeft);
            }else{
                selectedIndex = findClosestObjectUnder(valuesArrayE, weightPerPaletteLeft);
            }

            int selectedIndexUnder = findClosestObjectUnder(valuesArrayE, weightPerPaletteLeft);

            if(selectedIndex == -1 && selectedIndexUnder == -1){
                break;
            }else if(selectedIndex == -1){
                selectedIndex = selectedIndexUnder;
            }

            //ADD TO LIST
            String code = infoArray[selectedIndex][0];
            codesSelected.put(code, codesSelected.getOrDefault(code, 0.0) + 1);

            //Decrease the palettes in the table
            valuesArrayE[selectedIndex][0] -= 1;
            //UPDATE THE SELECTED PALETTES
            selectedPalettes += 1;
            //UPDATE THE SELECTED WEIGHT
            selectedWeight += valuesArray[selectedIndex][1];
            //UPDATE THE WEIGHT PER PALETTE
            weightPerPaletteLeft = (pMoyenCamion * C2 - selectedWeight) / (nMoyenCamion * C2 - selectedPalettes);

            if(selectedPalettes == 61.0){
                System.out.println();
            }
        }

        //In case it is now finished we have to:
        //test if it's alright now

        if(testFinalConstraint(selectedWeight, selectedPalettes)){

            optimizeAfterFinish2(selectedPalettes, selectedWeight);

            return new SolutionStats(0, P, N, selectedWeight / C1, selectedPalettes / C1, pMoyenCamion, nMoyenCamion);
        }

        while(testUnderMax(selectedWeight, selectedPalettes)){
            int selectedIndex = findClosestObject(valuesArrayE, weightPerPaletteLeft);

            if(selectedIndex == -1 || valuesArrayE[selectedIndex][1] + selectedWeight > P1){
                selectedIndex = findClosestObjectUnder(valuesArrayE, weightPerPaletteLeft);

                if(selectedIndex == -1 || valuesArrayE[selectedIndex][1] + selectedWeight > P1){

                    break;
                }
            }

            //ADD TO LIST
            String code = infoArray[selectedIndex][0];
            codesSelected.put(code, codesSelected.getOrDefault(code, 0.0) + 1);

            //Decrease the palettes in the table
            valuesArrayE[selectedIndex][0] -= 1;
            //UPDATE THE SELECTED PALETTES
            selectedPalettes += 1;
            //UPDATE THE SELECTED WEIGHT
            selectedWeight += valuesArray[selectedIndex][1];
            //UPDATE THE WEIGHT PER PALETTE
            weightPerPaletteLeft = (P_PER_C - selectedWeight) / (N_PER_C - selectedPalettes);

            if(testFinalConstraint(selectedWeight, selectedPalettes)){
                return new SolutionStats(0, P, N, selectedWeight / C1, selectedPalettes / C1, pMoyenCamion, nMoyenCamion);
            }
        }

        return new SolutionStats(-1, P, N, selectedWeight / C1, selectedPalettes / C1, pMoyenCamion, nMoyenCamion);
    }

    private SolutionStats launchApproach3(double threshold){
        double selectedPalettes = 0;
        double selectedWeight = 0;

        double nMoyenCamion = N / (C1 + C2);
        double pMoyenCamion = P / (C1 + C2);
        double pMoyenPalette = pMoyenCamion / nMoyenCamion;

        double weightPerPaletteLeft = pMoyenPalette;

        while(testMustContinue(selectedWeight, selectedPalettes)){

            double currentMoyenne = selectedWeight / selectedPalettes;

            int selectedIndex = -1;

            if(Math.min(currentMoyenne, pMoyenPalette) / Math.max(currentMoyenne, pMoyenPalette) < threshold){
                selectedIndex = findClosestObject(valuesArrayE, weightPerPaletteLeft);
            }
            else if(currentMoyenne <= pMoyenPalette){
                selectedIndex = findClosestObjectAbove(valuesArrayE, weightPerPaletteLeft);
            }else{
                selectedIndex = findClosestObjectUnder(valuesArrayE, weightPerPaletteLeft);
            }

            int selectedIndexUnder = findClosestObjectUnder(valuesArrayE, weightPerPaletteLeft);

            if(selectedIndex == -1 && selectedIndexUnder == -1){
                break;
            }else if(selectedIndex == -1){
                selectedIndex = selectedIndexUnder;
            }

            //ADD TO LIST
            String code = infoArray[selectedIndex][0];
            codesSelected.put(code, codesSelected.getOrDefault(code, 0.0) + 1);

            //Decrease the palettes in the table
            valuesArrayE[selectedIndex][0] -= 1;
            //UPDATE THE SELECTED PALETTES
            selectedPalettes += 1;
            //UPDATE THE SELECTED WEIGHT
            selectedWeight += valuesArray[selectedIndex][1];
            //UPDATE THE WEIGHT PER PALETTE
            weightPerPaletteLeft = (pMoyenCamion * C2 - selectedWeight) / (nMoyenCamion * C2 - selectedPalettes);

            if(selectedPalettes == 61.0){
                System.out.println();
            }
        }

        //In case it is now finished we have to:
        //test if it's alright now

        if(testFinalConstraint(selectedWeight, selectedPalettes)){

            optimizeAfterFinish2(selectedPalettes, selectedWeight);

            return new SolutionStats(0, P, N, selectedWeight / C1, selectedPalettes / C1, pMoyenCamion, nMoyenCamion);
        }

        while(testUnderMax(selectedWeight, selectedPalettes)){
            int selectedIndex = findClosestObject(valuesArrayE, weightPerPaletteLeft);

            if(selectedIndex == -1 || valuesArrayE[selectedIndex][1] + selectedWeight > P1){
                selectedIndex = findClosestObjectUnder(valuesArrayE, weightPerPaletteLeft);

                if(selectedIndex == -1 || valuesArrayE[selectedIndex][1] + selectedWeight > P1){

                    break;
                }
            }

            //ADD TO LIST
            String code = infoArray[selectedIndex][0];
            codesSelected.put(code, codesSelected.getOrDefault(code, 0.0) + 1);

            //Decrease the palettes in the table
            valuesArrayE[selectedIndex][0] -= 1;
            //UPDATE THE SELECTED PALETTES
            selectedPalettes += 1;
            //UPDATE THE SELECTED WEIGHT
            selectedWeight += valuesArray[selectedIndex][1];
            //UPDATE THE WEIGHT PER PALETTE
            weightPerPaletteLeft = (P_PER_C - selectedWeight) / (N_PER_C - selectedPalettes);

            if(testFinalConstraint(selectedWeight, selectedPalettes)){
                return new SolutionStats(0, P, N, selectedWeight / C1, selectedPalettes / C1, pMoyenCamion, nMoyenCamion);
            }
        }

        return new SolutionStats(-1, P, N, selectedWeight / C1, selectedPalettes / C1, pMoyenCamion, nMoyenCamion);
    }


    private void optimizeAfterFinish2(double selectedPalettes, double selectedWeight){
        double nMoyenCamion = N / (C1 + C2);
        double pMoyenCamion = P / (C1 + C2);
        double pMoyenPalette = pMoyenCamion / nMoyenCamion;
        double weightPerPaletteLeft = (pMoyenCamion * C2 - selectedWeight) / (nMoyenCamion * C2 - selectedPalettes);

        double selectedPalettesAfter = selectedPalettes;
        double selectedWeightAfter = selectedWeight;

        int selectedIndex = -1;

        if(selectedWeight / selectedPalettes < pMoyenPalette){
            selectedIndex = findClosestObjectAbove(valuesArrayE, weightPerPaletteLeft);
        }else{
            selectedIndex = findClosestObjectUnder(valuesArrayE, weightPerPaletteLeft);
        }


        if(selectedIndex == -1){
            return;
        }

        selectedPalettesAfter += 1;
        selectedWeightAfter += valuesArrayE[selectedIndex][1];

        while(selectedWeightAfter <= pMoyenCamion * C1 && selectedPalettesAfter <= nMoyenCamion * C1 && testFinalConstraint(selectedWeightAfter, selectedPalettesAfter)){

            selectedWeight = selectedWeightAfter;
            selectedPalettes = selectedPalettesAfter;

            String code = infoArray[selectedIndex][0];
            codesSelected.put(code, codesSelected.getOrDefault(code, 0.0) + 1);

            valuesArrayE[selectedIndex][0] -= 1;

            weightPerPaletteLeft = (pMoyenCamion * C2 - selectedWeight) / (nMoyenCamion * C2 - selectedPalettes);

            selectedIndex = findClosestObject(valuesArrayE, weightPerPaletteLeft);

            if(selectedIndex == -1){
                return;
            }

            selectedPalettesAfter += 1;
            selectedWeightAfter += valuesArrayE[selectedIndex][1];
        }

        if(testFinalConstraint(selectedWeightAfter, selectedPalettesAfter)){
            return;
        }else{
            double heuristicWeightAfter = (selectedWeightAfter - weightPerPaletteLeft) / weightPerPaletteLeft;
            double heuristicWeight = (selectedWeight - weightPerPaletteLeft) / weightPerPaletteLeft;

            double heuristicPalettesAfter = (selectedPalettesAfter - pMoyenPalette) / pMoyenPalette;
            double heuristicPalette = (selectedPalettes - pMoyenPalette) / pMoyenPalette;

            if(Math.abs(heuristicWeightAfter) + Math.abs(heuristicPalettesAfter) < Math.abs(heuristicWeight) + Math.abs(heuristicPalette)){
                selectedPalettes = selectedPalettesAfter;
                selectedWeight = selectedWeightAfter;

                String code = infoArray[selectedIndex][0];
                codesSelected.put(code, codesSelected.getOrDefault(code, 0.0) + 1);

                valuesArrayE[selectedIndex][0] -= 1;
            }
        }
    }

    private void optimizeAfterFinish3(double selectedPalettes, double selectedWeight, double threshold){
        double nMoyenCamion = N / (C1 + C2);
        double pMoyenCamion = P / (C1 + C2);
        double pMoyenPalette = pMoyenCamion / nMoyenCamion;
        double weightPerPaletteLeft = (pMoyenCamion * C2 - selectedWeight) / (nMoyenCamion * C2 - selectedPalettes);

        double selectedPalettesAfter = selectedPalettes;
        double selectedWeightAfter = selectedWeight;

        int selectedIndex = -1;

        if(Math.min(selectedWeight / selectedPalettes, pMoyenPalette) / Math.max(selectedWeight / selectedPalettes, pMoyenPalette) < threshold){
            selectedIndex = findClosestObject(valuesArrayE, weightPerPaletteLeft);
        }
        else if(selectedWeight / selectedPalettes < pMoyenPalette){
            selectedIndex = findClosestObjectAbove(valuesArrayE, weightPerPaletteLeft);
        }else{
            selectedIndex = findClosestObjectUnder(valuesArrayE, weightPerPaletteLeft);
        }


        if(selectedIndex == -1){
            return;
        }

        selectedPalettesAfter += 1;
        selectedWeightAfter += valuesArrayE[selectedIndex][1];

        while(selectedWeightAfter <= pMoyenCamion * C1 && selectedPalettesAfter <= nMoyenCamion * C1 && testFinalConstraint(selectedWeightAfter, selectedPalettesAfter)){

            selectedWeight = selectedWeightAfter;
            selectedPalettes = selectedPalettesAfter;

            String code = infoArray[selectedIndex][0];
            codesSelected.put(code, codesSelected.getOrDefault(code, 0.0) + 1);

            valuesArrayE[selectedIndex][0] -= 1;

            weightPerPaletteLeft = (pMoyenCamion * C2 - selectedWeight) / (nMoyenCamion * C2 - selectedPalettes);

            selectedIndex = findClosestObject(valuesArrayE, weightPerPaletteLeft);

            if(selectedIndex == -1){
                return;
            }

            selectedPalettesAfter += 1;
            selectedWeightAfter += valuesArrayE[selectedIndex][1];
        }

        if(testFinalConstraint(selectedWeightAfter, selectedPalettesAfter)){
            return;
        }else{
            double heuristicWeightAfter = (selectedWeightAfter - weightPerPaletteLeft) / weightPerPaletteLeft;
            double heuristicWeight = (selectedWeight - weightPerPaletteLeft) / weightPerPaletteLeft;

            double heuristicPalettesAfter = (selectedPalettesAfter - pMoyenPalette) / pMoyenPalette;
            double heuristicPalette = (selectedPalettes - pMoyenPalette) / pMoyenPalette;

            if(Math.abs(heuristicWeightAfter) + Math.abs(heuristicPalettesAfter) < Math.abs(heuristicWeight) + Math.abs(heuristicPalette)){
                selectedPalettes = selectedPalettesAfter;
                selectedWeight = selectedWeightAfter;

                String code = infoArray[selectedIndex][0];
                codesSelected.put(code, codesSelected.getOrDefault(code, 0.0) + 1);

                valuesArrayE[selectedIndex][0] -= 1;
            }
        }
    }

    private void optimizeAfterFinish(double selectedPalettes, double selectedWeight){
        double nMoyenCamion = N / (C1 + C2);
        double pMoyenCamion = P / (C1 + C2);
        double pMoyenPalette = pMoyenCamion / nMoyenCamion;
        double weightPerPaletteLeft = (pMoyenCamion - selectedWeight) / (nMoyenCamion - selectedPalettes);

        double selectedPalettesAfter = selectedPalettes;
        double selectedWeightAfter = selectedWeight;

        int selectedIndex = findClosestObject(valuesArrayE, weightPerPaletteLeft);

        if(selectedIndex == -1){
            return;
        }

        selectedPalettesAfter += 1;
        selectedWeightAfter += valuesArrayE[selectedIndex][1];

        while(selectedWeightAfter <= pMoyenCamion * C1 && selectedPalettesAfter <= nMoyenCamion * C1 && testFinalConstraint(selectedWeightAfter, selectedPalettesAfter)){

            selectedWeight = selectedWeightAfter;
            selectedPalettes = selectedPalettesAfter;

            String code = infoArray[selectedIndex][0];
            codesSelected.put(code, codesSelected.getOrDefault(code, 0.0) + 1);

            valuesArrayE[selectedIndex][0] -= 1;

            selectedIndex = findClosestObject(valuesArrayE, weightPerPaletteLeft);

            if(selectedIndex == -1){
                return;
            }

            selectedPalettesAfter += 1;
            selectedWeightAfter += valuesArrayE[selectedIndex][1];
        }

        if(testFinalConstraint(selectedWeightAfter, selectedPalettesAfter)){
            return;
        }else{
            double heuristicWeightAfter = (selectedWeightAfter - weightPerPaletteLeft) / weightPerPaletteLeft;
            double heuristicWeight = (selectedWeight - weightPerPaletteLeft) / weightPerPaletteLeft;

            double heuristicPalettesAfter = (selectedPalettesAfter - pMoyenPalette) / pMoyenPalette;
            double heuristicPalette = (selectedPalettes - pMoyenPalette) / pMoyenPalette;

            if(Math.abs(heuristicWeightAfter) + Math.abs(heuristicPalettesAfter) < Math.abs(heuristicWeight) + Math.abs(heuristicPalette)){
                selectedPalettes = selectedPalettesAfter;
                selectedWeight = selectedWeightAfter;

                String code = infoArray[selectedIndex][0];
                codesSelected.put(code, codesSelected.getOrDefault(code, 0.0) + 1);

                valuesArrayE[selectedIndex][0] -= 1;
            }
        }
    }

    private void analyzeSolution(){
        double nMoyenCamion = N / (C1 + C2);
        double pMoyenCamion = P / (C1 + C2);

        System.out.println("Weight Accuracy: " + Pr1 / (pMoyenCamion * C1) * 100 + "%");
        System.out.println("Palettes Amount Accuracy: " + Nr1 / (nMoyenCamion * C1) * 100 + "%");
    }


    public SolutionStats useApproach1(){
        SolutionStats stats1 = launchApproach1();
        isFeasible = stats1.result == 0;

        if(isFeasible){
            System.out.println("Result Approach 1:");
            processSolutionEntries();

            printSolution();

            analyzeSolution();
        }

        return stats1;
    }

    public SolutionStats useApproach2(){
        SolutionStats stats2 = launchApproach2();
        isFeasible = stats2.result == 0;

        if(isFeasible){
            System.out.println("Result Approach 2:");
            processSolutionEntries();

            printSolution();

            analyzeSolution();
        }

        return stats2;
    }

    public SolutionStats useApproach3(double threshold){
        SolutionStats stats3 = launchApproach3(threshold);
        isFeasible = stats3.result == 0;

        if(isFeasible){
            System.out.println("Result Approach 3:");
            processSolutionEntries();

            printSolution();

            analyzeSolution();
        }

        return stats3;
    }

    public List<SolutionEntry> getSolution(){
        if(isFeasible){
            return solutionEntries;
        }
        else{
            System.out.println("There was no solution with this approach.");
            return null;
        }
    }

    private int findClosestObject(double[][] valuesArrayE, double weightPerPalette){
        int indexSelected = -1;

        for (int i = 0; i < valuesArrayE.length; i++) {
            if(valuesArrayE[i][0] < 1 || valuesArrayE[i][3] != 1){
                continue;
            }

            if(indexSelected == -1){
                indexSelected = i;
                continue;
            }

            double valueReg = valuesArrayE[indexSelected][1];
            double valueI = valuesArrayE[i][1];

            if(Math.abs(valueI - weightPerPalette) < Math.abs(valueReg - weightPerPalette)){
                indexSelected = i;
            }
        }

        return indexSelected;
    }

    private int findClosestObjectUnder(double[][] valuesArrayE, double weightPerPalette){
        int indexSelected = -1;

        for (int i = 0; i < valuesArrayE.length; i++) {
            if(valuesArrayE[i][0] < 1 || valuesArrayE[i][1] > weightPerPalette || valuesArrayE[i][3] != 1){
                continue;
            }

            if(indexSelected == -1){
                indexSelected = i;
                continue;
            }

            double valueReg = valuesArrayE[indexSelected][1];
            double valueI = valuesArrayE[i][1];

            if(weightPerPalette - valueI < weightPerPalette - valueReg){
                indexSelected = i;
            }
        }

        return indexSelected;
    }

    private int findClosestObjectAbove(double[][] valuesArrayE, double weightPerPalette){
        int indexSelected = -1;

        for (int i = 0; i < valuesArrayE.length; i++) {
            if(valuesArrayE[i][0] < 1 || valuesArrayE[i][1] < weightPerPalette || valuesArrayE[i][3] != 1){
                continue;
            }

            if(indexSelected == -1){
                indexSelected = i;
                continue;
            }

            double valueReg = valuesArrayE[indexSelected][1];
            double valueI = valuesArrayE[i][1];

            if(weightPerPalette - valueI < weightPerPalette - valueReg){
                indexSelected = i;
            }
        }

        return indexSelected;
    }

    private void processSolutionEntries(){
        for (int i = 0; i < infoArray.length; i++) {
            if(codesSelected.containsKey(infoArray[i][0])){
                solutionEntries.add(new SolutionEntry(infoArray[i][0], infoArray[i][1], codesSelected.get(infoArray[i][0]), valuesArray[i]));
            }
        }
    }

    private void printSolution(){

        for (int i = 0; i < infoArray.length; i++) {
            if(codesSelected.containsKey(infoArray[i][0])){
                System.out.println("Codice: " + infoArray[i][0] + "\tDescription: " + infoArray[i][1] + "\t\t\tAmount: " + codesSelected.get(infoArray[i][0]));
            }
        }

        for (int i = 0; i < infoArray.length; i++) {
            if(codesSelected.containsKey(infoArray[i][0])){
                System.out.print(codesSelected.get(infoArray[i][0]) * valuesArray[i][1] + " + ");
                Pr1 += codesSelected.get(infoArray[i][0]) * valuesArray[i][1];
            }
        }
        System.out.println(" = " + Pr1);

        for (int i = 0; i < infoArray.length; i++) {
            if(codesSelected.containsKey(infoArray[i][0])){
                System.out.print(codesSelected.get(infoArray[i][0]) + " + ");
                Nr1 += codesSelected.get(infoArray[i][0]);
            }
        }
        System.out.println(" = " + Nr1);

    }

    private Boolean testUnderMax(double p, double n){
        return p < P1 && n < N1;
    }

    private Boolean testMustContinue(double p, double n){
        double minP = P - P2;
        double minN = N - N2;

        Boolean pConst = minP > p;
        Boolean nConst = minN > n;

        return pConst || nConst;
    }

    private Boolean testFinalConstraint(double p, double n){
        double minP = P - P2;
        double minN = N - N2;

        Boolean pConst = minP <= p && p <= P1;
        Boolean nConst = minN <= n && n <= N1;

        Boolean paConst = minP > p;
        Boolean naConst = minN > n;

        Boolean prout = pConst || nConst;

        return pConst && nConst;
    }


    public record SolutionEntry(String code, String description, double amount, double[] values){}
    public record SolutionStats(int result, double P, double N, double PperC, double NperC, double PtheoC, double NtheoC){}
}
