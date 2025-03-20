package Assignment_2.Database;

import java.sql.*;

public class DatabaseController {
    private static final String DB_URL = "jdbc:sqlite:src/Database/booktracker.db";

    public void addUser(int userID, int age, String gender, String name) {
        String query = "INSERT INTO User (userID, age, gender, name) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userID);
            pstmt.setInt(2, age);
            pstmt.setString(3, gender);
            if (name != null && !name.isEmpty()) {
                pstmt.setString(4, name);
            } else {
                pstmt.setNull(4, Types.VARCHAR);
            }
            pstmt.executeUpdate();
            System.out.println("User added successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve all reading habit data for a given user
    public void getUserReadingHabits(int userID) {
        String query = "SELECT * FROM ReadingHabit WHERE userID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                System.out.println("Habit ID: " + rs.getInt("habitID") +
                        ", Pages Read: " + rs.getInt("pagesRead") +
                        ", Book: " + rs.getString("book") +
                        ", Submission Date: " + rs.getString("submissionMoment"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update a book title in the database
    public void updateBookTitle(String oldTitle, String newTitle) {
        String checkQuery = "SELECT COUNT(*) FROM ReadingHabit WHERE book = ?";
        String updateQuery = "UPDATE ReadingHabit SET book = ? WHERE book = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
             PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {

            checkStmt.setString(1, oldTitle);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                updateStmt.setString(1, newTitle);
                updateStmt.setString(2, oldTitle);
                int affectedRows = updateStmt.executeUpdate();
                System.out.println(affectedRows + " records updated.");
            } else {
                System.out.println("No matching book title found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to delete a reading habit record
    public void deleteReadingHabit(int habitID) {
        String query = "DELETE FROM ReadingHabit WHERE habitID = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, habitID);
            int affectedRows = pstmt.executeUpdate();
            System.out.println(affectedRows + " record deleted.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get the mean age of users (SQL calculation)
    public void getMeanAge() {
        String query = "SELECT AVG(age) AS meanAge FROM User";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                System.out.println("Mean age of users: " + rs.getDouble("meanAge"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get total users who have read a specific book
    public void getUsersReadingBook(String bookTitle) {
        String query = "SELECT COUNT(DISTINCT userID) AS totalUsers FROM ReadingHabit WHERE book = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, bookTitle);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                System.out.println("Total users who read \"" + bookTitle + "\": " + rs.getInt("totalUsers"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get total pages read by all users
    public void getTotalPagesRead() {
        String query = "SELECT SUM(pagesRead) AS totalPages FROM ReadingHabit";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                System.out.println("Total pages read by all users: " + rs.getInt("totalPages"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to get the total number of users who have read more than one book
    public void getUsersReadingMultipleBooks() {
        String query = "SELECT COUNT(userID) AS userCount FROM (SELECT userID FROM ReadingHabit GROUP BY userID HAVING COUNT(DISTINCT book) > 1) AS temp";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                System.out.println("Users who read more than one book: " + rs.getInt("userCount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getTotalBooksRead() {
        String query = "SELECT COUNT(DISTINCT book) AS totalBooks FROM ReadingHabit";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                System.out.println("Total books recorded in database: " + rs.getInt("totalBooks"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void query(String query) {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            boolean isResultSet = stmt.execute(query); // Executes the query

            if (isResultSet) {
                try (ResultSet rs = stmt.getResultSet()) {
                    ResultSetMetaData metaData = rs.getMetaData();
                    int columnCount = metaData.getColumnCount();
                    while (rs.next()) {
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.print(metaData.getColumnName(i) + ": " + rs.getString(i) + " | ");
                        }
                        System.out.println();
                    }
                }
            } else {
                int affectedRows = stmt.getUpdateCount();
                System.out.println("Query executed. Rows affected: " + affectedRows);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
