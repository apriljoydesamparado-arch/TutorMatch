import javax.swing.*;
import java.awt.*;

public class RatingFrame extends JFrame {
    private Session session;
    private JTextField scoreField;
    private JTextArea commentArea;

    public RatingFrame(Session session) {
        this.session = session;

        setTitle("Rate Completed Session");
        setSize(350, 300);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        scorePanel.add(new JLabel("Score (1 to 5):"));
        scoreField = new JTextField("5", 5);
        scorePanel.add(scoreField);
        panel.add(scorePanel);

        JPanel commentPanel = new JPanel(new BorderLayout());
        commentPanel.add(new JLabel("Comment:"), BorderLayout.NORTH);
        commentArea = new JTextArea(3, 20);
        commentPanel.add(new JScrollPane(commentArea), BorderLayout.CENTER);
        panel.add(commentPanel);

        JButton submitBtn = new JButton("Submit Rating");
        panel.add(submitBtn);

        add(panel, BorderLayout.CENTER);

        submitBtn.addActionListener(e -> {
            try {
                int score = Integer.parseInt(scoreField.getText().trim());
                if (score < 1 || score > 5) {
                    throw new IllegalArgumentException("Score must be between 1 and 5.");
                }
                JOptionPane.showMessageDialog(this, "Thank you! Rating submitted successfully.");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}