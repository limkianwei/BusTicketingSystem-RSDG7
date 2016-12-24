/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da;

import domain.Schedule;
import domain.Bus;
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
public class ScheduleDATest {
    
    public ScheduleDATest() {
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
     * Test of getRecord method, of class ScheduleDA.
     */
    @Test
    public void testGetRecord() {
        System.out.println("\nGet Record Test");
        String id = "H00001";
        ScheduleDA instance = new ScheduleDA();
        Schedule expResult = new Schedule("H00001");
        Schedule result = instance.getRecord(id);
        if(result.getId() == expResult.getId()){
            System.out.println("Get Record Success!");
        }
        
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of addRecord method, of class ScheduleDA.
     */
    @Test
    public void testAddRecord() throws ParseException {
        System.out.println("\nAdd Record Test");
        String date = "2018-05-08";
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date departureDate = dateFormat2.parse(date);
        String time = "10:00:00";
        DateFormat timeFormat2 = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
        Date departureTime = timeFormat2.parse(time);
        Bus busId = new Bus("B0003");
        Schedule schedule = new Schedule("H00006", departureDate, departureTime,"Pudu Sentral","Ipoh",15.00, 40, busId, "Haveseat");
        ScheduleDA instance = new ScheduleDA();
        Schedule result = instance.addRecord(schedule);
        System.out.println("Add successful");
        
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of updateRecord method, of class ScheduleDA.
     */
    @Test
    public void testUpdateRecord() throws ParseException {
        System.out.println("\nUpdate Record Test");
        String date = "2017-02-02";
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date departureDate = dateFormat2.parse(date);
        String time = "13:00:00";
        DateFormat timeFormat2 = new SimpleDateFormat("hh:mm:ss", Locale.ENGLISH);
        Date departureTime = timeFormat2.parse(time);
        Bus busId = new Bus("B0002");
        Schedule schedule = new Schedule("H00006", departureDate, departureTime,"Pudu Sentral","Ipoh",15.00, 40, busId, "Haveseat");
        ScheduleDA instance = new ScheduleDA();
        Schedule result = instance.updateRecord(schedule);
        System.out.println("Update successful");

        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of generateScheduleID method, of class ScheduleDA.
     */
    @Test
    public void testGenerateScheduleID() {
        System.out.println("\nGenerate Schedule ID Test");
        ScheduleDA instance = new ScheduleDA();
        String expResult = "H00006";
        String result = instance.generateScheduleID();
        if(result.matches(expResult)){
             System.out.println("Generate Successful");
        }
        
        
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    
}
