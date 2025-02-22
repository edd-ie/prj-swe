/*
* @authors rostys-love, KhussThakrani
 */
package team9.fft.pojo;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TransactionRepo {

    private ArrayList<Transaction> transactions = new ArrayList<>();

    public List<Transaction> findTransactionsByType(String type) {
        List<Transaction> filteredTransactions = new ArrayList<>();
        for (Transaction transaction : transactions) {
            if (transaction.getType().equalsIgnoreCase(type)) {
                filteredTransactions.add(transaction);
            }
        }
        return filteredTransactions;
    }

    public void loadTransactionsFromExcel(String filePath) {
        try (FileInputStream fis = new FileInputStream(new File(filePath))) {
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheetAt(0); // Get the first sheet

            Iterator<Row> rowIterator = sheet.iterator();

            boolean firstRow = true;
            transactions.clear(); // Clear existing data

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (firstRow) {
                    firstRow = false; // Skip header row
                    continue;
                }

                Cell dateCell = row.getCell(0);
                Cell descriptionCell = row.getCell(1);
                Cell debitCell = row.getCell(2);
                Cell creditCell = row.getCell(3);

                String date = "";
                String description = "";
                String type = "";
                double amount;

                if (dateCell != null) {
                    if (dateCell.getCellType() == CellType.STRING) {
                        date = dateCell.getStringCellValue();
                    } else if (dateCell.getCellType() == CellType.NUMERIC) {
                        date = dateCell.getLocalDateTimeCellValue().toLocalDate().toString();
                    }

                }

                if (descriptionCell != null) {
                    description = descriptionCell.getStringCellValue();
                }

                if (debitCell != null && debitCell.getCellType() == CellType.NUMERIC) {
                    type = "Debit";
                    amount = debitCell.getNumericCellValue();
                } else if (creditCell != null && creditCell.getCellType() == CellType.NUMERIC) {
                    type = "Credit";
                    amount = creditCell.getNumericCellValue();
                } else {
                    continue; // Skip rows without valid debit or credit values
                }

                Transaction transaction = new Transaction(date, description, type, amount);
                transactions.add(transaction);
            }

            workbook.close();
        } catch (Exception e) {
            System.err.println("Error loading transactions from Excel: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Transaction transaction : transactions) {
            output.append(transaction).append("\n");
        }
        return output.toString();
    }
}