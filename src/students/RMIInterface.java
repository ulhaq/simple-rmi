package students;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface RMIInterface extends Remote
{
    public ArrayList<ArrayList<String>> getDBStudents() throws RemoteException, SQLException, ClassNotFoundException;

    public ArrayList<ArrayList<String>> getFileStudents(String filePath) throws RemoteException;

    public String fileToDB(String filePath) throws SQLException, RemoteException;

    public ArrayList<ArrayList<String>> getResult(String DBRs, String filePath) throws RemoteException, SQLException;
}