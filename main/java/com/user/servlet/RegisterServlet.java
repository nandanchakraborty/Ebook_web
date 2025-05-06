package com.user.servlet;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import com.DAO.UserDAOImpl;
import com.DB.DBConnect;
import com.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String name = req.getParameter("fname");
            String email = req.getParameter("email");
            String phno = req.getParameter("phno");
            String password = req.getParameter("password");
            String check = req.getParameter("check");

            HttpSession session = req.getSession();

            if (check != null) {
                UserDAOImpl dao = new UserDAOImpl(DBConnect.getConn());
                
                if (!dao.checkUser(email)) {
                    // Generate verification token
                    String verificationToken = UUID.randomUUID().toString();
                    
                    // Save user with verification status false
                    User us = new User();
                    us.setName(name);
                    us.setEmail(email);
                    us.setPhno(phno);
                    us.setPassword(password);
                    us.setVerified(false);
                    us.setVerificationToken(verificationToken);
                    
                    boolean registrationSuccess = dao.userRegister(us);
                    
                    if (registrationSuccess) {
                        // Send verification email
                        boolean emailSent = sendVerificationEmail(email, verificationToken);
                        
                        if(emailSent) {
                            session.setAttribute("succMsg", "Registration Successful! Please check your email to verify your account.");
                        } else {
                            session.setAttribute("succMsg", "Registration Successful! But failed to send verification email.");
                        }
                        resp.sendRedirect("register.jsp");
                    } else {
                        session.setAttribute("failedMsg", "Server Error: Please try again.");
                        resp.sendRedirect("register.jsp");
                    }
                } else {
                    session.setAttribute("failedMsg", "User already exists. Try another email.");
                    resp.sendRedirect("register.jsp");
                }
            } else {
                session.setAttribute("failedMsg", "Please agree to terms & conditions.");
                resp.sendRedirect("register.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.getSession().setAttribute("failedMsg", "An unexpected error occurred.");
            resp.sendRedirect("register.jsp");
        }
    }

    private boolean sendVerificationEmail(String recipientEmail, String token) {
        final String username = "your.email@gmail.com"; // Your Gmail address
        final String password = "your-app-password";   // Your Gmail app password
        
        // SMTP server configuration
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        // For debugging
        props.put("mail.debug", "true");

        try {
            // Create session with authenticator
            Session session = Session.getInstance(props, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password);
                }
            });

            // Create email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Verify Your Email Address");
            
            ServletRequest req = null;
			// Create verification link
            String verificationLink = "http://" + req.getServerName() + ":" + req.getServerPort() + 
                                    req.getContentLength() + "/verify?token=" + token;
            
            // HTML content for email
            String htmlContent = "<html><body>"
                    + "<h2>Email Verification</h2>"
                    + "<p>Thank you for registering!</p>"
                    + "<p>Please click the link below to verify your email address:</p>"
                    + "<p><a href=\"" + verificationLink + "\">Verify Email</a></p>"
                    + "<p>Or copy this link to your browser:<br>"
                    + verificationLink + "</p>"
                    + "</body></html>";

            // Set email content
            message.setContent(htmlContent, "text/html");

            // Send email
            Transport.send(message);
            System.out.println("Verification email sent successfully to: " + recipientEmail);
            return true;
            
        } catch (Exception e) {
            System.err.println("Failed to send verification email:");
            e.printStackTrace();
            return false;
        }
    }
}