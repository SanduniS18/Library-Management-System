## 📚 Library Management System

The Library Management System is a comprehensive Java desktop application designed to streamline library operations for small to medium-sized libraries. This system provides an intuitive graphical user interface for managing books, members, and transactions, making it easy for library staff to perform daily operations efficiently.

## Features
- **📖 Book Management**: Add, edit, delete, and search books in the library catalog
- **👥 Member Management**: Register, update, and manage library members
- **🔄 Transaction Processing**: Issue and return books with automatic due date calculation
- **⏰ Overdue Tracking**: Monitor overdue books and calculate fines
- **📊 Reporting**: Generate various reports including statistics, overdue books, and transaction history
- **🔍 Advanced Search**: Search functionality across books, members, and transactions
- **🎨 User-Friendly Interface**: Modern GUI with intuitive navigation and visual feedback

## Technology Stack
- **Programming Language**: Java
- **Database**: MySQL
- **GUI Framework**: Java Swing
- **Database Connectivity**: JDBC
- **Architecture**: MVC (Model-View-Controller) pattern

### Package Architecture
The application follows a layered architecture for better maintainability:

com.library/
├── Main.java                      # Application entry point
├── dao/                           # Data Access Objects
│   ├── BookDAO.java              # Database operations for books
│   ├── MemberDAO.java            # Database operations for members
│   └── TransactionDAO.java       # Database operations for transactions
├── db/                           # Database layer
│   └── DatabaseConnection.java   # Database connection management
├── models/                       # Entity classes
│   ├── Book.java                 # Book entity with properties and methods
│   ├── Member.java               # Member entity with validation logic
│   └── Transaction.java          # Transaction entity with business logic
├── services/                     # Business logic layer
│   └── LibraryService.java       # Service layer coordinating operations
└── ui/                           # User interface
    └── MainFrame.java           # Main application window with all GUI components

## 1. Database Connection Strategy
Initially used singleton pattern but encountered "connection closed" errors. Switched to connection-per-operation approach for better reliability. Each database call now creates a fresh connection, ensuring thread safety and preventing resource conflicts.

## 2. MVC Architecture
- **Adopted Model-View-Controller separation for:
- **Better code organization
- **Easier maintenance
- **Clear separation between UI, logic, and data layers

## 3. Stored Procedures
- **Implemented stored procedures for book issuing/returning because:
- **Encapsulate complex business logic at database level
- **Improve performance with reduced network calls
- **Enhance security against SQL injection

## 4. Resource Management
Implemented manual resource closing in DAO classes to prevent memory leaks. Each method now properly closes ResultSet, Statement, and Connection objects in finally blocks.

## Key Features Implementation

## Book Management
Add books with multiple copies
Track available vs. total copies
Search by title, author, ISBN, or category
Prevent deletion of issued books

## Member Management
Member registration with validation
Status management (Active/Inactive/Suspended)
Configurable borrowing limits
Search by name, email, or phone

## Transaction System
Automated due date calculation
Overdue tracking with fine computation
Transaction history with filtering
Integration with book availability

## User Interface
Tabbed interface for different modules
Color-coded status indicators
Real-time statistics dashboard
Responsive table layouts with sorting

## Installation
Prerequisites
JDK 8+
MySQL Server
MySQL Connector/J

## Setup Steps
Run sql in MySQL
Update database credentials in DatabaseConnection.java
Add MySQL connector to classpath
Compile and run the application

## Database Schema
books: Book inventory with copy management
members: Library member information
transactions: Borrowing records with fines

The schema includes foreign key constraints and indexes for performance.

## Error Handling
- **Comprehensive exception handling
- **User-friendly error messages
- **Database connection recovery
- **Input validation at multiple levels

## Challenges Solved

## Connection Issues
Fixed "No operations allowed after connection closed" by implementing proper resource lifecycle management in DAO classes.

## Data Integrity
Used stored procedures to ensure atomic operations for book issuing and returning, maintaining consistency.

## User Experience
Designed intuitive interface with clear navigation, reducing training requirements for library staff.

## Future Enhancements

- **Email notifications for due dates
- **Barcode/QR code integration
- **Multi-user authentication
- **Data export capabilities
- **Mobile companion app
