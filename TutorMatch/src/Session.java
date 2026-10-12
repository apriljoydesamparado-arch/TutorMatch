import java.time.LocalDateTime;

public class Session {
    private String sessionId;
    private String studentEmail;
    private String tutorEmail;
    private String subject;
    private LocalDateTime dateTime;
    private int durationMinutes;
    private String status; // PENDING, CONFIRMED, CANCELLED, COMPLETED, REJECTED

    public Session(String sessionId, String studentEmail, String tutorEmail, String subject, LocalDateTime dateTime, int durationMinutes) {
        this.sessionId = sessionId;
        this.studentEmail = studentEmail;
        this.tutorEmail = tutorEmail;
        this.subject = subject;
        this.dateTime = dateTime;
        this.durationMinutes = durationMinutes;
        this.status = "PENDING"; // Every new booking starts as pending until the tutor confirms
    }

    // Getters and Setters with Encapsulation & Validation
    public String getSessionId() {
        return sessionId;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getTutorEmail() {
        return tutorEmail;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        if (status == null || status.trim().isEmpty()) {
            throw new IllegalArgumentException("Status cannot be empty.");
        }
        this.status = status;
    }

    // Session Lifecycle Methods
    public void confirm() {
        if ("PENDING".equals(this.status)) {
            this.status = "CONFIRMED";
        } else {
            throw new IllegalStateException("Only pending sessions can be confirmed.");
        }
    }

    public void reject() {
        if ("PENDING".equals(this.status)) {
            this.status = "REJECTED";
        } else {
            throw new IllegalStateException("Only pending sessions can be rejected.");
        }
    }

    public void cancel() {
        if ("PENDING".equals(this.status) || "CONFIRMED".equals(this.status)) {
            this.status = "CANCELLED";
        } else {
            throw new IllegalStateException("Completed or already cancelled sessions cannot be cancelled.");
        }
    }

    public void complete() {
        if ("CONFIRMED".equals(this.status)) {
            this.status = "COMPLETED";
        } else {
            throw new IllegalStateException("Only confirmed sessions can be marked as completed.");
        }
    }
}