/**
 * @author rostys-love
 */

package team9.fft.pojo;

public class PreauthorizedTransaction extends Transaction {

    public PreauthorizedTransaction(String date, String description, String type, double amount) {
        super(date, description, type, amount);
    }

    public boolean isPreauthorized() {
        return getDescription().toLowerCase().contains("pap");
    }

    public String toString() {
        return "Preauthorized: " + super.toString();
    }
}
