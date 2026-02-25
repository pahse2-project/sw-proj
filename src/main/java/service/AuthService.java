package service;

import domain.Administrator;
import java.util.HashMap;
import java.util.Map;


public class AuthService {
    
    private Map<String, Administrator> adminDatabase;

    public AuthService() {
        this.adminDatabase = new HashMap<>();
    }

    public void registerAdmin(Administrator admin) {
        adminDatabase.put(admin.getUserId(), admin);
    }


    public boolean loginAdministrator(String userId, String password) {
        Administrator admin = adminDatabase.get(userId);
        
        // Check if admin exists and if the password matches
        if (admin != null && admin.getPassword().equals(password)) {
            System.out.println("Login success: Welcome, " + admin.getName() + "!");
            return true;
        }
        
        // If it fails, print the required error message
        System.out.println("Error: Invalid credentials. Login failed.");
        return false;
    }
}