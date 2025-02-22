/**
 * @author rostys-love
 */

package team9.fft.pojo;

public class Transaction {
    private String date;
    private String description;
    private double amount;
    private String type;
    private String category;
    private Buyer assignedBuyer;

    public Transaction(String date, String description, String type, double amount){
        //System.out.println("I am constructor: The date is : " + date);
        this.date = date;
        this.description = description;
        this.type = type;
        this.amount = amount;
        this.category = "";
    }

    public Transaction(String date, String description, String type, double amount, String category){
        this.date = date;
        this.description = description;
        this.type = type;
        this.amount = amount;
        this.category = category;
    }

    public String getDate(){
        return date;
    }

    public String getDescription(){
        return description;
    }

    public String getType(){
        return type;
    }

    public double getAmount(){
        return amount;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public Buyer getAssignedBuyer(){
        return assignedBuyer;
    }

    public void setAssignedBuyer(Buyer buyer){
         this.assignedBuyer = buyer;
    }

    @Override
    public String toString(){
        return date + " " +  description + " " + type + " " + amount+" "+category;
    }


}