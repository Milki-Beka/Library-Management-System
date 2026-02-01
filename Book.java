import java.io.Serializable;

public class Book implements serializable {
    private String id; // Unique ID for the book
    private String title; // Title of the book
    private String author; // Author of the book
    private String category; // Book category (enum)
    private int totalCopies; // Total copies available in library
    private boolean available; 
    private int availableCopies;
    

    // Constructor
public Book(String id, String title, String author, String category, int totalCopies) {
    this.id = id;
    this.title = title;
    this.author = author;
    this.category = category;
    this.totalCopies = totalCopies;
    this.available = true;
    this.availableCopies = totalCopies;
}
// Getters and setters
     public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
    @Override
    public String toString() {
        return "ID: " + id + " | Title: " + title + " | Author: " + author +
                " | Category: " + category + " | Available: " + availableCopies + "/" + totalCopies;
    }

   
  
 }



