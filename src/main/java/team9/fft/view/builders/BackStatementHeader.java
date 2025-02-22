package team9.fft.view.builders;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class BackStatementHeader implements Builder<Region> {

    public BackStatementHeader(){
    }

    @Override
    public Region build() {
        VBox results = new VBox(10); // 10 is the spacing between elements
        results.setId("bankStatementHeader");
        results.setAlignment(Pos.CENTER); // Center content horizontally
        results.getChildren().addAll(label(), subLabel());
        return results;
    }


    private Node label(){
        Label text = new Label("Welcome to Family Finance Manager");
        text.setId("header");
        return text;
    }

    private Node subLabel(){
        Label text = new Label("Please select or upload a bank statement to begin");
        text.setId("subHeader");
        return text;
    }
}