package pl.kul.tools;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.time.LocalDate;

public class FIleChooser {
    public static String getFilePathFromUser(String suffix, String prefix) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Zapisz inwentaryzację");

        String defaultFileName = prefix+"_" + LocalDate.now() +"."+ suffix;
        fileChooser.setSelectedFile(new File(defaultFileName));

        FileNameExtensionFilter filter = new FileNameExtensionFilter("Pliki "+suffix.toUpperCase(), suffix);
        fileChooser.setFileFilter(filter);

        int userSelection = fileChooser.showSaveDialog(null);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            String filePath = fileChooser.getSelectedFile().getAbsolutePath();
            if (!filePath.toLowerCase().endsWith("."+suffix)) {
                int lastDotIndex = filePath.lastIndexOf(".");
                if (lastDotIndex != -1) {
                    filePath = filePath.substring(0, lastDotIndex);
                }
                filePath += "."+suffix;
            }
            return filePath;
        }
        return null;
    }
}
