
package domain;

import java.io.Serializable;
import java.util.Date;

public class Bus implements Serializable{
    private String plate_no;
    private String type;
    private String status;
    private String model;
    private String total_seat;
    private Date date_purchase;
    private String bus_number;
    
    public Bus(){
        
    }
    
    public Bus(String plate_no){
        this.plate_no = plate_no;
    }
    
    public Bus(String plate_no, String type, String status, String model, String total_seat, Date date_purchase, String bus_number){
        this.plate_no = plate_no;
        this.type = type;
        this.status = status;
        this.model = model;
        this.total_seat = total_seat;
        this.date_purchase = date_purchase;
        this.bus_number = bus_number;
    }

    public void setPlate_no(String plate_no) {
        this.plate_no = plate_no;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setTotal_seat(String total_seat) {
        this.total_seat = total_seat;
    }

    public void setDate_purchase(Date date_purchase) {
        this.date_purchase = date_purchase;
    }

    public void setBus_number(String bus_number) {
        this.bus_number = bus_number;
    }


    public String getPlate_no() {
        return plate_no;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getModel() {
        return model;
    }

    public String getTotal_seat() {
        return total_seat;
    }

    public Date getDate_purchase() {
        return date_purchase;
    }

    public String getBus_number() {
        return bus_number;
    }
    
}
