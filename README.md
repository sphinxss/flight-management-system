# Flight Management System

A Java Swing desktop application for managing flight schedules stored in MySQL.
It supports creating, updating, deleting, searching, and filtering flights, with
colour-coded confirmation states and an activity log.

## Features

- Full CRUD for flights using JDBC and MySQL
- Search and filter the schedule table
- Confirmed / not confirmed / cancelled status badges
- Flight activity log table
- Swing UI with a clean card-based layout

## Requirements

- JDK 17 or newer
- MySQL 8+ or MariaDB
- [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) on the classpath

## Database setup

```sql
CREATE DATABASE flight_db;
```

```powershell
Get-Content database/flight_db.sql | mysql -u root -p flight_db
```

The dump creates the `flights` and `flight_logs` tables and adds sample rows.

## Configuration

The app reads its connection settings from environment variables:

| Variable | Default |
| --- | --- |
| `FLIGHT_DB_URL` | `jdbc:mysql://localhost:3306/flight_db` |
| `FLIGHT_DB_USER` | `root` |
| `FLIGHT_DB_PASS` | *(empty)* |

```powershell
$env:FLIGHT_DB_USER = "root"
$env:FLIGHT_DB_PASS = "your-password"
```

## Build and run

```powershell
javac -cp "mysql-connector-j-9.0.0.jar;." FlightManagementSchedule.java
java -cp "mysql-connector-j-9.0.0.jar;." FlightManagementSchedule
```

On macOS/Linux, replace `;` with `:` in the classpath.

## Project structure

```
FlightManagementSchedule.java   Swing UI, JDBC data access, CRUD logic
database/flight_db.sql          MySQL schema and sample data
```

## Notes

This is an academic project. The database credentials are not hard-coded; set
them through environment variables before running.
