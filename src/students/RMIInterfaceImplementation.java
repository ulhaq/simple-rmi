package students;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class RMIInterfaceImplementation extends UnicastRemoteObject implements RMIInterface {
    private Connection connection;

    public RMIInterfaceImplementation() throws RemoteException, SQLException, ClassNotFoundException {
        super();

        Class.forName("com.mysql.cj.jdbc.Driver");
        this.connection = DriverManager.getConnection("jdbc:mysql://localhost/cphstudents?serverTimezone=UTC", "root", "0000");
    }

    @Override
    public ArrayList<ArrayList<String>> getDBStudents() throws RemoteException, SQLException {
        PreparedStatement st = null;
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        try {
            st = connection.prepareStatement("SELECT * FROM students");
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String email = rs.getString("email");

                ArrayList<String> values = new ArrayList<>();

                values.add(String.valueOf(id));
                values.add(name);
                values.add(email);

                data.add(values);
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

        return data;
    }

    @Override
    public ArrayList<ArrayList<String>> getFileStudents(String filePath) throws RemoteException {
        ArrayList<ArrayList<String>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {

                String[] list = line.trim().split(",");

                ArrayList values = new ArrayList(Arrays.asList(list));

                data.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    @Override
    public String fileToDB(String filePath) throws SQLException, RemoteException {
        ArrayList<ArrayList<String>> data = getFileStudents(filePath);
        PreparedStatement st = null;

        try {
            st = connection.prepareStatement("INSERT INTO students (name, email) VALUES (?, ?)");

            for (ArrayList row : data) {
                st.setString(1, String.valueOf(row.get(1)));
                st.setString(2, String.valueOf(row.get(2)));
                st.addBatch();
            }

            st.executeBatch();

            return "Data imported successfully.";

        } catch (SQLException e) {
            return e.toString();
        }
    }

    @Override
    public ArrayList<ArrayList<String>> getResult(String DBRs, String filePath) throws RemoteException, SQLException {
        ArrayList<ArrayList<String>> data = new ArrayList<>();

        if (DBRs.equalsIgnoreCase("y")) {
            data.addAll(this.getDBStudents());
        }
        if (filePath != null) {
            data.addAll(this.getFileStudents(filePath));
        }

        return data;
    }
}