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
        if (admin != null && admin.getPassword().equals(password)) {
            System.out.println("Login success: Welcome, " + admin.getName() + "!");
            return true;
        }
        System.out.println("Error: Invalid credentials. Login failed.");
        return false;
    }

  
    public boolean logout() {
        System.out.println("Logout successful. Admin session closed.");
        return true;
    }
}