import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class Hospital implements IDBLoad {
    public int hospitalId;
    public String hospitalName;
    public int patientId;

    public Hospital getInputFromUser(Scanner sc){
        Hospital hospital = new Hospital();
        boolean isValidInput = true;

        do{
            try{
                System.out.println("Please enter hospital Id: ");
                hospital.hospitalId = sc.nextInt();
                sc.nextLine();
                System.out.println("Please enter hospital Name ");
                hospital.hospitalName = sc.nextLine();
                isValidInput = true;
            }
            catch (Exception ex){
                System.out.println("Input is not valid. Error: " + ex.getMessage());
                isValidInput = false;
                sc.next();
            }
        }while(isValidInput == false);

        return hospital;
    }

    @Override
    public String insertDataToDB(Connection con, Object data) {
        String message = "";

        try{
            Hospital hospital = (Hospital) data;
            String insertDetails = "insert Hospital values(?, ?, ?)";
            PreparedStatement insertStatement = con.prepareStatement(insertDetails);
            insertStatement.setInt(1, hospital.hospitalId);
            insertStatement.setString(2, hospital.hospitalName);
            insertStatement.setInt(3, hospital.patientId);
            int noOfDataInserted = insertStatement.executeUpdate();
            message = "Number of Rows Added in Hospital: " + noOfDataInserted;
        }
        catch(SQLException ex){
            System.out.println("Failed to Insert data in Hospital table. Error: " + ex.getMessage());
        }
        return message;
    }
}
