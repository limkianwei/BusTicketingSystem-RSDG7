
package control;

import da.ScheduleDA;
import domain.Schedule;
import java.util.ArrayList;
import java.sql.Date;

public class MaintainSchedule {
    private ScheduleDA scheduleDA;

    public MaintainSchedule() {
        scheduleDA = new ScheduleDA();
    }
    
    public Schedule getRecord(String id){
        return scheduleDA.getRecord(id);
    }
    
//    public Schedule getRecord2(Date date,String busId){
//        return scheduleDA.getRecord2(date, busId);
//    }
    
    public void addRecord(Schedule schedule){
        scheduleDA.addRecord(schedule);
    }
    
    public void updateRecord(Schedule schedule){
        scheduleDA.updateRecord(schedule);
    }
    
//    public void deleteRecord(Schedule schedule){
//        scheduleDA.deleteRecord(schedule);
//    }
    
    public Object[][] getAllRecord(){
        return scheduleDA.getAllRecord();
    }
    
    public ArrayList<Schedule> getSchedule(){
        return scheduleDA.getSchedule();
    }
    
    public String generateScheduleID() {
        return scheduleDA.generateScheduleID();
    }
    
    public void disconnectDatabase(){
        scheduleDA.shutDown();
    }
    
}
