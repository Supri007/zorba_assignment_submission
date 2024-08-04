import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class CreateDBData {
    public static void main(String[] args){
        try{
            //Create object of properties
            StringBuilder sb = new StringBuilder();
            Properties pr = new Properties();
            File inputFile = new File("C:\\Assignment SQL\\zorba_assignment_submission\\HospitalSystems\\src\\main\\resources\\dbValue.properties");
            FileInputStream inputStream = new FileInputStream(inputFile);
            pr.load(inputStream);

            //take input from users
            Scanner sc = new Scanner(System.in);
            HospitalBedInfo hospitalBedInfo1 = new HospitalBedInfo().getInputFromUser(sc);
            HospitalBedInfo hospitalBedInfo2 = new HospitalBedInfo().getInputFromUser(sc);

            Hospital hospital = new Hospital().getInputFromUser(sc);
            //Add hospital_id to hospitalBenInfo
            hospitalBedInfo1.hospital_id = hospital.hospitalId;
            hospitalBedInfo2.hospital_id = hospital.hospitalId;
            Patient patient = new Patient().getInputFromUser(sc);
            //Add patientId to Hospital table
            hospital.patientId = patient.patientId;
            sc.close();

            String urlDB = (String)pr.get("dbURL");
            String userName = (String)pr.get("userName");
            String password = (String)pr.get("password");

            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(urlDB,userName,password);
            System.out.println("DB connection successful");

            //Create table query
            String dbQuery1 = "Create table IF NOT Exists Hospital_Bed_Info (bed_id varchar(5) primary key, bed_type varchar(20), " +
                    "bed_charges_rate int, hospital_id int)";
            String dbQuery2 = "Create table IF NOT Exists Hospital (hospital_id int primary key, hospital_name varchar(20), " +
                    "patient_id int)";
            String dbQuery3 = "Create table IF NOT Exists Patient (patient_id int primary key, patient_name varchar(20), " +
                    "patient_type varchar(20), no_of_days int, total_bed_charges int)";
            String fkHospitalBedInfo = "alter table Hospital_Bed_Info add foreign key(hospital_id) " +
                    "references Hospital(hospital_id)";
            String fkHospital = "alter table Hospital add foreign key(patient_id) " +
                    "references Patient(patient_id)";

            Statement stat = con.createStatement();
            stat.execute(dbQuery1);
            stat.execute(dbQuery2);
            stat.execute(dbQuery3);
            stat.execute(fkHospitalBedInfo);
            stat.execute(fkHospital);
            System.out.println("Tables created in DB");

            sb.append(patient.insertDataToDB(con, patient)).append("\n");
            sb.append(hospital.insertDataToDB(con, hospital)).append("\n");
            sb.append(hospitalBedInfo1.insertDataToDB(con, hospitalBedInfo1)).append("\n");
            sb.append(hospitalBedInfo2.insertDataToDB(con, hospitalBedInfo2)).append("\n");
            System.out.println(sb.toString());

            //get data from DB
            String patientCondition = "";
            if (patient.patientType.equals("Critical")){
                patientCondition = "Emergency";
            }
            else{
                patientCondition = "Regular";
            }
            String getDataQuery = String.format("Select h.hospital_name, p.patient_name, p.patient_type, p.no_of_days, " +
                    "hbi.bed_type, hbi.bed_charges_rate from " +
                    "hospital_bed_info hbi " +
                    "inner join Hospital h on  hbi.hospital_id = h.hospital_id " +
                    "inner join Patient p on h.patient_id = p.patient_id " +
                    "where hbi.bed_type = '%s';", patientCondition);

            ExcelFileData excel = new ExcelFileData();
            try{
                ResultSet resultSet = stat.executeQuery(getDataQuery);
                while(resultSet.next()){
                    excel.hospitalName = resultSet.getString(1);
                    excel.patientName = resultSet.getString(2);
                    excel.patientType = resultSet.getString(3);
                    excel.bedType = resultSet.getString(5);
                    excel.numOfDays = resultSet.getInt(4);
                    excel.bedChargesRate = resultSet.getInt(6);
                    excel.totalBedCharges = excel.numOfDays * excel.bedChargesRate;
                }
            }
            catch (Exception ex){
                System.out.println("Failed to get query from DB");
            }

            //Write to excel file
            excel.WriteExcelFile(excel);
            con.close();
        }
        catch (Exception ex){
            System.out.println("Error while performing operation. Error: " + ex.getMessage());
        }
    }
}
