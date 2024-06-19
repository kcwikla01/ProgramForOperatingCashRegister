package pl.kul.gui;
import org.assertj.swing.core.matcher.JButtonMatcher;
import org.assertj.swing.core.matcher.JTextComponentMatcher;
import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.DialogFixture;
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

import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CashRegisterPanelTest {
        private FrameFixture window;
        private CashRegisterPanel  cashRegisterPanel;

        @BeforeEach
        public void  setup(){
            // given
            Storage storage = new Storage();
            // and
            storage.addProduct(new Product(1, "Test product1", 10, TaxRate.A), 10.0);
            storage.addProduct(new Product(2, "Test product2", 20, TaxRate.B), 20.0);
            // and
            Application.setStorage(storage);
            // and
            MainMenu mainMenu = GuiActionRunner.execute(MainMenu::new);
            cashRegisterPanel= mainMenu.getCashRegisterPanel();
            window = new FrameFixture(mainMenu.frame);
            window.show();
        }
    @AfterEach
    public void tearDown() {
        if(window != null)
            window.cleanUp();
    }

        @Test
        void should_initialize_all_buttons_text_fields_and_table() throws InterruptedException {
            //then
            Assertions.assertNotNull(cashRegisterPanel.getButton0());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getButton1());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getButton2());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getButton3());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getButton4());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getButton5());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getButton6());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getButton7());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getButton8());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getButton9());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getBackspaceButton());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getCButton());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getButtonDown());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getButtonUp());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getDeleteButton());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getENTERButton());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getCommaButton());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getSumOfProducts());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getQuantityButton());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getCloseTransaction());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getExitButton());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getProductIdLabel());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getProductIdTextArea());
            //and
            Assertions.assertNotNull(cashRegisterPanel.getTableOfListOfBoughtProducts());
        }
    @Test
    void should_add_product_to_table_with_correct_quantity_and_price() throws InterruptedException {
        //when
        cashRegisterPanel.getProductIdTextArea().setText("1");
        //and
        cashRegisterPanel.getENTERButton().doClick();
        //then
        DefaultTableModel model = (DefaultTableModel) cashRegisterPanel.getTableOfListOfBoughtProducts().getModel();
        //and
        Assertions.assertEquals(1, model.getRowCount());
        //and
        Assertions.assertEquals("Test product1", model.getValueAt(0, 0));
        //and
        Assertions.assertEquals(1.0, model.getValueAt(0, 1));
        //and
        Assertions.assertEquals(12.3, model.getValueAt(0, 2));
    }

    @Test
    void should_add_product_to_table_with_correct_quantity_after_quantity_button_click() throws InterruptedException {
        //when
        cashRegisterPanel.getProductIdTextArea().setText("1");
        //and
        cashRegisterPanel.getQuantityButton().doClick();
        //then
        DialogFixture dialog = window.dialog();
        //when
        dialog.button("dialogButton1").click();
        dialog.button("dialogButton2").click();
        dialog.button("dialogButtonEnter").click();
        //then
        DefaultTableModel model = (DefaultTableModel) cashRegisterPanel.getTableOfListOfBoughtProducts().getModel();
        //and
        Assertions.assertEquals(1, model.getRowCount());
        //and
        Assertions.assertEquals("Test product1", model.getValueAt(0, 0));
        //and
        Assertions.assertEquals(12.0, model.getValueAt(0, 1)); // 12 because we clicked buttons "1" and "2"
        //and
        Assertions.assertEquals(12.3, model.getValueAt(0, 2));
        //and
        Assertions.assertEquals(12.0 * 12.3, model.getValueAt(0, 3));
    }

    @Test
    void should_remove_first_row_when_selected() {
        // when
        cashRegisterPanel.getProductIdTextArea().setText("1");
        cashRegisterPanel.getENTERButton().doClick();
        cashRegisterPanel.getProductIdTextArea().setText("2");
        cashRegisterPanel.getENTERButton().doClick();
        // then
        cashRegisterPanel.getTableOfListOfBoughtProducts().setRowSelectionInterval(0, 0);
        // and
        cashRegisterPanel.getDeleteButton().doClick();
        // and
        DefaultTableModel model = (DefaultTableModel) cashRegisterPanel.getTableOfListOfBoughtProducts().getModel();
        // and
        assertEquals(1, model.getRowCount());
        // and
        assertEquals("Test product2", model.getValueAt(0, 0));
    }

    @Test
    void should_remove_second_row_when_selected() {
        // when
        cashRegisterPanel.getProductIdTextArea().setText("1");
        cashRegisterPanel.getENTERButton().doClick();
        cashRegisterPanel.getProductIdTextArea().setText("2");
        cashRegisterPanel.getENTERButton().doClick();
        // and
        cashRegisterPanel.getTableOfListOfBoughtProducts().setRowSelectionInterval(1, 1);
        // and
        cashRegisterPanel.getDeleteButton().doClick();
        //then
        DefaultTableModel model = (DefaultTableModel) cashRegisterPanel.getTableOfListOfBoughtProducts().getModel();
        assertEquals(1, model.getRowCount());
        assertEquals("Test product1", model.getValueAt(0, 0));
    }


    @Test
    void should_open_dialog_and_close_transaction(@TempDir Path tempDir) throws InterruptedException, AWTException, IOException {
         String tesFileName= "paragon.txt";
         Path testFilePath = tempDir.resolve(tesFileName);
            // given
        cashRegisterPanel.getProductIdTextArea().setText("1");
        cashRegisterPanel.getENTERButton().doClick();
        Robot robot = new Robot();
        robot.setAutoDelay(100);
        // when
        cashRegisterPanel.getCloseTransaction().doClick();

        // then
        DialogFixture dialog = window.dialog();
        Assertions.assertNotNull(dialog);

        // when
        dialog.textBox(JTextComponentMatcher.withName("cashField")).enterText("20");
        dialog.button("dialogButtonEnter").click();
        // then
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);

        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(200);
        window.fileChooser().setCurrentDirectory(tempDir.toFile());
        // and
        window.fileChooser().fileNameTextBox().setText(tesFileName);
        // and
        window.fileChooser().approve();
        // and
        DefaultTableModel model = (DefaultTableModel) cashRegisterPanel.getTableOfListOfBoughtProducts().getModel();
        Assertions.assertEquals(0, model.getRowCount());
        // and
        Assertions.assertTrue(testFilePath.toFile().exists());
        Assertions.assertLinesMatch(List.of(
                "Produkt         | Ilość | Cena jedn. | Cena razem" ,
                "--------        | ----- | ---------- | ----------",
                "Test product1   | 1.0   | 12.3       | 12.3      ",
                "--------        | ----- | ---------- | ----------",
                "Suma: 12,30 zł"), Files.readAllLines(testFilePath));
    }
    @Test
    void should_show_error_when_customer_pays_less_than_total(@TempDir Path tempDir) throws AWTException, InterruptedException {
        String tesFileName= "paragon.txt";
        Path testFilePath = tempDir.resolve(tesFileName);
        // given
        cashRegisterPanel.getProductIdTextArea().setText("1");
        cashRegisterPanel.getENTERButton().doClick();
        Robot robot = new Robot();
        robot.setAutoDelay(100);
        // when
        cashRegisterPanel.getCloseTransaction().doClick();
        // then
        DialogFixture dialog = window.dialog();
        Assertions.assertNotNull(dialog);
        // when
        dialog.textBox(JTextComponentMatcher.withName("cashField")).enterText("0");
        dialog.button("dialogButtonEnter").click();
        // then
        robot.keyPress(KeyEvent.VK_ENTER);
        robot.keyRelease(KeyEvent.VK_ENTER);
        Thread.sleep(200);
        // and
        Assertions.assertFalse(testFilePath.toFile().exists());
    }
}