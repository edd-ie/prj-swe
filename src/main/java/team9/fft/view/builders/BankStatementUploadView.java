package team9.fft.view.builders;

import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.DragEvent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;

public class BankStatementUploadView implements Builder<Region> {

    private final EventHandler<DragEvent> DRAG_OVER;
    private final EventHandler<DragEvent> DRAG_DROP;
    private final Runnable CLICK;

    public BankStatementUploadView(EventHandler<DragEvent> dragOver, EventHandler<DragEvent> dragDrop, Runnable click) {
        this.DRAG_OVER = dragOver;
        this.DRAG_DROP = dragDrop;
        this.CLICK = click;
    }

    @Override
    public Region build() {
        VBox results = new VBox(20); // 20 is the spacing between elements
        results.setId("bankStatementView");
        results.setAlignment(Pos.CENTER); // Center content horizontally
        results.setPadding(new Insets(20)); // Add padding

        results.getChildren().addAll(uploadIcon(), label(), subLabel());

        //actions
        results.setOnMouseClicked(_ ->CLICK.run());
        results.setOnDragOver(DRAG_OVER);
        results.setOnDragDropped(DRAG_DROP);

        return results;
    }

    private Node uploadIcon() {
        ImageView icon = new ImageView("images/upload icon.png");
        icon.setId("uploadIcon");

        return icon;
    }

    private Node label(){
        Label text = new Label("Drag and Drop or Click here\nto upload file");
        text.setId("uploadText");
        return text;
    }

    private Node subLabel(){
        Label text = new Label("Upload Excel file from computer");
        text.setId("subUploadText");
        return text;
    }
}
