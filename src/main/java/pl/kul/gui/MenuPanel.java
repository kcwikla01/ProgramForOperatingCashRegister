package pl.kul.gui;

import javax.swing.*;

public sealed interface MenuPanel permits MainMenu, CashRegisterPanel, StoragePanel, ProductPanel {
    JPanel panel = null;
    JFrame frame = new JFrame("KACMIL-POL");

    default JPanel getPanel() {
        return panel;
    }

    default void addPanelSwitchToButton(MenuPanel panel, JButton button) {
        button.addActionListener(e -> {
            switchToPanel(panel.getPanel());
        });
    }

    default void switchToPanel(JPanel panel) {
        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.revalidate();
    }
}
