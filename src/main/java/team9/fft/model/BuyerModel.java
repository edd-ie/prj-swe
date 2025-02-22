package team9.fft.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import team9.fft.datastore.AccessBuyers;
import team9.fft.pojo.Buyer;
import team9.fft.pojo.Transaction;
import java.util.ArrayList;

/*
* @author edd-ie, KhussThakrani
*/

public class BuyerModel {
    private final AccessBuyers BUYER_DATABASE;


    public BuyerModel(String fileName) {
        this.BUYER_DATABASE = new AccessBuyers(fileName);
    }

    public void readBuyers() {
        BUYER_DATABASE.readBuyers();
    }

    public void writeBuyers() {
        BUYER_DATABASE.writeBuyers();
    }

    public void addBuyer(String name, String initials) {
        Buyer buyer = new Buyer(name, initials);
        BUYER_DATABASE.addBuyer(buyer);
    }

    public void addBuyer(Buyer buyer) {
        BUYER_DATABASE.addBuyer(buyer);
    }


    public void assignTransaction(Buyer buyer, Transaction transaction){
        buyer.addTransaction(transaction);
        BUYER_DATABASE.saveBuyerTransactions(buyer);
    }

    public void assignTransactions(Buyer buyer, ArrayList<Transaction> transactions){
        for (Transaction transaction :  transactions){
            buyer.addTransaction(transaction);
        }
        BUYER_DATABASE.saveBuyerTransactions(buyer);
    }


    public ArrayList<Transaction> getBuyerTransactions(Buyer buyer){
        return BUYER_DATABASE.getBuyerTransactions(buyer);
    }


    public void assignCategory(Buyer buyer, String category){
        buyer.addCategory(category);
        BUYER_DATABASE.saveBuyerTransactions(buyer);
    }


    public void removeBuyer(Buyer buyer) {
        BUYER_DATABASE.removeBuyer(buyer);
    }

    public ArrayList<Buyer> getBuyers() {

        return BUYER_DATABASE.getBuyers();
    }

    public Buyer findBuyerByInitials(String initials) {
        if (initials != null && !initials.isEmpty()) {
            for (Buyer buyer : BUYER_DATABASE.getBuyers()) {
                if (buyer.getInitials().equalsIgnoreCase(initials)) {
                    return buyer;
                }
            }
        }
        return null;
    }

    public void updateNameOfBuyer(Buyer buyer, String newName) {
        if (buyer != null && newName != null && !newName.isEmpty()) {
            buyer.setName(newName);
        }
    }


    public void removeCategoryFromBuyer(Buyer buyer, String removedCategory) {
        if (buyer != null && removedCategory != null) {
            buyer.removeCategory(removedCategory);
            BUYER_DATABASE.saveBuyerTransactions(buyer);
        }
    }

    public void removeTransactionFromBuyer(Buyer buyer, Transaction removeTransaction) {
        if (buyer != null && removeTransaction != null) {
            buyer.removeTransaction(removeTransaction);
            BUYER_DATABASE.saveBuyerTransactions(buyer);
        }
    }



}
