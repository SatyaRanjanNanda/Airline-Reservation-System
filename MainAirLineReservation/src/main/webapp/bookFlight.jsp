<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Book Flight</title>
    <link rel="stylesheet" href="styles.css">
</head>
<body>
    <div class="container">
        <h2>Book Your Flight</h2>
        <form action="BookingServlet" method="post">
            
             <input type="text" name="flight_id" placeholder="Flight ID" required>
            
            <select name="seat_type">
                <option value="Economy">Economy</option>
                <option value="Business">Business</option>
            </select>
            <button type="submit">Confirm Booking</button>
        </form>
    </div>
</body>
</html>
