import java.util.ArrayList;
import java.util.List;

public class Tutor extends User {
     
    private List<String> subjectsOffered;
    private double hourlyRate;
    private String availability;
    private double averageRating;

    public Tutor(String name, String email, String contactNumber, String password, double hourlyRate) {
        super(name, email, contactNumber, password);
        this.subjectsOffered = new ArrayList<>();
        setHourlyRate(hourlyRate);  
        this.availability = "Not Set";
        this.averageRating = 0.0;
    }

    public List<String> getSubjectsOffered() {
        return subjectsOffered;
    }

    public void addSubject(String subject) {
        this.subjectsOffered.add(subject);
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        if (hourlyRate < 0) {
            throw new IllegalArgumentException("Hourly rate cannot be negative.");
        }
        this.hourlyRate = hourlyRate;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }
 
    @Override
    public void viewDashboard() {
        System.out.println("Opening Tutor Dashboard for: " + getName());
    }
}