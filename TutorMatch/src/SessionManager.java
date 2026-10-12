import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SessionManager {
    private List<Session> sessions;

    public SessionManager() {
        this.sessions = new ArrayList<>();
    }

    public List<Session> getSessions() {
        return sessions;
    }

    // Book a session with conflict checking
    public boolean bookSession(String sessionId, String studentEmail, String tutorEmail, String subject, LocalDateTime dateTime, int durationMinutes) {
        // Check for scheduling conflicts before creating the session
        if (hasConflict(tutorEmail, studentEmail, dateTime, durationMinutes)) {
            throw new IllegalArgumentException("Booking conflict detected: Tutor or student already has a confirmed session at this time.");
        }

        Session newSession = new Session(sessionId, studentEmail, tutorEmail, subject, dateTime, durationMinutes);
        sessions.add(newSession);
        return true;
    }

    // Helper method to check if a tutor or student has overlapping sessions
    public boolean hasConflict(String tutorEmail, String studentEmail, LocalDateTime newStart, int durationMinutes) {
        LocalDateTime newEnd = newStart.plusMinutes(durationMinutes);

        for (Session s : sessions) {
            // We only check conflicts against active CONFIRMED or PENDING sessions
            if ("CONFIRMED".equals(s.getStatus()) || "PENDING".equals(s.getStatus())) {
                LocalDateTime existingStart = s.getDateTime();
                LocalDateTime existingEnd = existingStart.plusMinutes(s.getDurationMinutes());

                // Check if time slots overlap and involve the same tutor or student
                boolean isSameTutorOrStudent = s.getTutorEmail().equals(tutorEmail) || s.getStudentEmail().equals(studentEmail);

                if (isSameTutorOrStudent) {
                    // Overlap formula: (StartA < EndB) and (EndA > StartB)
                    if (newStart.isBefore(existingEnd) && newEnd.isAfter(existingStart)) {
                        return true; // Conflict found!
                    }
                }
            }
        }
        return false;
    }

    // Retrieve schedule for a specific user (student or tutor)
    public List<Session> getUserSchedule(String userEmail) {
        List<Session> userSessions = new ArrayList<>();
        for (Session s : sessions) {
            if (s.getStudentEmail().equals(userEmail) || s.getTutorEmail().equals(userEmail)) {
                userSessions.add(s);
            }
        }
        return userSessions;
    }

    // Find session by ID
    public Session findSessionById(String sessionId) {
        for (Session s : sessions) {
            if (s.getSessionId().equals(sessionId)) {
                return s;
            }
        }
        return null;
    }
}