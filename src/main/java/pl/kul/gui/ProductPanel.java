package pl.kul.gui;

import pl.kul.Application;
import pl.kul.facilities.Product;
import pl.kul.facilities.TaxRate;

import javax.swing.*;


public final class ProductPanel implements MenuPanel{
    private JPanel productPanel;
    private JTextField productName;
    private JTextField productId;
    private JTextField quantity;
    private JTextField taxRate;
    private JTextField netUnitPrice;
    private JButton discardButton;
    private JButton confirmButton;
    private final StoragePanel storagePanel;
    private boolean editMode = false;

    public ProductPanel(StoragePanel storagePanel) {
        this.storagePanel = storagePanel;
        setupListeners();
    }

    public JPanel getPanel() {
        return productPanel;
    }

    private void setupListeners() {
        addDiscardListener();
        addConfirmListener();
    }

    private void addConfirmListener() {
        confirmButton.addActionListener(e -> {
            try {
                String idText = productId.getText();
                String name = productName.getText();
                String quantityText = quantity.getText();
                String taxRateText = taxRate.getText();
                String netUnitPriceText = netUnitPrice.getText();

                if(idText.isEmpty() || name.isEmpty() || quantityText.isEmpty() || taxRateText.isEmpty()|| netUnitPriceText.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Wszystkie pola muszą mieć poprawną wartość. Proszę uzupełnić brakujące pola.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                    long id = Long.parseLong(idText);
                if(!editMode) {
                    if (Application.getStorage().getProductIds().contains(id)) {
                        JOptionPane.showMessageDialog(null, "Produkt o podanym ID już istnieje. Proszę podać inne ID.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                }

                double quantity = Double.parseDouble(quantityText);

                TaxRate taxRate;
                if(taxRateText.toUpperCase().matches("[A-D]")){
                    taxRate = TaxRate.valueOf(taxRateText.toUpperCase());
                } else {
                    taxRate = TaxRate.getTaxRate(Integer.parseInt(taxRateText));
                }
                double netUnitPrice = Double.parseDouble(netUnitPriceText);

                Product product = new Product(id, name, netUnitPrice, taxRate);
                if(editMode) {
                    editMode = false;
                    Application.getStorage().removeProduct(Application.getStorage().getProductById(id));
                }
                Application.getStorage().addProduct(product, quantity);
                storagePanel.updateStorageTable();
                clearFields();

                switchToPanel(storagePanel.getPanel());
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Wszystkie pola muszą mieć poprawny format.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Wystąpił błąd podczas dodawania produktu. Proszę spróbować ponownie.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void addDiscardListener() {
        discardButton.addActionListener(e -> {
            editMode = false;
            clearFields();
            switchToPanel(storagePanel.getPanel());
        });
    }

    private void clearFields() {
        productName.setText("");
        productId.setText("");
        quantity.setText("");
        taxRate.setText("");
        netUnitPrice.setText("");
    }

    public void setProductToUpdate(Product product, double quantity) {
        editMode = true;
        productId.setEnabled(false);
        productName.setText(product.getName());
        productId.setText(String.valueOf(product.getId()));
        this.quantity.setText(String.valueOf(quantity));
        taxRate.setText(String.valueOf(product.getTaxRate()));
        netUnitPrice.setText(String.valueOf(product.getNetPrice()));
    }

}
