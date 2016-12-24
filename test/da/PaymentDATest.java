/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da;

import domain.Transaction;
import domain.Payment;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
 * @author KY
 */
public class PaymentDATest {
    
    public PaymentDATest() {
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
     * Test of getRecord method, of class PaymentDA.
     */
    @Test
    public void testGetRecord() {
        
        System.out.println("\nGet Record Test");
        String id = "P0001";
        PaymentDA instance = new PaymentDA();
        Payment expResult = new Payment("P0001");
        Payment result = instance.getRecord(id);
        if(result.getId() == expResult.getId()){
            System.out.println("Get Record Success!");
        }
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of addRecord method, of class PaymentDA.
     */
    @Test
    public void testAddRecord() throws ParseException {
        System.out.println("\nAdd Record Test");
        String date = "2016-12-12";
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date paymentDate = dateFormat2.parse(date);
        Transaction transactionId = new Transaction("T0002");
        Payment payment = new Payment("P0002", paymentDate, "cash", 6.00 , 10.00, 4.00, "No available", "No available", "No available", "No available", "No available", transactionId);
        PaymentDA instance = new PaymentDA();
        Payment result = instance.addRecord(payment);
        System.out.println("Add successful");

        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }


    /**
     * Test of generatePaymentID method, of class PaymentDA.
     */
    @Test
    public void testGeneratePaymentID() {
        
        System.out.println("\nGenerate Payment ID");
        PaymentDA instance = new PaymentDA();
        String expResult = "P0001";
        String result = instance.generatePaymentID();
        if(result.matches(expResult)){
             System.out.println("Generate Successful");
        }

        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    
}
