# 📚 Library Book Issue Management System

A modern, desktop-based **Library Book Issue Management System** built using Java Swing and MySQL. It provides a clean dark-themed interface for managing book issue records, member information, contact details, and return dates.

## ✨ Features

- **Dashboard:** View total records, active loans, books due today, and overdue records.
- **Issue a Book:** Add new book issue records with member and contact information.
- **Update Records:** Search by record ID and update existing records.
- **Delete Records:** Review record details and confirm deletion.
- **Search:** Instantly filter records by ID, member name, phone, email, address, or book title.
- **All Records:** View all stored records and sort them by column.
- **Automatic Loan Status:** Identify active loans, books due today, and overdue books.
- **Input Validation:** Validate IDs, phone numbers, email addresses, and dates.
- **MySQL Integration:** Persist records in a MySQL database using JDBC.
- **Demo Mode:** Launch the application without connecting to MySQL.
- **Custom UI:** Dark-themed interface with rounded buttons, status indicators, and styled tables.

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Application logic and object-oriented programming |
| Java Swing | Desktop graphical user interface |
| JDBC | Database connectivity |
| MySQL | Persistent record storage |
| IntelliJ IDEA / VS Code | Development environment |
| Git and GitHub | Version control and project hosting |

## 📋 Prerequisites

Install the following before running the application:

1. **JDK 17 or later** — required for modern Java language features used in the code.
2. **MySQL Server 8.x** — for storing library records.
3. **MySQL Connector/J** — the JDBC driver that allows Java to connect to MySQL.
4. **VS Code or IntelliJ IDEA** — an IDE or editor for Java development.

Useful official resources:

- Java JDK: https://jdk.java.net/
- MySQL Community Server: https://dev.mysql.com/downloads/mysql/
- MySQL Connector/J: https://dev.mysql.com/downloads/connector/j/
- Visual Studio Code: https://code.visualstudio.com/
- Java extensions for VS Code: https://code.visualstudio.com/docs/languages/java

## 🚀 Installation and Setup

### Step 1: Clone the Repository

Open a terminal and run:

```bash
git clone https://github.com/AnirudhSingh1245/Project-3---Library-Management.git
```

Move into the project directory:

```bash
cd Project-3---Library-Management
```

If your repository has a different name, replace the URL and directory name with your actual repository details.

### Step 2: Verify Java Installation

Check whether Java is installed:

```bash
java -version
javac -version
```

Both commands should work. If `javac` is not recognized, install a JDK and configure your `PATH`.

### Step 3: Install and Start MySQL

Install MySQL Community Server and ensure that the MySQL service is running.

Open the MySQL command-line client or MySQL Workbench and sign in using your MySQL credentials.

For a local command-line installation, you can use:

```bash
mysql -u root -p
```

Enter your MySQL password when prompted.

> **Important:** The Java code currently uses the `root` account with a hardcoded password. The password configured in `PROJECT2.java` must match your MySQL setup unless you modify the connection code.

### Step 4: Create the Database and Table

Run the following SQL statements in MySQL Workbench or the MySQL command-line client.

```sql
CREATE DATABASE IF NOT EXISTS Library;

USE Library;

CREATE TABLE IF NOT EXISTS lib (
    id INT NOT NULL PRIMARY KEY,
    Name VARCHAR(20) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(100) NOT NULL,
    address VARCHAR(255) NOT NULL,
    book VARCHAR(50) NOT NULL,
    Date_of_issue DATE NOT NULL,
    Date_of_returning DATE NOT NULL
);
```

The table structure matches the column names used by the application's SQL queries.

To verify that the table was created, run:

```sql
USE Library;

DESCRIBE lib;

SELECT * FROM lib;
```

The `SELECT` statement will initially return no records if the table is empty. Records will appear when you add them through the application.

**Database details:**

- Database name: `Library`
- Table name: `lib`
- Primary key: `id`
- Issue date column: `Date_of_issue`
- Return date column: `Date_of_returning`

### Step 5: Install MySQL Connector/J

Download MySQL Connector/J from:

https://dev.mysql.com/downloads/connector/j/

Extract the downloaded archive and locate the `.jar` file, named similarly to:

`mysql-connector-j-x.x.x.jar`

Create a folder named `lib` in the project root and copy the JAR file into it.

Your project should contain:

```text
Project-3---Library-Management/
├── lib/
│   └── mysql-connector-j-x.x.x.jar
├── src/
│   └── question/
│       └── company/
│           └── PROJECT2.java
└── README.md
```

Replace `x.x.x` with the actual downloaded version.

If your source file is currently in a different location, either organize it as shown above or adjust the commands below to match your actual directory structure.

### Step 6: Configure the Database Connection

In `PROJECT2.java`, find the `MySqlStore()` constructor. The current code uses:

```java
c = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/",
    "root",
    "happy"
);
```

Replace `happy` with your actual MySQL password if it differs.

The application then executes:

```java
Statement s = c.createStatement();
s.executeUpdate("use Library");
```

This selects the `Library` database.

**Security recommendation:** Avoid committing database passwords to GitHub. A better setup uses environment variables or a local configuration file excluded through `.gitignore`. The current `env()` helper method does not automatically configure the connection because the constructor does not use it yet.

### Step 7: Compile the Application

Open a terminal in the project root.

**Windows PowerShell or Command Prompt:**

```powershell
mkdir out
javac -cp "lib/*" -d out src/question/company/PROJECT2.java
```

**Ubuntu / Linux / macOS:**

```bash
mkdir -p out
javac -cp "lib/*" -d out src/question/company/PROJECT2.java
```

The compiled classes will be placed in the `out` directory.

If you receive a file-not-found error, verify the location of `PROJECT2.java` and update the source path in the command.

### Step 8: Run the Application

**Windows:**

```powershell
java -cp "out;lib/*" question.company.PROJECT2
```

**Ubuntu / Linux / macOS:**

```bash
java -cp "out:lib/*" question.company.PROJECT2
```

The application should open the Library Book Issue Management window.

If MySQL is unavailable or the credentials are incorrect, the application displays a connection error.

### Step 9: Run Without MySQL (Demo Mode)

You can start the application in demo mode without a database connection.

**Windows:**

```powershell
java -cp "out;lib/*" question.company.PROJECT2 --demo
```

**Ubuntu / Linux / macOS:**

```bash
java -cp "out:lib/*" question.company.PROJECT2 --demo
```

Demo mode uses an in-memory data store. Records created in this mode are temporary and disappear when the application closes.

The current `MemoryStore` starts empty; add records through the interface to test the features.

## 🖥️ How to Use the Application

### 1. Dashboard

The dashboard displays the total number of records, active loans, records due today, and overdue records. It also displays recently issued records.

### 2. Issue a Book

1. Open **Issue Book** from the sidebar.
2. Enter a unique record ID.
3. Enter the member's name, phone number, email, and address.
4. Enter the book title.
5. Enter the issue date and return date in `YYYY-MM-DD` format.
6. Click **Save record**.

### 3. Update a Record

1. Open **Update Record**.
2. Enter an existing record ID.
3. Click **Load record**.
4. Edit the required fields.
5. Click **Save changes**.

### 4. Delete a Record

1. Open **Delete Record**.
2. Enter the record ID and click **Find record**.
3. Review the displayed details.
4. Click **Delete record** and confirm the operation.

Deletion permanently removes the corresponding record from the database.

### 5. Search Records

Open **Search** and type a keyword. The table filters matching records across its columns automatically.

### 6. View All Records

Open **All Records** to view all stored records. Click a column header to sort the results.

## ✅ Validation Rules

- Record IDs must be positive whole numbers and unique.
- Member names and book titles are required.
- Phone numbers must contain between 7 and 15 digits.
- Email addresses must follow a valid basic email format.
- Addresses are required.
- Dates must use `YYYY-MM-DD`.
- The return date cannot be earlier than the issue date.

## 🔧 Troubleshooting

### MySQL connection failed

Check that:

- MySQL Server is installed and running.
- Your username and password are correct.
- MySQL is listening on `localhost:3306`.
- The `Library` database exists.
- The database user has the necessary permissions.

### `No suitable driver found`

Ensure MySQL Connector/J is present in the `lib` directory and included in the runtime classpath.

### `Unknown database 'Library'`

Run the database creation SQL from Step 4.

### `Table 'Library.lib' doesn't exist`

Run the `CREATE TABLE` statement from Step 4 after selecting the `Library` database.

### `Duplicate entry` or an existing ID error

Each record must have a unique primary key. Choose another unused record ID.

### `javac` or `java` is not recognized

Install a JDK and configure the system `PATH`, then reopen your terminal.

### Changes disappear after closing the application

Check whether you launched the application with `--demo`. The in-memory store does not save records permanently.

## 📁 Project Structure

```text
Project-3---Library-Management/
├── src/
│   └── question/
│       └── company/
│           └── PROJECT2.java
├── database/
│   └── library.sql
├── lib/
│   └── mysql-connector-j-x.x.x.jar
├── out/                    # Generated class files
├── .gitignore
└── README.md
```

This is the recommended structure. Adapt it to the actual files in your repository. The `database/library.sql` file should contain the SQL setup script from Step 4.

## 🔐 Security Notes

- Do not publish database passwords or other secrets in source code.
- Avoid using the MySQL `root` account for normal application access.
- Create a dedicated database user with only the required permissions.
- Keep credentials in environment variables or an ignored local configuration file.
- Do not commit generated `.class` files, downloaded dependency JARs, or local secrets unless your project intentionally manages them in version control.

## 🚧 Possible Future Improvements

- Fine-grained member and book management.
- Separate book inventory and member tables.
- Export records to CSV or PDF.
- Book availability and return processing.
- Login and role-based access.
- Configurable database connection settings.
- Automated tests and improved exception logging.

## 👨‍💻 Author

**Anirudh Singh**

- GitHub: https://github.com/AnirudhSingh1245
- LinkedIn: https://www.linkedin.com/in/anirudh-singh-a3243a362/
- Portfolio: https://portfolio-beta-ten-5s0rrvqeux.vercel.app/

## 📄 License

This project is available for learning and personal development. Add a `LICENSE` file to the repository if you want to specify formal permissions for reuse and distribution.

---

⭐ If you find this project useful, consider starring the repository!
