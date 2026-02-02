# Project title: Library Management System
The system enables librarians to manage book records which they can use to perform searches, delete, add and make updates on book information's. The library system provides a book borrowing and returning system for students.

## Features
### Librarian Features
- Adding System
- Updating System
- Deleting System
- Search Engine

### Student Features
-	Borrowing System
-	Returning system

# Language
 Java "OOP"
## Project Structure

### Core Classes
- **Book.java**
- **Library.java**
- **User.java**

### GUI Classes
- **LoginFrame.java**
- **StudentFrame.java**
- **LibrarianFrame.java**

- **Main.java**

## UML Overview 
- User → Frame	(The LoginFrame determines whether to open a StudentFrame or LibrarianFrame based on the User type.)
- Library ◇ Book	(The Library maintains a collection of Books; books exist even if the library system is offline.)
- Student ↔ Book	(This is a "Many-to-Many" relationship (one student can have many books, one book can be borrowed by many students over time).
- Main → launches the application

## Contributors
| Name                 | ID          | 
-----------------------|-------------|
| Fenet Bekele         | UGR/2989/17 | 
| Fikirte Endeshaw     | UGR/7495/17 |
| Gelila Abi           | UGR/5457/17 |
| Feriha Ahmed         | UGR/6917/17 |
| Freweyni Gebremedhin | UGR/5375/17 |
