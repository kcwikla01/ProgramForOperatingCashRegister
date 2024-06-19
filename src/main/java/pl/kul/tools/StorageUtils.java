package pl.kul.tools;

import pl.kul.facilities.Product;
import pl.kul.facilities.Storage;
import pl.kul.facilities.TaxRate;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class StorageUtils {

    public Storage setupStorage(String filePath) {
        if(storageFileExist(filePath)) {
            return readStorageFromFile(filePath);
        } else {
            return new Storage();
        }
    }

    private Storage readStorageFromFile(String filePath) {
        Storage storage = new Storage();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line = reader.readLine();
            if (line == null) {
                return storage;
            } else if (!line.equals("ID,Nazwa,Cena netto,Stawka VAT,Cena brutto,Ilość,Razem")) {
                return storage;
            }
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                long id = Long.parseLong(parts[0]);
                String name = parts[1];
                double netPrice = Double.parseDouble(parts[2]);
                TaxRate taxRate = TaxRate.valueOf(parts[3]);
                double quantity = Double.parseDouble(parts[5]);

                Product product = new Product(id, name, netPrice, taxRate);
                storage.addProduct(product, quantity);
            }
        } catch (IOException e) {
            System.out.println("Could not read storage file");
        }
        return storage;
    }

    private boolean storageFileExist(String filePath) {
        Path path = Paths.get(filePath);
        return Files.exists(path);
    }
}
