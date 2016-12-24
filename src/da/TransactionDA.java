package da;

import domain.Schedule;
import domain.Bus;
import domain.Transaction;
import domain.Staff;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import javax.swing.*;

public class TransactionDA {
    
    private String host = "jdbc:derby://localhost:1527/kmb";
    private String user = "nbuser";
    private String password = "123";
    private String tableName = "TRANSACTION1";
    private Connection connection;
    private PreparedStatement statement;
    
    public TransactionDA() {
        createConnection();
    }
    
    public Transaction getRecord(String id) {
        Transaction transaction = null;
        String queryStr = "SELECT * FROM " + tableName + " WHERE TRANSACTION_ID = ?";
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Schedule schedule = new ScheduleDA().getRecord(rs.getString(10));
                transaction = new Transaction(id, rs.getDate(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), schedule);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return transaction;
    }
    
    public Transaction addRecord(Transaction transaction) {
        String queryStr = "INSERT INTO " + tableName
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Transaction b = null;
        java.sql.Date sqlDate = new java.sql.Date(transaction.getDate().getTime());
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, transaction.getId());
            statement.setDate(2, sqlDate);
            statement.setString(3, "" + transaction.getSeatPurchase());
            statement.setString(4, transaction.getSeat());
            statement.setString(5, transaction.getSeat1());
            statement.setString(6, transaction.getSeat2());
            statement.setString(7, transaction.getSeat3());
            statement.setString(8, transaction.getBooking_status());
            statement.setString(9, transaction.getPayment_status());
            statement.setString(10, transaction.getSchedule().getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return b;
    }

    public Transaction updateRecord(Transaction transaction) {
        String queryStr = "UPDATE " + tableName
                + " SET TRANSACTION_DATE = ?, SEAT_PURCHASE = ?, NO_OF_SEAT = ?, NO_OF_SEAT1 = ?, NO_OF_SEAT2 = ?, NO_OF_SEAT3 = ?, BOOKING_STATUS = ?, PAYMENT_STATUS = ?, SCHEDULE_ID = ?"
                + " WHERE TRANSACTION_ID = ?";
        Transaction b = null;
        java.sql.Date sqlDate = new java.sql.Date(transaction.getDate().getTime());
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setDate(1, sqlDate);
            statement.setString(2, "" + transaction.getSeatPurchase());
            statement.setString(3, transaction.getSeat());
            statement.setString(4, transaction.getSeat1());
            statement.setString(5, transaction.getSeat2());
            statement.setString(6, transaction.getSeat3());
            statement.setString(7, transaction.getBooking_status());
            statement.setString(8, transaction.getPayment_status());
            statement.setString(9, transaction.getSchedule().getId());
            statement.setString(10, transaction.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return b;
    }
    
    public Transaction deleteRecord(Transaction transaction) {
        String queryStr = "DELETE FROM " + tableName + " WHERE TRANSACTION_ID = ?";
        Transaction b = null;
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, transaction.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return b;
    }
    
    public ArrayList<Transaction> getTransaction() {
        ArrayList<Transaction> transaction = new ArrayList<Transaction>();
        ResultSet rs = null;
        String queryStr = "SELECT * FROM " + tableName ;
        try {
            statement = connection.prepareStatement(queryStr);
            rs = statement.executeQuery();
            while (rs.next()) {
                Schedule schedule = new ScheduleDA().getRecord(rs.getString(10));
                transaction.add(new Transaction(rs.getString(1), rs.getDate(2), rs.getInt(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7),rs.getString(8), rs.getString(9) ,schedule));
            }
        } catch (SQLException ex) {
            ex.getMessage();
        }

        return transaction;
    }
    
    public Object[][] getAllRecord() {
        ResultSet rs = null;
        String queryStr = "SELECT * FROM " + tableName;
        //Table variable
        String transaction_id, seat, seat1, seat2, seat3, schedule_id, booking_status ,payment_status;
        java.util.Date transaction_date;
        int seat_purchase;
        int row = 0;
        Object record[][] = null;
        try {
            statement = connection.prepareStatement(queryStr);
            rs = statement.executeQuery();
            while (rs.next()) {
                transaction_id = rs.getString(1);
                transaction_date = rs.getDate(2);
                seat_purchase = rs.getInt(3);
                seat = rs.getString(4);
                seat1 = rs.getString(5);
                seat2 = rs.getString(6);
                seat3 = rs.getString(7);
                booking_status = rs.getString(8);
                payment_status = rs.getString(9);
                schedule_id = rs.getString(10);
                row++;
                record = new Object[row][10];
                rs = statement.executeQuery();
                for (int i = 0; i < row; i++) {
                    rs.next();
                    for (int j = 0; j < 10; j++) {
                        record[i][j] = rs.getObject(j + 1);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransactionDA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return record;
    }
    
    public String generateTransactionID() {
        String sqlQuery = "SELECT * FROM " + tableName + " ORDER BY TRANSACTION_ID";
        ResultSet rs = null;
        String transactionID = "";
        try {
            statement = connection.prepareStatement(sqlQuery);
            rs = statement.executeQuery();
            while (rs.next()) {
                transactionID = rs.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The data is error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return transactionID;
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
