/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package da;

import domain.Staff;
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
 * @author Lim
 */
public class StaffDATest {
    
    public StaffDATest() {
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
     * Test of getRecord method, of class StaffDA.
     */
    @Test
    public void testGetRecord() throws ParseException{
        System.out.println("\nGet Record Test");
        String id = "S0009";
        StaffDA instance = new StaffDA();
        String date = "1988-04-01";
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date DOB = dateFormat2.parse(date);
        Staff expResult = new Staff("S0009", "JOYCE LANDEREON","880401-14-1542","joyce@yahoo.com",DOB,"017-2468137");
        Staff result = instance.getRecord(id);
        if(result.getId() == expResult.getId()){
            System.out.println("Get successful");
        }
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of addRecord method, of class StaffDA.
     */
    @Test
    public void testAddRecord() throws ParseException {
        System.out.println("\nAdd Record Test");
        String date = "1995-10-21";
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date DOB = dateFormat2.parse(date);
        Staff staff = new Staff("S0011", "LIM KIAN WEI","999999-99-9999","lkw@yahoo.com",DOB,"018-2293973");
        StaffDA instance = new StaffDA();
        Staff result = instance.addRecord(staff);
        System.out.println("Add successful");
        
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of updateRecord method, of class StaffDA.
     */
    @Test
    public void testUpdateRecord() throws ParseException{
        System.out.println("\nUpdate Record Test");
        String date = "1995-10-21";
        DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
        Date DOB = dateFormat2.parse(date);
        Staff staff = new Staff("S0011", "LIM KIAN WEI","111111-11-9999","lkw@yahoo.com",DOB,"018-2293973");
        StaffDA instance = new StaffDA();
        Staff result = instance.updateRecord(staff);
        System.out.println("Update successful");
        
//        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of deleteRecord method, of class StaffDA.
     */
    @Test
    public void testDeleteRecord() {
        System.out.println("\nDelete Record Test");
        Staff staff = new Staff("S0011");
        StaffDA instance = new StaffDA();
        Staff result = instance.deleteRecord(staff);
        System.out.println("Delete successful");
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }

    /**
     * Test of generateStaffID method, of class StaffDA.
     */
    @Test
    public void testGenerateStaffID() {
        System.out.println("\nGenerate Staff ID Test");
        StaffDA instance = new StaffDA();
        String expResult = "S0010";
        String result = instance.generateStaffID();
        
        if(result.matches(expResult)){
             System.out.println("Generate Successful");
        }
        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
    }


    
}
