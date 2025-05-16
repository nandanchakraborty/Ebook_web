package com.admin.servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.DAO.BookDAOImpl;
import com.DB.DBConnect;
import com.entity.BookDtls;

@WebServlet("/editbooks")
public class EditBooksServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String bookName = req.getParameter("bname");
            String author = req.getParameter("author");
            String price = req.getParameter("price");
            String status = req.getParameter("status");
            String category = req.getParameter("categories");
            String isbn = req.getParameter("isbn");

            BookDtls b = new BookDtls();
            b.setBookId(id);
            b.setBookName(bookName);
            b.setAuthor(author);
            b.setPrice(price);
            b.setStatus(status);
            b.setBookCategory(category);
            b.setIsbn(isbn);

            BookDAOImpl dao = new BookDAOImpl(DBConnect.getConn());
            boolean f = dao.updateEditBooks(b);

            HttpSession session = req.getSession();

            if (f) {
                session.setAttribute("succMsg", "Book Updated Successfully.");
            } else {
                session.setAttribute("failedMsg", "Something went wrong on the server.");
            }
            resp.sendRedirect("admin/all_books.jsp");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
