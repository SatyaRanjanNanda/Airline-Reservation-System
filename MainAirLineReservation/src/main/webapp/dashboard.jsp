<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="jakarta.servlet.http.HttpSession" %>

<%
    // Check if the user is logged in
    HttpSession userSession = request.getSession();
    Integer userId = (Integer) userSession.getAttribute("user_id");
    String username = (String) userSession.getAttribute("username");

    if (userId == null) {
        response.sendRedirect("login.jsp?error=Please login first.");
        return;
    }

    // Database connection details
    String DB_URL = "jdbc:mysql://localhost:3306/airlinedb";
    String DB_USER = "root";
    String DB_PASSWORD = "bubun@22";
    
    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;
%>

<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <div class="container">
        <h2>Welcome, <%= username %>!</h2>

        <h3>Your Bookings</h3>
        <table border="1">
            <tr>
                <th>Booking ID</th>
                <th>Flight ID</th>
                <th>Seat Type</th>
                <th>Fare</th>
                <th>Status</th>
                <th>Cancel</th>
            </tr>

            <%
                try {
                    Class.forName("com.mysql.cj.jdbc.Driver");
                    conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                    String sql = "SELECT * FROM bookings WHERE user_id = ?";
                    stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, userId);
                    rs = stmt.executeQuery();

                    while (rs.next()) {
            %>
                        <tr>
                            <td><%= rs.getInt("booking_id") %></td>
                            <td><%= rs.getInt("flight_id") %></td>
                            <td><%= rs.getString("seat_type") %></td>
                            <td>₹<%= rs.getDouble("fare") %></td>
                            <td><%= rs.getString("booking_status") %></td>
                            <td>
                                <% if ("CONFIRMED".equals(rs.getString("booking_status"))) { %>
                                    <form action="CancelBookingServlet" method="post">
                                        <input type="hidden" name="booking_id" value="<%= rs.getInt("booking_id") %>">
                                        <button type="submit">Cancel</button>
                                    </form>
                                <% } else { %>
                                    Canceled
                                <% } %>
                            </td>
                        </tr>
            <%
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (rs != null) rs.close();
                    if (stmt != null) stmt.close();
                    if (conn != null) conn.close();
                }
            %>
        </table>

        <br>
        <a href="searchFlights.jsp"><button>Book a Flight</button></a>
        <a href="LogoutServlet"><button>Logout</button></a>
    </div>
</body>
</html>
