import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
    private AuthService authService;
    private SessionManager sessionManager;
    
    private JTextField emailField;
    private JPasswordField passwordField;

    // Custom Color Theme
    private final Color PRIMARY_DARK = Color.decode("#3b0800"); // Deep Brown
    private final Color ACCENT_TEAL = Color.decode("#008989");  // Teal
    private final Color DARK_TEAL = Color.decode("#003b3b");    // Dark Teal
    private final Color BG_COLOR = new Color(245, 243, 240);

    public LoginFrame(AuthService authService, SessionManager sessionManager) {
        this.authService = authService;
        this.sessionManager = sessionManager;

        setTitle("TutorMatch - Login");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main Panel with background
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_COLOR);

        // Header Title
        JLabel titleLabel = new JLabel("Welcome to TutorMatch", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(PRIMARY_DARK);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Form Center Panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 15));
        formPanel.setBackground(BG_COLOR);
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        JLabel emailLbl = new JLabel("Email:");
        emailLbl.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(emailLbl);
        emailField = new JTextField();
        formPanel.add(emailField);

        JLabel passLbl = new JLabel("Password:");
        passLbl.setFont(new Font("Arial", Font.BOLD, 12));
        formPanel.add(passLbl);
        passwordField = new JPasswordField();
        formPanel.add(passwordField);

        JButton loginButton = new JButton("Login");
        loginButton.setBackground(ACCENT_TEAL);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 12));
        loginButton.setFocusPainted(false);

        JButton registerButton = new JButton("Register");
        registerButton.setBackground(DARK_TEAL);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFont(new Font("Arial", Font.BOLD, 12));
        registerButton.setFocusPainted(false);

        formPanel.add(loginButton);
        formPanel.add(registerButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);

        // Login Action
        loginButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());

            try {
                User user = authService.login(email, password);
                JOptionPane.showMessageDialog(this, "Login Successful! Welcome, " + user.getName());
                
                if (user instanceof Student) {
                    new StudentDashboard((Student) user, authService, sessionManager).setVisible(true);
                } else if (user instanceof Tutor) {
                    new TutorDashboard((Tutor) user, authService, sessionManager).setVisible(true);
                }
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Register Action
        registerButton.addActionListener(e -> {
            new RegistrationFrame(authService, sessionManager).setVisible(true);
            dispose();
        });
    }
}