package pl.kul.tools;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.kul.facilities.Product;
import pl.kul.facilities.Storage;
import pl.kul.facilities.TaxRate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

class StorageUtilsTest {

    @Test
    void setup_storage_should_create_empty_storage_when_no_storage_file_exists(
            @TempDir Path tempDir
    ) {
        // given
        StorageUtils sut = new StorageUtils();
        // when
        Storage storage = sut.setupStorage(tempDir.resolve("storage.csv").toString());
        // then
        Assertions.assertThat(storage.getProducts()).isEmpty();
    }

    @Test
    void setup_storage_should_create_empty_storage_when_storage_file_is_empty(
            @TempDir Path tempDir
    ) throws IOException {
        // given
        StorageUtils sut = new StorageUtils();
        // and
        Path storageFilePath = tempDir.resolve("storage.csv");
        Files.createFile(storageFilePath);
        // when
        Storage storage = sut.setupStorage(storageFilePath.toString());
        // then
        Assertions.assertThat(storage.getProducts()).isEmpty();
    }

    @Test
    void setup_storage_should_create_empty_storage_when_file_content_is_invalid(
            @TempDir Path tempDir
    ) {
        // given
        StorageUtils sut = new StorageUtils();
        // and
        Path storageFilePath = tempDir.resolve("storage.csv");
        StorageFileHelper.createStorageFileWithLines(storageFilePath, List.of(
                "TEST DATA",
                "123123",
                "This is a test"
        ));
        // when
        Storage storage = sut.setupStorage(storageFilePath.toString());
        // then
        Assertions.assertThat(storage.getProducts()).isEmpty();
    }

    @Test
    void setup_storage_should_create_storage_with_products_when_file_content_is_valid(
            @TempDir Path tempDir
    ){
        // given
        StorageUtils sut = new StorageUtils();
        // and
        Path storageFilePath = tempDir.resolve("storage.csv");
        StorageFileHelper.createStorageFileWithLines(storageFilePath, List.of(
                "ID,Nazwa,Cena netto,Stawka VAT,Cena brutto,Ilość,Razem",
                "1,Product 1,10.0,A,12.3,5.0,61.5",
                "2,Product 2,20.0,B,21.6,10.0,216.0"
        ));
        // when
        Storage storage = sut.setupStorage(storageFilePath.toString());
        // then
        Assertions.assertThat(storage).isNotNull();
        // and
        Assertions.assertThat(storage.getProductsMap()).hasSize(2);
        // and
        Assertions.assertThat(storage.getProductsMap().entrySet()).containsExactlyElementsOf(
                List.of(
                        Map.entry(
                                new Product(1, "Product 1", 10.0, TaxRate.A),
                                5.0
                        ),
                        Map.entry(
                                new Product(2, "Product 2", 20.0, TaxRate.B),
                                10.0
                        )
                )
        );
    }
}