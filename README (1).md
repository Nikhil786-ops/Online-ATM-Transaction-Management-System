# Online ATM Transaction Management System

A web-based ATM simulation built as a mini project for **Advanced Internet Programming**. The application demonstrates how Java Servlets, JSP, JDBC, and PostgreSQL can work together to support basic ATM operations and a separate administrator panel.

> **Academic/demo project:** This application is intended for learning and local testing. It is not production-ready banking software. Do not use real customer data or real banking credentials.

## Table of Contents

- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Installation and Setup](#installation-and-setup)
- [Database Setup](#database-setup)
- [Database Configuration](#database-configuration)
- [Run the Project](#run-the-project)
- [Demo Login Details](#demo-login-details)
- [Test Checklist](#test-checklist)
- [Troubleshooting](#troubleshooting)
- [Important Security Notes](#important-security-notes)
- [Learning Concepts](#learning-concepts)

## Features

### User module
- Login using an account number and PIN.
- View the current account balance.
- Deposit money into an account.
- Withdraw money, with a check for insufficient balance.
- View the latest 10 transactions in a mini statement.
- Change the account PIN.
- Log out and invalidate the user session.

### Admin module
- Separate administrator login.
- View all accounts.
- Create a new account.
- Prevent duplicate account numbers.
- Delete an account and its associated transaction records.
- Log out of the admin session.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java (JDK 17 or higher) | Application logic |
| Java Servlets (`jakarta.servlet`) | Request handling and controllers |
| JSP | Dynamic web pages and views |
| JDBC | Database connectivity |
| PostgreSQL | Relational database |
| PostgreSQL JDBC Driver | Connects Java to PostgreSQL |
| Apache Tomcat 11 | Servlet/JSP web server |
| Eclipse IDE for Enterprise Java and Web Developers | Development environment |
| HTML and CSS | User interface |

**Compatibility note:** Tomcat 10 and later use `jakarta.servlet.*`, not `javax.servlet.*`. The source code should use the `jakarta` namespace and a compatible Tomcat runtime.

## Architecture

The project follows a simple MVC-style flow:

```text
Browser
   |
   v
Apache Tomcat
   |
   v
Servlet (Controller)
   |
   v
JDBC
   |
   v
PostgreSQL (Database)

Servlet --forwards data--> JSP (View) --> Browser
```

- **Model/data layer:** PostgreSQL tables accessed through JDBC.
- **Controller:** Java Servlets process requests and coordinate database operations.
- **View:** JSP pages display forms, balances, transaction history, and admin account information.

## Project Structure

The project is named `OnlineATMSystem`.

```text
OnlineATMSystem/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── atm/
│       │           ├── util/
│       │           │   └── DBConnection.java
│       │           └── servlet/
│       │               ├── LoginServlet.java
│       │               ├── TransactionServlet.java
│       │               ├── StatementServlet.java
│       │               ├── PinChangeServlet.java
│       │               ├── LogoutServlet.java
│       │               ├── AdminLoginServlet.java
│       │               ├── AdminServlet.java
│       │               └── AdminLogoutServlet.java
│       └── webapp/
│           ├── css/
│           │   └── style.css
│           ├── index.jsp
│           ├── login.jsp
│           ├── dashboard.jsp
│           ├── statement.jsp
│           ├── changepin.jsp
│           ├── adminlogin.jsp
│           ├── admin.jsp
│           └── WEB-INF/
│               ├── web.xml
│               └── lib/
│                   └── postgresql-42.x.x.jar
└── README.md
```

> The exact layout may vary depending on how the Dynamic Web Project was created in Eclipse. The supplied setup also mentions `web.xml`. The optional `TestDBServlet.java` is only for testing the database connection and should be removed before submission if you added it.

## Prerequisites

Install the following before starting:

1. **JDK 17 or higher** — verify in a terminal or Command Prompt:
   ```bash
   java -version
   ```
2. **Eclipse IDE for Enterprise Java and Web Developers.**
3. **Apache Tomcat 11** — download and extract it to a local folder.
4. **PostgreSQL** and **pgAdmin 4**.
5. **PostgreSQL JDBC Driver** — download a compatible driver from [jdbc.postgresql.org](https://jdbc.postgresql.org/).

## Installation and Setup

### 1. Configure Tomcat in Eclipse

1. Open Eclipse.
2. Go to **Window → Preferences → Server → Runtime Environments**.
3. Click **Add**.
4. Select **Apache Tomcat v11.0**, then click **Next**.
5. Select the extracted Tomcat installation directory and the appropriate JRE.
6. Click **Finish**.

### 2. Create the Eclipse project

1. Select **File → New → Dynamic Web Project**.
2. Set the project name to `OnlineATMSystem`.
3. Select **Tomcat v11.0** as the target runtime.
4. If available, select **Generate web.xml deployment descriptor**.
5. Finish creating the project.
6. Create the Java packages:
   - `com.atm.util`
   - `com.atm.servlet`
7. Create the `css` directory inside `src/main/webapp` (or the web content folder used by your Eclipse project).

If **New → Package** is not available, use **File → New → Other → Java → Package** and select the project's Java source folder.

### 3. Add the PostgreSQL JDBC driver

1. Download the PostgreSQL JDBC `.jar` file.
2. Place it in `src/main/webapp/WEB-INF/lib/`.
3. Confirm that the jar is included in the deployed web application's libraries.

The file name will contain a version number, for example `postgresql-42.x.x.jar`; use the actual downloaded file name.

## Database Setup

### 1. Create the database

Open pgAdmin 4 and create a database named:

```sql
atm_db
```

Open the **Query Tool for `atm_db`** and execute the following SQL.

### 2. Create tables

```sql
CREATE TABLE accounts (
    id SERIAL PRIMARY KEY,
    account_no VARCHAR(12) UNIQUE NOT NULL,
    name VARCHAR(50) NOT NULL,
    pin VARCHAR(10) NOT NULL,
    balance NUMERIC(12,2) DEFAULT 0
);

CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    account_id INT NOT NULL REFERENCES accounts(id),
    type VARCHAR(20) NOT NULL,
    amount NUMERIC(12,2) NOT NULL,
    txn_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE admins (
    id SERIAL PRIMARY KEY,
    username VARCHAR(30) UNIQUE NOT NULL,
    password VARCHAR(30) NOT NULL
);
```

### 3. Insert demo records

```sql
INSERT INTO accounts (account_no, name, pin, balance)
VALUES ('1001', 'Tanish', '1234', 5000);

INSERT INTO admins (username, password)
VALUES ('admin', 'admin123');
```

Verify that the sample account was inserted:

```sql
SELECT * FROM accounts;
```

> Run the schema and demo inserts once in the `atm_db` database. If you run the inserts again, the unique account number or admin username may cause a duplicate-key error.

## Database Configuration

Open `com.atm.util.DBConnection.java`. Configure the connection for your local PostgreSQL installation:

```java
package com.atm.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {
    private static final String URL =
            "jdbc:postgresql://localhost:5432/atm_db";
    private static final String USER = "postgres";
    private static final String PASSWORD = "your_postgres_password";

    public static Connection getConnection() throws Exception {
        Class.forName("org.postgresql.Driver");
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
```

Replace `your_postgres_password` with your local PostgreSQL password.

**Before pushing to GitHub:** do not commit your actual database password. For a learning project, you can keep a `DBConnection.example.java` file with placeholders and load real credentials from environment variables or an untracked local configuration file. If a password has already been committed, changing the file alone does not remove it from Git history; rotate the exposed password.

## Run the Project

1. Save all files in Eclipse (`Ctrl + S`).
2. Make sure PostgreSQL is running and the `atm_db` database and tables exist.
3. Check that `DBConnection.java` has the correct local database credentials.
4. Right-click the project and select **Run As → Run on Server**.
5. Select the configured Tomcat 11 server and finish.
6. Open the application in your browser:

   [http://localhost:8080/OnlineATMSystem/](http://localhost:8080/OnlineATMSystem/)

The port may differ if your local Tomcat server is configured to use another port.

### Optional database connection test

If you add the `TestDBServlet` described in the setup notes, run the application and open:

```text
http://localhost:8080/OnlineATMSystem/testdb
```

A successful connection displays `DB connected!`. Remove the test servlet before final submission if it is no longer needed.

## Demo Login Details

These are the sample values inserted by the SQL script. They are for local demonstration only.

| Role | Account / Username | PIN / Password |
|---|---|---|
| User | Account number: `1001` | PIN: `1234` |
| Admin | Username: `admin` | Password: `admin123` |

Change or remove demo credentials before sharing a deployed application. Never use these sample credentials for a real system.

## Test Checklist

After starting the application, verify the following scenarios.

| # | Test | Expected result |
|---:|---|---|
| 1 | Log in with account `1001` and PIN `1234` | Dashboard displays the account balance |
| 2 | Enter an incorrect PIN | Invalid account/PIN message appears |
| 3 | Deposit `1000` | Balance increases by `1000` |
| 4 | Withdraw `2000` | Balance decreases by `2000` |
| 5 | Withdraw more than the available balance | Insufficient-balance message appears |
| 6 | Open Mini Statement | Up to the latest 10 transactions appear |
| 7 | Change the PIN from `1234` to `4321` | Success message appears; the new PIN should work |
| 8 | Log out and attempt to open the dashboard | User is redirected to login |
| 9 | Log in as admin | Account list appears |
| 10 | Create account `1002` | New account appears in the list |
| 11 | Try creating a duplicate account number | Duplicate-account error appears |
| 12 | Delete an account | Account and its transaction records are deleted |

Use only test data when trying deposits, withdrawals, PIN changes, or account deletion.

## Troubleshooting

| Problem | Possible cause | Suggested fix |
|---|---|---|
| `jakarta.servlet` cannot be resolved | Tomcat runtime is missing from the build path | Open **Project → Properties → Java Build Path → Libraries → Add Library → Server Runtime**, then select Tomcat 11 |
| Servlet URL returns 404 | Incorrect mapping, package, or deployment | Check `@WebServlet` mappings and Eclipse errors; clean and restart the server |
| Application root returns 404 | Welcome page or context path is incorrect | Confirm `index.jsp` exists and check the deployed context path |
| `ClassNotFoundException: org.postgresql.Driver` | JDBC jar is missing from the deployed application | Put the driver jar in `WEB-INF/lib` and redeploy |
| `password authentication failed` | Database username or password is incorrect | Correct the credentials in the connection configuration |
| Relation/table `accounts` does not exist | SQL was run in the wrong database or tables were not created | Run the schema in the Query Tool connected to `atm_db` |
| HTTP 405 at `/changepin` | The servlet accepts POST requests only | Open `changepin.jsp` from the dashboard and submit the form |
| CSS is not applied | Stylesheet path or deployment is incorrect | Check `webapp/css/style.css`, the link path, and refresh the browser with `Ctrl + F5` |
| Changes do not appear | Old deployment or server state | Save files, clean/re-publish the project, and restart Tomcat |

## Important Security Notes

This is an educational demonstration, not a secure banking implementation. The supplied code uses simple PIN/password storage and should **not** be used with real financial data.

Before adapting it beyond a classroom demo, consider:
- Store passwords and PINs using an appropriate secure hashing approach; never store them as plain text.
- Keep database credentials and other secrets out of source control.
- Add server-side validation for all input, including numeric values and allowed actions.
- Use secure session handling, authorization checks, CSRF protection, and appropriate cookie settings.
- Use HTTPS for deployed applications.
- Apply least-privilege database permissions and handle errors without exposing internal details.
- Review transaction rollback and concurrency handling for money-related updates.
- Add audit logging and tests before any real-world use.

## Learning Concepts

This project provides practice with:

- Servlet lifecycle and request handling (`doGet`, `doPost`).
- JSP pages and form processing.
- JDBC connections, `PreparedStatement`, and `ResultSet`.
- Session management with `HttpSession`.
- Request forwarding with `RequestDispatcher.forward()` and navigation with `sendRedirect()`.
- SQL operations: `SELECT`, `INSERT`, `UPDATE`, and `DELETE`.
- Database transactions using `setAutoCommit(false)` and `commit()`.
- Basic MVC separation between controllers, views, and database access.

---

## Author / Contribution

This repository is a learning project for the **Advanced Internet Programming** subject. Contributions, suggestions, and bug reports are welcome.

If you fork or adapt this project, please keep credentials private and document any changes to the setup instructions.
