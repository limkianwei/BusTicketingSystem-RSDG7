
package control;

import da.BusDA;
import domain.Bus;
import java.util.ArrayList;

public class MaintainBus {
    private BusDA busDA;

    public MaintainBus() {
        busDA = new BusDA();
    }
    
    public Bus getRecord(String plate_no){
        return busDA.getRecord(plate_no);
    }
    
//    public Bus getRecord1(String bus_number){
//        return busDA.getRecord1(bus_number);
//    }
    
    public void addRecord(Bus bus){
        busDA.addRecord(bus);
    }
    
    public void updateRecord(Bus bus){
        busDA.updateRecord(bus);
    }
    
    public void deleteRecord(Bus bus){
        busDA.deleteRecord(bus);
    }
    
    public Object[][] getAllRecord(){
        return busDA.getAllRecord();
    }
    
    public String generateBusID() {
        return busDA.generateBusID();
    }
    
    public void disconnectDatabase(){
        busDA.shutDown();
    }
    
    public ArrayList<Bus> getAll() {
        return busDA.getAll();
    }
    
//    public ArrayList<Bus> getBus(){
//        return busDA.getBus();
//    }
}
