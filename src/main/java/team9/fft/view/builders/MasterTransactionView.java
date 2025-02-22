/*
 * @author rostys-love, KhussThakrani, edd-ie, ChatGPT-4o
 */

package team9.fft.view.builders;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Builder;
import javafx.util.Callback;
import javafx.util.Pair;
import team9.fft.pojo.Buyer;
import team9.fft.pojo.Transaction;
import team9.fft.pojo.TransactionRepo;
import javafx.scene.layout.HBox;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

public class MasterTransactionView implements Builder<Region> {
    private ObservableList<String> buyers;
    private Callback<Pair<String, String>, Void> onAssignBuyer;
    private VBox rootContainer;
    private Runnable NEXT_SCENE;
    private String selectedBuyerInitials;
    private String[] categories;
    private String category;
    public String filePath;
    //HAVE TO CHANGE FILENAME DEPENDS ON BANK STATEMENT SELECTED
    String FILENAME = "Example1";

    public MasterTransactionView(ObservableList<String> buyers, String filePath) {
        this.buyers = buyers;
        this.filePath = filePath;
    }

    public MasterTransactionView(ObservableList<String> buyers, Callback<Pair<String, String>, Void> onAssignBuyer, Runnable click) {
        this.buyers = buyers;
        this.onAssignBuyer = onAssignBuyer;
        this.NEXT_SCENE = click;
        this.filePath = filePath;
    }

    @Override
    public Region build() {
        if (rootContainer == null) { // Initialize only once
            rootContainer = new VBox(20);
            rootContainer.setId("allTransaction");
            rootContainer.setAlignment(Pos.CENTER);
            rootContainer.setPadding(new Insets(20));
            rootContainer.getChildren().addAll(transactionsList());

        }
        return rootContainer;
    }

    private Node transactionsList() {
        TransactionRepo repo = new TransactionRepo();
        String filePath = "src/main/resources/BankStatements/" + FILENAME + ".xlsx";
        repo.loadTransactionsFromExcel(filePath);

        List<Transaction> transactions = repo.getTransactions();
        List<CheckBox> checkBoxes = new ArrayList<>();

        categories = loadCategories();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(10);
        gridPane.addRow(0, new Label("Select"), new Label("Date"), new Label("Description"),
                new Label("Type"), new Label("Amount"), new Label("Assigned Buyer"), new Label("Categories"));

        int row = 1;
        for (Transaction transaction : transactions) {
            CheckBox checkBox = new CheckBox();
            checkBoxes.add(checkBox);

            TextField dateField = createNonEditableTextField(transaction.getDate());
            TextField descriptionField = createNonEditableTextField(transaction.getDescription());
            TextField typeField = createNonEditableTextField(transaction.getType());
            TextField amountField = createNonEditableTextField(String.valueOf(transaction.getAmount()));
            TextField buyerField = createNonEditableTextField(transaction.getAssignedBuyer() != null ? transaction.getAssignedBuyer().getName() : "");

            ComboBox<String> categorySelector = new ComboBox<>();
            categorySelector.getItems().addAll(categories);
            categorySelector.setPromptText("Select Category");

            if (transaction.getCategory() != null) {
                categorySelector.setValue(transaction.getCategory());
            }
            categorySelector.setOnAction(event -> {
                transaction.setCategory(categorySelector.getValue());
            });

            gridPane.addRow(row++, checkBox, dateField, descriptionField, typeField, amountField, buyerField, categorySelector);
        }

        ComboBox<String> buyerSelector = new ComboBox<>(buyers);
        buyerSelector.setPromptText("Select Buyer");

        Button assignBuyerButton = new Button("Assign Buyer to Selected Transactions");
        Button confirmButton = new Button("Confirm Buyer and Categories");
        assignBuyerButton.setOnAction(event -> {
            String selectedBuyer = buyerSelector.getValue();

            if (selectedBuyer != null) {
                String[] nameParts = selectedBuyer.split(" ");
                if (nameParts.length >= 2) {
                    selectedBuyerInitials = nameParts[0].substring(0, 1).toLowerCase() + nameParts[1].substring(0, 1).toLowerCase();
                } else {
                    selectedBuyerInitials = nameParts[0].substring(0, 1).toLowerCase();
                }
                assignBuyerToSelectedTransactions(transactions, checkBoxes, selectedBuyer);
                updateTransactionUI(transactions, gridPane);
            } else {
                System.out.println("Please select a buyer before assigning");
            }
        });
        confirmButton.setOnAction(e -> {
            writeTransactionsToFileByMonth(transactions);
            goToNextScene();
        });

        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setPannable(true);

        HBox buttonBox = new HBox(20, buyerSelector, assignBuyerButton, confirmButton);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));

        VBox container = new VBox(10, scrollPane, buttonBox);
        return container;
    }

    private TextField createNonEditableTextField(String text) {
        TextField textField = new TextField(text);
        textField.setEditable(false);
        return textField;
    }

    public void assignBuyerToSelectedTransactions(List<Transaction> transactions, List<CheckBox> checkBoxes, String selectedBuyer) {
        for (int i = 0; i < transactions.size(); i++) {
            if (checkBoxes.get(i).isSelected()) {
                Transaction transaction = transactions.get(i);
                Buyer buyer = new Buyer(selectedBuyer, selectedBuyerInitials.toUpperCase());
                transaction.setAssignedBuyer(buyer);

                if (onAssignBuyer != null) {
                    onAssignBuyer.call(new Pair<>(selectedBuyer, transaction.getDescription()));
                }
            }
        }
    }


    public void updateTransactionUI(List<Transaction> transactions, GridPane gridPane) {
        for (Transaction transaction : transactions) {
            for (Node node : gridPane.getChildren()) {
                if (GridPane.getRowIndex(node) == transactions.indexOf(transaction) + 1) {
                    if (node instanceof TextField) {
                        switch (GridPane.getColumnIndex(node)) {
                            case 5 -> ((TextField) node).setText(
                                    transaction.getAssignedBuyer() != null ? transaction.getAssignedBuyer().getName() : ""
                            );
                            case 6 -> ((TextField) node).setText(
                                    transaction.getCategory() != null ? transaction.getCategory() : ""
                            );
                        }
                    }
                }
            }
        }
    }

    //Writes transaction to file using key and value in hash map
    private void writeTransactionsToFileByMonth(List<Transaction> transactions) {
        Map<String, List<Transaction>> transactionsByMonth = new HashMap<>();

        for (Transaction eachTransaction : transactions) {
            String transactionMonth = identifyStatementMonth(eachTransaction);
            transactionsByMonth.computeIfAbsent(transactionMonth, k -> new ArrayList<>()).add(eachTransaction);
        }

        for (Map.Entry<String, List<Transaction>> entry : transactionsByMonth.entrySet()) {
            String month = entry.getKey();
            List<Transaction> monthTransactions = entry.getValue();

            if (!monthTransactions.isEmpty()) {
                String fileName = "src/main/resources/Transactions/" + FILENAME + "/" + selectedBuyerInitials + "_" + month.toLowerCase() + ".txt";
                File file = new File(fileName);

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    for (Transaction transaction : monthTransactions) {
                        if (transaction.getAssignedBuyer() != null) {
                            writer.write("Transaction: " + transaction.toString() + ", Buyer: " + transaction.getAssignedBuyer().getName());
                            writer.newLine();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String[] loadCategories() {
        List<String> categoryList = new ArrayList<>();
        String filePath = "src/main/resources/Categories/Categories.txt";
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                categoryList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return categoryList.toArray(new String[0]);
    }

    private String identifyStatementMonth(Transaction transaction){
        String dateNumeric = transaction.getDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate date = LocalDate.parse(dateNumeric, formatter);
        String monthName = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        return monthName;
    }


    //Takes user to next scene and gives an option to download and store transaction files locally

    private void goToNextScene() {
        TransactionRepo repo = new TransactionRepo();
        repo.loadTransactionsFromExcel(filePath);
        List<Transaction> transactions = repo.getTransactions();

        List<Transaction> buyerTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getAssignedBuyer() != null &&
                    transaction.getAssignedBuyer().getInitials().equalsIgnoreCase(selectedBuyerInitials)) {
                buyerTransactions.add(transaction);
            }
        }

        Map<String, List<Transaction>> transactionsByMonth = groupTransactionsByMonth(buyerTransactions);
        createMonthlyFiles(transactionsByMonth, selectedBuyerInitials);

        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label titleLabel = new Label("Transactions for Buyer: " + selectedBuyerInitials.toUpperCase());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Accordion accordion = new Accordion();

        for (String month : transactionsByMonth.keySet()) {
            List<Transaction> monthTransactions = transactionsByMonth.get(month);

            VBox monthBox = new VBox(10);
            monthBox.setPadding(new Insets(10));

            for (Transaction transaction : monthTransactions) {
                Label transactionLabel = new Label(
                        "Date: " + transaction.getDate() +
                                ", Description: " + transaction.getDescription() +
                                ", Type: " + transaction.getType() +
                                ", Amount: " + transaction.getAmount() +
                                ", Category: " + (transaction.getCategory() != null ? transaction.getCategory() : "Uncategorized")
                );
                monthBox.getChildren().add(transactionLabel);
            }

            TitledPane monthPane = new TitledPane(month, monthBox);
            accordion.getPanes().add(monthPane);
        }

        Button openDirectoryButton = new Button("Display monthly transaction files ");
        openDirectoryButton.setOnAction(event -> {
            String directoryPath = "src/main/resources/Transactions/" + FILENAME + "/";
            File directory = new File(directoryPath);
            if (directory.exists() && directory.isDirectory()) {
                File[] files = directory.listFiles();

                if (files != null) {
                    for (File file : files) {
                        if (file.isFile() && file.getName().startsWith(selectedBuyerInitials)) {
                            System.out.println("Opening file: " + file.getAbsolutePath());
                            File filePath = new File(file.getPath());
                            if (directory.exists()) {
                                try {
                                    java.awt.Desktop.getDesktop().open(directory);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    Alert alert = new Alert(Alert.AlertType.ERROR, "Unable to open directory.");
                                    alert.show();
                                }
                            } else {
                                Alert alert = new Alert(Alert.AlertType.WARNING, "Transaction directory does not exist.");
                                alert.show();
                            }

                        }
                    }
                }
            } else {
                System.out.println("Directory does not exist or is not a directory.");
            }
        });

        root.getChildren().addAll(titleLabel, accordion, openDirectoryButton);

        Scene nextScene = new Scene(root, 800, 600);

        Stage stage = (Stage) rootContainer.getScene().getWindow();
        stage.setScene(nextScene);
    }

    //Author: Rostyslav Kolodij, ChatGPT-4o(Explain the use of hash maps in java and give an example of creating one)
    private Map<String, List<Transaction>> groupTransactionsByMonth(List<Transaction> transactions) {
        Map<String, List<Transaction>> transactionsByMonth = new TreeMap<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

        for (Transaction transaction : transactions) {
            LocalDate date = LocalDate.parse(transaction.getDate(), formatter);
            String month = date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + date.getYear();

            transactionsByMonth.computeIfAbsent(month, k -> new ArrayList<>()).add(transaction);
        }

        return transactionsByMonth;
    }

    private void createMonthlyFiles(Map<String, List<Transaction>> transactionsByMonth, String buyerInitials) {
        String directoryPath = "src/main/resources/Transactions/" + buyerInitials;
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Map.Entry<String, List<Transaction>> entry : transactionsByMonth.entrySet()) {
            String month = entry.getKey(); // e.g., "January 2024"
            List<Transaction> monthTransactions = entry.getValue();

            String fileName = directoryPath + "/" + month.replace(" ", "_") + ".txt";
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
                for (Transaction transaction : monthTransactions) {
                    writer.write("Date: " + transaction.getDate());
                    writer.write(", Description: " + transaction.getDescription());
                    writer.write(", Type: " + transaction.getType());
                    writer.write(", Amount: " + transaction.getAmount());
                    writer.write(", Category: " + (transaction.getCategory() != null ? transaction.getCategory() : "Uncategorized"));
                    writer.newLine();
                }
                System.out.println("File created for " + month + ": " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



}