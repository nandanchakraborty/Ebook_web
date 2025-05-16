package com.user.servlet;

import java.io.IOException;

import com.DAO.UserDAOImpl;
import com.DB.DBConnect;
import com.entity.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

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

				boolean userExists = dao.checkUser(email);  // true if user already in DB

				if (!userExists) {
					boolean inserted = dao.userRegister(us);

					if (inserted) {
						session.setAttribute("succMsg", "Registration Successful.");
						resp.sendRedirect("register.jsp");
					} else {
						session.setAttribute("failedMsg", "Something went wrong on the server.");
						resp.sendRedirect("register.jsp");
					}

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
}
