import java.io.*;
import java.time.LocalDateTime;
import java.util.List;

public class DataManager {
    // File paths for local storage
    private static final String USERS_FILE = "../data/users.txt";
    private static final String SESSIONS_FILE = "../data/sessions.txt";
    private static final String RATINGS_FILE = "../data/ratings.txt";

    // Save Users to File
    public static void saveUsers(List<User> users) {
        ensureDirectoryExists();
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (User u : users) {
                if (u instanceof Student) {
                    writer.println("STUDENT," + u.getName() + "," + u.getEmail() + "," + u.getContactNumber() + "," + u.getPassword());
                } else if (u instanceof Tutor) {
                    Tutor t = (Tutor) u;
                    writer.println("TUTOR," + t.getName() + "," + t.getEmail() + "," + t.getContactNumber() + "," + t.getPassword() + "," + t.getHourlyRate() + "," + t.getAvailability());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving users: " + e.getMessage());
        }
    }

    // Load Users from File
    public static void loadUsers(AuthService authService) {
        File file = new File(USERS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String role = parts[0];
                    String name = parts[1];
                    String email = parts[2];
                    String contact = parts[3];
                    String password = parts[4];

                    if ("STUDENT".equals(role)) {
                        authService.registerStudent(name, email, contact, password);
                    } else if ("TUTOR".equals(role) && parts.length >= 7) {
                        double rate = Double.parseDouble(parts[5]);
                        String availability = parts[6];
                        authService.registerTutor(name, email, contact, password, rate);
                        User u = authService.findUserByEmail(email);
                        if (u instanceof Tutor) {
                            ((Tutor) u).setAvailability(availability);
                        }
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading users: " + e.getMessage());
        }
    }

    // Save Sessions to File
    public static void saveSessions(List<Session> sessions) {
        ensureDirectoryExists();
        try (PrintWriter writer = new PrintWriter(new FileWriter(SESSIONS_FILE))) {
            for (Session s : sessions) {
                writer.println(s.getSessionId() + "," + s.getStudentEmail() + "," + s.getTutorEmail() + "," + s.getSubject() + "," + s.getDateTime() + "," + s.getDurationMinutes() + "," + s.getStatus());
            }
        } catch (IOException e) {
            System.err.println("Error saving sessions: " + e.getMessage());
        }
    }

    // Load Sessions from File
    public static void loadSessions(SessionManager sessionManager) {
        File file = new File(SESSIONS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 7) {
                    String sessionId = parts[0];
                    String studentEmail = parts[1];
                    String tutorEmail = parts[2];
                    String subject = parts[3];
                    LocalDateTime dateTime = LocalDateTime.parse(parts[4]);
                    int duration = Integer.parseInt(parts[5]);
                    String status = parts[6];

                    sessionManager.bookSession(sessionId, studentEmail, tutorEmail, subject, dateTime, duration);
                    Session loadedSession = sessionManager.findSessionById(sessionId);
                    if (loadedSession != null) {
                        loadedSession.setStatus(status);
                    }
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            System.err.println("Error loading sessions: " + e.getMessage());
        }
    }

    // Helper to ensure the data folder exists
    private static void ensureDirectoryExists() {
        File dir = new File("../data");
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
}