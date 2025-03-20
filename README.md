# BookTracker - SQLite & Java

## Required Dependencies
This project requires the following dependencies:

### Maven Dependencies
Ensure the following dependencies are included in your `pom.xml`:

```xml
<dependencies>
    <dependency>
        <groupId>org.xerial</groupId>
        <artifactId>sqlite-jdbc</artifactId>
        <version>3.40.1.0</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.apache.poi/poi -->
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi</artifactId>
        <version>5.4.0</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.apache.poi/poi-ooxml -->
    <dependency>
        <groupId>org.apache.poi</groupId>
        <artifactId>poi-ooxml</artifactId>
        <version>5.4.0</version>
    </dependency>
</dependencies>
```


## How to Use the Program

### 1. Setup the Database
Running the Main.java class will automnatically initialize the database if the file is not found, otherwise it will ask you whether you want to use the data that has been saved already in the database or if you would like ot re-initialize the database with the data that was provided from the Microsoft Excel Sheet.

### 2. Running the Command-Line Interface
Compile and run `Main.java` to access the interactive menu.

```sh
javac Assignment_2/Database/Main.java
java Assignment_2.Database.Main
```

### 3. Functionalities
The program provides the following functionalities:

- **Add a User**: Enter user ID, age, gender, and optional name.
- **View Reading Habits**: Enter a user ID to display their reading history.
- **Update a Book Title**: Provide an existing book title and a new title.
- **Delete a Reading Habit**: Remove a reading habit entry by habit ID.
- **Get Mean Age of Users**: Calculates and displays the average age.
- **Get Users Reading a Specific Book**: Enter a book title to see how many users have read it.
- **Get Total Pages Read**: Displays the sum of all pages read by users.
- **Get Users Reading Multiple Books**: Counts users who have read more than one book.
- **Get Total Books Read**: Displays the number of distinct books in the database.
- **Run a Custom SQL Query**: Enter an SQL query to interact with the database directly.
- **Re-Initialize the Database**: Clears the current database and uses the data from the provided excel sheet to re-populate the fields.

### 4. Exiting the Program
To exit, select option `0` from the menu.