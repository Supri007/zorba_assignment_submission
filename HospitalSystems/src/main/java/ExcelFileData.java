import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelFileData {
    public String hospitalName;
    public String patientName;
    public String patientType;
    public String bedType;
    public int numOfDays;
    public int bedChargesRate;
    public int totalBedCharges;

    public void WriteExcelFile(ExcelFileData excel) throws IOException{
        File excelFile = new File("C:\\Assignment SQL\\zorba_assignment_submission\\HospitalSystems\\src\\main\\resources\\patientData.xlsx");
        try{
            FileOutputStream outPut = new FileOutputStream(excelFile);
            XSSFWorkbook workbook = new XSSFWorkbook();

            XSSFSheet sheet = workbook.createSheet("PatientData");
            Row columnHeader = sheet.createRow(0);
            Row row1 = sheet.createRow(1);

            Cell cell = null;
            for (int i = 0; i < 2; i++){
                for (int j = 0; j < 6; j++){
                    if (i == 0){
                        switch(j){
                            case 0:
                                cell = columnHeader.createCell(j, CellType.STRING);
                                cell.setCellValue("Hospital Name");
                                cell = row1.createCell(j, CellType.STRING);
                                cell.setCellValue(excel.hospitalName);
                                break;
                            case 1:
                                cell = columnHeader.createCell(j, CellType.STRING);
                                cell.setCellValue("Patient Name");
                                cell = row1.createCell(j, CellType.STRING);
                                cell.setCellValue(excel.patientName);
                                break;
                            case 2:
                                cell = columnHeader.createCell(j, CellType.STRING);
                                cell.setCellValue("Patient Type");
                                cell = row1.createCell(j, CellType.STRING);
                                cell.setCellValue(excel.patientType);
                                break;
                            case 3:
                                cell = columnHeader.createCell(j, CellType.STRING);
                                cell.setCellValue("Bed Type");
                                cell = row1.createCell(j, CellType.STRING);
                                cell.setCellValue(excel.bedType);
                                break;
                            case 4:
                                cell = columnHeader.createCell(j, CellType.STRING);
                                cell.setCellValue("No Of Days");
                                cell = row1.createCell(j, CellType.NUMERIC);
                                cell.setCellValue(excel.numOfDays);
                                break;
                            case 5:
                                cell = columnHeader.createCell(j, CellType.STRING);
                                cell.setCellValue("Total Bed Charges");
                                cell = row1.createCell(j, CellType.NUMERIC);
                                cell.setCellValue(excel.totalBedCharges);
                                break;
                        }
                    }
                }
            }
            workbook.write(outPut);
            outPut.close();
            System.out.println("Excel file written successfully");
        }
        catch (FileNotFoundException ex){
            System.out.println("Error while writing to excel file. Error: " + ex.getMessage());
        }
    }
}
