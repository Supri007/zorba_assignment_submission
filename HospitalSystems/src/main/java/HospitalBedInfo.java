import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class HospitalBedInfo implements IDBLoad {
    public String bedType;
    public String bedId;
    public int bed_charges_rate;
    public int hospital_id;

    public HospitalBedInfo getInputFromUser(Scanner sc){
        HospitalBedInfo hospitalBedInfo = new HospitalBedInfo();
        boolean isValidInput = true;
        do{
            try{
                System.out.println("Please enter bed Id: ");
                hospitalBedInfo.bedId = sc.nextLine();
                System.out.println("Please enter bed type Emergency or Regular: ");
                hospitalBedInfo.bedType = sc.nextLine();
                if (!hospitalBedInfo.bedType.equals("Emergency") && !hospitalBedInfo.bedType.equals("Regular")){
                    throw new InputMismatchException("Input can be Emergency or Regular only. Please enter again.");
                }
                System.out.println("Please enter bed charges rate $50 or $30: ");
                hospitalBedInfo.bed_charges_rate = sc.nextInt();
                if (hospitalBedInfo.bed_charges_rate != 50 && hospitalBedInfo.bed_charges_rate != 30){
                    throw new InputMismatchException("Input can be 50 or 30 only. Please enter again");
                }
                sc.nextLine();
                isValidInput = true;
            }
            catch (Exception ex){
                System.out.println("Input is not valid. Error: " + ex.getMessage());
                isValidInput = false;
                sc.nextLine();
                //only for if Id in entered as not int
                if (ex.getMessage() == null){
                    sc.next();
                }
            }

        } while(isValidInput == false);
        return hospitalBedInfo;
    }

    @Override
    public String insertDataToDB(Connection con, Object data) {
        String message = "";

        try{
            HospitalBedInfo hbi = (HospitalBedInfo) data;
            String insertDetails = "insert Hospital_Bed_Info values(?, ?, ?, ?)";
            PreparedStatement insertStatement = con.prepareStatement(insertDetails);
            insertStatement.setString(1, hbi.bedId);
            insertStatement.setString(2, hbi.bedType);
            insertStatement.setInt(3, hbi.bed_charges_rate);
            insertStatement.setInt(4, hbi.hospital_id);
            int noOfDataInserted = insertStatement.executeUpdate();
            message = "Number of Rows Added in Hospital_Bed_Info: " + noOfDataInserted;
        }
        catch(SQLException ex){
            System.out.println("Failed to Insert data in Hospital_Bed_Info table. Error: " + ex.getMessage());
        }
        return message;
    }
}
