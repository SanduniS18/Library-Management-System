📚 Library Management System

The Library Management System is a comprehensive Java desktop application designed to streamline library operations for small to medium-sized libraries. This system provides an intuitive graphical user interface for managing books, members, and transactions, making it easy for library staff to perform daily operations efficiently.

Features
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

```
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
