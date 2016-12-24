
package control;

import da.SeatDA;
import domain.Bus;
import domain.Seat;
import java.util.ArrayList;

public class MaintainSeat {
    private SeatDA seatDA;

    public MaintainSeat() {
        seatDA = new SeatDA();
    }
    
    public Seat getRecord(String id){
        return seatDA.getRecord(id);
    }
    
    public Seat getRecord1(String bus_id, String schedule_id){
        return seatDA.getRecord1(bus_id, schedule_id);
    }
    
    public Seat getRecord2(String bus_no){
        return seatDA.getRecord2(bus_no);
    }
    
    public void addRecord(Seat seat){
        seatDA.addRecord(seat);
    }
    
    public void updateRecord(Seat seat){
        seatDA.updateRecord(seat);
    }
    
    public void updateRecord1(Seat seat){
        seatDA.updateRecord(seat);
    }
    
    public void updateRecord2(Seat seat){
        seatDA.updateRecord(seat);
    }
    
    public void updateRecord3(Seat seat){
        seatDA.updateRecord(seat);
    }
    
    public void deleteRecord(Seat seat){
        seatDA.deleteRecord(seat);
    }
    
    public Object[][] getAllRecord(){
        return seatDA.getAllRecord();
    }
    
    public String generateSeatID() {
        return seatDA.generateSeatID();
    }
    
    public ArrayList<Seat> getAll() {
        return seatDA.getAll();
    }
    
    public ArrayList<Seat> getAllSeat(String schedule,String bus){
        return seatDA.getAllSeat(schedule,bus);
    }    
    
    public ArrayList<Seat> getAllSeat1(String schedule,String bus){
        return seatDA.getAllSeat(schedule,bus);
    }    
    
    public String generateSeatNo() {
        return seatDA.generateSeatNo();
    }
    
    public void disconnectDatabase(){
        seatDA.shutDown();
    }
    
}