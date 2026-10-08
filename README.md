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
- [License](#license)

## Course Overview

This final project for **CTINFMGL (Information Management)** demonstrates key concepts in database design and information management:

- **Database Schema Design**: Implementing normalized relational database structures
- **CRUD Operations**: Create, Read, Update, and Delete operations for data management
- **Data Integrity**: Using foreign keys and constraints to maintain data consistency
- **Query Optimization**: Complex SQL queries for meaningful data retrieval
- **Information Systems**: Building an integrated system for managing business information

## Features

**Core Functionality**
- **Create Operations**: Add users, sellers, games, and transactions to the marketplace
- **Read Operations**: Query and retrieve data from the system
- **Update Operations**: Modify existing records
- **Delete Operations**: Remove records from the database
- **Advanced Queries**: Complex database searches and filtering

**Technical Features**
- SQLite database with persistent storage
- Full schema management with foreign key constraints
- Sample data initialization on first run
- Type-safe database operations
- Input validation with re-prompting on invalid entries
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
├── bin/                        # Compiled class files (generated)
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

Compiled and run with the commands above, in a temporary folder per run, so the committed
`database.db` stayed unchanged. The program was run **100 times** with scripted stdin:
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

## License

This project is licensed under the MIT License. See `LICENSE` file for details.

---

**README Template Based On**: [Awesome README](https://github.com/matiassingers/awesome-readme) - Template collection by [Matias Singers](https://mts.io/)
