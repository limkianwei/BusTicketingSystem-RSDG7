
package da;

import domain.Payment;
import domain.Schedule;
import domain.Bus;
import domain.Transaction;
import domain.Staff;
import domain.Seat;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.*;
import javax.swing.*;

public class PaymentDA {
    private String host = "jdbc:derby://localhost:1527/kmb";
    private String user = "nbuser";
    private String password = "123";
    private String tableName = "PAYMENT";
    private Connection connection;
    private PreparedStatement statement;
    
    public PaymentDA() {
        createConnection();
    }
    
    public Payment getRecord(String id) {
        Payment payment = null;
        String queryStr = "SELECT * FROM " + tableName + " WHERE PAYMENT_ID = ?";
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                Transaction transaction = new TransactionDA().getRecord(rs.getString(12));
                payment = new Payment(id, rs.getDate(2), rs.getString(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6), rs.getString(7), rs.getString(8),
                        rs.getString(9), rs.getString(10), rs.getString(11), transaction);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return payment;
    }
    
    public Payment addRecord(Payment payment) {
        String queryStr = "INSERT INTO " + tableName
                + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Payment b = null;
        java.sql.Date paymentDate = new java.sql.Date(payment.getPayment_date().getTime());
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, payment.getId());
            statement.setDate(2, paymentDate);
            statement.setString(3, payment.getPayment_type());
            statement.setString(4, "" + payment.getTotal_amount());
            statement.setString(5, "" + payment.getPayment_amount());
            statement.setString(6, "" + payment.getBalance());
            statement.setString(7, payment.getCard_no());
            statement.setString(8, payment.getCard_holder());
            statement.setString(9, payment.getExpiry_date());
            statement.setString(10, payment.getCard_type());
            statement.setString(11, payment.getBank_name());
            statement.setString(12, payment.getTransaction().getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return b;
    }

    public Payment updateRecord(Payment payment) {
        String queryStr = "UPDATE " + tableName
                + " SET PAYMENT_DATE = ?, PAYMENT_TYPE = ?, TOTAL_AMOUNT = ?, PAYMENT_AMOUNT = ?, BALANCE = ?,"
                + " CARD_NO = ?, CARD_HOLDER = ?, EXPIRY_DATE = ?,  CARD_TYPE = ?,"
                + " BANK_NAME = ?, TRANSACTION_ID = ?"
                + " WHERE PAYMENT_ID = ?";
        Payment b = null;
        java.sql.Date paymentDate = new java.sql.Date(payment.getPayment_date().getTime());
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setDate(1, paymentDate);
            statement.setString(2, payment.getPayment_type());
            statement.setString(3, "" + payment.getTotal_amount());
            statement.setString(4, "" + payment.getPayment_amount());
            statement.setString(5, "" + payment.getBalance());
            statement.setString(6, payment.getCard_no());
            statement.setString(7, payment.getCard_holder());
            statement.setString(8, payment.getExpiry_date());
            statement.setString(9, payment.getCard_type());
            statement.setString(10, payment.getBank_name());
            statement.setString(11, payment.getTransaction().getId());
            statement.setString(12, payment.getId());
            statement.executeUpdate();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
        }
        return b;
    }
    
    public Payment deleteRecord(Payment payment) {
        String queryStr = "DELETE FROM " + tableName + " WHERE PAYMENT_ID = ?";
        Payment b = null;
        try {
            statement = connection.prepareStatement(queryStr);
            statement.setString(1, payment.getId());
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
        String payment_id, payment_type, card_no, card_holder, card_type, bank_name, transaction_id, expiry_date;
        java.util.Date payment_date;
        double total_amount, payment_amount, balance;
        int row = 0;
        Object record[][] = null;
        try {
            statement = connection.prepareStatement(queryStr);
            rs = statement.executeQuery();
            while (rs.next()) {
                payment_id = rs.getString(1);
                payment_date = rs.getDate(2);
                payment_type = rs.getString(3);
                total_amount = rs.getDouble(4);
                payment_amount = rs.getDouble(5);
                balance = rs.getDouble(6);
                card_no = rs.getString(7);
                card_holder = rs.getString(8);
                expiry_date = rs.getString(9);
                card_type = rs.getString(10);
                bank_name = rs.getString(11);
                transaction_id = rs.getString(12);
                row++;
                record = new Object[row][12];
                rs = statement.executeQuery();
                for (int i = 0; i < row; i++) {
                    rs.next();
                    for (int j = 0; j < 12; j++) {
                        record[i][j] = rs.getObject(j + 1);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(PaymentDA.class.getName()).log(Level.SEVERE, null, ex);
        }
        return record;
    }
    
    public String generatePaymentID() {
        String sqlQuery = "SELECT * FROM " + tableName + " ORDER BY PAYMENT_ID";
        ResultSet rs = null;
        String paymentID = "";
        try {
            statement = connection.prepareStatement(sqlQuery);
            rs = statement.executeQuery();
            while (rs.next()) {
                paymentID = rs.getString(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "The data is error.", "Error", JOptionPane.ERROR_MESSAGE);
        }
        return paymentID;
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
