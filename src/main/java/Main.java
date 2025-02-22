import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import team9.fft.model.BuyerModel;
import team9.fft.view.controllers.*;


import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author edd-ie
 */

public class Main extends Application {
    public static int mainWidth = 1200;
    public static int mainHeight = 720;
    public String fileName;
    Consumer<String> fileNameSetter; // consumer to set fileName
    public BuyerModel buyerModel;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        fileNameSetter = fileName -> {
            this.fileName = fileName;
            this.buyerModel = new BuyerModel(fileName);

            primaryStage.setScene(buyerCreation(primaryStage, allTransactions(primaryStage)));
        };

        primaryStage.setTitle("Family Finance¡ Manager");

        Scene mainScene = mainScene(primaryStage, fileNameSetter);

        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


    public Scene mainScene(Stage primaryStage, Consumer<String> fileNameSetter) {
        //Main layout properties
        GridPane homePage = new GridPane(20,20); // Horizontal and vertical gaps of 20 pixels
        homePage.setId("homePage");

        //Insertion of components
        BankStatementSelectController selectController = new BankStatementSelectController(primaryStage, fileNameSetter);
        BankStatementUploadController uploadController = new BankStatementUploadController(selectController.getUI());




        homePage.add(new BankStatementHeaderController().getView(), 0, 0);
        homePage.add(uploadController.getView(), 0, 1);
        homePage.add(selectController.getView(), 1, 1);

        LedgersController ledgersController = new LedgersController(primaryStage, fileNameSetter);
        homePage.add(ledgersController.getView(), 2, 1);
        Scene scene = new Scene(homePage, mainWidth, mainHeight);
        String mainCss = Objects.requireNonNull(this.getClass().getResource("stylesheet/main.css")).toExternalForm();
        scene.getStylesheets().add(mainCss);

        return scene;
    }

    private Scene buyerCreation(Stage primaryStage, Scene next) {
        VBox buyerCreation = new VBox();
        buyerCreation.setId("buyerCreation");
        buyerCreation.setAlignment(Pos.CENTER); // Center content horizontally
        buyerCreation.getChildren().addAll(new BuyerCreationController(this.buyerModel, primaryStage, next).getView());

        Scene scene = new Scene(buyerCreation, mainWidth, mainHeight);
        String buyerCSs = Objects.requireNonNull(this.getClass().getResource("stylesheet/buyerCreation.css")).toExternalForm();
        scene.getStylesheets().add(buyerCSs);

        return scene;
    }

    private Scene allTransactions(Stage primaryStage) {
        VBox allTransactions = new VBox(20);
        allTransactions.setId("allTransactions");
        allTransactions.getChildren().addAll(new MasterTransactionController(this.buyerModel).getView());

        Scene scene = new Scene(allTransactions, mainWidth, mainHeight);
        String allTransactionsCSS = Objects.requireNonNull(this.getClass().getResource("stylesheet/allTransactions.css")).toExternalForm();
        scene.getStylesheets().add(allTransactionsCSS);

        return scene;
    }
}