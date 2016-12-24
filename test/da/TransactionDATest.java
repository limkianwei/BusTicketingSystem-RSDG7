/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da;

import domain.Bus;
import domain.Schedule;
import domain.Transaction;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Lim
 */
public class TransactionDATest {
    
    public TransactionDATest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getRecord method, of class TransactionDA.
     */
    @Test
    public void testGetRecord() {
        
        System.out.println("\nGet Record Test");
        String id = "T0001";
        TransactionDA instance = new TransactionDA();
        Transaction expResult = new Transaction("T0001");
        Transaction result = instance.getRecord(id);
        if(result.getId() == expResult.getId()){
            System.out.println("Get Record Success!");
        }
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of addRecord method, of class TransactionDA.
     */
    @Test
    public void testAddRecord() throws ParseException {
         System.out.println("\nAdd Record Test");
        String date = "2016-12-12";
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date departureDate = dateFormat2.parse(date);
        Schedule scheduleId = new Schedule("H00005");
        Transaction transaction = new Transaction("T0002", departureDate, 1,"E0161","No available", "No available", "No available", "InProcess", "HaventPay", scheduleId);
        TransactionDA instance = new TransactionDA();
        Transaction result = instance.addRecord(transaction);
        System.out.println("Add successful");
        
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of updateRecord method, of class TransactionDA.
     */
    @Test
    public void testUpdateRecord() throws ParseException {
        System.out.println("\nUpdate Record Test");
        String date = "2016-12-12";
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date departureDate = dateFormat2.parse(date);
        Schedule scheduleId = new Schedule("H00005");
        Transaction transaction = new Transaction("T0002", departureDate, 1,"E0161","No available", "No available", "No available", "Cancel", "HaventPay", scheduleId);
        TransactionDA instance = new TransactionDA();
        Transaction result = instance.updateRecord(transaction);
        System.out.println("Update successful");
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of generateTransactionID method, of class TransactionDA.
     */
    @Test
    public void testGenerateTransactionID() {
        System.out.println("\nGenerate Transaction ID");
        TransactionDA instance = new TransactionDA();
        String expResult = "T0002";
        String result = instance.generateTransactionID();
        if(result.matches(expResult)){
             System.out.println("Generate Successful");
        }
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

}
