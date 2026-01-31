import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class StudentFrame extends JFrame {
    private Library library;
    private User student;
    private JTable booksTable;
    private JTable borrowedTable;
    private DefaultTableModel booksTableModel;
    private DefaultTableModel borrowedTableModel;

    public StudentFrame(Library library, User student) {
        this.library = library;
        this.student = student;
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Library Management System - Student Panel");
        setSize(1100, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create menu bar
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem logoutItem = new JMenuItem("Logout");
        logoutItem.addActionListener(e -> logout());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(logoutItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        // Create main panel with JSplitPane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(650);

        // Left panel - Available Books
        JPanel leftPanel = createAvailableBooksPanel();
        splitPane.setLeftComponent(leftPanel);

        // Right panel - Borrowed Books
        JPanel rightPanel = createBorrowedBooksPanel();
        splitPane.setRightComponent(rightPanel);

        add(splitPane, BorderLayout.CENTER);

        // Welcome panel
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        welcomePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JLabel welcomeLabel = new JLabel("Welcome, " + student.getName() + " (" + student.getUsername() + ")");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        welcomePanel.add(welcomeLabel);
        add(welcomePanel, BorderLayout.NORTH);

        loadAvailableBooks();
        loadBorrowedBooks();
    }

    private JPanel createAvailableBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Available Books"));

        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JComboBox<String> searchType = new JComboBox<>(new String[]{"ID", "Title", "Author", "Category"});
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");
        JButton borrowButton = new JButton("Borrow Book");

        searchButton.addActionListener(e -> {
            String type = (String) searchType.getSelectedItem();
            String query = searchField.getText().trim();
            searchBooks(type, query);
        });

        showAllButton.addActionListener(e -> loadAvailableBooks());

        borrowButton.addActionListener(new BorrowBookListener());

        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchType);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);
        searchPanel.add(borrowButton);

        panel.add(searchPanel, BorderLayout.NORTH);

        // Books table
        String[] columnNames = {"ID", "Title", "Author", "Category", "Available"};
        booksTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        booksTable = new JTable(booksTableModel);
        booksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        booksTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(booksTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createBorrowedBooksPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("My Borrowed Books"));

        // Return button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton returnButton = new JButton("Return Book");
        returnButton.addActionListener(new ReturnBookListener());
        buttonPanel.add(returnButton);
        panel.add(buttonPanel, BorderLayout.NORTH);

        // Borrowed books table
        String[] columnNames = {"ID", "Title", "Author"};
        borrowedTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        borrowedTable = new JTable(borrowedTableModel);
        borrowedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        borrowedTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        JScrollPane scrollPane = new JScrollPane(borrowedTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Info label
        JLabel infoLabel = new JLabel("<html><center>Select a book and click 'Borrow' or 'Return'<br>You can borrow multiple copies of the same book</center></html>",
                SwingConstants.CENTER);
        panel.add(infoLabel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadAvailableBooks() {
        booksTableModel.setRowCount(0);
        List<Book> books = library.getAllBooks();
        for (Book book : books) {
            if (book.getAvailableCopies() > 0) {
                Object[] row = {
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor(),
                        book.getCategory(),
                        book.getAvailableCopies()
                };
                booksTableModel.addRow(row);
            }
        }
    }

    private void loadBorrowedBooks() {
        borrowedTableModel.setRowCount(0);
        for (String id : student.getBorrowedBooks()) {
            Book book = library.searchBook(id);
            if (book != null) {
                Object[] row = {
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor()
                };
                borrowedTableModel.addRow(row);
            }
        }
    }

    private void searchBooks(String type, String query) {
        booksTableModel.setRowCount(0);
        List<Book> books;

        switch (type) {
            case "ID":
                Book book = library.searchBook(query);
                if (book != null && book.getAvailableCopies() > 0) {
                    Object[] row = {
                            book.getId(),
                            book.getTitle(),
                            book.getAuthor(),
                            book.getCategory(),
                            book.getAvailableCopies()
                    };
                    booksTableModel.addRow(row);
                }
                break;
            case "Title":
                books = library.searchBooksByTitle(query);
                for (Book b : books) {
                    if (b.getAvailableCopies() > 0) {
                        Object[] row = {
                                b.getId(),
                                b.getTitle(),
                                b.getAuthor(),
                                b.getCategory(),
                                b.getAvailableCopies()
                        };
                        booksTableModel.addRow(row);
                    }
                }
                break;
            case "Author":
                books = library.searchBooksByAuthor(query);
                for (Book b : books) {
                    if (b.getAvailableCopies() > 0) {
                        Object[] row = {
                                b.getId(),
                                b.getTitle(),
                                b.getAuthor(),
                                b.getCategory(),
                                b.getAvailableCopies()
                        };
                        booksTableModel.addRow(row);
                    }
                }
                break;
            case "Category":
                books = library.searchBooksByCategory(query);
                for (Book b : books) {
                    if (b.getAvailableCopies() > 0) {
                        Object[] row = {
                                b.getId(),
                                b.getTitle(),
                                b.getAuthor(),
                                b.getCategory(),
                                b.getAvailableCopies()
                        };
                        booksTableModel.addRow(row);
                    }
                }
                break;
        }
    }

    private void logout() {
        dispose();
        new LoginFrame(library).setVisible(true);
    }

    private class BorrowBookListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = booksTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(StudentFrame.this,
                        "Please select a book to borrow!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String id = (String) booksTableModel.getValueAt(selectedRow, 0);
            String title = (String) booksTableModel.getValueAt(selectedRow, 1);

            int confirm = JOptionPane.showConfirmDialog(StudentFrame.this,
                    "Do you want to borrow: " + title + "book ?",
                    "Confirm Borrow",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (library.borrowBook(id, student)) {
                    JOptionPane.showMessageDialog(StudentFrame.this,
                            " Book borrowed successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAvailableBooks();
                    loadBorrowedBooks();
                } else {
                    JOptionPane.showMessageDialog(StudentFrame.this,
                            "Error borrowing book!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class ReturnBookListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = borrowedTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(StudentFrame.this,
                        "Please select a book u want to return!",
                        "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String id = (String) borrowedTableModel.getValueAt(selectedRow, 0);
            String title = (String) borrowedTableModel.getValueAt(selectedRow, 1);

            int confirm = JOptionPane.showConfirmDialog(StudentFrame.this,
                    "Do you want to return: " + title + "book ?",
                    "Confirm Return",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                if (library.returnBook(id, student)) {
                    JOptionPane.showMessageDialog(StudentFrame.this,
                            "Book returned successfully!",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadAvailableBooks();
                    loadBorrowedBooks();
                } else {
                    JOptionPane.showMessageDialog(StudentFrame.this,
                            "Error in returning book!",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}