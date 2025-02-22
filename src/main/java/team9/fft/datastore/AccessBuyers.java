package team9.fft.datastore;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import team9.fft.pojo.Buyer;
import team9.fft.pojo.Transaction;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author _edd.ie_, Gemini(google), rostys-love
 */

public class AccessBuyers {
    private final Path BUYER_TXT;
    private final Path FOLDER_PATH;
    private ArrayList<Buyer> buyers = new ArrayList<>(); // Initialize inline
    private static final Logger LOGGER = Logger.getLogger(AccessBuyers.class.getName());

    public AccessBuyers(String fileName) {
        String storage = "src/main/resources/Transactions/";
        this.FOLDER_PATH = Paths.get(storage, fileName);
        this.BUYER_TXT = Paths.get(storage, fileName, "Buyers.txt");
        if (Files.notExists(BUYER_TXT)) {
            try {
                Files.createDirectories(BUYER_TXT.getParent());
                Files.createFile(BUYER_TXT);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error creating Buyers.txt file", e);
//                throw new RuntimeException("Could not create buyer's file: " + e.getMessage());
            }
        }else{
            readBuyers();
            for(Buyer x: buyers){
                getBuyerTransactions(x);
            }
        }
    }


    public Buyer getBuyer(Buyer buyer) {
        return buyers.get(buyers.indexOf(buyer));
    }

    public void readBuyers() {
        try {
            List<String> lines = Files.readAllLines(BUYER_TXT); // Read all lines from the file

            for (String line : lines) {
                String[] parts = line.split(","); // Split each line by comma
                if (parts.length == 2) { // Ensure there are two parts (name and initials)
                    String name = parts[0].trim();
                    String initials = parts[1].trim();
                    Buyer buyer = new Buyer(name, initials);
                    buyers.add(buyer);
                } else {
                    LOGGER.log(Level.WARNING, "Invalid line format in Buyers.txt: " + line);
                }
            }

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading Buyers.txt", e);
        }
    }

    public void writeBuyers() {
        // Check if the directory exists. Create if not.
        try {
            Files.createDirectories(BUYER_TXT.getParent());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error creating directory for Buyers.txt", e);
            return; // Stop if directory creation fails
        }


        try (FileWriter writer = new FileWriter(BUYER_TXT.toFile(), StandardCharsets.UTF_8)) {
            for (Buyer buyer : buyers) {
                writer.write(buyer.getName() + "," + buyer.getInitials() + "\n");
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error writing to Buyers.txt", e);
        }
    }

    public void addBuyerToFile(Buyer buyer) {
        // Check if directory exists and create if not (as before)
        try {
            Files.createDirectories(BUYER_TXT.getParent());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error creating directory for Buyers.txt: ", e);
            return;
        }

        try (BufferedWriter writer = Files.newBufferedWriter(BUYER_TXT, StandardCharsets.UTF_8, StandardOpenOption.APPEND, StandardOpenOption.CREATE)) {
            writer.write(buyer.getName() + "," + buyer.getInitials() + System.lineSeparator()); //Use System.lineSeparator() for platform independence
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error appending to Buyers.txt", e);
        }
    }

    public ArrayList<Buyer> getBuyers() {
        return buyers;
    }

    public void setBuyers(ArrayList<Buyer> buyers) {
        this.buyers = buyers;
        writeBuyers();
    }

    public void addBuyer(Buyer buyer) {
        buyers.add(buyer);
        addBuyerToFile(buyer);
    }

    public void removeBuyer(Buyer buyer) {
        buyers.remove(buyer);
        writeBuyers();
    }

    public void saveBuyerTransactions(Buyer buyer) {
        if (buyer == null || buyer.getInitials() == null || buyer.getInitials().isEmpty()) {
            LOGGER.log(Level.WARNING, "Cannot save transactions for buyer with null or empty initials.");
            return; // Or throw an exception if you prefer
        }

        if (buyer.getTransactions() == null || buyer.getTransactions().isEmpty()) {
            LOGGER.log(Level.INFO, "The specified buyer has no transactions to log yet.");
            return;
        }

        Path transactionsFile = FOLDER_PATH.resolve(buyer.getInitials() + "_Transactions.txt");

        try {
            Files.createDirectories(FOLDER_PATH); // Ensure directory exists

            try (BufferedWriter writer = Files.newBufferedWriter(transactionsFile, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                for (Transaction transaction : buyer.getTransactions()) {
                    writer.write(transaction.toString()); // Or format as needed (e.g., CSV)
                    writer.newLine(); // Add newline for each transaction
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving buyer transactions to file: " + transactionsFile, e);
            // Handle exception appropriately (e.g., throw a higher-level exception)
        }
    }

    public ArrayList<Transaction> getBuyerTransactions(Buyer buyer) {
        ArrayList<Transaction> transactions = new ArrayList<>();

        if (buyer == null || buyer.getInitials() == null || buyer.getInitials().isEmpty()) {
            LOGGER.log(Level.WARNING, "Cannot get transactions for buyer with null or empty initials.");
            return transactions; // Return empty list for invalid input
        }

        Path transactionsFile = FOLDER_PATH.resolve(buyer.getInitials() + "rd_Transactions.txt");

        if (Files.exists(transactionsFile)) {
            try {
                List<String> lines = Files.readAllLines(transactionsFile);
                for (String line : lines) {
                    try {
                        Transaction transaction = parseTransaction(line); // Assuming you have a parse method
                        if (transaction != null) {
                            transactions.add(transaction);
                        }
                    } catch (Exception e) { // Catch parsing errors
                        LOGGER.log(Level.WARNING, "Error parsing transaction line: " + line, e);
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error reading transactions from file: " + transactionsFile, e);
            }
        } else {
            LOGGER.log(Level.INFO, "Transaction file not found for " + buyer.getInitials());
        }

        buyer.setTransactions(transactions);
        return transactions;
    }


    private Transaction parseTransaction(String line) {
        String[] parts = line.split(" ");
        if (parts.length > 3) {
            try {
                if (parts.length == 4)
                    return new Transaction(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]));
                if (parts.length == 5)
                    return new Transaction(parts[0], parts[1], parts[2], Double.parseDouble(parts[3]), parts[4]);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.WARNING, "Error parsing line, returning null: " + line, e);
                return null;
            }

        }
        return null;
    }
}
