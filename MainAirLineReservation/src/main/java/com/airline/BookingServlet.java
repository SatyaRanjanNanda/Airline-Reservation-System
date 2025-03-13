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

public class BookingServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/AirlineDB";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "bubun@22";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("user_id");

        if (userId == null) {
            response.sendRedirect("login.jsp?error=Please login to book a flight.");
            return;
        }

        String flightIdStr = request.getParameter("flight_id");
        String seatType = request.getParameter("seat_type");

        try {
            int flightId = Integer.parseInt(flightIdStr);

            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Establish database connection
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);

            // Get flight price from flights table
            String queryPrice = "SELECT price FROM flights WHERE flight_id = ?";
            PreparedStatement stmtPrice = conn.prepareStatement(queryPrice);
            stmtPrice.setInt(1, flightId);
            ResultSet rs = stmtPrice.executeQuery();

            if (rs.next()) {
                double fare = rs.getDouble("price");

                // Insert booking into bookings table
                String sql = "INSERT INTO bookings (user_id, flight_id, seat_type, fare, booking_status) VALUES (?, ?, ?, ?, 'CONFIRMED')";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, userId);
                stmt.setInt(2, flightId);
                stmt.setString(3, seatType);
                stmt.setDouble(4, fare);

                int rowsInserted = stmt.executeUpdate();

                if (rowsInserted > 0) {
                    response.sendRedirect("dashboard.jsp?success=Booking confirmed successfully!");
                } else {
                    response.sendRedirect("book.jsp?error=Booking failed. Try again.");
                }

                // Close resources
                stmt.close();
            } else {
                response.sendRedirect("book.jsp?error=Flight not found.");
            }

            rs.close();
            stmtPrice.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("book.jsp?error=Something went wrong.");
        }
    }
}
