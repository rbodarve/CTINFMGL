# CTINFMGL Final Project: E-Games Digital Marketplace

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/)
[![SQLite](https://img.shields.io/badge/Database-SQLite-green.svg)](https://www.sqlite.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

**Course**: CTINFMGL (Information Management)  
**Project Type**: Final Project  

A comprehensive Java-based e-commerce platform demonstrating information management principles through CRUD operations, database schema design, advanced querying, and SQLite database management on a digital game marketplace system.

## Table of Contents

- [Course Overview](#course-overview)
- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Compilation & Execution](#compilation--execution)
- [Troubleshooting](#troubleshooting)
- [Simulation Test Run](#simulation-test-run-2026-10-08-javacjava-21012)
- [Proposed Simplifications](#proposed-simplifications-2026-10-11-not-applied)
- [Review Notes](#review-notes-2026-10-14)
- [License](#license)

## Course Overview

This final project for **CTINFMGL (Information Management)** demonstrates key concepts in database design and information management:

- **Database Schema Design**: Implementing normalized relational database structures
- **CRUD Operations**: Create, Read, Update, and Delete operations for data management
- **Data Integrity**: Using foreign keys and constraints to maintain data consistency
- **Query Optimization**: Complex SQL queries for meaningful data retrieval
- **Information Systems**: Building an integrated system for managing business information

### About the course

CTINFMGL (Information Management) is a 3-unit professional course in the BS Information
Technology program of the National University College of Computing and Information
Technologies. The prerequisite is CCDATRCL. The course covers the theory and the practical
skills of databases and database management systems in ICT applications.

**Course outcomes.** At the end of the course, the student must be able to:

1. Distinguish database concepts, structures and database development processes.
2. Develop a relational database integrated into a database management system.

**Topics (in order):** database systems → data models → the relational database model →
entity-relationship modelling → advanced data modelling → normalization → Structured
Query Language (SQL) → project presentation.

**Grading:** assessment tasks 40%, long exam 30%, project 30%. The project requirement is
to *develop and document a functional relational database management system that solves
a real-world business problem*. This repository is that project.

### How this project meets the final-project rubric

| Criterion (weight) | What the rubric asks for | Where this project shows it |
|---|---|---|
| Functionality (30%) | Full CRUD with error handling | All four CRUD menus work. Invalid input is re-prompted. Database errors print a `❌` message without a crash (100/100 simulation runs passed). |
| User Interface (20%) | Clear navigation and feedback | Numbered console menus with "Back" options, a `✅`, `⚠️` or `❌` message after each create, update and delete, and a "No … found" message when a report is empty. |
| Database Design (30%) | Normalized tables, correct relationships and constraints | Four tables in 3NF, primary keys, foreign keys with `ON DELETE CASCADE`, `UNIQUE` and `NOT NULL` constraints, and foreign-key enforcement turned on in the connection. |
| Documentation (20%) | Proposal, architecture, user manual, technical notes | This README (usage, structure, test results) plus the separate project documentation. |

### Required documentation topics mapped to the program

The course documentation format asks for these SQL topics. Each one is a menu option:

| Documentation topic | Menu option | SQL used |
|---|---|---|
| Create (INSERT) | Create → Add User / Seller / Game / Transaction | `INSERT` with prepared statements |
| Basic SELECT | Read → View All Users | `SELECT … ORDER BY Username` |
| Filtering and sorting | Read → View Games by Category | `WHERE Category = ? ORDER BY Price DESC` |
| Update (UPDATE) | Update → Update User Email | `UPDATE … WHERE UserID = ?` |
| Delete (DELETE) | Delete → Delete User | `DELETE …` (the user's transactions are removed by cascade) |
| Inner join + SUM | Advanced → Total Spending Per User | `JOIN`, `SUM`, `GROUP BY` |
| Subquery + AVG | Advanced → Users Above Average Spending | `HAVING … > (SELECT AVG(…) FROM (…))` |
| Left join | Advanced → All Users With Transactions | `LEFT JOIN` (users without purchases still appear) |
| Right join | Advanced → All Sellers With Games | `RIGHT JOIN` (needs SQLite 3.39 or later; the bundled driver is 3.49.1) |
| Left join + COUNT | Read → View Sellers with Games Count | `LEFT JOIN`, `COUNT`, `GROUP BY` |

### From proposal to implementation

The project proposal (March 2025) modelled four entities: Customer, Seller, Game and
Transaction. The program keeps the same four entities, with these changes:

| Proposal | Program | Reason |
|---|---|---|
| `Customer` table | `users` table with a `UserType` column (`Admin` or `Guest`) | One table holds both kinds of account. |
| `Seller.ContactNum` | `sellerinfo.ContactInfo` | Same data, renamed. |
| `Game` table | `gamesinfo` table | Same attributes. |
| `Transaction` stores `SellerID`, the game name and the price | `playstoretransaction` stores `GameID` and `TotalAmount` | The seller and the game name are reached through `GameID`. They are no longer stored twice, which removes a transitive dependency (3NF). |
| `FullName` and `Address` in `Customer` | Not stored | Not part of the implemented schema. |

## Features

**Core Functionality**
- **Create Operations**: Add users, sellers, games, and transactions to the marketplace
- **Read Operations**: Query and retrieve data from the system
- **Update Operations**: Change a user's email (the only update offered)
- **Delete Operations**: Remove a user; the user's transactions are deleted with it (cascade)
- **Advanced Queries**: Complex database searches and filtering

**Technical Features**
- SQLite database with persistent storage
- Full schema management with foreign key constraints
- Sample data initialization on first run
- Type-safe database operations
- Input validation with re-prompting for numbers, dates, user type and required (non-empty) fields; email, category and contact info are not checked
- Interactive command-line interface

## Requirements

- **Java**: JDK 21 or later
- **SQLite JDBC Driver**: 3.49.1.0 (included in `/lib` directory)
- **Build Tools**: javac (Java Compiler)

## Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/rbodarve/CTINFMGL.git
   cd CTINFMGL
   ```

2. **Verify Java version**:
   ```bash
   java -version
   # Expected: OpenJDK 21 or later
   ```

3. **Check SQLite JDBC driver**:
   ```bash
   ls lib/
   # Should show: sqlite-jdbc-3.49.1.0.jar
   ```

## Usage

### Compilation & Execution

**Compile all source files**:
```bash
javac -cp lib/sqlite-jdbc-3.49.1.0.jar src/*.java
```

**Run the application**:
```bash
java -cp "lib/sqlite-jdbc-3.49.1.0.jar;src" Main
```

> **Linux/macOS note**: the classpath separator is `:` instead of `;`, e.g.
> `java -cp lib/sqlite-jdbc-3.49.1.0.jar:src Main`

### Interactive Menu

Once running, the application presents a main menu:
```
=====================
E-Games: Digital Game Marketplace
=====================

=== MAIN MENU ===
1. Create Operations (Add data)
2. Read Operations (Query data)
3. Update Operations
4. Delete Operations
5. Advanced Queries
0. Exit
```

Navigate using numeric inputs (1-5 for operations, 0 to exit).

## Project Structure

```
CTINFMGL/
├── src/
│   ├── Main.java              # Application entry point & CLI
│   └── DatabaseManager.java   # Database connection & schema management
├── lib/
│   └── sqlite-jdbc-3.49.1.0.jar  # SQLite JDBC driver
├── database.db                 # SQLite database (generated on first run)
├── .gitignore                  # Ignores build artifacts & generated DB
├── LICENSE                     # MIT license
└── README.md                   # This file
```

## Troubleshooting

- Always compile with the SQLite driver on the classpath (see [Compilation & Execution](#compilation--execution) above).
- If you see "cannot find symbol: class DatabaseManager", ensure you're compiling all files together with the driver
- Clean compiled files before rebuilding:
  ```bash
  rm -f src/*.class
  ```

## Simulation test run (2026-10-08, javac/java 21.0.12)

Compiled and run with the commands above, in a temporary folder per run, so the local
`database.db` (git-ignored, not tracked) stayed unchanged. The program was run **100 times** with scripted stdin:
85 fixed scenarios and 15 random read-only menu walks (seed 42). 90 runs started from an
empty folder (fresh schema and sample data); 10 used a copy of `database.db`.
The scenarios cover every menu option, invalid and empty input, out-of-range numbers,
bad dates, end of input at a prompt, CRLF input, SQL-injection text, Unicode, foreign-key
and UNIQUE violations, and delete cascades. A run passed when it exited with code 0
within 20 s, printed no Java stack trace, reached "Goodbye", and printed `❌` only where
a database error was expected.

**Result:** 100/100 passed. Invalid input is re-prompted, missing user IDs give
`⚠️ No user found`, foreign-key violations give a `❌` message without a crash, and
prepared statements store injection text as plain data.

**Findings (recorded, not fixed):**

| # | Where | Finding |
|---|-------|---------|
| 1 | `Main.addUser` (`src/Main.java:76`) | An empty email is stored as `""`, not `NULL`. `users.Email` is `UNIQUE`, so the second user with an empty email fails: `UNIQUE constraint failed: users.Email`. |
| 2 | `Main.getNonNegativeDoubleInput` (`src/Main.java:285`) | `Scanner.nextDouble()` accepts `NaN`. `NaN >= 0` is false, so the prompt says "Value cannot be negative." The re-prompt works; the message is wrong. |
| 3 | `Main.getNonNegativeDoubleInput` (`src/Main.java:285`) | `Infinity` passes `value >= 0` and is saved as a game price or transaction amount. |

## Proposed simplifications (2026-10-11, not applied)

The program in this repository runs as it is: it passed all 100 simulation runs above.
The two changes below are optional clean-ups for the `DatabaseManager` class. They were
tried on a scratch copy and then reverted, so the committed code does not include them.

- **Remove `loadDriver()` and its call in the constructor (−11 lines).** JDBC 4 loads
  `org.sqlite.JDBC` automatically through the driver jar's service registration, so `Class.forName("org.sqlite.JDBC")` is not needed. Visible effect: the startup
  line `✅ SQLite JDBC Driver loaded successfully!` would no longer be printed.
- **Add one private helper `printRows(sql, emptyMsg, errorMsg, printer)` (−48 lines net).**
  Six report methods repeat the same pattern: open connection → create statement → run
  query → print each row → print a "no rows" message when empty → print an error message
  on `SQLException`. The helper would hold that pattern once, and each method would pass
  its SQL, its messages and a lambda that prints one row. Methods that would use it:
  `getUsersSortedByUsername`, `getTotalSpendingPerUser`, `getSellersWithGames`,
  `getUsersAboveAverageSpending`, `getAllUsersWithTransactions`, `getAllSellersWithGames`.
  `getGamesByCategory` would stay as it is, because it prints its
  `Games in category: ...` header only after the query succeeds.

**Expected result:** the `DatabaseManager` class would go from 363 to 304 lines. `Main.java` and
all public method signatures would not change. On the scratch copy, the same scripted
stdin (every read and report option, from an empty folder and from a copy of
`database.db`) gave byte-identical output, except for the removed driver line.

**More ideas from the 2026-10-14 review (not tried):**

- **One write helper (about −40 lines).** `addUser`, `addSeller`, `addGame`, `addTransaction`,
  `updateUserEmail` and `deleteUser` repeat connect → prepare → set parameters → execute →
  catch. One `int execute(sql, errLabel, Object... params)` helper with `setObject` would hold
  that once and return the row count (`src/DatabaseManager.java:106-217`).
- **One number-input helper (about −12 lines).** `getIntInput` and `getDoubleInput` are the
  same loop. Reading a line and parsing it with `Integer.parseInt` / `Double.parseDouble`
  removes the `nextInt`/`nextLine` pairing and makes input independent of the system locale
  (issue E2 below) (`src/Main.java:212-242`).
- **Drop `isDatabaseEmpty` (about −6 lines).** The schema already uses `CREATE TABLE IF NOT EXISTS`,
  so `createSchema` can always run; sample data is inserted only when `users` has 0 rows
  (`src/DatabaseManager.java:9-33`).
- **Final scanner field (−3 lines).** `private static final Scanner scanner = new Scanner(System.in);`
  instead of assigning it inside `try` (`src/Main.java:9,18-19`).
- **Shorter user-type check (−3 lines).** Loop over `{"Admin", "Guest"}` instead of two `if`s
  (`src/Main.java:259-270`).
- **Optional: table-driven menus (about −40 lines).** The five menu loops share one shape, and
  the Update and Delete menus have one item each. A `Map<Integer, Runnable>` menu helper would
  replace them. Kept as is because the course rubric shows the separate CRUD menus
  (`src/Main.java:50-200`).

## Review notes (2026-10-14)

Code review of `src/Main.java` and `src/DatabaseManager.java`. Nothing was fixed.
Tags: **[ran]** = reproduced with a test run; **[read]** = found by reading the source only.
Findings #1-#3 of the simulation test run above are not repeated here.

**Known issues**

| # | Severity | Where | Issue | Tag |
|---|----------|-------|-------|-----|
| E1 | High | `src/DatabaseManager.java:15-23` | If the SQLite driver jar is missing, the error is only printed; the menu still opens and every action fails with `❌ No suitable driver`. | [ran] |
| E2 | Medium | `src/Main.java:232`, `src/DatabaseManager.java:232,256,306,333` | Number input and output follow the system locale: with German settings `49.99` is rejected, `49,99` is accepted and prices print as `$49,99`. In English settings `1,000` is read as 1000. | [ran] |
| E3 | Medium | `src/Main.java:165`, `src/DatabaseManager.java:68` | Deleting a user also deletes the user's transactions (`ON DELETE CASCADE`), but the warning mentions only the user record. | [ran] |
| E4 | Medium | `src/Main.java:125`, `src/DatabaseManager.java:245` | Category search is case- and space-sensitive: `action` and ` Action` find nothing. | [ran] |
| E5 | Low | `src/DatabaseManager.java:43,167` | Usernames compare case-sensitively: `customer1` is accepted beside `Customer1`, and the sorted list puts `alice` after `GameSeller1`. | [ran] |
| E6 | Low | `src/Main.java:167-168` | The delete confirmation is not trimmed: ` y` cancels the delete. | [ran] |
| E7 | Low | `src/DatabaseManager.java:5` | The database path is relative to the working folder; starting the program elsewhere creates a new, seeded `database.db` there. | [ran] |
| E8 | Low | `src/DatabaseManager.java:25-33` | `isDatabaseEmpty` checks only that table `users` exists and returns `true` on any `SQLException`; a missing other table is never created. | [read] |
| E9 | Low | `src/DatabaseManager.java:77-104` | Sample data is inserted with 4 statements and no transaction; a failure part-way leaves partial sample data. | [read] |
| E10 | Low | `src/Main.java:107`, `src/DatabaseManager.java:151` | The transaction amount is typed by hand, not taken from the game's price. | [read] |
| E11 | Low | `src/DatabaseManager.java:297-298` | "Above average" averages only users with at least one transaction; with the sample data (one buyer) the result is always empty. | [ran] |
| E12 | Low | `src/DatabaseManager.java:57,67`, `src/Main.java:95,107` | Money is stored as `double`/`REAL`; `DECIMAL(10,2)` does not round in SQLite, so `1.999` would be stored as is and shown as `2.00`. | [read] |
| E13 | Low | `src/Main.java:76,146` | Email is optional and unchecked when a user is added, but required when it is updated; no format check anywhere. | [read] |
| E14 | Low | `src/Main.java:97` | Year published accepts any positive number (e.g. 99999). | [read] |
| E15 | Low | `src/DatabaseManager.java:45,82-83` | Passwords are stored in plain text; `UserType` (Admin/Guest) is stored but not used for access control. | [read] |

**Tested 2026-10-14:** 10 targeted runs, each in an empty scratch folder (missing driver, German locale, `1,000`, delete cascade, category case and spaces, username case and sort, ` y` confirmation, other working folder, above-average with sample data); all ended normally with no stack trace.

**Not tested:** two programs using one database at the same time; a read-only or locked `database.db`; very long strings; E8, E9 and E12 (source reading only).

## License

This project is licensed under the MIT License. See `LICENSE` file for details.

---

**README Template Based On**: [Awesome README](https://github.com/matiassingers/awesome-readme) - Template collection by [Matias Singers](https://mts.io/)
