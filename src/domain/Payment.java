
package domain;

import java.io.Serializable;
import java.util.Date;

public class Payment implements Serializable{
    private String id;
    private Date payment_date;
    private String payment_type;
    private double total_amount;
    private double payment_amount;
    private double balance;
    private String card_no;
    private String card_holder;
    private String expiry_date;
    private String card_type;
    private String bank_name;
    private Transaction transaction;
    
   public Payment(){
       
   }
   
   public Payment(String id){
       this.id = id;
    }

    public Payment(String id, Date payment_date, String payment_type, double total_amount, double payment_amount,double balance ,String card_no, String card_holder, String expiry_date, String card_type, String bank_name, Transaction transaction) {
        this.id = id;
        this.payment_date = payment_date;
        this.payment_type = payment_type;
        this.total_amount = total_amount;
        this.payment_amount = payment_amount;
        this.balance = balance;
        this.card_no = card_no;
        this.card_holder = card_holder;
        this.expiry_date = expiry_date;
        this.card_type = card_type;
        this.bank_name = bank_name;
        this.transaction = transaction;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPayment_date(Date payment_date) {
        this.payment_date = payment_date;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public void setPayment_amount(double payment_amount) {
        this.payment_amount = payment_amount;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void setCard_no(String card_no) {
        this.card_no = card_no;
    }

    public void setCard_holder(String card_holder) {
        this.card_holder = card_holder;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }

    public void setCard_type(String card_type) {
        this.card_type = card_type;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getId() {
        return id;
    }

    public Date getPayment_date() {
        return payment_date;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public double getPayment_amount() {
        return payment_amount;
    }

    public double getBalance() {
        return balance;
    }
    
    public String getCard_no() {
        return card_no;
    }

    public String getCard_holder() {
        return card_holder;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public String getCard_type() {
        return card_type;
    }

    public String getBank_name() {
        return bank_name;
    }

    public Transaction getTransaction() {
        return transaction;
    }
   
   
    
    
}
