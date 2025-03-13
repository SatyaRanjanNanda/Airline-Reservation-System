package com.airline;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class CancelBookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/airlinedb";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "bubun@22";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");

        // Check if user is logged in
        if (userId == null) {
            response.sendRedirect("login.jsp?error=Please login first.");
            return;
        }

        // Get the booking ID from the request
        String bookingIdStr = request.getParameter("booking_id");
        if (bookingIdStr == null || bookingIdStr.isEmpty()) {
            response.sendRedirect("dashboard.jsp?error=Invalid booking ID.");
            return;
        }

        int bookingId = Integer.parseInt(bookingIdStr);

        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish a connection
            conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Update the booking status to "CANCELED"
            String sql = "UPDATE bookings SET booking_status = 'CANCELED' WHERE booking_id = ? AND user_id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, bookingId);
            stmt.setInt(2, userId);

            int rowsUpdated = stmt.executeUpdate();

            if (rowsUpdated > 0) {
                response.sendRedirect("dashboard.jsp?success=Booking canceled successfully.");
            } else {
                response.sendRedirect("dashboard.jsp?error=Booking cancellation failed.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("dashboard.jsp?error=An error occurred. Please try again.");
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

