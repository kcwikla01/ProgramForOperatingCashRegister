package pl.kul.gui;

import lombok.Getter;
import pl.kul.Application;
import pl.kul.facilities.Product;
import pl.kul.facilities.Storage;
import pl.kul.tools.PhysicalInventoryGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

@Getter
public final class StoragePanel implements MenuPanel {
    private JPanel panel;
    private final ProductPanel productPanel;
    private JButton editProductButton;
    private JButton downButton;
    private JButton generatePhysicalButton;
    private JButton exitButton;
    private JButton searchButton;
    private JButton addProductButton;
    private JButton upButton;
    private JButton removeProductButton;
    private JTextField searchField;
    private JTable storageTable;
    private int selectedRow = 0;
    private boolean changesMade = false;
    private final MainMenu mainMenuPanel;

    public StoragePanel(MainMenu mainMenuPanel) {
        this.mainMenuPanel = mainMenuPanel;
        productPanel = new ProductPanel(StoragePanel.this);
        setupStorageTable();
        addListeners();
    }

    private void setupStorageTable(){
        DefaultTableModel tableModel = new DefaultTableModel();
        String[] columnNames = {"ID", "Nazwa", "Ilość", "Cena jedn. Netto", "Podatek", "Cena jedn. Brutto"};
        tableModel.setColumnIdentifiers(columnNames);

        Storage storage = Application.getStorage();
        storage.getProductsMap().forEach((product, quantity) -> {
            Object[] rowData = {
                    product.getId(),
                    product.getName(),
                    quantity,
                    product.getNetPrice(),
                    product.getTaxRate() + "(" + product.getTaxRate().getRate() + "%)",
                    product.getGrossPrice()
            };
            tableModel.addRow(rowData);
        });
        storageTable.setModel(tableModel);
        if(storageTable.getRowCount() == 0) {
            selectedRow = -1;
        }
        if(storageTable.getRowCount() > 0) {
            storageTable.setRowSelectionInterval(0, 0);
        }
        storageTable.setEnabled(false);
    }


    private void addListeners() {
        addEditProductListener();
        addGenerateListListener();
        addExitListener();
        addSearchListener();
        addAddProductListener();
        addScrollDownListener();
        addScrollUpListener();
        addDeleteProductListener();
    }

    private void addAddProductListener() {
        addProductButton.addActionListener(e -> {
            changesMade = true;
            switchToPanel(productPanel.getPanel());
        });
    }

    private void addDeleteProductListener() {
        removeProductButton.addActionListener(e -> {
            DefaultTableModel tableModel = (DefaultTableModel) storageTable.getModel();
            if(selectedRow != -1) {
                Product product = Application.getStorage().getProductById((long) storageTable.getValueAt(selectedRow, 0));
                tableModel.removeRow(selectedRow);
                Application.getStorage().removeProduct(product);
                changesMade = true;
                selectedRow--;
                if(selectedRow == -1 && storageTable.getRowCount() > 0) {
                    selectedRow = 0;
                    storageTable.setRowSelectionInterval(selectedRow, selectedRow);
                } else if(selectedRow >= 0) {
                    storageTable.setRowSelectionInterval(selectedRow, selectedRow);
                } else {
                    storageTable.clearSelection();
                }
            }
        });
    }

    private void addScrollUpListener() {
        upButton.addActionListener(e -> {
            if(storageTable.getSelectedRow() > 0 &&
                    selectedRow != -1) {
                selectedRow--;
                storageTable.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });
    }

    private void addScrollDownListener() {
        downButton.addActionListener(e -> {
            if(selectedRow < storageTable.getRowCount() - 1 &&
                    storageTable.getSelectedRow() < storageTable.getRowCount() - 1 &&
                    selectedRow != -1) {
                selectedRow++;
                storageTable.setRowSelectionInterval(selectedRow, selectedRow);
            }
        });
    }

    private void addSearchListener() {
        searchButton.addActionListener(e -> {
            String searchText = searchField.getText();
            if(searchText.isEmpty() || selectedRow == -1) {
                return;
            }
            for(int i = 0; i < storageTable.getRowCount(); i++) {
                if(storageTable.getValueAt(i, 1).toString().toLowerCase().matches(".*" + searchText.toLowerCase() + ".*")) {
                    storageTable.setRowSelectionInterval(i, i);
                    selectedRow = i;
                    searchField.setText("");
                    return;
                }
            }
            if(storageTable.getRowCount() > 0) {
                storageTable.setRowSelectionInterval(0, 0);
                selectedRow = 0;
            }
        });
    }

    private void addExitListener() {
        exitButton.addActionListener(e -> {
            if(changesMade) {
                PhysicalInventoryGenerator physicalInventoryGenerator = new PhysicalInventoryGenerator();
                physicalInventoryGenerator.writeInventoryToFile(Application.getStorage(), "storage.csv");
            }
            switchToPanel(mainMenuPanel.getPanel());
        });
    }

    private void addGenerateListListener() {
        generatePhysicalButton.addActionListener(e -> {
            PhysicalInventoryGenerator physicalInventoryGenerator = new PhysicalInventoryGenerator();
            physicalInventoryGenerator.generatePhysicalInventory(Application.getStorage());
        });
    }

    private void addEditProductListener() {
        editProductButton.addActionListener(e -> {
            if(selectedRow != -1) {
                Product product = Application.getStorage().getProductById((long) storageTable.getValueAt(selectedRow, 0));
                double quantity = (double) storageTable.getValueAt(selectedRow, 2);
                productPanel.setProductToUpdate(product, quantity);
                switchToPanel(productPanel.getPanel());
            }
        });
    }

    public void updateStorageTable() {
       if(storageTable.getRowCount() == 1) {
           selectedRow = 0;
       }
       setupStorageTable();
    }
}
