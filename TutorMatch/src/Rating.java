import java.util.List;

public class Rating {
    private String sessionId;
    private String studentEmail;
    private String tutorEmail;
    private int score; // Score from 1 to 5
    private String comment;

    public Rating(String sessionId, String studentEmail, String tutorEmail, int score, String comment) {
        this.sessionId = sessionId;
        this.studentEmail = studentEmail;
        this.tutorEmail = tutorEmail;
        setScore(score); // Encapsulation: validated via setter
        this.comment = comment;
    }

    // Getters and Setters with Validation (Encapsulation)
    public String getSessionId() {
        return sessionId;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public String getTutorEmail() {
        return tutorEmail;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Rating score must be between 1 and 5.");
        }
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    // Static helper method to calculate the average rating for a specific tutor
    public static double calculateAverageRating(String tutorEmail, List<Rating> allRatings) {
        int totalScore = 0;
        int count = 0;

        for (Rating r : allRatings) {
            if (r.getTutorEmail().equals(tutorEmail)) {
                totalScore += r.getScore();
                count++;
            }
        }

        if (count == 0) {
            return 0.0; // Default rating if no reviews exist yet
        }

        // Return rounded average to 1 decimal place
        return Math.round(((double) totalScore / count) * 10.0) / 10.0;
    }
}