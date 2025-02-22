package team9.fft.pojo;

import javafx.collections.ObservableList;

import java.util.ArrayList;

/**
 * @authors _edd.ie_, KhussThakrani
 */

public class Buyer {
    private String name;
    private String initials;
    private final ArrayList<Transaction> TRANSACTIONS;
    private final ArrayList<String> CATEGORIES;
    private ObservableList<Buyer> buyerList;

    public Buyer(String name, String initials) {
        this.name = name;
        this.initials = initials;
        this.TRANSACTIONS = new ArrayList<>();
        this.CATEGORIES = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String setName(String newName){return this.name = newName; }

    public String getInitials() {
        return initials;
    }

    public ArrayList<Transaction> getTransactions() {
        return TRANSACTIONS;
    }

    public ArrayList<String> getCATEGORIES() {
        return CATEGORIES;
    }

    public void addTransaction(Transaction transaction) {
        TRANSACTIONS.add(transaction);
    }

    private Buyer findExistingBuyer(String buyerName) {
        Buyer existingBuyer = null;

        for (Buyer buyer : buyerList) {
            if (buyer.getName().equalsIgnoreCase(buyerName)) {
                existingBuyer = buyer;
                break;
            }
        }

        if (existingBuyer == null) {
            existingBuyer = new Buyer(buyerName, "INIT");
            buyerList.add(existingBuyer);
        }

        return existingBuyer;
    }





    public void addCategory(String category) {
        CATEGORIES.add(category);
    }

    public void removeTransaction(Transaction transaction) {
        TRANSACTIONS.remove(transaction);
    }

    public void removeCategory(String category) {
        CATEGORIES.remove(category);
    }

    public String toString() {
        return name + "(" + initials + "):\n\tNumber of transactions: " + TRANSACTIONS.size() + "\n\tNumber of categories: " + CATEGORIES.size() + "\n";
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.TRANSACTIONS.clear();
        this.TRANSACTIONS.addAll(transactions);
    }
}
