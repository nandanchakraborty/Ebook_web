package com.user.servlet;

import java.io.File;
import java.io.IOException;

import com.DAO.BookDAOImpl;
import com.DB.DBConnect;
import com.entity.BookDtls;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/add_old_book")
@MultipartConfig
public class AddOldBook extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Get form data
            String bookName = req.getParameter("bname");
            String author = req.getParameter("author");
            String price = req.getParameter("price");
            String isbn = req.getParameter("isbn");
            String userEmail = req.getParameter("user");

            String categories = "Old";
            String status = "Active";

            // Get uploaded file
            Part part = req.getPart("bimg");
            String fileName = part.getSubmittedFileName();

            // Create Book object
            BookDtls book = new BookDtls(bookName, author, price, categories, status, fileName, userEmail, isbn);

            // Save to database
            BookDAOImpl dao = new BookDAOImpl(DBConnect.getConn());
            boolean success = dao.addBooks(book);

            // Prepare session for feedback
            HttpSession session = req.getSession();

            if (success) {
                // File upload path
                String path = getServletContext().getRealPath("") + File.separator + "book";

                // Create folder if not exists
                File uploadDir = new File(path);
                if (!uploadDir.exists()) {
                    uploadDir.mkdirs();
                }

                // Save the file
                part.write(path + File.separator + fileName);

                session.setAttribute("succMsg", "Book added successfully.");
            } else {
                session.setAttribute("failedMsg", "Something went wrong on the server.");
            }

            resp.sendRedirect("sell_book.jsp");

        } catch (Exception e) {
            e.printStackTrace();
            req.getSession().setAttribute("failedMsg", "Exception: " + e.getMessage());
            resp.sendRedirect("sell_book.jsp");
        }
    }
}
