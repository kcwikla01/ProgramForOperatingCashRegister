package pl.kul.tools;

import pl.kul.Application;
import pl.kul.facilities.Storage;

import javax.swing.table.DefaultTableModel;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class ReceiptGenerator {
    public static void generateReceipt(DefaultTableModel TableOfListOfBoughtProducts, javax.swing.JLabel sumOfProducts) {
        String filepath = FIleChooser.getFilePathFromUser("txt", "paragon");
        if (filepath!=null){
            try (BufferedWriter writer = new BufferedWriter(new FileWriter (filepath))){
                writer.write(String.format("%-15s | %-5s | %-10s | %-10s%n", "Produkt", "Ilość", "Cena jedn.", "Cena razem"));
                writer.write(String.format("%-15s | %-5s | %-10s | %-10s%n", "--------", "-----", "----------", "----------"));
                Storage storage = Application.getStorage();
                for (int i = 0; i < TableOfListOfBoughtProducts.getRowCount(); i++) {
                    String productName = TableOfListOfBoughtProducts.getValueAt(i, 0).toString();
                    String quantity = TableOfListOfBoughtProducts.getValueAt(i, 1).toString();
                    String price = TableOfListOfBoughtProducts.getValueAt(i, 2).toString();
                    String total = TableOfListOfBoughtProducts.getValueAt(i, 3).toString();
                    writer.write(String.format("%-15s | %-5s | %-10s | %-10s%n", productName, quantity, price, total));
                    storage.removeProduct(storage.getProductByName(productName), Double.parseDouble(quantity));
                }
                writer.write(String.format("%-15s | %-5s | %-10s | %-10s%n", "--------", "-----", "----------", "----------"));
                writer.write("Suma: " + sumOfProducts.getText().replace("Razem: ", ""));
            } catch(
                    IOException e){
                System.out.println("Błąd zapisu do pliku");
            }
        }
    }
}