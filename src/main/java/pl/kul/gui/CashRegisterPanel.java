package pl.kul.gui;

import lombok.Getter;
import pl.kul.Application;
import pl.kul.facilities.Product;
import pl.kul.facilities.Storage;
import pl.kul.tools.ReceiptGenerator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Getter
public final class CashRegisterPanel implements MenuPanel {
    private JPanel panel;
    private JButton BackspaceButton;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    private JButton button5;
    private JButton button6;
    private JButton button7;
    private JButton button8;
    private JButton button9;
    private JButton buttonUp;
    private JButton ButtonDown;
    private JButton DeleteButton;
    private JButton Button0;
    private JButton commaButton;
    private JButton ENTERButton;
    private JButton closeTransaction;
    private JButton DisountButton;
    private JTextArea productIdTextArea;
    private JLabel productIdLabel;
    private JButton exitButton;
    private JButton cButton;
    private JTable TableOfListOfBoughtProducts;
    private JLabel sumOfProducts;
    private JButton quantityButton;
    private final MainMenu mainMenuPanel;
    private JDialog popup;
    private JDialog popupCloseTransaction;


    public CashRegisterPanel(MainMenu mainMenuPanel) {
        this.mainMenuPanel = mainMenuPanel;
        setupStorageTable();
        numbersButtonListener();
        commaButtonListener();
        backspaceAndCButtonListener();
        exitButtonListener();
        enterButtonListener();
        quantityButtonListener();
        closeTransactionListener();
        addScrollUpListener();
        addScrollDownListener();
        addDeleteButtonListener();
    }

    private void closeTransactionListener() {
        closeTransaction.addActionListener(e -> {
            popupCloseTransaction = new JDialog();
            popupCloseTransaction.setTitle("Wpisz kwotę, którą klient dał:");
            popupCloseTransaction.setLayout(new BorderLayout());
            popupCloseTransaction.pack();
            popupCloseTransaction.setLocationRelativeTo(null);

            JTextField cashField = new JTextField();
            cashField.setName("cashField");

            cashField.setBorder(BorderFactory.createCompoundBorder(
                    cashField.getBorder(),
                    BorderFactory.createEmptyBorder(5, 5, 5, 20))); // top, left, bottom, right
            popupCloseTransaction.add(cashField, BorderLayout.NORTH);

            JPanel numberPanel = new JPanel(new GridLayout(4, 3));
            popupCloseTransaction.add(numberPanel, BorderLayout.CENTER);

            for (int i = 1; i <= 9; i++) {
                JButton button = new JButton(String.valueOf(i));
                button.setName("dialogButton" + i); // nadajemy unikalną nazwę przyciskowi
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        cashField.setText(cashField.getText() + button.getText());
                    }
                });
                numberPanel.add(button);
            }

            JButton zeroButton = new JButton("0");
            zeroButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cashField.setText(cashField.getText() + zeroButton.getText());
                }
            });
            numberPanel.add(zeroButton);

            JButton dotButton = new JButton(".");
            dotButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String currentText = cashField.getText();
                    if (!currentText.contains(".")) {
                        cashField.setText(currentText + dotButton.getText());
                    }
                }
            });
            numberPanel.add(dotButton);

            // Create a panel for zero and enter buttons
            JPanel southPanel = new JPanel(new BorderLayout());
            popupCloseTransaction.add(southPanel, BorderLayout.SOUTH);

            // Create a panel for enter and cancel buttons
            JPanel enterCancelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            southPanel.add(enterCancelPanel, BorderLayout.SOUTH);
            // Create enter button
            JButton enterButton = new JButton("Enter");
            enterButton.setName("dialogButtonEnter");
            enterButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Here you can add the code that is currently in your closeTransactionListener
                    // For example:
                    try {
                        String totalStr = sumOfProducts.getText().replace("Razem: ", "").replace(" zł", "").replace(",", ".");
                        double total = Double.parseDouble(totalStr);

                        double cash = Double.parseDouble(cashField.getText().replace(",", "."));

                        double change = cash - total;
                        JOptionPane dialog= new JOptionPane();
                        if (change < 0) {
                           JOptionPane.showConfirmDialog(null, "Brakuje: " + String.format("%.2f",-change) + " zł","Brakująca kwota",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showConfirmDialog(null, "Reszta: " + String.format("%.2f", change) + " zł","Reszta",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE);
                            JOptionPane.showConfirmDialog(null, "Generowanie paragonu...", "Paragon", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE);
                            ReceiptGenerator.generateReceipt((DefaultTableModel) TableOfListOfBoughtProducts.getModel(), sumOfProducts);
                            DefaultTableModel model = (DefaultTableModel) TableOfListOfBoughtProducts.getModel();
                            model.setRowCount(0);
                            sumOfProducts.setText("Razem: ");
                        }
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(null, "Nieprawidłowa kwota");
                    }
                    popupCloseTransaction.dispose();
                }
            });
            enterCancelPanel.add(enterButton);

            // Create cancel button
            JButton cancelButton = new JButton("Anuluj");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    popupCloseTransaction.dispose();
                }
            });
            enterCancelPanel.add(cancelButton);

            popupCloseTransaction.pack();
            popupCloseTransaction.setVisible(true);
        });
    }



    private void setupStorageTable() {
        DefaultTableModel tableModel = new DefaultTableModel();
        String[] columnNames = {"Nazwa", "Ilość", "Cena jedn.", "Cena razem"};
        tableModel.setColumnIdentifiers(columnNames);
        TableOfListOfBoughtProducts.setModel(tableModel);
        TableOfListOfBoughtProducts.setEnabled(false);

        if (TableOfListOfBoughtProducts.getRowCount() > 0) {
            TableOfListOfBoughtProducts.setRowSelectionInterval(0, 0);
        }
    }

    private void addDeleteButtonListener() {
        DeleteButton.addActionListener(e -> {
            int selectedRow = TableOfListOfBoughtProducts.getSelectedRow();
            if (selectedRow != -1) {
                DefaultTableModel model = (DefaultTableModel) TableOfListOfBoughtProducts.getModel();

                String priceToRemoveStr = model.getValueAt(selectedRow, 3).toString().replace(",", ".");
                double priceToRemove = Double.parseDouble(priceToRemoveStr);

                String totalStr = sumOfProducts.getText().replace("Razem: ", "").replace(" zł", "").replace(",", ".");
                double total = Double.parseDouble(totalStr);
                total = total - priceToRemove;
                sumOfProducts.setText("Razem: " + String.format("%.2f", total) + " zł");

                model.removeRow(selectedRow);
                if (model.getRowCount() > 0) {
                    if (selectedRow == model.getRowCount()) {
                        TableOfListOfBoughtProducts.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
                    } else {
                        TableOfListOfBoughtProducts.setRowSelectionInterval(selectedRow, selectedRow);
                    }
                }
            }
        });
    }

    private void addScrollUpListener() {
        buttonUp.addActionListener(e -> {
            int selectedRow = TableOfListOfBoughtProducts.getSelectedRow();
            if (selectedRow > 0) {
                TableOfListOfBoughtProducts.setRowSelectionInterval(selectedRow - 1, selectedRow - 1);
            }
        });
    }

    private void addScrollDownListener() {
        ButtonDown.addActionListener(e -> {
            int selectedRow = TableOfListOfBoughtProducts.getSelectedRow();
            if (selectedRow < TableOfListOfBoughtProducts.getRowCount() - 1) {
                TableOfListOfBoughtProducts.setRowSelectionInterval(selectedRow + 1, selectedRow + 1);
            }
        });
    }

    private void quantityButtonListener() {
        quantityButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    long productId = Long.parseLong(productIdTextArea.getText());
                    Storage storage = Application.getStorage();
                    Product product = storage.getProductById(productId);
                    if (product != null) {
                        String productName = product.getName();
                        double productPrice = product.getGrossPrice();

                        popup = new JDialog();
                        popup.setTitle("Wpisz ilość:");
                        popup.setLayout(new BorderLayout());

                        JTextField quantityField = new JTextField();

                        quantityField.setBorder(BorderFactory.createCompoundBorder(
                                quantityField.getBorder(),
                                BorderFactory.createEmptyBorder(5, 5, 5, 20))); // top, left, bottom, right
                        popup.add(quantityField, BorderLayout.NORTH);

                        JPanel numberPanel = new JPanel(new GridLayout(4, 3));
                        popup.add(numberPanel, BorderLayout.CENTER);

                        for (int i = 1; i <= 9; i++) {
                            JButton button = new JButton(String.valueOf(i));
                            button.setName("dialogButton" + i); // nadajemy unikalną nazwę przyciskowi
                            button.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    quantityField.setText(quantityField.getText() + button.getText());
                                }
                            });
                            numberPanel.add(button);
                        }

                        JButton zeroButton = new JButton("0");
                        zeroButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                quantityField.setText(quantityField.getText() + zeroButton.getText());
                            }
                        });
                        numberPanel.add(zeroButton);

                        JButton dotButton = new JButton(".");
                        dotButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                String currentText = quantityField.getText();
                                if (!currentText.contains(".")) {
                                    quantityField.setText(currentText + dotButton.getText());
                                }
                            }
                        });
                        numberPanel.add(dotButton);

                        // Create a panel for zero and enter buttons
                        JPanel southPanel = new JPanel(new BorderLayout());
                        popup.add(southPanel, BorderLayout.SOUTH);



                        // Create a panel for enter and cancel buttons
                        JPanel enterCancelPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                        southPanel.add(enterCancelPanel, BorderLayout.SOUTH);

                        // Create enter button
                        JButton enterButton = new JButton("Enter");
                        enterButton.setName("dialogButtonEnter");
                        enterButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                double quantity = Double.parseDouble(quantityField.getText());
                                addAndSumProduct(productName, quantity, productPrice);
                                popup.dispose();
                            }
                        });
                        enterCancelPanel.add(enterButton);

                        // Create cancel button
                        JButton cancelButton = new JButton("Anuluj");
                        cancelButton.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                popup.dispose();
                            }
                        });
                        enterCancelPanel.add(cancelButton);

                        popup.pack();
                        popup.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(null, "Nie ma takiego produktu");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Nie ma takiego produktu");
                }
                productIdTextArea.setText("");
            }
        });
    }

    private void enterButtonListener() {
        ENTERButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    long productId = Long.parseLong(productIdTextArea.getText());
                    Storage storage = Application.getStorage();
                    Product product = storage.getProductById(productId);
                    if (product != null) {
                        String productName = product.getName();
                        double productPrice = product.getGrossPrice();
                        double quantity = 1;
                        addAndSumProduct(productName, quantity, productPrice);
                    } else {
                        JOptionPane.showMessageDialog(null, "Nie ma takiego produktu");
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Nie ma takiego produktu");
                }
                productIdTextArea.setText("");
            }
        });
    }

    private void addAndSumProduct(String productName, double quantity, double productPrice) {
        DefaultTableModel model = (DefaultTableModel) TableOfListOfBoughtProducts.getModel();
        model.addRow(new Object[]{productName, quantity, productPrice, productPrice * quantity});

        double total = 0;
        for (int i = 0; i < model.getRowCount(); i++) {
            total += (double) model.getValueAt(i, 3);
        }
        sumOfProducts.setText("Razem: " + String.format("%.2f", total) + " zł");

        if (TableOfListOfBoughtProducts.getSelectedRow() == -1 && TableOfListOfBoughtProducts.getRowCount() > 0) {
            TableOfListOfBoughtProducts.setRowSelectionInterval(0, 0);
        }
    }

    private void exitButtonListener() {
        exitButton.addActionListener(e -> {
            DefaultTableModel model = (DefaultTableModel) TableOfListOfBoughtProducts.getModel();
            model.setRowCount(0);
            sumOfProducts.setText("Razem: ");
            switchToPanel(mainMenuPanel.getPanel());
        });
    }

    private void backspaceAndCButtonListener() {
        BackspaceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = productIdTextArea.getText();
                if (!currentText.isEmpty()) {
                    productIdTextArea.setText(currentText.substring(0, currentText.length() - 1));
                }
            }
        });

        cButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productIdTextArea.setText("");
            }
        });
    }

    private void commaButtonListener() {
        commaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String currentText = productIdTextArea.getText();
                if (!currentText.isEmpty() && !currentText.endsWith(".") && !currentText.contains(".")) {
                    productIdTextArea.append(commaButton.getText());
                }
            }
        });
    }

    private void numbersButtonListener() {
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productIdTextArea.append(button1.getText());
            }
        });

        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productIdTextArea.append(button2.getText());
            }
        });

        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productIdTextArea.append(button3.getText());
            }
        });

        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productIdTextArea.append(button4.getText());
            }
        });

        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productIdTextArea.append(button5.getText());
            }
        });

        button6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productIdTextArea.append(button6.getText());
            }
        });

        button7.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productIdTextArea.append(button7.getText());
            }
        });

        button8.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productIdTextArea.append(button8.getText());
            }
        });

        button9.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productIdTextArea.append(button9.getText());
            }
        });

        Button0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                productIdTextArea.append(Button0.getText());
            }
        });
    }
}
