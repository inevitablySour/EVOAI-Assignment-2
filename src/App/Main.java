package Assignment_2.App;

import Assignment_2.Database.DatabaseController;
import Assignment_2.Database.DatabaseInitializer;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        DatabaseController db = new DatabaseController();
        DatabaseInitializer di = null;
        Scanner scanner = new Scanner(System.in);
        File f = new File("/Users/joelkumi/Documents/Development/EVOAI-Assignment-2/src/Database/booktracker.db");
        if(f.exists() && !f.isDirectory()) {
            System.out.println("Use Saved Databse? (y/n): ");
            String databasePreference= "";
            try {
                databasePreference = scanner.nextLine();
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a letter (y/n).");
            }
            if (databasePreference.equalsIgnoreCase("n")) {
                di = new DatabaseInitializer();
            } else {
                System.out.println("Using Saved Databse");
            }
        }
        else{
            di = new DatabaseInitializer();
        }

        while (true) {
            System.out.println("\nBOOKTRACKER MENU");
            System.out.println("1. Add a new user");
            System.out.println("2. View reading habits for a user");
            System.out.println("3. Update a book title");
            System.out.println("4. Delete a reading habit");
            System.out.println("5. Get mean age of users");
            System.out.println("6. Get total users who read a book");
            System.out.println("7. Get total pages read");
            System.out.println("8. Get total users who read more than one book");
            System.out.println("9. Get total books recorded");
            System.out.println("10. Run a custom SQL query (for debugging)");
            System.out.println("11. Re-initialize the database (Resets the databse to the initial state)");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter User ID: ");
                    int userID = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Age: ");
                    int age = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter Gender (m/f): ");
                    String gender = scanner.nextLine();
                    System.out.print("Enter Name (or press Enter to leave empty): ");
                    String name = scanner.nextLine();
                    db.addUser(userID, age, gender, name.isEmpty() ? null : name);
                    break;

                case 2:
                    System.out.print("Enter User ID: ");
                    int userIdForHabits = Integer.parseInt(scanner.nextLine());
                    db.getUserReadingHabits(userIdForHabits);
                    break;

                case 3:
                    System.out.print("Enter current book title: ");
                    String oldTitle = scanner.nextLine();
                    System.out.print("Enter new book title: ");
                    String newTitle = scanner.nextLine();
                    db.updateBookTitle(oldTitle, newTitle);
                    break;

                case 4:
                    System.out.print("Enter Habit ID to delete: ");
                    int habitID = Integer.parseInt(scanner.nextLine());
                    db.deleteReadingHabit(habitID);
                    break;

                case 5:
                    db.getMeanAge();
                    break;

                case 6:
                    System.out.print("Enter book title: ");
                    String bookTitle = scanner.nextLine();
                    db.getUsersReadingBook(bookTitle);
                    break;

                case 7:
                    db.getTotalPagesRead();
                    break;

                case 8:
                    db.getUsersReadingMultipleBooks();
                    break;

                case 9:
                    db.getTotalBooksRead();
                    break;

                case 10:
                    System.out.print("Enter SQL query: ");
                    String customQuery = scanner.nextLine();
                    db.query(customQuery);
                    break;

                case 11:
                    if (di == null) {
                        di = new DatabaseInitializer();
                    }
                    System.out.println("\nRe-Initializing Database");
                    di.initializeDatabase();
                    break;

                case 0:
                    System.out.println("Exiting BookTracker. Goodbye!");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid choice. Please choose a valid option.");
            }
        }
    }
}
