package pl.kul.gui;

import lombok.Getter;
import pl.kul.Application;
import pl.kul.tools.PhysicalInventoryGenerator;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@Getter
public final class MainMenu implements MenuPanel {
    private JPanel panel;
    private final StoragePanel storagePanel;
    private final CashRegisterPanel cashRegisterPanel;

    private JButton cashRegisterButton;
    private JButton storageButton;
    private JButton exitButton;

    public MainMenu() {
        storagePanel = new StoragePanel(MainMenu.this);
        cashRegisterPanel = new CashRegisterPanel(MainMenu.this);

        addListeners();
        setupMenuFrame();
    }

    private void setupMenuFrame() {
        frame.setContentPane(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void addListeners() {
        addPanelSwitchToButton(storagePanel, storageButton);
        addPanelSwitchToButton(cashRegisterPanel, cashRegisterButton);
        addExitListener();
        addWindowListener();
    }

    private void addWindowListener() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                PhysicalInventoryGenerator physicalInventoryGenerator = new PhysicalInventoryGenerator();
                physicalInventoryGenerator.writeInventoryToFile(Application.getStorage(), "storage.csv");
                super.windowClosing(e);
            }
        });
    }

    private void addExitListener() {
        exitButton.addActionListener(e -> frame.dispose());
    }
}
