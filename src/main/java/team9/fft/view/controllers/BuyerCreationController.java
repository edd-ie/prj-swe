package team9.fft.view.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Builder;
import team9.fft.model.BuyerModel;
import team9.fft.pojo.Buyer;
import team9.fft.view.builders.BuyerCreationView;

import java.io.File;
import java.util.logging.Logger;

/**
 * @author edd-ie
 */

public class BuyerCreationController {
    public Builder<Region> builder;
    private static final Logger LOGGER = Logger.getLogger(BuyerCreationController.class.getName());
    private final BuyerModel buyerModel;
    private final Stage primaryStage;
    private final Scene nxt;
    private ObservableList<String> buyers;
    private StringProperty inputName;
    private StringProperty inputInitials;



    public BuyerCreationController(BuyerModel model, Stage primaryStage, Scene next) {
        this.buyerModel = model;
        buyerSetup();

        inputName = new SimpleStringProperty();
        inputInitials = new SimpleStringProperty();
        this.builder = new BuyerCreationView(buyers, inputName, inputInitials, this::handleClick, this::handleSubmit);
        this.primaryStage = primaryStage;
        this.nxt = next;
    }

    public void buyerSetup(){
        buyers = FXCollections.observableArrayList();
        for (Buyer x : buyerModel.getBuyers()){
            buyers.add(x.getName());
        }
    }

    private void handleClick(){
        primaryStage.setScene(nxt);
    }

    private void handleSubmit(){
        System.out.println(inputName.getValue()+"\n"+inputInitials.getValue());
        buyerModel.addBuyer(inputName.getValue(), inputInitials.getValue());
        buyers.add(inputName.getValue());
    }


    public Region getView(){
        return builder.build();
    }
}
