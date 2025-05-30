package com.user.servlet;

import java.io.IOException;
import com.DAO.UserDAOImpl;
import com.DB.DBConnect;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/forgot")
public class ForgotPassword extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try {

			String email = req.getParameter("email");
			String phno = req.getParameter("phno");
			String password = req.getParameter("password");
			HttpSession session = req.getSession();
			UserDAOImpl dao = new UserDAOImpl(DBConnect.getConn());
			if (dao.forgotPassword(email, phno, password)) {
				session.setAttribute("succMsg", "Password change sucessfully");
				resp.sendRedirect("forgot.jsp");
			} else {
				session.setAttribute("failedMsg", "something wrong on server ! try again");
				resp.sendRedirect("forgot.jsp");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}