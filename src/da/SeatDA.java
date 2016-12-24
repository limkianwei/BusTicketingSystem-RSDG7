
package da;

import domain.Seat;
import domain.Bus;
import domain.Schedule;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import javax.swing.*;

public class SeatDA {

    private String host = "jdbc:derby://localhost:1527/kmb";
    private String user = "nbuser";
    private String password = "123";
    private String tableName = "SEAT";
    private Connection connection;
    private PreparedStatement statement;

    public SeatDA() {
        createConnection();
    }

    public Seat getRecord(String id) {
        Seat seat = null;
        String queryStr = "SELECT * FROM " + tableName + " WHERE SEAT_ID = ?";
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Bus bus = new BusDA().getRecord(rs.getString(4));
                Schedule schedule = new ScheduleDA().getRecord(rs.getString(5));
                seat = new Seat(id, rs.getString(2), rs.getString(3), bus, schedule);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return seat;
    }
    
    public Seat getRecord1(String bus_id, String schedule_id) {
        Seat seat = null;
        String queryStr = "SELECT * FROM " + tableName + " WHERE BUS_PLATE_NO = ? AND SCHEDULE_ID = ?";
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, bus_id);
            statement.setString(2, schedule_id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Bus bus = new BusDA().getRecord(bus_id);
                Schedule schedule = new ScheduleDA().getRecord(schedule_id);
                seat = new Seat(rs.getString(1), rs.getString(2), rs.getString(3), bus, schedule);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return seat;
    }
    
    public Seat getRecord2(String seat_no) {
        Seat seat = null;
        String queryStr = "SELECT * FROM " + tableName + " WHERE SEAT_NO = ?";
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, seat_no);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Bus bus = new BusDA().getRecord(rs.getString(4));
                Schedule schedule = new ScheduleDA().getRecord(rs.getString(5));
                seat = new Seat(rs.getString(1), seat_no, rs.getString(3), bus, schedule);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return seat;
    }
    
    public Seat addRecord(Seat seat) {
        String queryStr = "INSERT INTO " + tableName
                + " VALUES(?, ?, ?, ?, ?)";
        Seat s = null;
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, seat.getId());
            statement.setString(2, seat.getNo());
            statement.setString(3, seat.getStatus());
            statement.setString(4, "" + seat.getBus().getPlate_no());
            statement.setString(5, "" + seat.getSchedule().getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return s;
    }

    public Seat updateRecord(Seat seat) {
        String queryStr = "UPDATE " + tableName
                + " SET SEAT_NO = ?, SEAT_STATUS = ?, BUS_PLATE_NO = ?, SCHEDULE_ID = ?"
                + " WHERE SEAT_ID = ?";
        Seat s = null;
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, seat.getNo());
            statement.setString(2, seat.getStatus());
            statement.setString(3, "" + seat.getBus().getPlate_no());
            statement.setString(4, "" + seat.getSchedule().getId());
            statement.setString(5, seat.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return s;
    }
    
    public Seat updateRecord1(Seat seat) {
        String queryStr = "UPDATE " + tableName
                + " SET SEAT_NO = ?, SEAT_STATUS = ?, BUS_PLATE_NO = ?, SCHEDULE_ID = ?"
                + " WHERE SEAT_ID = ?";
        Seat s = null;
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, seat.getNo());
            statement.setString(2, seat.getStatus());
            statement.setString(3, "" + seat.getBus().getPlate_no());
            statement.setString(4, "" + seat.getSchedule().getId());
            statement.setString(5, seat.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return s;
    }
    
    public Seat updateRecord2(Seat seat) {
        String queryStr = "UPDATE " + tableName
                + " SET SEAT_NO = ?, SEAT_STATUS = ?, BUS_PLATE_NO = ?, SCHEDULE_ID = ?"
                + " WHERE SEAT_ID = ?";
        Seat s = null;
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, seat.getNo());
            statement.setString(2, seat.getStatus());
            statement.setString(3, "" + seat.getBus().getPlate_no());
            statement.setString(4, "" + seat.getSchedule().getId());
            statement.setString(5, seat.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return s;
    }
    
    public Seat updateRecord3(Seat seat) {
        String queryStr = "UPDATE " + tableName
                + " SET SEAT_NO = ?, SEAT_STATUS = ?, BUS_PLATE_NO = ?, SCHEDULE_ID = ?"
                + " WHERE SEAT_ID = ?";
        Seat s = null;
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, seat.getNo());
            statement.setString(2, seat.getStatus());
            statement.setString(3, "" + seat.getBus().getPlate_no());
            statement.setString(4, "" + seat.getSchedule().getId());
            statement.setString(5, seat.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return s;
    }
    
    public Seat deleteRecord(Seat seat) {
        String queryStr = "DELETE FROM " + tableName + " WHERE SEAT_ID = ?";
        Seat s = null;
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, seat.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return s;
    }

    public Object[][] getAllRecord() {
        ResultSet rs = null;
        String queryStr = "SELECT * FROM " + tableName;
        //Table variable
        String id, no, status, plate_no, schedule_id;
        int row = 0;
        Object record[][] = null;
        try {
            statement = connection.prepareStatement(queryStr);
            rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getString(1);
                no = rs.getString(2);
                status = rs.getString(3);
                plate_no = rs.getString(4);
                schedule_id = rs.getString(5);
                row++;
                record = new Object[row][5];
                rs = statement.executeQuery();
                for (int i = 0; i < row; i++) {
                    rs.next();
                    for (int j = 0; j < 5; j++) {
                        record[i][j] = rs.getObject(j + 1);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SeatDA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return record;
    }
    
    public ArrayList<Seat> getAll() {
        ArrayList<Seat> seat = new ArrayList<Seat>();
        ResultSet rs = null;
        String queryStr = "SELECT * FROM " + tableName + " WHERE SEAT_STATUS = 'Available'";
        try {
            statement = connection.prepareStatement(queryStr);
            rs = statement.executeQuery();
            while (rs.next()) {
                Bus bus = new BusDA().getRecord(rs.getString(4));
                Schedule schedule = new ScheduleDA().getRecord(rs.getString(5));
                seat.add(new Seat(rs.getString(1), rs.getString(2), rs.getString(3), bus, schedule));
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }

        return seat;
    }
    
    public ArrayList<Seat> getAllSeat(String schedule, String bus ) {
        ArrayList<Seat> seat = new ArrayList<Seat>();
        ResultSet rs = null;
        String queryStr = "SELECT * FROM " + tableName + " WHERE SCHEDULE_ID = ? AND BUS_PLATE_NO = ? AND SEAT_STATUS = 'Available'" ;
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, schedule);
            statement.setString(2, bus);
            rs = statement.executeQuery();
            while (rs.next()) {
                Bus bus1 = new BusDA().getRecord(rs.getString(4));
                Schedule schedule1 = new ScheduleDA().getRecord(rs.getString(5));
                seat.add(new Seat(rs.getString(1), rs.getString(2), rs.getString(3), bus1, schedule1));
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }

        return seat;
    }
    
    public ArrayList<Seat> getAllSeat1(String schedule, String bus ) {
        ArrayList<Seat> seat = new ArrayList<Seat>();
        ResultSet rs = null;
        String queryStr = "SELECT * FROM " + tableName + " WHERE SCHEDULE_ID = ? AND BUS_PLATE_NO = ?" ;
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, schedule);
            statement.setString(2, bus);
            rs = statement.executeQuery();
            while (rs.next()) {
                Bus bus1 = new BusDA().getRecord(rs.getString(4));
                Schedule schedule1 = new ScheduleDA().getRecord(rs.getString(5));
                seat.add(new Seat(rs.getString(1), rs.getString(2), rs.getString(3), bus1, schedule1));
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }

        return seat;
    }
    
    public String generateSeatID() {
        String sqlQuery = "SELECT * FROM " + tableName + " ORDER BY SEAT_ID";
        ResultSet rs = null;
        String seatID = "";
        try {
            statement = connection.prepareStatement(sqlQuery);
            rs = statement.executeQuery();
            while (rs.next()) {
                seatID = rs.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return seatID;
    }
    
    public String generateSeatNo() {
        String sqlQuery = "SELECT * FROM " + tableName + " ORDER BY SEAT_NO";
        ResultSet rs = null;
        String seatNo = "";
        try {
            statement = connection.prepareStatement(sqlQuery);
            rs = statement.executeQuery();
            while (rs.next()) {
                seatNo = rs.getString(2);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return seatNo;
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

