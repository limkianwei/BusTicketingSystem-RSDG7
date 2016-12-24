
package da;

import domain.Bus;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import javax.swing.*;

public class BusDA {
    
    private String host = "jdbc:derby://localhost:1527/kmb";
    private String user = "nbuser";
    private String password = "123";
    private String tableName = "BUS";
    private Connection connection;
    private PreparedStatement statement;
    
    public BusDA() {
        createConnection();
    }
    
    public Bus getRecord(String plate_no) {
        Bus bus = null;
        String queryStr = "SELECT * FROM " + tableName + " WHERE BUS_PLATE_NO = ?";
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, plate_no);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                bus = new Bus(plate_no, rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6), rs.getString(7));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return bus;
    }
    
//    public Bus getRecord1(String bus_number) {
//        Bus bus = null;
//        String queryStr = "SELECT * FROM " + tableName + " WHERE BUS_NUMBER = ?";
//        try {
//            statement = connection.prepareStatement(queryStr);
//            statement.setString(1, bus_number);
//            ResultSet rs = statement.executeQuery();
//            if (rs.next()) {
//                bus = new Bus(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6), bus_number);
//            } else {
//                JOptionPane.showMessageDialog(null, "This bus plate number is not exists.");
//            }
//        } catch (SQLException ex) {
//            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
//        }
//        return bus;
//    }
    
    public Bus addRecord(Bus bus) {
        String queryStr = "INSERT INTO " + tableName
                + " VALUES(?, ?, ?, ?, ?, ?, ?)";
        Bus b = null;
        java.sql.Date sqlDOB = new java.sql.Date(bus.getDate_purchase().getTime());
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, bus.getPlate_no());
            statement.setString(2, bus.getType());
            statement.setString(3, bus.getStatus());
            statement.setString(4, bus.getModel());
            statement.setString(5, bus.getTotal_seat());
            statement.setDate(6, sqlDOB);
            statement.setString(7, bus.getBus_number());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return b;
    }

    public Bus updateRecord(Bus bus) {
        String queryStr = "UPDATE " + tableName
                + " SET BUS_TYPE = ?, BUS_STATUS = ?, BUS_MODEL = ?, TOTAL_SEAT = ?, DATE_PURCHASE = ?, BUS_NUMBER = ?"
                + " WHERE BUS_PLATE_NO = ?";
        Bus b = null;
        java.sql.Date sqlDOB = new java.sql.Date(bus.getDate_purchase().getTime());
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, bus.getType());
            statement.setString(2, bus.getStatus());
            statement.setString(3, bus.getModel());
            statement.setString(4, bus.getTotal_seat());
            statement.setDate(5, sqlDOB);
            statement.setString(6, bus.getBus_number());
            statement.setString(7, bus.getPlate_no());
            
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return b;
    }
    
    public Bus deleteRecord(Bus bus) {
        String queryStr = "DELETE FROM " + tableName + " WHERE BUS_PLATE_NO = ?";
        Bus b = null;
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, bus.getPlate_no());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return b;
    }

    public Object[][] getAllRecord() {
        ResultSet rs = null;
        String queryStr = "SELECT * FROM " + tableName;
        //Table variable
        String plate_no, status, model, total_seat, type, bus_number;
        java.util.Date date_purchase;
        int row = 0;
        Object record[][] = null;
        try {
            statement = connection.prepareStatement(queryStr);
            rs = statement.executeQuery();
            while (rs.next()) {
                plate_no = rs.getString(1);
                type = rs.getString(2);
                status = rs.getString(3);
                model = rs.getString(4);
                total_seat = rs.getString(5);
                date_purchase = rs.getDate(6);
                bus_number = rs.getString(7);
                row++;
                record = new Object[row][7];
                rs = statement.executeQuery();
                for (int i = 0; i < row; i++) {
                    rs.next();
                    for (int j = 0; j < 7; j++) {
                        record[i][j] = rs.getObject(j + 1);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(BusDA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return record;
    }
    
    public ArrayList<Bus> getAll() {
        ArrayList<Bus> bus = new ArrayList<Bus>();
        ResultSet rs = null;
        String queryStr = "SELECT * FROM " + tableName + " WHERE BUS_STATUS = 'Available'";
        try {
            statement = connection.prepareStatement(queryStr);
            rs = statement.executeQuery();
            while (rs.next()) {
                bus.add(new Bus(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6), rs.getString(7)));
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }

        return bus;
    }
    
//    public ArrayList<Bus> getBus() {
//        ArrayList<Bus> bus = new ArrayList<Bus>();
//        ResultSet rs = null;
//        String queryStr = "SELECT * FROM " + tableName ;
//        try {
//            statement = connection.prepareStatement(queryStr);
//            rs = statement.executeQuery();
//            while (rs.next()) {
//                bus.add(new Bus(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getDate(6), rs.getString(7)));
//            }
//        } catch (SQLException ex) {
//            ex.getMessage();
//        }
//
//        return bus;
//    }
    
    public String generateBusID() {
        String sqlQuery = "SELECT * FROM " + tableName + " ORDER BY BUS_PLATE_NO";
        ResultSet rs = null;
        String plateNo = "";
        try {
            statement = connection.prepareStatement(sqlQuery);
            rs = statement.executeQuery();
            while (rs.next()) {
                plateNo = rs.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return plateNo;
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
