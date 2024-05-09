import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class BipartiteKnapsackSolver {

    public List<String> getUnprocessedObjectDescriptions(){
        ArrayList<String> objectsList = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);

        String readString = scanner.nextLine();

        while (!Objects.equals(readString, "")){
            objectsList.add(readString);
            readString = scanner.nextLine();
        }

        System.out.println(objectsList.toString());

        return objectsList;
    }

}
