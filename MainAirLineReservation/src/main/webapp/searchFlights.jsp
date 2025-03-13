<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Search Flights</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <div class="container">
        <h2>Search Flights</h2>
        <form action="SearchFlightServlet" method="post">
            <select name="source" id="source" required>
                <option value="" disabled selected>Select Source</option>
                <option value="Delhi">Delhi</option>
                <option value="Mumbai">Mumbai</option>
                <option value="Bangalore">Bangalore</option>
                <option value="Chennai">Chennai</option>
                <option value="Hyderabad">Hyderabad</option>
                <option value="Kolkata">Kolkata</option>
                <option value="Goa">Goa</option>
                <option value="Jaipur">Jaipur</option>
            </select>

            <select name="destination" id="destination" required>
                <option value="" disabled selected>Select Destination</option>
                <option value="Delhi">Delhi</option>
                <option value="Mumbai">Mumbai</option>
                <option value="Bangalore">Bangalore</option>
                <option value="Chennai">Chennai</option>
                <option value="Hyderabad">Hyderabad</option>
                <option value="Kolkata">Kolkata</option>
                <option value="Goa">Goa</option>
                <option value="Jaipur">Jaipur</option>
            </select>

            <button type="submit">Search</button>
        </form>

        <% 
            String flights = (String) request.getAttribute("flights");
            if (flights != null) { 
        %>
            <h3>Available Flights</h3>
            <%= flights %>
        <% } %>
    </div>
</body>
</html>
