# PII Data Hashing

## Overview
This Java application scans a PostgreSQL database for Personally Identifiable Information (PII) and replaces it with a 
hashed value. It also inserts sample data into the database while ensuring the required tables exist.

## Tech Stack
- **Java** (Core application logic)
- **Spring Boot** (Optional for enhancements)
- **PostgreSQL** (Database management)
- **JDBC** (Database connectivity)
- **pgAdmin4** (Database administration)

## Features
- Scans the database for PII data and replaces it with a hash.
- Creates necessary tables (`users`, `employees`, `customers`) if they do not exist.
- Inserts sample data into the database.
- Uses JDBC for database connection.

## Prerequisites
- Java 8 or higher
- PostgreSQL installed and running
- pgAdmin4 (optional, for database management)

## Database Configuration
Update the `DatabaseConfig` class with your PostgreSQL connection details:

```java
public class DatabaseConfig {
    public static final String DB_URL = "jdbc:postgresql://localhost:5432/your_database";
    public static final String USER = "your_username";
    public static final String PASSWORD = "your_password";
}
```

## How to Run
1. Ensure PostgreSQL is running.
2. Compile and run the Java application:
   ```sh
   javac com/rd/db/DataInserter.java
   java com.rd.db.DataInserter
   ```

## Error Handling
If you get `ERROR: no schema has been selected to create in`, ensure:
- The `public` schema exists (`CREATE SCHEMA public;` if missing)
- Your `search_path` is set correctly (`ALTER ROLE your_username SET search_path TO public;`)
- The PostgreSQL service is running (`net start postgresql-x64-15` on Windows)

## PII Hashing
The application uses `PiiScanner.scanAndReplacePii(DB_URL);` to hash PII data. Ensure `PiiScanner` is implemented correctly to modify and secure sensitive data.

## License
This project is open-source and available for modification and distribution.

