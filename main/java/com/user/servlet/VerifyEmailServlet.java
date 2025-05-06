package com.user.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import com.DAO.UserDAOImpl;
import com.DB.DBConnect;

@WebServlet("/verify")
public class VerifyEmailServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String token = request.getParameter("token");
        UserDAOImpl dao = new UserDAOImpl(DBConnect.getConn());
        
        if(dao.verifyUser(token)) {
            request.getSession().setAttribute("succMsg", 
                "Email verified successfully! You can now login.");
        } else {
            request.getSession().setAttribute("failedMsg", 
                "Invalid or expired verification link.");
        }
        response.sendRedirect("login.jsp");
    }
}