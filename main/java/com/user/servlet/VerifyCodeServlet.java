package com.user.servlet;

import java.io.IOException;

import com.DAO.UserDAOImpl;
import com.DB.DBConnect;
import com.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@WebServlet("/verify-code")
public class VerifyCodeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        String inputCode = req.getParameter("code");
        String actualCode = (String) session.getAttribute("authCode");
        User tempUser = (User) session.getAttribute("tempUser");

        if (actualCode != null && inputCode != null && actualCode.equals(inputCode)) {
            UserDAOImpl dao = new UserDAOImpl(DBConnect.getConn());
            boolean legacyHeadHandling = false;
			tempUser.setVerified(true, legacyHeadHandling);  // optional if column exists
            boolean inserted = dao.userRegister(tempUser);

            if (inserted) {
                session.removeAttribute("authCode");
                session.removeAttribute("tempUser");
                session.setAttribute("succMsg", "Registration successful! You may now log in.");
                resp.sendRedirect("login.jsp");
            } else {
                session.setAttribute("failedMsg", "Registration failed, try again.");
                resp.sendRedirect("register.jsp");
            }
        } else {
            session.setAttribute("failedMsg", "Invalid OTP.");
            resp.sendRedirect("verify_code.jsp");
        }
    }
}
