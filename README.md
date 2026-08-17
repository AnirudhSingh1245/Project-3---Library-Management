📚 Library Book Issue Management System

A desktop-based library management application built with Java Swing, JDBC, and MySQL for managing book issue records through a simple graphical interface.

<p align="center">

Java Swing • MySQL • JDBC • CRUD Operations • Desktop Application

</p>
✨ Overview

Library Book Issue Management System is a Java desktop application designed to simplify the management of library book-issue records.

The application provides a graphical interface through which library data can be managed and stored in a MySQL database.

This project was created to practice and demonstrate:

🖥️ Java Swing GUI development
🗄️ MySQL database management
🔌 JDBC database connectivity
🧩 CRUD operations
⚡ Event-driven programming
🧠 SQL query execution from Java
📁 Database-backed desktop applications
🛠️ Tech Stack
Technology	Purpose
☕ Java	Application logic
🎨 Java Swing	Graphical user interface
🔌 JDBC	Java ↔ MySQL connectivity
🗄️ MySQL	Persistent data storage
📝 SQL	Database operations
🐙 Git & GitHub	Version control
🏗️ Application Architecture
┌─────────────────────────────┐
│       Java Swing GUI        │
│                             │
│  Insert │ Update │ Delete   │
│              │ Search       │
└──────────────┬──────────────┘
               │
               │ JDBC
               ▼
┌─────────────────────────────┐
│           MySQL             │
│                             │
│       Library Database      │
│                             │
│          ┌───────┐          │
│          │  lib  │          │
│          └───────┘          │
└─────────────────────────────┘

🗄️ Database

The project includes a ready-to-import SQL dump:

database/
└── library.sql


The database contains a table named:

lib

lib Table
Column	Type
id	INT
Name	VARCHAR(20)
book	VARCHAR(50)
Date_of_issue	DATE
Date_of_returning	DATE

id is used as the primary key for each library issue record.

The SQL dump also contains sample records for testing.

📂 Project Structure
Project-3---Library-Management/
│
├── 📄 Project3.java
│   └── Main Java Swing application
│
├── 📁 database/
│   └── library.sql
│       └── MySQL database schema + sample data
│
├── 📄 .gitignore
│
└── 📄 README.md

⚙️ Installation & Setup
1️⃣ Clone the Repository
git clone https://github.com/AnirudhSingh1245/Project-3---Library-Management.git
cd Project-3---Library-Management

2️⃣ Install Requirements

Make sure you have:

☕ Java JDK
🗄️ MySQL Server
🔌 MySQL JDBC Driver

You can use IntelliJ IDEA, Eclipse, NetBeans, or VS Code.

3️⃣ Create the Database
CREATE DATABASE Library;
USE Library;


Then import:

database/library.sql

4️⃣ Configure MySQL Connection

The application connects to:

jdbc:mysql://localhost:3306/


and uses the Library database.

⚠️ Important: Keep your MySQL username and password private. Avoid committing real credentials to GitHub.

5️⃣ Add MySQL JDBC Driver

Make sure MySQL Connector/J is available on the project's classpath.

▶️ Run the Application
javac Project3.java
java Project3


Or run Project3.java directly from your IDE.

🧪 Example Data

The included SQL dump contains sample records such as:

ID    Name      Book
────────────────────────────────────────
1     anni      Oliver Twist
2     anil      Around the World in 80 Days
3     anirudh   Tom Sawyer
4     janna     Phantom
5     Kalu      Fight Club
9     anni      Harry Potter

🔐 Security Notes

This project is intended primarily as a learning/academic project.

For a production-ready application, consider:

🔒 Moving database credentials outside the source code
🛡️ Using PreparedStatement
✅ Validating user input
🧹 Improving exception handling
📝 Adding proper logging
🧩 Separating GUI, database, and business logic
🔮 Future Improvements

The project can be expanded with:

📚 Library
Add/remove books
Manage authors
Track available copies
Track borrowed books
Track overdue books
Automatic return-date calculation
👤 Members
Member registration
Member profiles
Borrowing history
Active loans
Fine calculation
🧠 What This Project Demonstrates

This project demonstrates how a Java desktop application communicates with a relational database:

User Input
    ↓
Java Swing Interface
    ↓
Event Listener
    ↓
JDBC
    ↓
SQL Query
    ↓
MySQL Database
    ↓
Result / Confirmation


Java → Swing → JDBC → SQL → MySQL

👨‍💻 Author

Anirudh Singh

GitHub: @AnirudhSingh1245

⭐ If you found this project useful, consider giving it a star!
