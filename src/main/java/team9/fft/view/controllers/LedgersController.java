/*
 * @author rostys-love
 */

package team9.fft.view.controllers;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Builder;
import team9.fft.model.BuyerModel;
import team9.fft.view.builders.BankStatementSelectView;
import team9.fft.view.builders.LedgersView;

import java.util.function.Consumer;

public class LedgersController {
    private final Stage primaryStage;
    public Builder<Region> builder;
    private final Consumer<String> onFileSelected;

    public LedgersController(Stage primaryStage, Consumer<String> fileNameSetter) {
        this.primaryStage = primaryStage;
        this.builder = new LedgersView(this::handleFileSelection, this::handleClick);

        onFileSelected = fileNameSetter;
    }

    public LedgersView getUI() {
        return (LedgersView) builder;
    }

    public Region getView() {
        return builder.build();
    }

    private void handleClick() {
        System.out.println("Ledger selected!");
    }

    private void handleFileSelection(String fileName) {
        onFileSelected.accept(fileName); // Use the callback to set filename in Main
    }
}
