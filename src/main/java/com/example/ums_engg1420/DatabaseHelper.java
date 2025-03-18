package com.example.ums_engg1420;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper {
    private static final String URL = "jdbc:sqlite:events.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS events ("
                + " event_id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " event_name TEXT NOT NULL,"
                + " event_code TEXT UNIQUE NOT NULL,"
                + " description TEXT,"
                + " header_image TEXT,"
                + " location TEXT NOT NULL,"
                + " date_time TEXT NOT NULL,"
                + " capacity INTEGER NOT NULL,"
                + " cost REAL,"
                + " registered_students TEXT"
                + ");";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Event> getAllEvents() {
        List<Event> events = new ArrayList<>();
        String sql = "SELECT * FROM events";

        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                events.add(new Event(
                        rs.getInt("event_id"),
                        rs.getString("event_name"),
                        rs.getString("event_code"),
                        rs.getString("description"),
                        rs.getString("header_image"),
                        rs.getString("location"),
                        rs.getString("date_time"),
                        rs.getInt("capacity"),
                        rs.getDouble("cost"),
                        rs.getString("registered_students")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public static void addEvent(Event event) {
        String sql = "INSERT INTO events(event_name, event_code, description, header_image, location, date_time, capacity, cost, registered_students) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, event.getEventName());
            pstmt.setString(2, event.getEventCode());
            pstmt.setString(3, event.getDescription());
            pstmt.setString(4, event.getHeaderImage());
            pstmt.setString(5, event.getLocation());
            pstmt.setString(6, event.getDateTime());
            pstmt.setInt(7, event.getCapacity());
            pstmt.setDouble(8, event.getCost());
            pstmt.setString(9, event.getRegisteredStudents());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
