import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    protected String username; // Unique username for the user
    protected String password; // User's password
    protected String name; // User's name
    protected String role; // user's role
    protected List<String> borrowedBooks; // Number of books currently borrowed

    // Constructor
    public User(String username, String password, String name, String role) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.role = role;
        this.borrowedBooks = new ArrayList<>();
    }

    // Getters and setters
   public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(String id) {
        if (!borrowedBooks.contains(id)) {
            borrowedBooks.add(id);
        }
    }

    public void returnBook(String id) {
        borrowedBooks.remove(id);
    }

    public boolean hasBorrowedBook(String id) {
        return borrowedBooks.contains(id);
    }

    @Override
    public String toString() {
        return "Username: " + username + " | Name: " + name + " | Role: " + role;
    }


   
}


