package com.airline;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SignupServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/airlinedb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "bubun@22";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // SQL query to insert new user
            String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, email);
            stmt.setString(3, password);

            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                // Redirect to login page on successful signup
                response.sendRedirect("login.jsp?success=Account created successfully. Please login.");
            } else {
                // Redirect back to signup page with error
                response.sendRedirect("signup.jsp?error=Registration failed. Try again.");
            }

            // Close resources
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("signup.jsp?error=Something went wrong");
        }
    }
}
