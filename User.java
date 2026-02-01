package library;
/**
 * Represents a library user.
 * Each user has an ID, a name, a role (like STUDENT or LIBRARIAN),
 * and can borrow books.
 */

public class User {
    private int id; // Unique ID for the user
    private String name; // User's name
    private Role role; // User's role (enum)
    private int borrowedCount; // Number of books currently borrowed

    // Constructor
    public User(int id, String name, Role role) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.borrowedCount = 0;
    }
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public Role getRole() { return role; }
    public int getBorrowedCount() { return borrowedCount;}

    // Borrow a book
    public void borrowBook() {
        borrowedCount++;
        System.out.println( name + " (" + role + ") borrowed a book! Total borrowed: " + borrowedCount);
    }

    // Return a book
    public void returnBook() {
        if (borrowedCount > 0) {
            borrowedCount--;
            System.out.println( + name + " returned a book. Remaining borrowed: " + borrowedCount);
        } else {
            System.out.println( + name + " has no books to return!");
        }
    }
    // Display user info
    public void showInfo() {
        System.out.println(" User: " + name + " (ID: " + id + ", Role: " + role + ") | Borrowed: " + borrowedCount);
    }
}

