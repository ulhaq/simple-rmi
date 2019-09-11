package students;

import java.rmi.Naming;
import java.util.Scanner;

public class RMIClient {

    private static String isDBRs = null;
    private static String filePath = null;
    private static String isImport = null;

    public static void getInput() {
        Scanner inp = new Scanner(System.in);

        System.out.println("Load data from DB [Y/N]?");
        isDBRs = inp.nextLine();

        System.out.println("Load data from File [Y/N]?");
        String isFileRs = inp.nextLine();

        if (isFileRs.equalsIgnoreCase("y")) {
            System.out.println("Enter file path: ");
            filePath = inp.nextLine();

            System.out.println("Would you like to import the file data to DB [Y/N]?");
            isImport = inp.nextLine();
        }
    }

    public static void getService() throws Exception {
        String remoteEngine = "rmi://localhost/Compute";

        RMIInterface obj = (RMIInterface) Naming.lookup(remoteEngine);

        obj.getResult(isDBRs, filePath).forEach(rs -> System.out.println(rs));

        if (isImport.equalsIgnoreCase("y")) {
            System.out.println(obj.fileToDB(filePath));
        }
    }

    public static void main(String[] args) {
        try {
            getInput();
            getService();
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        }
    }
}
