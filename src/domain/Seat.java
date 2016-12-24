
package domain;

import java.io.Serializable;
import java.util.Date;

public class Seat implements Serializable{
    private String id;
    private String no;
    private String status;
    private Bus bus;
    private Schedule schedule;
    
    public Seat(){
        
    }
    
    public Seat(String id){
        this.id = id;
    }
    
    public Seat(String id, String no, String status, Bus bus, Schedule schedule){
        this.id = id;
        this.no = no;
        this.status = status;
        this.bus = bus;
        this.schedule = schedule;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getId() {
        return id;
    }

    public String getNo() {
        return no;
    }

    public String getStatus() {
        return status;
    }

    public Bus getBus() {
        return bus;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
