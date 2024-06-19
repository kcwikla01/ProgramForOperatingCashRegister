package pl.kul.tools;

import pl.kul.facilities.Storage;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;

public class PhysicalInventoryGenerator {

public void generatePhysicalInventory(Storage storage) {
        String filePath = FIleChooser.getFilePathFromUser("csv","spis");
        if (filePath != null) {
            writeInventoryToFile(storage, filePath);
        }
    }

    public void writeInventoryToFile(Storage storage, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("ID,Nazwa,Cena netto,Stawka VAT,Cena brutto,Ilość,Razem\n");
            storage.getProductsMap().forEach((product, quantity) -> {
                try {
                    writer.append(String.valueOf(product.getId()))
                            .append(",")
                            .append(product.getName()).append(",")
                            .append(String.valueOf(product.getNetPrice())).append(",")
                            .append(String.valueOf(product.getTaxRate())).append(",")
                            .append(String.valueOf(product.getGrossPrice())).append(",")
                            .append(String.valueOf(quantity)).append(",")
                            .append(String.valueOf(product.getGrossPrice() * quantity)).append("\n");
                } catch (IOException e) {
                    System.out.println("Błąd zapisu do pliku");
                }
            });
        } catch (IOException e) {
            System.out.println("Błąd zapisu do pliku");
        }
    }
}
