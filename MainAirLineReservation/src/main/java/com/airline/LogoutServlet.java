package com.airline;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get the current session, if it exists
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate(); // Destroy session
        }

        // Redirect to login page with a logout success message
        response.sendRedirect("login.jsp?message=Logged out successfully");
    }
}
