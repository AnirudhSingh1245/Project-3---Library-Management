# 📚 Library Book Issue Management System

A desktop application built with **Java Swing** and **MySQL** for managing library book issue records. It has a dark-themed GUI and supports full CRUD operations (add, update, delete, search) plus a table view of every record, all through **JDBC**.

![Java](https://img.shields.io/badge/Java-Swing-orange?logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-JDBC-4479A1?logo=mysql&logoColor=white)
![Status](https://img.shields.io/badge/status-learning%20project-blue)

---

## ✨ Features

| Feature | What it does |
|---|---|
| **Insert Data** | Adds a new record: ID, member name, book name, issue date and return date |
| **Update Data** | Edits the name, book and dates of an existing record, found by ID |
| **Delete Data** | Removes a record by ID |
| **Search Data** | Looks up a single record by ID and shows all its details |
| **View All Records** | Shows every record in a scrollable table |
| **Navigation** | The main menu hides while a window is open and comes back when you press **Back** or close the window |
| **Custom UI** | Dark theme, styled buttons with hover effect and a custom window icon |

## 🛠️ Tech Stack

- **Language:** Java (JDK 8 or newer)
- **GUI:** Java Swing / AWT
- **Database:** MySQL
- **Connectivity:** JDBC with MySQL Connector/J (`com.mysql.cj.jdbc.Driver`)

## 📁 Project Structure

```
Project-3---Library-Management/
├── database/          # SQL script(s) for the Library database
├── Project3.java      # Main application (all windows and database logic)
├── .gitignore
└── README.md
```

## 🗄️ Database Schema

The app uses a database named `Library` with a single table, `lib`:

```sql
CREATE DATABASE IF NOT EXISTS Library;
USE Library;

CREATE TABLE lib (
    id                INT PRIMARY KEY,
    Name              VARCHAR(100),
    book              VARCHAR(150),
    Date_of_issue     DATE,
    Date_of_returning DATE
);
```

> The same script should be in the [`database/`](database) folder. Dates use the `YYYY-MM-DD` format.

## 🚀 Getting Started

### Prerequisites

- [JDK 8+](https://adoptium.net/) installed
- [MySQL Server](https://dev.mysql.com/downloads/mysql/) running on `localhost:3306`
- [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) (the JDBC driver `.jar`)

### Setup

1. **Clone the repository**
   ```bash
   git clone https://github.com/AnirudhSingh1245/Project-3---Library-Management.git
   cd Project-3---Library-Management
   ```

2. **Create the database.** Run the script from the `database/` folder (or the schema above) in MySQL:
   ```bash
   mysql -u root -p < database/<script-name>.sql
   ```

3. **Set your MySQL credentials.** In `Project3.java`, find the connection line and use your own username and password:
   ```java
   Connection c = DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "your_password");
   ```

4. **Add the JDBC driver** to the project classpath:
   - **IDE (IntelliJ / Eclipse / NetBeans):** add the Connector/J `.jar` as a library, then run `Project3`.
   - **Command line:**
     ```bash
     # Windows uses ";"   macOS / Linux uses ":"
     javac -cp ".;mysql-connector-j-<version>.jar" Project3.java
     java  -cp ".;mysql-connector-j-<version>.jar" Project3
     ```
     If `Project3.java` starts with a `package` line, compile from the source root and run it by its full package name.

5. **Window icon.** The code loads `Bookshelf.png` from the same folder as the class. Put the image there, or the app will fail to start.

## 🖱️ How to Use

1. Launch the app to open the **main menu**.
2. Pick an action:
   - **Insert Data:** fill in all five fields and press **Submit**.
   - **Update Data:** enter the ID of an existing record plus the new values, then press **Update**.
   - **Delete Data:** enter the ID and press **Submit**.
   - **Search Data:** enter an ID and press **Search** to see that record.
   - **View All Records:** browse every record in a table.
3. Press **Back** (or close the window) to return to the main menu.

<!-- Add screenshots here, e.g.
![Main Menu](screenshots/menu.png)
![View Records](screenshots/view.png)
-->

## ⚠️ Known Limitations

This project is meant for learning, and a few things are not production-ready yet:

- Queries are built by joining text-field input into SQL strings, which is open to **SQL injection**. `PreparedStatement` should be used instead.
- Database credentials are written directly in the source code.
- There is no input validation, so an empty field, a text ID or a wrongly formatted date can cause an error.
- Update and Delete show a success message even if no record matched the ID.
- Windows use fixed positions (`setLayout(null)`), so the layout is tuned for one screen size.

## 🔮 Future Improvements

- [ ] Use `PreparedStatement` for all queries
- [ ] Validate inputs and show clear error messages
- [ ] Load credentials from a config file or environment variables
- [ ] Add a return-book workflow with overdue tracking
- [ ] Use layout managers for a responsive UI
- [ ] Split the code into separate UI and database classes

## 👤 Author

**Anirudh Singh**
GitHub: [@AnirudhSingh1245](https://github.com/AnirudhSingh1245)

---

⭐ If you found this project useful, consider giving it a star.
