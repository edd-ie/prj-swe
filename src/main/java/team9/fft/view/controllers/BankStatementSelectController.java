package team9.fft.view.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;
import team9.fft.model.BuyerModel;
import team9.fft.view.builders.BankStatementSelectView;

import java.util.function.Consumer;

public class BankStatementSelectController {
    private final Stage primaryStage;
    public Builder<Region> builder;
    private final Consumer<String> onFileSelected;

    public BankStatementSelectController(Stage primaryStage, Consumer<String> fileNameSetter) {
        this.primaryStage = primaryStage; //Assign stage instance
        // Instantiate the view and pass a click handler (which currently prints to the console)
        this.builder = new BankStatementSelectView(this::handleFileSelection, this::handleClick);

        onFileSelected = fileNameSetter;
    }


    public BankStatementSelectView getUI(){
        return (BankStatementSelectView)builder;
    }

    public Region getView() {
        return builder.build();
    }

    // Placeholder for any actions triggered by the user interaction (e.g., clicking a bank statement).
    private void handleClick() {
        System.out.println("Bank Statement selected!");
    }

    private void handleFileSelection(String fileName) {
        onFileSelected.accept(fileName); // Use the callback to set filename in Main
//        primaryStage.setScene(NEXT); //  Pass the scene directly instead of creating it here.
    }

}
