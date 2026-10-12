import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Ensure the application runs safely on the Swing Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            try {
                // 1. Initialize core backend services
                AuthService authService = new AuthService();
                SessionManager sessionManager = new SessionManager();

                // 2. Load saved data from disk (Data Persistence)
                DataManager.loadUsers(authService);
                DataManager.loadSessions(sessionManager);

                // 3. Launch the Login GUI window (Screen 1)
                LoginFrame loginFrame = new LoginFrame(authService, sessionManager);
                loginFrame.setVisible(true);

            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, 
                    "Error starting TutorMatch: " + e.getMessage(), 
                    "Startup Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}