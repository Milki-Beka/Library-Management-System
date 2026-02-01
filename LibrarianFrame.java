import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LibrarianFrame extends JFrame {
    private final Color PRIMARY_COLOR = new Color(124, 224, 232);
    private final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 16);
    private Library library;
    private User librarian;
    private JTable booksTable;
    private DefaultTableModel tableModel;
    //construction method
    public LibrarianFrame(Library library, User librarian) {
        this.library = library;
        this.librarian = librarian;
        initializeUI();
    }
    //Create initializeUI method
    private void initializeUI() {
        //The method available on a JFrame object
        setTitle("Library Management System - Librarian Panel");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        fileMenu.add(logoutItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
        
         // Welcome panel
       JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.setBackground(PRIMARY_COLOR);
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel welcomeLabel = new JLabel(
               "Welcome, " + librarian.getUsername()+ "!"
        );
        welcomeLabel.setFont(TITLE_FONT);
        welcomeLabel.setForeground(Color.white);

        welcomePanel.add(welcomeLabel, BorderLayout.WEST);
        add(welcomePanel, BorderLayout.NORTH);


        // Create main panel with BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Book Management"));

        JButton addButton = new JButton("Add Book");
        addButton.addActionListener(new AddBookListener());
        buttonPanel.add(addButton);

        JButton updateButton = new JButton("Update Book");
        updateButton.addActionListener(new UpdateBookListener());
        buttonPanel.add(updateButton);

        JButton deleteButton = new JButton("Delete Book");
        deleteButton.addActionListener(new DeleteBookListener());
        buttonPanel.add(deleteButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);

        // Create search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Books"));

        JComboBox<String> searchType = new JComboBox<>(new String[]{"ID", "Title", "Author", "Category"});
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");

        searchButton.addActionListener(e -> {
            String type = (String) searchType.getSelectedItem();
            String query = searchField.getText().trim();
            searchBooks(type, query);
        });

        showAllButton.addActionListener(e -> loadBooks());

        searchPanel.add(new JLabel("Search by:"));
        searchPanel.add(searchType);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        mainPanel.add(searchPanel, BorderLayout.CENTER);

        // Create table
        String[] columnNames = {"ID", "Title", "Author", "Category", "Total Copies", "Available Copies"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        booksTable = new JTable(tableModel);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        booksTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        booksTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        booksTable.getColumnModel().getColumn(2).setPreferredWidth(150);
        booksTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        booksTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        booksTable.getColumnModel().getColumn(5).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(booksTable);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Book List"));
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(tablePanel, BorderLayout.SOUTH);

        add(mainPanel);
        loadBooks();
    }
    private void addBookToTable(Book book) {
        Object[] row ={
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getCategory(),
                book.getTotalCopies(),
                book.getAvailableCopies()
        };
        tableModel.addRow(row);
    }

    private void loadBooks() {
        tableModel.setRowCount(0);
        List<Book> books = library.getAllBooks();
        for (Book book : books) {
            addBookToTable(book);
        }
    }

    private void searchBooks(String type, String query) {
        tableModel.setRowCount(0);
        List<Book> books;

        switch (type) {
            case "ID":
                Book book = library.searchBook(query);
                if (book != null) {
                    addBookToTable(book);
                }
                break;
            case "Title":
                books = library.searchBooksByTitle(query);
                for (Book b : books) {
                    addBookToTable(b);
                }
                break;
            case "Author":
                books = library.searchBooksByAuthor(query);
                for (Book b : books) {
                    addBookToTable(b);
                }
                break;
            case "Category":
                books = library.searchBooksByCategory(query);
                for (Book b : books) {
                    addBookToTable(b);
                }
                break;
        }
    }

    private void logout() {
        dispose();
        new LoginFrame(library).setVisible(true);
    }
    // Create AddBookListener class which implements ActionListener
    private class AddBookListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog dialog = new JDialog(LibrarianFrame.this, "Add New Book", true);
            dialog.setSize(400, 350);
            dialog.setLocationRelativeTo(LibrarianFrame.this);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField idField = new JTextField(20);
            JTextField titleField = new JTextField(20);
            JTextField authorField = new JTextField(20);
            JTextField categoryField = new JTextField(20);
            JTextField copiesField = new JTextField(20);

            gbc.gridy = 0;
            panel.add(new JLabel("ID:"), gbc);
            gbc.gridx = 1;
            panel.add(idField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(new JLabel("Title:"), gbc);
            gbc.gridx = 1;
            panel.add(titleField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(new JLabel("Author:"), gbc);
            gbc.gridx = 1;
            panel.add(authorField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(new JLabel("Category:"), gbc);
            gbc.gridx = 1;
            panel.add(categoryField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            panel.add(new JLabel("Total Copies:"), gbc);
            gbc.gridx = 1;
            panel.add(copiesField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 2;
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton addButton = new JButton("Add");
            JButton cancelButton = new JButton("Cancel");

            addButton.addActionListener(ev -> {
                String id = idField.getText().trim();
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String category = categoryField.getText().trim();
                String copiesStr = copiesField.getText().trim();

                if (id.isEmpty() || title.isEmpty() || author.isEmpty() ||
                        category.isEmpty() || copiesStr.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill all fields!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int copies = Integer.parseInt(copiesStr);
                    if (copies <= 0) {
                        JOptionPane.showMessageDialog(dialog, "Copies must be positive!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    Book newBook = new Book(id, title, author, category, copies);
                    if (library.addBook(newBook)) {
                        JOptionPane.showMessageDialog(dialog, "Book added successfully!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadBooks();
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(dialog, "Book with this ID already exists!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid number for copies!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelButton.addActionListener(ev -> dialog.dispose());

            buttonPanel.add(addButton);
            buttonPanel.add(cancelButton);
            panel.add(buttonPanel, gbc);

            dialog.add(panel);
            dialog.setVisible(true);
        }
    }

    private class UpdateBookListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(LibrarianFrame.this,
                        "Please select a book to update!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String id = (String) tableModel.getValueAt(selectedRow, 0);
            Book book = library.searchBook(id);
            if (book == null) return;

            JDialog dialog = new JDialog(LibrarianFrame.this, "Update Book", true);
            dialog.setSize(400, 350);
            dialog.setLocationRelativeTo(LibrarianFrame.this);

            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JTextField idField = new JTextField(book.getId(), 20);
            idField.setEditable(false);
            JTextField titleField = new JTextField(book.getTitle(), 20);
            JTextField authorField = new JTextField(book.getAuthor(), 20);
            JTextField categoryField = new JTextField(book.getCategory(), 20);
            JTextField copiesField = new JTextField(String.valueOf(book.getTotalCopies()), 20);

            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(new JLabel("ID:"), gbc);
            gbc.gridx = 1;
            panel.add(idField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 1;
            panel.add(new JLabel("Title:"), gbc);
            gbc.gridx = 1;
            panel.add(titleField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 2;
            panel.add(new JLabel("Author:"), gbc);
            gbc.gridx = 1;
            panel.add(authorField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 3;
            panel.add(new JLabel("Category:"), gbc);
            gbc.gridx = 1;
            panel.add(categoryField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 4;
            panel.add(new JLabel("Total Copies:"), gbc);
            gbc.gridx = 1;
            panel.add(copiesField, gbc);

            gbc.gridx = 0;
            gbc.gridy = 5;
            gbc.gridwidth = 2;
            JPanel buttonPanel = new JPanel(new FlowLayout());
            JButton updateButton = new JButton("Update");
            JButton cancelButton = new JButton("Cancel");

            updateButton.addActionListener(ev -> {
                String title = titleField.getText().trim();
                String author = authorField.getText().trim();
                String category = categoryField.getText().trim();
                String copiesStr = copiesField.getText().trim();

                if (title.isEmpty() || author.isEmpty() || category.isEmpty() || copiesStr.isEmpty()) {
                    JOptionPane.showMessageDialog(dialog, "Please fill all fields!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                try {
                    int copies = Integer.parseInt(copiesStr);
                    if (copies <= 0) {
                        JOptionPane.showMessageDialog(dialog, "Copies must be positive!",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (library.updateBook(id, title, author, category, copies)) {
                        JOptionPane.showMessageDialog(dialog, "Book updated successfully!",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadBooks();
                        dialog.dispose();
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(dialog, "Invalid number for copies!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            cancelButton.addActionListener(ev -> dialog.dispose());

            buttonPanel.add(updateButton);
            buttonPanel.add(cancelButton);
            panel.add(buttonPanel, gbc);

            dialog.add(panel);
            dialog.setVisible(true);
        }
    }

    private class DeleteBookListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(LibrarianFrame.this,
                        "Please select a book to delete!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String id = (String) tableModel.getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(LibrarianFrame.this,
                    "Are you sure you want to delete this book?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (library.deleteBook(id)) {
                    JOptionPane.showMessageDialog(LibrarianFrame.this,
                            "Book deleted successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadBooks();
                } else {
                    JOptionPane.showMessageDialog(LibrarianFrame.this,
                            "Error deleting book!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}

