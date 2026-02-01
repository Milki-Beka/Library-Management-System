package library;
/**
 * Represents a book in the library.
 * Each book has an ID, title, author, category, and total copies.
 */

public class Book {
    private int id; // Unique ID for the book
    private String title; // Title of the book
    private String author; // Author of the book
    private Category category; // Book category (enum)
    private int totalCopies; // Total copies available in library
    private int borrowedCopies; // Number of copies currently borrowed

    // Constructor
public Book(int id, String title, String author, Category category, int totalCopies) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.category = category;
    this.totalCopies = totalCopies;
    this.borrowedCopies = 0;
}
// Getters
    public int getId() {return id; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public Category getCategory() { return category; }
    public int getTotalCopies() { return totalCopies; }
    public int getBorrowedCopies() { return borrowedCopies; }

    // Borrow a copy
    public void borrow() {
        if (borrowedCopies < totalCopies) {
            borrowedCopies++;
            System.out.println("📖 \"" + title + "\" borrowed! Copies left: " + (totalCopies - borrowedCopies));
        } else {System.out.println("❌ No copies of \"" + title + "\" are available.");

        }
    }
    // Return a copy
    public void returnBook() {
        if (borrowedCopies > 0) {
            borrowedCopies--;
            System.out.println("🔄 \"" + title + "\" returned. Copies left: " + (totalCopies - borrowedCopies));
        } else {
            System.out.println("⚠️ No borrowed copies of \"" + title + "\" to return.");
        }
    }
    // Display book info
    public void showInfo() {
        String status = (borrowedCopies < totalCopies) ? "✅ Available" : "❌ All borrowed";
        System.out.println("📘 Book: " + title + " by " + author + " (ID: " + id + ", Category: " + category + ")\n " +
                " Total: " + totalCopies + "\n" +
                "  Borrowed: " + borrowedCopies + "\n" +
                "  Status: " + status);
    }
 }
