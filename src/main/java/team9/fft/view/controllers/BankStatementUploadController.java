package team9.fft.view.controllers;

import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.util.Builder;
import team9.fft.view.builders.BankStatementSelectView;
import team9.fft.view.builders.BankStatementUploadView;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankStatementUploadController {
    public Builder<Region> builder;
    private BankStatementSelectView bankStatementSelectView;
    private String selectedFilePath;
    private static final Logger LOGGER = Logger.getLogger(BankStatementUploadController.class.getName());

    public BankStatementUploadController(BankStatementSelectView bankStatementSelectView){
        this.builder = new BankStatementUploadView(this::handleDragOver,this::handleDragDrop, this::handleClick);
        this.bankStatementSelectView = bankStatementSelectView;
    }

    public Region getView(){
        return builder.build();
    }

    public String getSelectedFilePath() {
        return selectedFilePath;
    }

    private void fileCopy(String fileName, String filePath){
        String storage = "src/main/resources/BankStatements/";
        if(Files.exists(Paths.get(storage+fileName))){
            LocalDate today = LocalDate.now();
            LOGGER.log(Level.INFO, "File already exists: "+storage+fileName);
            fileName = today.getDayOfMonth()+"-"+today.getMonthValue()+"-"+today.getYear()+"-"+fileName;
        }

        try {
            // OPTION 2: Use REPLACE_EXISTING to overwrite if file exists.
            Files.copy(Paths.get(filePath), Paths.get(storage + fileName), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File copied successfully to: " + storage + fileName);
            bankStatementSelectView.refreshFileList();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error copying file: " + filePath, e);
        }
    }

    private void handleClick(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Bank Statement File");

        // Set extension filters correctly
        FileChooser.ExtensionFilter xlsFilter = new FileChooser.ExtensionFilter("Excel Files (.xls)", "*.xls");
        FileChooser.ExtensionFilter xlsxFilter = new FileChooser.ExtensionFilter("Excel Files (.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().addAll(xlsxFilter, xlsFilter);

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            fileCopy(selectedFile.getName(), selectedFile.getAbsolutePath());
        }
    }

    private void handleDragOver(DragEvent event) {
        Dragboard db = event.getDragboard();
        if (db.hasFiles()) {
            boolean hasExcelFiles = db.getFiles().stream()
                    .anyMatch(file -> file.getName().toLowerCase().endsWith(".xls") || file.getName().toLowerCase().endsWith(".xlsx"));

            if (hasExcelFiles) {
                event.acceptTransferModes(TransferMode.COPY);
            }
        } else {
            event.consume();
        }
        event.consume();

    }

    private void handleDragDrop(DragEvent event){
        Dragboard db = event.getDragboard();
        String filePath, fileName;
        if(db.hasFiles()){
            List<File> files = db.getFiles();
            for(File file : files){
                boolean isExcelFile = file.getName().toLowerCase().endsWith(".xls") || file.getName().toLowerCase().endsWith(".xlsx");
                if(isExcelFile){
                    fileName = file.getName();
                    filePath = file.getAbsolutePath();
                    fileCopy(fileName, filePath);
                }
            }
        }
    }
}
