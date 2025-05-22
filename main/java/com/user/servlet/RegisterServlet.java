package com.user.servlet;

import java.io.IOException;
import java.util.Properties;
import java.util.Random;

import com.DAO.UserDAOImpl;
import com.DB.DBConnect;
import com.entity.User;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
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
                    // Create user
                    User us = new User();
                    us.setName(name);
                    us.setEmail(email);
                    us.setPhno(phno);
                    us.setPassword(password);
                    boolean legacyHeadHandling = false;
					us.setVerified(false, legacyHeadHandling); // Add this field to your User model

                    // Generate OTP
                    int otp = new Random().nextInt(900000) + 100000;

                    // Store in session
                    session.setAttribute("tempUser", us);
                    session.setAttribute("authCode", String.valueOf(otp));

                    // Send OTP Email
                    sendOtpEmail(email, otp);

                    resp.sendRedirect("verify_code.jsp");
                } else {
                    session.setAttribute("failedMsg", "User already exists. Try another email.");
                    resp.sendRedirect("register.jsp");
                }
            } else {
                session.setAttribute("failedMsg", "Please agree to the terms & conditions.");
                resp.sendRedirect("register.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
            req.getSession().setAttribute("failedMsg", "Internal server error.");
            resp.sendRedirect("register.jsp");
        }
    }

    private void sendOtpEmail(String toEmail, int otp) {
        final String fromEmail = "nandanchakraborty90@gmail.com";
        final String password = "ziqtrrgvtubgkftz";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("OTP Verification");
            message.setContent("<h3>Your OTP is <b>" + otp + "</b></h3>", "text/html");
            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
