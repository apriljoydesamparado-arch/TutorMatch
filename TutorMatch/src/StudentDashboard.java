import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class StudentDashboard extends JFrame {
    private Student student;
    private AuthService authService;
    private SessionManager sessionManager;
    private JTable tutorTable;
    private DefaultTableModel tutorModel;

    private JTextField subjectFilterField, availabilityFilterField, ratingFilterField;

    // Custom Color Theme
    private final Color DARK_TEAL = Color.decode("#003b3b");
    private final Color ACCENT_TEAL = Color.decode("#008989");
    private final Color PRIMARY_DARK = Color.decode("#3b0800");

    public StudentDashboard(Student student, AuthService authService, SessionManager sessionManager) {
        this.student = student;
        this.authService = authService;
        this.sessionManager = sessionManager;

        setTitle("TutorMatch - Student Dashboard (" + student.getName() + ")");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Panel: Welcome and Logout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(DARK_TEAL);
        topPanel.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));
        
        JLabel welcomeLabel = new JLabel("Welcome, " + student.getName() + " (Student)");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 15));
        welcomeLabel.setForeground(Color.WHITE);
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(PRIMARY_DARK);
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        topPanel.add(logoutBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel: Filters & Table
        JPanel centerPanel = new JPanel(new BorderLayout(5, 5));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));

        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBorder(BorderFactory.createTitledBorder("Search & Filter Tutors"));
        
        filterPanel.add(new JLabel("Subject:"));
        subjectFilterField = new JTextField(10);
        filterPanel.add(subjectFilterField);

        filterPanel.add(new JLabel("Availability:"));
        availabilityFilterField = new JTextField(10);
        filterPanel.add(availabilityFilterField);

        filterPanel.add(new JLabel("Min Rating:"));
        ratingFilterField = new JTextField(5);
        filterPanel.add(ratingFilterField);

        JButton filterBtn = new JButton("Apply Filters");
        filterBtn.setBackground(ACCENT_TEAL);
        filterBtn.setForeground(Color.WHITE);
        filterBtn.setFocusPainted(false);
        filterPanel.add(filterBtn);

        centerPanel.add(filterPanel, BorderLayout.NORTH);

        // Tutor Table
        tutorModel = new DefaultTableModel(new String[]{"Name", "Email", "Subjects Offered", "Rate ($/hr)", "Availability", "Rating"}, 0);
        tutorTable = new JTable(tutorModel);
        tutorTable.setRowHeight(25);
        loadTutors("", "", 0.0);
        centerPanel.add(new JScrollPane(tutorTable), BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Action Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        JButton bookBtn = new JButton("Book Tutor");
        JButton scheduleBtn = new JButton("My Schedule");
        JButton rateBtn = new JButton("Rate Session");
        JButton cancelBtn = new JButton("Cancel Session");

        // Style bottom buttons
        for (JButton btn : new JButton[]{bookBtn, scheduleBtn, rateBtn, cancelBtn}) {
            btn.setBackground(ACCENT_TEAL);
            btn.setForeground(Color.WHITE);
            btn.setFont(new Font("Arial", Font.BOLD, 12));
            btn.setFocusPainted(false);
        }

        bottomPanel.add(bookBtn);
        bottomPanel.add(scheduleBtn);
        bottomPanel.add(rateBtn);
        bottomPanel.add(cancelBtn);

        add(bottomPanel, BorderLayout.SOUTH);

        // Action Listeners
        filterBtn.addActionListener(e -> {
            String sub = subjectFilterField.getText().trim();
            String avail = availabilityFilterField.getText().trim();
            double minRating = 0.0;
            try {
                if (!ratingFilterField.getText().trim().isEmpty()) {
                    minRating = Double.parseDouble(ratingFilterField.getText().trim());
                }
            } catch (NumberFormatException ignored) {}
            loadTutors(sub, avail, minRating);
        });

        bookBtn.addActionListener(e -> {
            int selectedRow = tutorTable.getSelectedRow();
            if (selectedRow >= 0) {
                String tutorEmail = (String) tutorModel.getValueAt(selectedRow, 1);
                Tutor selectedTutor = (Tutor) authService.findUserByEmail(tutorEmail);
                new BookingFrame(student, selectedTutor, sessionManager).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a tutor from the table first.");
            }
        });

        scheduleBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("Your Enrolled Sessions:\n\n");
            List<Session> sessions = sessionManager.getUserSchedule(student.getEmail());
            if (sessions.isEmpty()) {
                sb.append("No active sessions found.");
            } else {
                for (Session s : sessions) {
                    sb.append("ID: ").append(s.getSessionId())
                      .append(" | Tutor: ").append(s.getTutorEmail())
                      .append(" | Subject: ").append(s.getSubject())
                      .append(" | Status: ").append(s.getStatus()).append("\n");
                }
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "My Schedule", JOptionPane.INFORMATION_MESSAGE);
        });

        rateBtn.addActionListener(e -> {
            String sessionId = JOptionPane.showInputDialog(this, "Enter Session ID to rate:");
            if (sessionId != null && !sessionId.trim().isEmpty()) {
                Session s = sessionManager.findSessionById(sessionId.trim());
                if (s != null && s.getStudentEmail().equals(student.getEmail())) {
                    if ("COMPLETED".equals(s.getStatus())) {
                        new RatingFrame(s).setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "Only completed sessions can be rated.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Session ID or session does not belong to you.");
                }
            }
        });

        cancelBtn.addActionListener(e -> {
            String sessionId = JOptionPane.showInputDialog(this, "Enter Session ID to cancel:");
            if (sessionId != null && !sessionId.trim().isEmpty()) {
                Session s = sessionManager.findSessionById(sessionId.trim());
                if (s != null && s.getStudentEmail().equals(student.getEmail())) {
                    try {
                        s.cancel();
                        DataManager.saveSessions(sessionManager.getSessions());
                        JOptionPane.showMessageDialog(this, "Session cancelled successfully.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid Session ID.");
                }
            }
        });

        logoutBtn.addActionListener(e -> {
            new LoginFrame(authService, sessionManager).setVisible(true);
            dispose();
        });
    }

    private void loadTutors(String subjectFilter, String availabilityFilter, double minRating) {
        tutorModel.setRowCount(0);
        for (User u : authService.getUsers()) {
            if (u instanceof Tutor) {
                Tutor t = (Tutor) u;
                boolean matchesSubject = subjectFilter.isEmpty() || t.getSubjectsOffered().stream().anyMatch(sub -> sub.equalsIgnoreCase(subjectFilter));
                boolean matchesAvailability = availabilityFilter.isEmpty() || t.getAvailability().toLowerCase().contains(availabilityFilter.toLowerCase());
                boolean matchesRating = t.getAverageRating() >= minRating;

                if (matchesSubject && matchesAvailability && matchesRating) {
                    tutorModel.addRow(new Object[]{
                        t.getName(),
                        t.getEmail(),
                        t.getSubjectsOffered().toString(),
                        t.getHourlyRate(),
                        t.getAvailability(),
                        t.getAverageRating()
                    });
                }
            }
        }
    }
}