
package da;

import domain.Staff;
import java.sql.*;
import java.util.logging.*;
import javax.swing.*;

public class StaffDA {

    private String host = "jdbc:derby://localhost:1527/kmb";
    private String user = "nbuser";
    private String password = "123";
    private String tableName = "STAFF";
    private Connection connection;
    private PreparedStatement statement;

    public StaffDA() {
        createConnection();
    }

    public Staff getRecord(String id) {
        Staff staff = null;
        String queryStr = "SELECT * FROM " + tableName + " WHERE STAFF_ID = ?";
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                staff = new Staff(id, rs.getString(2), rs.getString(3), rs.getString(4),
                        rs.getDate(5), rs.getString(6));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return staff;
    }

    public Staff getRecord1(String name) {
        Staff staff = null;
        String queryStr = "SELECT * FROM " + tableName + " WHERE NAME = ?";
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, name);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                staff = new Staff(rs.getString(1), name, rs.getString(3), rs.getString(4),
                        rs.getDate(5), rs.getString(6));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return staff;
    }
    
    public Staff getRecord2(String ic) {
        Staff staff = null;
        String queryStr = "SELECT * FROM " + tableName + " WHERE IC = ?";
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, ic);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                staff = new Staff(rs.getString(1), rs.getString(2), ic, rs.getString(4),
                        rs.getDate(5), rs.getString(6));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return staff;
    }
    
    public Staff addRecord(Staff staff) {
        String queryStr = "INSERT INTO " + tableName
                + " VALUES(?, ?, ?, ?, ?, ?)";
        Staff s = null;
        java.sql.Date sqlDOB = new java.sql.Date(staff.getDob().getTime());
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, staff.getId());
            statement.setString(2, staff.getName());
            statement.setString(3, staff.getIc());
            statement.setString(4, staff.getEmail());
            statement.setDate(5, sqlDOB);
            statement.setString(6, staff.getContactNo());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return s;
    }

    public Staff updateRecord(Staff staff) {
        String queryStr = "UPDATE " + tableName
                + " SET NAME = ?, IC = ?, EMAIL = ?, DOB = ?, CONTACTNO = ?"
                + " WHERE STAFF_ID = ?";
        Staff s = null;
        java.sql.Date sqlDOB = new java.sql.Date(staff.getDob().getTime());
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, staff.getName());
            statement.setString(2, staff.getIc());
            statement.setString(3, staff.getEmail());
            statement.setDate(4, sqlDOB);
            statement.setString(5, staff.getContactNo());
            statement.setString(6, staff.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return s;
    }

    public Staff updateRecord1(Staff staff) {
        String queryStr = "UPDATE " + tableName
                + " SET STAFF_ID = ?, IC = ?, EMAIL = ?, DOB = ?, CONTACTNO = ?"
                + " WHERE NAME = ?";
        Staff s = null;
        java.sql.Date sqlDOB = new java.sql.Date(staff.getDob().getTime());
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, staff.getId());
            statement.setString(2, staff.getIc());
            statement.setString(3, staff.getEmail());
            statement.setDate(4, sqlDOB);
            statement.setString(5, staff.getContactNo());
            statement.setString(6, staff.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return s;
    }
    
    public Staff deleteRecord(Staff staff) {
        String queryStr = "DELETE FROM " + tableName + " WHERE STAFF_ID = ?";
        Staff s = null;
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, staff.getId());
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
        String id, name, ic, email, contactNo;
        java.util.Date DOB;
        int row = 0;
        Object record[][] = null;
        try {
            statement = connection.prepareStatement(queryStr);
            rs = statement.executeQuery();
            while (rs.next()) {
                id = rs.getString(1);
                name = rs.getString(2);
                ic = rs.getString(3);
                email = rs.getString(4);
                DOB = rs.getDate(5);
                contactNo = rs.getString(6);
                
                row++;
                record = new Object[row][6];
                rs = statement.executeQuery();
                for (int i = 0; i < row; i++) {
                    rs.next();
                    for (int j = 0; j < 6; j++) {
                        record[i][j] = rs.getObject(j + 1);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(StaffDA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return record;
    }
    
    public String generateStaffID() {
        String sqlQuery = "SELECT * FROM " + tableName + " ORDER BY STAFF_ID";
        ResultSet rs = null;
        String staffID = "";
        try {
            statement = connection.prepareStatement(sqlQuery);
            rs = statement.executeQuery();
            while (rs.next()) {
                staffID = rs.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return staffID;
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

