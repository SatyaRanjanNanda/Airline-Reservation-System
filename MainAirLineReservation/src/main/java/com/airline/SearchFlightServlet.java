package com.airline;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SearchFlightServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");

        

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/AirlineDB", "root", "bubun@22")) {
             String source = request.getParameter("source");
             String destination = request.getParameter("destination");   
                
                PreparedStatement pst = con.prepareStatement("SELECT * FROM flights WHERE source=? AND destination=?");
                pst.setString(1, source);
                pst.setString(2, destination);
                ResultSet rs = pst.executeQuery();

                StringBuilder flights = new StringBuilder("<table border='1'><tr><th>Flight id</th><th>Airline</th><th>Departure</th><th>Arrival</th><th>Price</th><th>Seats</th><th>Book</th></tr>");

                boolean hasFlights = false;
                while (rs.next()) {
                    hasFlights = true;
                    flights.append("<tr>")
                    		.append("<td>").append(rs.getInt("flight_id")).append("</td>")
                            .append("<td>").append(rs.getString("airline")).append("</td>")
                            .append("<td>").append(rs.getString("departure_time")).append("</td>")
                            .append("<td>").append(rs.getString("arrival_time")).append("</td>")
                            .append("<td>").append(rs.getInt("price")).append("</td>")
                            .append("<td>").append(rs.getInt("seats_available")).append("</td>")
                            .append("<td><a href='bookFlight.jsp?flight_id=").append(rs.getInt("flight_id")).append("'>Book</a></td>")
                            .append("</tr>");
                }
                flights.append("</table>");

                if (hasFlights) {
                    request.setAttribute("flights", flights.toString());
                } else {
                    request.setAttribute("errorMessage", "No flights found for the selected route.");
                }

                // Forward to JSP page for displaying results
                RequestDispatcher rd = request.getRequestDispatcher("searchFlights.jsp");
                rd.forward(request, response);
                
            } catch (SQLException e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Database error occurred.");
                RequestDispatcher rd = request.getRequestDispatcher("searchFlights.jsp");
                rd.forward(request, response);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Database driver not found.");
            RequestDispatcher rd = request.getRequestDispatcher("searchFlights.jsp");
            rd.forward(request, response);
        }
    }
}
