import java.util.ArrayList;
import java.util.List;

public class Student extends User {
    
    private List<String> preferredSubjects;
    private List<String> enrolledSessions;

    public Student(String name, String email, String contactNumber, String password) {
        super(name, email, contactNumber, password);  
        this.preferredSubjects = new ArrayList<>();
        this.enrolledSessions = new ArrayList<>();
    }

    public List<String> getPreferredSubjects() {
        return preferredSubjects;
    }

    public void addPreferredSubject(String subject) {
        this.preferredSubjects.add(subject);
    }

    public List<String> getEnrolledSessions() {
        return enrolledSessions;
    }

    public void addEnrolledSession(String sessionId) {
        this.enrolledSessions.add(sessionId);
    }
 
    @Override
    public void viewDashboard() {
        System.out.println("Opening Student Dashboard for: " + getName());
    }

    public void searchTutors(String subject) {
        System.out.println("Searching for available tutors teaching: " + subject);
    }
}