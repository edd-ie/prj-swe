/*
 * @author rostys-love
 */

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

public class LedgersView implements javafx.util.Builder<Region> {
    private ListView<String> ledgerStatementListView;
    private final Runnable CLICK;
    private final Consumer<String> onFileSelected;

    public LedgersView(Consumer<String> onFileSelected, Runnable click) {
        this.CLICK = click;
        this.onFileSelected = onFileSelected;
    }

    @Override
    public Region build() {
        BorderPane borderPane = new BorderPane();

        ledgerStatementListView = new ListView<>();
        ledgerStatementListView.setId("ledgerListView");

        loadLedgers();

        borderPane.setCenter(ledgerStatementListView);

        return borderPane;
    }


    public void loadLedgers() {
        File directory = new File("src/main/resources/Ledgers");

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".xls") || name.endsWith(".xlsx"));

            if (files != null && files.length > 0) {
                ObservableList<String> fileNames = FXCollections.observableArrayList();
                Arrays.stream(files).map(File::getName).forEach(fileNames::add);
                ledgerStatementListView.setItems(fileNames);

                ledgerStatementListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        String val = removeExtension(newValue);
                        onFileSelected.accept(val);
                    }
                });
            } else {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("No Ledgers");
                alert.setHeaderText(null);
                alert.setContentText("No ledgers found. Please upload a ledger statement.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Directory Not Found");
            alert.setHeaderText(null);
            alert.setContentText("Ledgers directory not found. Please upload a ledger statement.");
            alert.showAndWait();
        }
    }

    public String removeExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }

        Path path = Paths.get(fileName);


        String fileNameWithoutExtension = path.getFileName().toString();
        int lastDot = fileNameWithoutExtension.lastIndexOf('.');
        if (lastDot > 0) {
            fileNameWithoutExtension = fileNameWithoutExtension.substring(0, lastDot);
        }

        return fileNameWithoutExtension;
    }

    public void refreshFileList() {
        File directory = new File("src/main/resources/Ledgers");
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".xls") || name.endsWith(".xlsx"));
            if (files != null) {
                ObservableList<String> fileNames = FXCollections.observableArrayList();
                Arrays.stream(files).map(File::getName).forEach(fileNames::add);
                ledgerStatementListView.setItems(fileNames);
            }
        }
    }
}

