/*
 * @author KhussThakrani, edd-ie
 */

package team9.fft.view.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.util.Builder;
import team9.fft.model.BuyerModel;
import team9.fft.pojo.Buyer;
import team9.fft.view.builders.MasterTransactionView;
import java.util.logging.Logger;

public class MasterTransactionController {
    public Builder<Region> builder;
    private static final Logger LOGGER = Logger.getLogger(MasterTransactionController.class.getName());
    private ObservableList<String> buyers;
    private final BuyerModel buyerModel;
    private String filePath;

    public MasterTransactionController(BuyerModel model) {
        this.buyerModel = model;
        buyerSetup();

        this.builder = new MasterTransactionView(buyers,filePath);
    }

    public void buyerSetup(){
        buyers = FXCollections.observableArrayList();
        for (Buyer x : buyerModel.getBuyers()){
            buyers.add(x.getName());
        }
    }

    public Region getView(){
        return builder.build();
    }

}