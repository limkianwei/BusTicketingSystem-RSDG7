
package control;

import da.TransactionDA;
import domain.Transaction;
import java.util.ArrayList;

public class MaintainTransaction {
    private TransactionDA transactionDA;

    public MaintainTransaction() {
        transactionDA = new TransactionDA();
    }
    
    public Transaction getRecord(String id){
        return transactionDA.getRecord(id);
    }
    
    public void addRecord(Transaction transaction){
        transactionDA.addRecord(transaction);
    }
    
    public void updateRecord(Transaction transaction){
        transactionDA.updateRecord(transaction);
    }
    
    public void deleteRecord(Transaction transaction){
        transactionDA.deleteRecord(transaction);
    }
    
    public Object[][] getAllRecord(){
        return transactionDA.getAllRecord();
    }
    
    public ArrayList<Transaction> getTransaction(){
        return transactionDA.getTransaction();
    }
    
    public String generateTransactionID() {
        return transactionDA.generateTransactionID();
    }
    
    public void disconnectDatabase(){
        transactionDA.shutDown();
    }
    
}
