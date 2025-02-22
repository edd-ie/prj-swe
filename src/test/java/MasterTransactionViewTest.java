import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import javafx.scene.control.CheckBox;
import team9.fft.pojo.Transaction;
import team9.fft.view.builders.MasterTransactionView;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class MasterTransactionViewTest {

    @Test
    public void testAssignBuyerToSelectedTransactions() {
        ObservableList<String> buyers = FXCollections.observableArrayList("Test Buyer", "Other Buyer");
        MasterTransactionView view = new MasterTransactionView(buyers, "src/main/resources/BankStatements/Example1.xlsx");

        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction1 = new Transaction("01/15/2024", "Transaction 1", "Type1", 100.0);
        Transaction transaction2 = new Transaction("01/20/2024", "Transaction 2", "Type2", 200.0);
        transactions.add(transaction1);
        transactions.add(transaction2);

        List<CheckBox> checkBoxes = Arrays.asList(new CheckBox(), new CheckBox());
        checkBoxes.get(0).setSelected(true);
        checkBoxes.get(1).setSelected(false);

        String buyerName = "Test Buyer";
        view.assignBuyerToSelectedTransactions(transactions, checkBoxes, buyerName);

        assertNotNull(transactions.get(0).getAssignedBuyer(), "Buyer should be assigned to the selected transaction.");
        assertEquals(buyerName, transactions.get(0).getAssignedBuyer().getName(), "Assigned buyer name should match.");
        assertNull(transactions.get(1).getAssignedBuyer(), "Buyer should not be assigned to unselected transactions.");
    }

    @Test
    public void testFilePath() {
        String expectedFilePath = "src/main/resources/BankStatements/Example1.xlsx";
        MasterTransactionView view = new MasterTransactionView(null, expectedFilePath);

        assertEquals(expectedFilePath, view.filePath, "File path should match the initialized value.");
    }

    @Test
    public void testLoadCategories() throws IOException {
        Path categoriesFile = Paths.get("src/main/resources/Categories/Categories.txt");

        Files.createDirectories(categoriesFile.getParent());
        Files.write(categoriesFile, String.join("\n", "Category 1", "Category 2", "Category 3").getBytes());

        MasterTransactionView view = new MasterTransactionView(null, "dummyFilePath");
        String[] categories = view.loadCategories();

        assertArrayEquals(new String[]{"Category 1", "Category 2", "Category 3"}, categories, "Categories should match.");
        Files.deleteIfExists(categoriesFile);
    }


    @Test
    public void testLoadCategories_FileReadError() throws IOException {
        Path categoriesFile = Paths.get("src/main/resources/Categories/Categories.txt");

        Files.createDirectories(categoriesFile.getParent());

        Files.createFile(categoriesFile);

        File file = categoriesFile.toFile();
        ((java.io.File) file).setReadable(false);

        MasterTransactionView view = new MasterTransactionView(null, "dummyFilePath");
        String[] categories = view.loadCategories();

        assertArrayEquals(new String[]{}, categories, "Categories should be empty if there is a file read error.");

        file.setReadable(true);
        Files.deleteIfExists(categoriesFile);
    }
}
