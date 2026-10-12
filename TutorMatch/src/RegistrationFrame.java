import javax.swing.*;
import java.awt.*;

public class RegistrationFrame extends JFrame {
    private AuthService authService;
    private SessionManager sessionManager;

    private JTextField nameField, emailField, contactField, rateField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;

    public RegistrationFrame(AuthService authService, SessionManager sessionManager) {
        this.authService = authService;
        this.sessionManager = sessionManager;

        setTitle("TutorMatch - Register");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Full Name:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Contact Number:"));
        contactField = new JTextField();
        panel.add(contactField);

        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Account Role:"));
        roleCombo = new JComboBox<>(new String[]{"Student", "Tutor"});
        panel.add(roleCombo);

        panel.add(new JLabel("Hourly Rate (Tutors only):"));
        rateField = new JTextField("0.0");
        panel.add(rateField);

        JButton registerButton = new JButton("Register");
        JButton backButton = new JButton("Back to Login");

        panel.add(registerButton);
        panel.add(backButton);

        add(panel);

        // Register button logic
        registerButton.addActionListener(e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            String contact = contactField.getText().trim();
            String password = new String(passwordField.getPassword());
            String role = (String) roleCombo.getSelectedItem();

            try {
                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    throw new IllegalArgumentException("Required fields cannot be empty.");
                }

                if ("Student".equals(role)) {
                    authService.registerStudent(name, email, contact, password);
                } else {
                    double rate = Double.parseDouble(rateField.getText().trim());
                    authService.registerTutor(name, email, contact, password, rate);
                }

                JOptionPane.showMessageDialog(this, "Registration Successful! Please log in.");
                DataManager.saveUsers(authService.getUsers());
                new LoginFrame(authService, sessionManager).setVisible(true);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            new LoginFrame(authService, sessionManager).setVisible(true);
            dispose();
        });
    }
}