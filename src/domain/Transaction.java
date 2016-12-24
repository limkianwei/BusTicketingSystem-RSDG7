
package domain;

import java.io.Serializable;
import java.util.Date;

public class Transaction implements Serializable{
    private String id;
    private Date date;
    private int seatPurchase;
    private String seat;
    private String seat1;
    private String seat2;
    private String seat3;
    private String booking_status;
    private String payment_status;
    private Schedule schedule;
    
    
    public Transaction(){
        
    }
    
    public Transaction(String id){
        this.id = id;
    }

    public Transaction(String id, Date date, int seatPurchase,String seat, String seat1, String seat2, String seat3, String booking_status, String payment_status, Schedule schedule ) {
        this.id = id;
        this.date = date;
        this.seatPurchase = seatPurchase;
        this.seat = seat;
        this.seat1 = seat1;
        this.seat2 = seat2;
        this.seat3 = seat3;
        this.booking_status = booking_status;
        this.payment_status = payment_status;
        this.schedule = schedule;
        
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    public void setSeatPurchase(int seatPurchase) {
        this.seatPurchase = seatPurchase;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public void setSeat1(String seat1) {
        this.seat1 = seat1;
    }

    public void setSeat2(String seat2) {
        this.seat2 = seat2;
    }

    public void setSeat3(String seat3) {
        this.seat3 = seat3;
    }

    public void setBooking_status(String booking_status) {
        this.booking_status = booking_status;
    }
    
    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }
    
    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public String getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public int getSeatPurchase() {
        return seatPurchase;
    }

    public String getSeat() {
        return seat;
    }

    public String getSeat1() {
        return seat1;
    }

    public String getSeat2() {
        return seat2;
    }

    public String getSeat3() {
        return seat3;
    }
    
    public String getBooking_status() {
        return booking_status;
    }

    
    public String getPayment_status() {
        return payment_status;
    }
    
    public Schedule getSchedule() {
        return schedule;
    }
    
}
