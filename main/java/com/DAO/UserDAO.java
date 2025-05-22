package com.DAO;

import com.entity.Feedback;
import com.entity.User;

public interface UserDAO {

    // Registration
    public boolean userRegister(User us);

    // Login
    public User login(String email, String password);

    // Profile update
    public boolean updateProfile(User us);

    // Check if user exists by email
    public boolean checkUser(String em);

    // Password verification for reset
    public boolean checkPasswordAuthen(String email, String phno);

    // Password check by user ID
    public boolean checkPassword(int id, String ps);

    // Save feedback
    public boolean saveFeedback(Feedback f);

    // Get user by ID
    public User getUserById(int uid);

    // Forgot password update
    public boolean forgotPassword(String email, String phno, String password);

    // Update user's verification status (set verified = true and clear token)
    public boolean updateUser(User us);

    // Verify user using token
    public boolean verifyUser(String token);
}
