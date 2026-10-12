import java.util.ArrayList;
import java.util.List;

public class AuthService {
    private List<User> users;

    public AuthService() {
        this.users = new ArrayList<>();
    }

    public List<User> getUsers() {
        return users;
    }

    // Register a new Student
    public boolean registerStudent(String name, String email, String contactNumber, String password) {
        if (emailExists(email)) {
            throw new IllegalArgumentException("Email is already registered. Please use a different email.");
        }
        
        Student newStudent = new Student(name, email, contactNumber, password);
        users.add(newStudent);
        return true;
    }

    // Register a new Tutor
    public boolean registerTutor(String name, String email, String contactNumber, String password, double hourlyRate) {
        if (emailExists(email)) {
            throw new IllegalArgumentException("Email is already registered. Please use a different email.");
        }

        Tutor newTutor = new Tutor(name, email, contactNumber, password, hourlyRate);
        users.add(newTutor);
        return true;
    }

    // Helper method to check if an email is already taken
    public boolean emailExists(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    // Authenticate login credentials
    public User login(String email, String password) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getPassword().equals(password)) {
                return u; // Return the logged-in user object (Student or Tutor)
            }
        }
        throw new IllegalArgumentException("Invalid email or password.");
    }

    // Find user by email
    public User findUserByEmail(String email) {
        for (User u : users) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }
}