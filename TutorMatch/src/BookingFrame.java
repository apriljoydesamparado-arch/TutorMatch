import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.UUID;

public class BookingFrame extends JFrame {
    private Student student;
    private Tutor tutor;
    private SessionManager sessionManager;

    private JTextField subjectField, dateField, timeField, durationField;

    public BookingFrame(Student student, Tutor tutor, SessionManager sessionManager) {
        this.student = student;
        this.tutor = tutor;
        this.sessionManager = sessionManager;

        setTitle("Book Session with " + tutor.getName());
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("Subject:"));
        subjectField = new JTextField();
        panel.add(subjectField);

        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        dateField = new JTextField("2026-10-15");
        panel.add(dateField);

        panel.add(new JLabel("Start Time (HH:MM):"));
        timeField = new JTextField("10:00");
        panel.add(timeField);

        panel.add(new JLabel("Duration (Minutes):"));
        durationField = new JTextField("60");
        panel.add(durationField);

        JButton bookBtn = new JButton("Submit Booking Request");
        panel.add(bookBtn);

        add(panel);

        bookBtn.addActionListener(e -> {
            try {
                String subject = subjectField.getText().trim();
                LocalDateTime dateTime = LocalDateTime.parse(dateField.getText().trim() + "T" + timeField.getText().trim() + ":00");
                int duration = Integer.parseInt(durationField.getText().trim());
                String sessionId = "SESS-" + UUID.randomUUID().toString().substring(0, 5);

                sessionManager.bookSession(sessionId, student.getEmail(), tutor.getEmail(), subject, dateTime, duration);
                DataManager.saveSessions(sessionManager.getSessions());

                JOptionPane.showMessageDialog(this, "Booking request submitted successfully!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Booking Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}