package Assignment_2.Database;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseInitializer {
    private static final String DB_URL = "jdbc:sqlite:src/Database/booktracker.db";
    private static final String EXCEL_PATH = "src/Database/reading_habits_dataset.xlsx";

    public DatabaseInitializer(){
        initializeDatabase();
    }
    public static void initializeDatabase(){
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Drop existing tables to prevent duplication
            stmt.execute("DROP TABLE IF EXISTS ReadingHabit;");
            stmt.execute("DROP TABLE IF EXISTS User;");

            // Create User table
            String createUserTable = "CREATE TABLE IF NOT EXISTS User ("
                    + "userID INTEGER PRIMARY KEY, "
                    + "age INTEGER NOT NULL, "
                    + "gender TEXT NOT NULL, "
                    + "name TEXT NULL"
                    + ");";  // <- Ensure proper closure
            stmt.execute(createUserTable);

            // Create ReadingHabit table
            String createReadingHabitTable = "CREATE TABLE IF NOT EXISTS ReadingHabit ("
                    + "habitID INTEGER PRIMARY KEY, "
                    + "userID INTEGER NOT NULL, "
                    + "pagesRead INTEGER NOT NULL, "
                    + "book TEXT NOT NULL, "
                    + "submissionMoment DATETIME NOT NULL, "
                    + "FOREIGN KEY (userID) REFERENCES User(userID) ON DELETE CASCADE"
                    + ");";  // <- Ensure no extra commas before closing
            stmt.execute(createReadingHabitTable);

            // Read and insert data from Excel file
            readAndInsertData(conn);

            System.out.println("Database setup completed successfully with Excel data.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void readAndInsertData(Connection conn) {
        try (FileInputStream fis = new FileInputStream(new File(EXCEL_PATH));
             Workbook workbook = new XSSFWorkbook(fis)) {

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            // Read User sheet
            Sheet userSheet = workbook.getSheet("User");
            String userQuery = "INSERT INTO User (userID, age, gender, name) VALUES (?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(userQuery)) {
                for (Row row : userSheet) {
                    if (row.getRowNum() == 0) continue; // Skip header
                    pstmt.setInt(1, (int) row.getCell(0).getNumericCellValue()); // userID
                    pstmt.setInt(2, (int) row.getCell(1).getNumericCellValue()); // age
                    pstmt.setString(3, row.getCell(2).getStringCellValue()); // gender

                    // Handle name column (can be NULL)
                    Cell nameCell = row.getCell(3);
                    if (nameCell != null && nameCell.getCellType() == CellType.STRING) {
                        pstmt.setString(4, nameCell.getStringCellValue());
                    } else {
                        pstmt.setNull(4, Types.VARCHAR);
                    }
                    pstmt.executeUpdate();
                }
            }

            // Read ReadingHabit sheet
            Sheet habitSheet = workbook.getSheet("ReadingHabit");
            String habitQuery = "INSERT INTO ReadingHabit (habitID, userID, pagesRead, book, submissionMoment) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(habitQuery)) {
                for (Row row : habitSheet) {
                    if (row.getRowNum() == 0) continue; // Skip header
                    pstmt.setInt(1, (int) row.getCell(0).getNumericCellValue()); // habitID
                    pstmt.setInt(2, (int) row.getCell(1).getNumericCellValue()); // userID
                    pstmt.setInt(3, (int) row.getCell(2).getNumericCellValue()); // pagesRead

                    // Handle book column (STRING but might be NUMERIC)
                    Cell bookCell = row.getCell(3);
                    if (bookCell.getCellType() == CellType.STRING) {
                        pstmt.setString(4, bookCell.getStringCellValue());
                    } else if (bookCell.getCellType() == CellType.NUMERIC) {
                        pstmt.setString(4, String.valueOf((int) bookCell.getNumericCellValue())); // Convert to string
                    } else {
                        pstmt.setNull(4, Types.VARCHAR);
                    }

                    // Handle submissionMoment column (DATE conversion)
                    Cell dateCell = row.getCell(4);
                    if (dateCell != null) {
                        if (dateCell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(dateCell)) {
                            Date date = dateCell.getDateCellValue();
                            String formattedDate = dateFormat.format(date);
                            pstmt.setString(5, formattedDate);
                        } else if (dateCell.getCellType() == CellType.STRING) {
                            pstmt.setString(5, dateCell.getStringCellValue()); // If already a string, insert as is
                        } else {
                            pstmt.setNull(5, Types.VARCHAR);
                        }
                    }

                    pstmt.executeUpdate();
                }
            }

            System.out.println("Excel data successfully inserted into the database.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}