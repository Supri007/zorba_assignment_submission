import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Patient implements IDBLoad {
    public int patientId;
    public String patientName;
    public String patientType;
    public int noOfDays;
    public int totalBedCharges;

    public Patient getInputFromUser(Scanner sc){
        Patient patient = new Patient();
        boolean isValidInput = true;

        do{
            try{
                System.out.println("Please enter patient id: ");
                patient.patientId = sc.nextInt();
                sc.nextLine();
                System.out.println("Please enter patient name: ");
                patient.patientName = sc.nextLine();
                System.out.println("Please enter patient type (Critical or Normal): ");
                patient.patientType = sc.nextLine();
                if (!patient.patientType.equals("Critical") && !patient.patientType.equals("Normal")){
                    throw new InputMismatchException("Input can be Critical or Normal. Please enter again.");
                }
                System.out.println("Please enter number of days: ");
                patient.noOfDays = sc.nextInt();
                sc.close();
                isValidInput = true;
            }
            catch (Exception ex){
                System.out.println("Input is not valid. Error: " + ex.getMessage());
                isValidInput = false;
                if (ex.getMessage() == null){
                    sc.next();
                }
            }

        }while(isValidInput == false);
        return patient;
    }

    @Override
    public String insertDataToDB(Connection con, Object data) {
        String message = "";

        try{
            Patient patient = (Patient) data;
            String insertDetails = "insert Patient values(?, ?, ?, ?, ?)";
            PreparedStatement insertStatement = con.prepareStatement(insertDetails);
            insertStatement.setInt(1, patient.patientId);
            insertStatement.setString(2, patient.patientName);
            insertStatement.setString(3, patient.patientType);
            insertStatement.setInt(4,patient.noOfDays);
            insertStatement.setInt(5,patient.totalBedCharges);
            int noOfDataInserted = insertStatement.executeUpdate();
            message = "Number of Rows Added in Patient: " + noOfDataInserted;
        }
        catch(SQLException ex){
            System.out.println("Failed to Insert data in Patient table. Error: " + ex.getMessage());
        }
        return message;
    }
}
