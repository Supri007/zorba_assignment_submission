

import java.sql.Connection;
import java.util.Scanner;

public interface IDBLoad {
    Object getInputFromUser(Scanner sc);
    String insertDataToDB(Connection con, Object data);
}
