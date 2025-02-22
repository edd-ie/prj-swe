package team9.fft.view.builders;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.function.Consumer;

public class BankStatementSelectView implements javafx.util.Builder<Region> {
    private ListView<String> bankStatementListView;
    private final Runnable CLICK;
    private final Consumer<String> onFileSelected; // Add a callback

    public BankStatementSelectView(Consumer<String> onFileSelected, Runnable click) { // Add onFileSelected
        // Initialize runnable and stage instance
        this.CLICK = click;
        this.onFileSelected = onFileSelected;
    }

    @Override
    public Region build() {
        // Create the BorderPane layout
        BorderPane borderPane = new BorderPane();

        // Create the ListView to display files
        bankStatementListView = new ListView<>();
        bankStatementListView.setId("bankStatementListView");

        refreshFileList(); // Call the refresh method initially
        loadBankStatements();

        // Set the ListView on the right side of the BorderPane
        borderPane.setCenter(bankStatementListView);

        return borderPane;
    }

    public void loadBankStatements() {
        // Path to the BankStatements directory
        File directory = new File("src/main/resources/BankStatements");

        // If the directory exists, list all .xls and .xlsx files
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".xls") || name.endsWith(".xlsx"));

            // If files are found, populate the ListView
            if (files != null && files.length > 0) {
                ObservableList<String> fileNames = FXCollections.observableArrayList();
                Arrays.stream(files).map(File::getName).forEach(fileNames::add);
                bankStatementListView.setItems(fileNames);

                // Handle file selection
                bankStatementListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        String val = removeExtension(newValue);
                        onFileSelected.accept(val);
                    }
                });
            } else {
                // If no files are found, show an alert
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("No Bank Statements");
                alert.setHeaderText(null);
                alert.setContentText("No bank statements found. Please upload a bank statement.");
                alert.showAndWait();
            }
        } else {
            // If the directory doesn't exist, show an alert
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Directory Not Found");
            alert.setHeaderText(null);
            alert.setContentText("BankStatements directory not found. Please upload a bank statement.");
            alert.showAndWait();
        }
    }

    public String removeExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return ""; // Or handle null/empty as needed
        }

        Path path = Paths.get(fileName); // Use Path for better handling of separators


        String fileNameWithoutExtension = path.getFileName().toString(); // Use toString to get string
        int lastDot = fileNameWithoutExtension.lastIndexOf('.');
        if (lastDot > 0) {
            fileNameWithoutExtension = fileNameWithoutExtension.substring(0, lastDot);
        }

        return fileNameWithoutExtension;
    }

    public void refreshFileList() {
        File directory = new File("src/main/resources/BankStatements");
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".xls") || name.endsWith(".xlsx"));
            if (files != null) {
                ObservableList<String> fileNames = FXCollections.observableArrayList();
                Arrays.stream(files).map(File::getName).forEach(fileNames::add);
                bankStatementListView.setItems(fileNames);
            }
            // Handle Alerts:  You might want to add alerts here if you still want to tell the user that no files have been found
        }
    }
}
