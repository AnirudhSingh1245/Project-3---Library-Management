# 📚 Library Book Issue Management System

A desktop-based library management application built with **Java Swing, JDBC, and MySQL** for managing book issue records through a simple graphical interface.

<p align="center">

**Java Swing** • **MySQL** • **JDBC** • **CRUD Operations** • **Desktop Application**

</p>

---

## ✨ Overview

Library Book Issue Management System is a Java desktop application designed to manage library book-issue records using a graphical interface and MySQL database.

This project demonstrates:

- 🖥️ Java Swing GUI development
- 🗄️ MySQL database management
- 🔌 JDBC connectivity
- 🧩 CRUD operations
- ⚡ Event-driven programming
- 🧠 SQL queries with Java

---

## 🛠️ Tech Stack

| Technology | Purpose |
|------------|---------|
| ☕ Java | Application logic |
| 🎨 Java Swing | Graphical interface |
| 🔌 JDBC | Java-MySQL connectivity |
| 🗄️ MySQL | Data storage |
| 📝 SQL | Database operations |
| 🐙 Git & GitHub | Version control |

---

## 🏗️ Architecture

```text
┌─────────────────────────────┐
│        Java Swing GUI       │
│                             │
│  Insert │ Update │ Delete   │
│              │ Search       │
└──────────────┬──────────────┘
               │
              JDBC
               │
               ▼
┌─────────────────────────────┐
│           MySQL             │
│                             │
│       Library Database      │
│                             │
│            lib              │
└─────────────────────────────┘

🗄️ Database
The project includes a ready-to-import SQL file:

database/
└── library.sql

lib Table
Column	Type
id	INT
Name	VARCHAR(20)
book	VARCHAR(50)
Date_of_issue	DATE
Date_of_returning	DATE

The id column is used as the primary key.

📂 Project Structure
Project-3---Library-Management/
│
├── 📄 Project3.java
├── 📁 database/
│   └── 📄 library.sql
├── 📄 .gitignore
└── 📄 README.md

⚙️ Installation & Setup
1️⃣ Clone the Repository
git clone https://github.com/AnirudhSingh1245/Project-3---Library-Management.git

cd Project-3---Library-Management

2️⃣ Requirements
Make sure you have:

☕ Java JDK
🗄️ MySQL Server
🔌 MySQL Connector/J
You can use IntelliJ IDEA, Eclipse, NetBeans, or VS Code.

3️⃣ Setup Database
Create the database:

CREATE DATABASE Library;

USE Library;

Then import:

database/library.sql

4️⃣ Configure MySQL
Update the MySQL username and password in Project3.java.

⚠️ Do not upload real database passwords to GitHub.

5️⃣ Run
javac Project3.java

java Project3

Or run Project3.java directly from your IDE.

🧪 Sample Data
The SQL file includes sample library records:

ID    Name      Book
────────────────────────────────────
1     anni      Oliver Twist
2     anil      Around the World in 80 Days
3     anirudh   Tom Sawyer
4     janna     Phantom
5     Kalu      Fight Club
9     anni      Harry Potter

🔐 Security Notes
For a production-ready version, consider:

🔒 Moving database credentials outside the source code
🛡️ Using PreparedStatement
✅ Validating user input
🧹 Improving exception handling
🧩 Separating GUI and database logic
🔮 Future Improvements
📚 Book management
👤 Member management
📖 Book availability tracking
⏰ Overdue book tracking
💰 Fine calculation
🔐 Login system
📊 Library statistics
🧠 What This Project Demonstrates
User Input
    ↓
Java Swing
    ↓
Event Listener
    ↓
JDBC
    ↓
SQL Query
    ↓
MySQL
    ↓
Result

Java → Swing → JDBC → SQL → MySQL

👨‍💻 Author
Anirudh Singh

GitHub: @AnirudhSingh1245

⭐ If you like this project, consider giving it a star!
