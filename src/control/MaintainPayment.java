
package control;

import da.PaymentDA;
import domain.Payment;
import java.util.ArrayList;

public class MaintainPayment {
    private PaymentDA paymentDA;

    public MaintainPayment() {
        paymentDA = new PaymentDA();
    }
    
    public Payment getRecord(String id){
        return paymentDA.getRecord(id);
    }
    
    public void addRecord(Payment payment){
        paymentDA.addRecord(payment);
    }
    
//    public void updateRecord(Payment payment){
//        paymentDA.updateRecord(payment);
//    }
//    
//    public void deleteRecord(Payment payment){
//        paymentDA.deleteRecord(payment);
//    }
    
    public Object[][] getAllRecord(){
        return paymentDA.getAllRecord();
    }
    
    public String generatePaymentID() {
        return paymentDA.generatePaymentID();
    }
    
    public void disconnectDatabase(){
        paymentDA.shutDown();
    }
}
