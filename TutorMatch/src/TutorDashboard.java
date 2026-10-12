import javax.swing.*;
import java.awt.*;
import java.util.List;

public class TutorDashboard extends JFrame {
    private Tutor tutor;
    private AuthService authService;
    private SessionManager sessionManager;

    public TutorDashboard(Tutor tutor, AuthService authService, SessionManager sessionManager) {
        this.tutor = tutor;
        this.authService = authService;
        this.sessionManager = sessionManager;

        setTitle("TutorMatch - Tutor Dashboard (" + tutor.getName() + ")");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Top Panel
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        JLabel welcomeLabel = new JLabel("Welcome, " + tutor.getName() + " (Tutor) | Rating: " + tutor.getAverageRating() + " ★");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        topPanel.add(logoutBtn, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Center Panel with Feature Buttons
        JPanel centerPanel = new JPanel(new GridLayout(5, 1, 15, 15));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        JButton updateProfileBtn = new JButton("Update Profile (Subjects & Availability)");
        JButton viewRequestsBtn = new JButton("View & Manage Pending Session Requests");
        JButton viewScheduleBtn = new JButton("View Teaching Schedule");
        JButton completeSessionBtn = new JButton("Mark Session as Completed");

        centerPanel.add(updateProfileBtn);
        centerPanel.add(viewRequestsBtn);
        centerPanel.add(viewScheduleBtn);
        centerPanel.add(completeSessionBtn);

        add(centerPanel, BorderLayout.CENTER);

        // --- Action Listeners ---
        updateProfileBtn.addActionListener(e -> {
            String sub = JOptionPane.showInputDialog(this, "Enter new subject offered (e.g., Mathematics):");
            if (sub != null && !sub.trim().isEmpty()) {
                tutor.addSubject(sub.trim());
            }
            String avail = JOptionPane.showInputDialog(this, "Enter your availability (e.g., Mon-Wed 1-5 PM):");
            if (avail != null && !avail.trim().isEmpty()) {
                tutor.setAvailability(avail.trim());
            }
            String rateStr = JOptionPane.showInputDialog(this, "Enter hourly rate ($):", tutor.getHourlyRate());
            if (rateStr != null && !rateStr.trim().isEmpty()) {
                try {
                    double newRate = Double.parseDouble(rateStr.trim());
                    tutor.setHourlyRate(newRate);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid rate format.");
                }
            }
            DataManager.saveUsers(authService.getUsers());
            JOptionPane.showMessageDialog(this, "Profile updated successfully!");
        });

        viewRequestsBtn.addActionListener(e -> {
            List<Session> sessions = sessionManager.getUserSchedule(tutor.getEmail());
            boolean hasPending = false;
            for (Session s : sessions) {
                if ("PENDING".equals(s.getStatus())) {
                    hasPending = true;
                    int choice = JOptionPane.showConfirmDialog(this, 
                        "Pending Request:\nStudent: " + s.getStudentEmail() + 
                        "\nSubject: " + s.getSubject() + 
                        "\nDate/Time: " + s.getDateTime() + 
                        "\n\nAccept this booking request?", 
                        "Manage Request", JOptionPane.YES_NO_OPTION);
                    
                    if (choice == JOptionPane.YES_OPTION) {
                        try {
                            s.confirm();
                            DataManager.saveSessions(sessionManager.getSessions());
                            JOptionPane.showMessageDialog(this, "Session confirmed successfully!");
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, ex.getMessage(), "Conflict Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        s.reject();
                        DataManager.saveSessions(sessionManager.getSessions());
                        JOptionPane.showMessageDialog(this, "Session rejected.");
                    }
                }
            }
            if (!hasPending) {
                JOptionPane.showMessageDialog(this, "No pending session requests.");
            }
        });

        viewScheduleBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder("Your Teaching Schedule:\n\n");
            List<Session> sessions = sessionManager.getUserSchedule(tutor.getEmail());
            if (sessions.isEmpty()) {
                sb.append("No sessions scheduled.");
            } else {
                for (Session s : sessions) {
                    sb.append("ID: ").append(s.getSessionId())
                      .append(" | Student: ").append(s.getStudentEmail())
                      .append(" | Subject: ").append(s.getSubject())
                      .append(" | Status: ").append(s.getStatus()).append("\n");
                }
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Schedule", JOptionPane.INFORMATION_MESSAGE);
        });

        completeSessionBtn.addActionListener(e -> {
            String sessionId = JOptionPane.showInputDialog(this, "Enter Session ID to mark as completed:");
            if (sessionId != null && !sessionId.trim().isEmpty()) {
                Session s = sessionManager.findSessionById(sessionId.trim());
                if (s != null && s.getTutorEmail().equals(tutor.getEmail())) {
                    try {
                        s.complete();
                        DataManager.saveSessions(sessionManager.getSessions());
                        JOptionPane.showMessageDialog(this, "Session marked as completed!");
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
}