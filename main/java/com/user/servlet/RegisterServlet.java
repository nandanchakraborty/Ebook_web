package com.user.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.DAO.UserDAOImpl;
import com.DB.DBConnect;
import com.entity.User;

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

            User us = new User();
            us.setName(name);
            us.setEmail(email);
            us.setPhno(phno);
            us.setPassword(password);

            HttpSession session = req.getSession();

            if (check != null) {
                UserDAOImpl dao = new UserDAOImpl(DBConnect.getConn());
                boolean userExists = dao.checkUser(email);

                if (!userExists) {
                    boolean registrationSuccess = dao.userRegister(us);

                    if (registrationSuccess) {
                        session.setAttribute("succMsg", "Registration Successful!");
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
            req.getSession().setAttribute("failedMsg", "An unexpected error occurred");
            resp.sendRedirect("register.jsp");
        }
    }
}