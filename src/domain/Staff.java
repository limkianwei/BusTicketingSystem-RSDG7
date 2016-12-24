
package domain;

import java.io.Serializable;
import java.util.Date;

public class Staff implements Serializable{
    private String id;
    private String name;
    private String ic;
    private String email;
    private Date dob;
    private String contactNo;
    
    public Staff(){
        
    }
    
    public Staff(String id){
        this.id=id;
    }
    
    public Staff(String id, String name, String ic, String email, Date dob, String contactNo){
        this.id=id;
        this.name=name;
        this.ic=ic;
        this.email=email;
        this.dob=dob;
        this.contactNo=contactNo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIc() {
        return ic;
    }

    public String getEmail() {
        return email;
    }

    public Date getDob() {
        return dob;
    }

    public String getContactNo() {
        return contactNo;
    }
    
    
    
}
