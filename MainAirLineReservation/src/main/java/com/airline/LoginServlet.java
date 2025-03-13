package com.airline;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/airlinedb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "bubun@22";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Query to check if user exists
            String sql = "SELECT id, username FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // User found, create session
                HttpSession session = request.getSession();
                session.setAttribute("user_id", rs.getInt("id"));
                session.setAttribute("username", rs.getString("username"));

                // Redirect to homepage or dashboard
                response.sendRedirect("dashboard.jsp");
            } else {
                // Invalid credentials, redirect back to login page with error
                response.sendRedirect("login.jsp?error=Invalid credentials");
            }

            // Close resources
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("login.jsp?error=Something went wrong");
        }
    }
}
