package domain;

public class Administrator extends User {
    
    private String password;

    public Administrator(String userId, String name, String email, String password) {
        // Calls the constructor of the parent 'User' class
        super(userId, name, email);
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}