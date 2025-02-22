package team9.fft.view.builders;

import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import team9.fft.pojo.Buyer;

public class BuyerCreationView  implements Builder<Region> {
    private ObservableList<String> buyers;
    private StringProperty name;
    private StringProperty initials;
    private final Runnable NEXT_SCENE;
    private final Runnable SUBMIT_BUYER;


    public BuyerCreationView(ObservableList<String> buyers,StringProperty in, StringProperty in2, Runnable click, Runnable handleSubmit) {
        this.buyers = buyers;
        this.name = in;
        this.initials = in2;
        this.NEXT_SCENE = click;
        this.SUBMIT_BUYER = handleSubmit;
    }

    @Override
    public Region build() {
        HBox results = new HBox(); // 20 is the spacing between elements
        results.setId("buyerCreationView");
        results.setAlignment(Pos.CENTER); // Center content horizontally

        results.getChildren().addAll(addBuyer(), rightSection());

        return results;
    }

    private Node addBuyer(){
        VBox div = new VBox();
        div.setId("addBuyerDiv");

        Label header = new Label("Add Buyer");
        header.setId("h1AddBuyer");
        div.getChildren().addAll(header,
                inputForm("Full name:", name),
                inputForm("Initials:", initials),
                submitBtn());

        return div;
    }

    private Node inputForm(String labelText, StringProperty input){
        HBox div = new HBox();
        div.setId("inputFormDiv");

        Label context = new Label(labelText);
        TextField inputText = new TextField();
        inputText.setId("inputBuyerText");
        inputText.setEditable(true);
        inputText.textProperty().bindBidirectional(input);

        div.getChildren().addAll(context, inputText);

        return div;
    }



    private Node submitBtn(){
        HBox div = new HBox();
        div.setId("submitBtnDiv");

        Label context = new Label("Submit");

        div.setOnMouseClicked(event -> SUBMIT_BUYER.run());
        div.getChildren().add(context);

        return div;
    }



    private Node rightSection(){
        VBox div = new VBox();
        div.setId("listBuyersDiv");
        div.getChildren().addAll(buyersList(), nextScene());

        return div;
    }

    //Implement the buyer list here
    private Node buyersList(){
        VBox div = new VBox();
        div.setId("currentBuyersDiv");
        /*for (Buyer buyer : buyers){
            Label buyer_label =
            System.out.println(buyer.getName());
        }*/
        ListView<String> list = new ListView<>();
        list.setItems(buyers);
        div.getChildren().add(list);

        return div;
    }

    private Node nextScene(){
        Button nextBtn = new Button("Next");
        nextBtn.setId("buyerNextBtn");
        nextBtn.setOnMouseClicked(event -> NEXT_SCENE.run());
        return nextBtn;
    }

}

