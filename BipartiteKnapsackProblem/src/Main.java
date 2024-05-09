import java.util.*;

public class Main {
    public static void main(String[] args) {

        //GET THE DATA UNPROCESSED
        BipartiteDataProcessing dataProcessing = new BipartiteDataProcessing();
        ArrayList<String> dataUnprocessed = dataProcessing.getUnprocessedObjectDescriptions();
        BipartiteDataProcessing.ParsedInput parsedInput = dataProcessing.parseObjectLines(dataUnprocessed, dataUnprocessed.size());

        //System.out.println(Arrays.toString(parsedInput.objectCodes()));
        //System.out.println(Arrays.deepToString(parsedInput.objectDescriptions()));
    }




}