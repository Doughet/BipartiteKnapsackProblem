import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class BipartiteDataProcessing {

    /*
    This is the function that gets the inputs of the products.
    The input should be of the form:
    ProductCode TotalWeight PalettesAmount
     */
    public ArrayList<String> getUnprocessedObjectDescriptions(){
        ArrayList<String> objectsList = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        String readString = scanner.nextLine();

        while (!Objects.equals(readString, "")){
            objectsList.add(readString);
            readString = scanner.nextLine();
        }

        return objectsList;
    }

    public ParsedInput parseObjectLines(ArrayList<String> objectLines, int size){
        String[] resultingCodes = new String[size];
        int[][] resultingTable = new int[size][2];

        for (int i = 0; i < objectLines.size(); i++) {
            String[] splittedString = objectLines.get(i).split(" ");

            resultingCodes[i] = splittedString[0];

            resultingTable[i][0] = Integer.parseInt(splittedString[1]);
            resultingTable[i][1] = Integer.parseInt(splittedString[2]);
        }

        return new ParsedInput(resultingCodes, resultingTable);

    }

    public record ParsedInput(String[] objectCodes, int[][] objectDescriptions){}
}
