/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da;

import domain.Bus;
import control.MaintainBus;
import da.BusDA;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class BusDATest {

    public BusDATest(){
        
    }

    @BeforeClass
    public static void setUpClass() {
        
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp(){
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getRecord method, of class BusDA.
     */
    @Test
    public void testGetRecord() throws ParseException {
        System.out.println("\nGet Record Test");
        String plate_no = "B0001";
        BusDA instance = new BusDA();
        String date = "2016-02-03";
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date purchaseDate = dateFormat2.parse(date);
        Bus expResult = new Bus("B0001", "Economic", "Available", "MAN-SE", "40", purchaseDate, "WBK2013");
        Bus result = instance.getRecord(plate_no);
        if (result.getPlate_no() == expResult.getPlate_no()) {
            System.out.println("Get Record Success!");

        }

        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of addRecord method, of class BusDA.
     */
    @Test
    public void testAddRecord() throws ParseException {
        System.out.println("\nAdd Record Test");
        BusDA instance = new BusDA();
        String date = "2016-03-23";
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date purchaseDate = dateFormat2.parse(date);
        Bus bus = new Bus("B0006", "Premium", "Available", "ACG-675", "20", purchaseDate, "BMSA154");
        Bus result = instance.addRecord(bus);
        System.out.println("Add successful");

        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of updateRecord method, of class BusDA.
     */
    @Test
    public void testUpdateRecord() throws ParseException {
        System.out.println("\nUpdate Record Test");
        String date = "2017-03-24";
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date purchaseDate = dateFormat2.parse(date);
        Bus bus = new Bus("B0006", "Premium", "Maintenance", "ACG-675", "20", purchaseDate, "BMSA154");
        BusDA instance = new BusDA();
        Bus result = instance.updateRecord(bus);
        System.out.println("Update successful");
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteRecord method, of class BusDA.
     */
    @Test
    public void testDeleteRecord() {
        System.out.println("\nDelete Record Test");
        Bus bus = new Bus("B0006");
        BusDA instance = new BusDA();
        Bus result = instance.deleteRecord(bus);
        System.out.println("Delete successful");

        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of generateBusID method, of class BusDA.
     */
    @Test
    public void testGenerateBusID() {
        System.out.println("\nGenerate Bus ID Test");
        BusDA instance = new BusDA();

        String expResult = "B0009";
        String result = instance.generateBusID();
        
        if(result.matches(expResult)){
             System.out.println("Generate Successful");
        }
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }


}
