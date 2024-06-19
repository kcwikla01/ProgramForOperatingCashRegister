package pl.kul.gui;

import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pl.kul.Application;
import pl.kul.facilities.Product;
import pl.kul.facilities.Storage;
import pl.kul.facilities.TaxRate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;

public class StorageAndProductPanelTest {
    private FrameFixture window;
    private StoragePanel storagePanel;

    @BeforeEach
    public void setup() {
        // given
        Storage storage = new Storage();
        // and
        storage.addProduct(new Product(1, "Test product1", 10, TaxRate.A), 10.0);
        storage.addProduct(new Product(2, "Test product2", 20, TaxRate.B), 20.0);
        // and
        Application.setStorage(storage);
        // and
        MainMenu mainMenu = GuiActionRunner.execute(MainMenu::new);
        storagePanel = mainMenu.getStoragePanel();
        window = new FrameFixture(mainMenu.frame);
        window.show();
    }

    @Test
    void should_initialize_all_buttons_text_fields_and_table() {
        // then
        Assertions.assertNotNull(storagePanel.getAddProductButton());
        // and
        Assertions.assertNotNull(storagePanel.getEditProductButton());
        // and
        Assertions.assertNotNull(storagePanel.getRemoveProductButton());
        // and
        Assertions.assertNotNull(storagePanel.getGeneratePhysicalButton());
        // and
        Assertions.assertNotNull(storagePanel.getUpButton());
        // and
        Assertions.assertNotNull(storagePanel.getDownButton());
        // and
        Assertions.assertNotNull(storagePanel.getSearchButton());
        // and
        Assertions.assertNotNull(storagePanel.getExitButton());
        // and
        Assertions.assertNotNull(storagePanel.getSearchField());
        // and
        Assertions.assertNotNull(storagePanel.getStorageTable());
    }

    @Test
    void should_initialize_product_panel() {
        // then
        Assertions.assertNotNull(storagePanel.getProductPanel());
    }

    @Test
    void should_switch_panel_to_product_panel_after_clicking_add_product_button(){
        // when
        window.button("storageButton").click();
        // and
        window.button("addProductButton").click();
        // then
        Assertions.assertTrue(window.panel("productPanel").target().isVisible());
    }

    @Test
    void should_switch_panel_to_product_panel_after_clicking_edit_product_button(){
        // when
        window.button("storageButton").click();
        // and
        window.button("editProductButton").click();
        // then
        Assertions.assertTrue(window.panel("productPanel").target().isVisible());
    }

    @Test
    void should_add_new_product_to_storage(){
        // and
        Map.Entry<Product,Double> entry = new AbstractMap.SimpleEntry<>(
                new Product(
                        3, "Product1", 10, TaxRate.A
                ), 10.0
        );
        // when
        window.button("storageButton").click();
        // and
        window.button("addProductButton").click();
        // and
        setProductFields(entry);
        // and
        window.button(JButtonMatcher.withText("Zatwierdź")).click();
        // then
        Assertions.assertEquals(3, Application.getStorage().getProductsMap().size());
        // and
        Assertions.assertTrue(Application.getStorage().getProductsMap().containsKey(entry.getKey()));
        // and
        Assertions.assertEquals(10, Application.getStorage().getProductsMap().get(entry.getKey()));
    }

    @Test
    void should_edit_product_in_storage(){
        // and
        Map.Entry<Product,Double> entry = new AbstractMap.SimpleEntry<>(
                new Product(
                        1, "Edited product", 11, TaxRate.B
                ), 11.0
        );
        // when
        window.button("storageButton").click();
        // and
        window.button("editProductButton").click();
        // and
        setProductFields(entry);
        // and
        window.button("confirmButton").click();

        // then
        assertEquals(2, Application.getStorage().getProductsMap().size());
        // and
        assertEquals(entry.getKey(), Application.getStorage().getProductById(1));
        // and
        assertEquals(11.0, Application.getStorage().getProductsMap().get(entry.getKey()));
    }

    @Test
    void down_button_should_increase_selected_row_by_one() {
        // given
        window.button("storageButton").click();
        // when
        window.button("downButton").click();
        // then
        assertEquals(1, window.table("storageTable").target().getSelectedRow());
    }

    @Test
    void down_button_should_not_increase_selected_row_when_last_row_is_selected() {
        // given
        window.button("storageButton").click();
        // and
        window.button("downButton").click();
        // when
        window.button("downButton").click();
        // then
        assertEquals(1, window.table("storageTable").target().getSelectedRow());
    }

    @Test
    void up_button_should_not_increase_selected_row_when_first_row_is_selected() {
        // given
        window.button("storageButton").click();
        // when
        window.button("upButton").click();
        // then
        assertEquals(0, window.table("storageTable").target().getSelectedRow());
    }

    @Test
    void up_button_should_decrease_selected_row_by_one() {
        // given
        window.button("storageButton").click();
        // and
        window.button("downButton").click();
        // when
        window.button("upButton").click();
        // then
        assertEquals(0, window.table("storageTable").target().getSelectedRow());
    }

    @Test
    void search_button_should_select_first_row_when_no_text_is_entered() {
        // given
        window.button("storageButton").click();
        // when
        window.button("searchButton").click();
        // then
        assertEquals(0, window.table("storageTable").target().getSelectedRow());
    }

    @Test
    void search_button_should_select_first_row_when_no_matching_text_is_entered() {
        // given
        window.button("storageButton").click();
        // when
        window.textBox("searchField").setText("Non existing product");
        // and
        window.button("searchButton").click();
        // then
        assertEquals(0, window.table("storageTable").target().getSelectedRow());
    }

    @Test
    void search_button_should_select_first_matching_row_when_text_is_entered() {
        // given
        window.button("storageButton").click();
        // when
        window.textBox("searchField").setText("Test product2");
        // and
        window.button("searchButton").click();
        // then
        assertEquals(1, window.table("storageTable").target().getSelectedRow());
    }

    @Test
    void should_remove_only_first_product_from_storage_when_multiple_products_are_in_storage() {
        // when
        window.button("storageButton").click();
        // and
        window.button("removeProductButton").click();
        // then
        assertEquals(1, Application.getStorage().getProductsMap().size());
        // and
        Assertions.assertFalse(Application.getStorage().getProductsMap().containsKey(new Product(1, "Test product1", 10, TaxRate.A)));
    }

    @Test
    void should_remove_only_second_product_from_storage_when_multiple_products_are_in_storage() {
        // when
        window.button("storageButton").click();
        // and
        window.button("downButton").click();
        // and
        window.button("removeProductButton").click();
        // then
        assertEquals(1, Application.getStorage().getProductsMap().size());
        // and
        Assertions.assertFalse(Application.getStorage().getProductsMap().containsKey(new Product(2, "Test product2", 20, TaxRate.B)));
    }

    @Test
    void remove_product_button_should_clear_selection_when_no_products() {
        // given
        window.button("storageButton").click();
        // when
        window.button("removeProductButton").click();
        window.button("removeProductButton").click();
        window.button("removeProductButton").click();
        // then
        assertEquals(-1, window.table("storageTable").target().getSelectedRow());
        // and

    }

    @Test
    void should_generate_physical_inventory_file_when_generate_physical_inventory_button_is_clicked(@TempDir Path tempDir) throws IOException, InterruptedException {
        // and
        String testFileName = "test.csv";
        // and
        Path physicalInventoryFile = tempDir.resolve(testFileName);
        // when
        window.button("storageButton").click();
        // and
        window.button("generatePhysicalInventoryButton").click();
        // and
        window.fileChooser().setCurrentDirectory(tempDir.toFile());
        window.fileChooser().fileNameTextBox().setText(testFileName);
        // and
        Thread.sleep(300);
        window.fileChooser().approve();
        // then
        Assertions.assertTrue(physicalInventoryFile.toFile().exists());
        // and
        assertLinesMatch(
                List.of(
                        "ID,Nazwa,Cena netto,Stawka VAT,Cena brutto,Ilość,Razem",
                        "1,Test product1,10.0,A,12.3,10.0,123.0",
                        "2,Test product2,20.0,B,21.6,20.0,432.0"
                ),
                Files.readAllLines(physicalInventoryFile)
        );
    }

    @Test
    void should_switch_panel_to_main_menu_after_clicking_exit_button(){
        // when
        window.button("storageButton").click();
        // and
        window.button("exitButton").click();
        // then
        Assertions.assertTrue(window.panel("mainMenuPanel").target().isVisible());
    }

    @AfterEach
    public void tearDown() {
        if(window != null)
            window.cleanUp();
    }

    private void setProductFields(Map.Entry<Product, Double> entryToInsert) {
        if(window.textBox("productIdField").isEnabled())
            window.textBox("productIdField").setText(String.valueOf(entryToInsert.getKey().getId()));
        window.textBox("productNameField").setText(entryToInsert.getKey().getName());
        window.textBox("quantityField").setText(String.valueOf(entryToInsert.getValue()));
        window.textBox("taxRateField").setText(String.valueOf(entryToInsert.getKey().getTaxRate()));
        window.textBox("netUnitPriceField").setText(String.valueOf(entryToInsert.getKey().getNetPrice()));
    }

}