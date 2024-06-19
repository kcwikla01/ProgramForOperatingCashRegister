package pl.kul;

import lombok.Getter;
import lombok.Setter;
import pl.kul.facilities.Storage;
import pl.kul.gui.MainMenu;
import pl.kul.tools.StorageUtils;

import javax.swing.*;

public class Application {

    @Getter
    @Setter
    private static Storage storage;


    public void run(){
        if(storage == null)
            storage = new StorageUtils().setupStorage("storage.csv");

        SwingUtilities.invokeLater(MainMenu::new);

    }

    public static void main(String[] args) {
        new Application().run();
    }
}
