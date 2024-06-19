package pl.kul.tools;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.kul.facilities.Product;
import pl.kul.facilities.Storage;
import pl.kul.facilities.TaxRate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PhysicalInventoryGeneratorTest {
    @Test
    void should_generate_physical_inventory_file_with_correct_data(
            @TempDir Path tempDir
    ) throws IOException {
        // given
        PhysicalInventoryGenerator sut = new PhysicalInventoryGenerator();
        // and
        Storage storage = new Storage();
        // and
        storage.addProduct(new Product(
                1,
                "Product 1",
                10.0,
                TaxRate.A
                ), 5.0);
        storage.addProduct(new Product(
                2,
                "Product 2",
                20.0,
                TaxRate.B
                ), 10.0);
        // when
        sut.writeInventoryToFile(storage, tempDir.resolve("test.csv").toString());
        // then
        assertTrue(Files.exists(tempDir.resolve("test.csv")));
        // and
        assertLinesMatch(
                Files.readAllLines(tempDir.resolve("test.csv")),
                List.of(
                        "ID,Nazwa,Cena netto,Stawka VAT,Cena brutto,Ilość,Razem",
                        "1,Product 1,10.0,A,12.3,5.0,61.5",
                        "2,Product 2,20.0,B,21.6,10.0,216.0"
                ));
    }
}