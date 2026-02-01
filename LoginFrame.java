import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoginFrame extends JFrame {
    private Library library;
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame(Library library) {
        this.library = library;
        initializeUI();
    }

    private void initializeUI() {
        // Line added inside initializeUI()
        Color primaryBlue = new Color(36, 122, 175);
        Color backgroundColor = new Color(245, 245, 245);
        setTitle("Library Management System - Login");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel titleLabel = new JLabel("Library Management System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, gbc);

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel usernameLabel = new JLabel("Username:");
        mainPanel.add(usernameLabel, gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        usernameField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        mainPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel passwordLabel = new JLabel("Password:");
        mainPanel.add(passwordLabel, gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        mainPanel.add(passwordField, gbc);

        // Login Button
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton loginButton = new JButton("Login");

// These two lines are the "magic" fix for background colors
        loginButton.setBackground(primaryBlue);
        loginButton.setOpaque(true);
        loginButton.setBorderPainted(false); // This is usually the missing piece!

        loginButton.setForeground(Color.WHITE);
        loginButton.setPreferredSize(new Dimension(200, 35));

        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(primaryBlue.darker());
            }
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(primaryBlue);
            }
        });

        loginButton.addActionListener(new LoginListener());
        mainPanel.add(loginButton, gbc);

        // Default credentials info
        gbc.gridy = 4;
        JLabel infoLabel = new JLabel("<html><center>Librarian and student users system</center></html>", SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        mainPanel.add(infoLabel, gbc);

        add(mainPanel);
    }

    private class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Please enter username and password!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = library.authenticateUser(username, password);
            if (user != null) {
                dispose();
                if (user.getRole().equals("Librarian")) {
                    new LibrarianFrame(library, user).setVisible(true);
                } else {
                    new StudentFrame(library, user).setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(LoginFrame.this,
                        "Invalid username or password!", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
