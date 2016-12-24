
package domain;

import java.io.Serializable;
import java.util.Date;

public class Schedule implements Serializable{
     private String id;
    private Date departureDate;
    private Date departureTime;
    private String departurePlace;
    private String destination;
    private Double price;
    private int availableSeat;
    private Bus bus;
    private String bus_seat_availability;
    
    public Schedule(){
        
    }

    public Schedule(String id){
        this.id = id;
    }
    
    public Schedule(String id, Date departureDate, Date departureTime, String departurePlace, String destination, Double price, int availableSeat, Bus bus, String bus_seat_availability) {
        this.id = id;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.departurePlace = departurePlace;
        this.destination = destination;
        this.price = price;
        this.availableSeat = availableSeat;
        this.bus = bus;
        this.bus_seat_availability = bus_seat_availability;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public void setDepartureTime(Date departureTime) {
        this.departureTime = departureTime;
    }

    public void setDeparturePlace(String departurePlace) {
        this.departurePlace = departurePlace;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setAvailableSeat(int availableSeat) {
        this.availableSeat = availableSeat;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }

    public void setBus_seat_availability(String bus_seat_availability) {
        this.bus_seat_availability = bus_seat_availability;
    }

    public String getId() {
        return id;
    }

    public Date getDepartureDate() {
        return departureDate;
    }

    public Date getDepartureTime() {
        return departureTime;
    }

    public String getDeparturePlace() {
        return departurePlace;
    }

    public String getDestination() {
        return destination;
    }

    public double getPrice() {
        return price;
    }

    public int getAvailableSeat() {
        return availableSeat;
    }

    public Bus getBus() {
        return bus;
    }
    
    public String getBus_seat_availability() {
        return bus_seat_availability;
    }
    
}
