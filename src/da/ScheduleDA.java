package da;

import domain.Schedule;
import domain.Bus;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import javax.swing.*;

public class ScheduleDA {
    
    private String host = "jdbc:derby://localhost:1527/kmb";
    private String user = "nbuser";
    private String password = "123";
    private String tableName = "SCHEDULE";
    private Connection connection;
    private PreparedStatement statement;
    
    public ScheduleDA() {
        createConnection();
    }
    
    public Schedule getRecord(String id) {
        Schedule schedule = null;
        String queryStr = "SELECT * FROM " + tableName + " WHERE SCHEDULE_ID = ?";
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Bus bus = new BusDA().getRecord(rs.getString(8));
                schedule = new Schedule(id, rs.getDate(2), rs.getTime(3), rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getInt(7), bus, rs.getString(9));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return schedule;
    }
    
//    public Schedule getRecord2(Date date, String busId) {
//        Schedule schedule = null;
//        String queryStr = "SELECT * FROM " + tableName + " WHERE SCHEDULE_ID = ? AND BUS_PLATE_NO = ?";
//        try {
//            statement = connection.prepareStatement(queryStr);
//            statement.setDate(1, date);
//            statement.setString(2, busId);
//            ResultSet rs = statement.executeQuery();
//            if (rs.next()) {
//                Bus bus = new BusDA().getRecord(busId);
//                schedule = new Schedule(rs.getString(1), date, rs.getTime(3), rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getInt(7), bus, rs.getString(9));
//            }
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
//        }
//        return schedule;
//    }
    
    public Schedule addRecord(Schedule schedule) {
        String queryStr = "INSERT INTO " + tableName
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Schedule b = null;
        java.sql.Date sqlDate = new java.sql.Date(schedule.getDepartureDate().getTime());
        java.sql.Time sqlTime = new java.sql.Time(schedule.getDepartureTime().getTime());
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, schedule.getId());
            statement.setDate(2, sqlDate);
            statement.setTime(3, sqlTime);
            statement.setString(4, schedule.getDeparturePlace());
            statement.setString(5, schedule.getDestination());
            statement.setString(6, "" + schedule.getPrice());
            statement.setString(7, "" + schedule.getAvailableSeat());
            statement.setString(8, "" + schedule.getBus().getPlate_no());
            statement.setString(9, schedule.getBus_seat_availability());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return b;
    }

    public Schedule updateRecord(Schedule schedule) {
        String queryStr = "UPDATE " + tableName
                + " SET DEPARTURE_DATE = ?, DEPARTURE_TIME = ?, DEPARTURE_PLACE = ?, DESTINATION = ?, PRICE = ?, NO_OF_AVAILABLE_SEAT = ?, BUS_PLATE_NO = ?, BUS_SEAT_AVAILABILITY = ?"
                + " WHERE SCHEDULE_ID = ?";
        Schedule b = null;
        java.sql.Date sqlDate = new java.sql.Date(schedule.getDepartureDate().getTime());
        java.sql.Time sqlTime = new java.sql.Time(schedule.getDepartureTime().getTime());
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setDate(1, sqlDate);
            statement.setTime(2, sqlTime);
            statement.setString(3, schedule.getDeparturePlace());
            statement.setString(4, schedule.getDestination());
            statement.setString(5, "" + schedule.getPrice());
            statement.setString(6, "" + schedule.getAvailableSeat());
            statement.setString(7, "" + schedule.getBus().getPlate_no());
            statement.setString(8, schedule.getBus_seat_availability());
            statement.setString(9, schedule.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return b;
    }
    
//    public Schedule deleteRecord(Schedule schedule) {
//        String queryStr = "DELETE FROM " + tableName + " WHERE SCHEDULE_ID = ?";
//        Schedule b = null;
//        try {
//            statement = connection.prepareStatement(queryStr);
//            statement.setString(1, schedule.getId());
//            statement.executeUpdate();
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
//        }
//        return b;
//    }
    
    public ArrayList<Schedule> getSchedule() {
        ArrayList<Schedule> schedule = new ArrayList<Schedule>();
        ResultSet rs = null;
        String queryStr = "SELECT * FROM " + tableName ;
        try {
            statement = connection.prepareStatement(queryStr);
            rs = statement.executeQuery();
            while (rs.next()) {
                Bus bus = new BusDA().getRecord(rs.getString(8));
                schedule.add(new Schedule(rs.getString(1), rs.getDate(2), rs.getTime(3), rs.getString(4), rs.getString(5), rs.getDouble(6), rs.getInt(7), bus, rs.getString(9)));
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }

        return schedule;
    }
    
    
    public Object[][] getAllRecord() {
        ResultSet rs = null;
        String queryStr = "SELECT * FROM " + tableName;
        //Table variable
        String schedule_id, departure_place, destination, bus_plate_no, bus_seat_availability;
        java.util.Date departure_date, departure_time;
        int available_seat;
        double price;
        int row = 0;
        Object record[][] = null;
        try {
            statement = connection.prepareStatement(queryStr);
            rs = statement.executeQuery();
            while (rs.next()) {
                schedule_id = rs.getString(1);
                departure_date = rs.getDate(2);
                departure_time = rs.getTime(3);
                departure_place = rs.getString(4);
                destination = rs.getString(5);
                price = rs.getDouble(6);
                available_seat = rs.getInt(7);
                bus_plate_no = rs.getString(8);
                bus_seat_availability = rs.getString(9);
                row++;
                record = new Object[row][9];
                rs = statement.executeQuery();
                for (int i = 0; i < row; i++) {
                    rs.next();
                    for (int j = 0; j < 9; j++) {
                        record[i][j] = rs.getObject(j + 1);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ScheduleDA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return record;
    }
    
    public String generateScheduleID() {
        String sqlQuery = "SELECT * FROM " + tableName + " ORDER BY SCHEDULE_ID";
        ResultSet rs = null;
        String id = "";
        try {
            statement = connection.prepareStatement(sqlQuery);
            rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return id;
    }

    
    private void createConnection() {
        try {
            connection = DriverManager.getConnection(host, user, password);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void shutDown() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
}
