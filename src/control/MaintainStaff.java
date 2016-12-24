
package control;

import da.StaffDA;
import domain.Staff;

public class MaintainStaff {
    private StaffDA staffDA;

    public MaintainStaff() {
        staffDA = new StaffDA();
    }
    
    public Staff getRecord(String id){
        return staffDA.getRecord(id);
    }
    
    public Staff getRecord1(String name){
        return staffDA.getRecord1(name);
    }
    
    public Staff getRecord2(String ic){
        return staffDA.getRecord2(ic);
    }
    
    public void addRecord(Staff staff){
        staffDA.addRecord(staff);
    }
    
    public void updateRecord(Staff staff){
        staffDA.updateRecord(staff);
    }
    
    public void updateRecord1(Staff staff){
        staffDA.updateRecord(staff);
    }
    
    public void deleteRecord(Staff staff){
        staffDA.deleteRecord(staff);
    }
    
    public Object[][] getAllRecord(){
        return staffDA.getAllRecord();
    }
    
    public String generateStaffID() {
        return staffDA.generateStaffID();
    }
    
    public void disconnectDatabase(){
        staffDA.shutDown();
    }
    
}
