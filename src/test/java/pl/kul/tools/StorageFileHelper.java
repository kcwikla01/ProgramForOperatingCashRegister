package pl.kul.tools;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class StorageFileHelper {
    public static void createStorageFileWithLines(Path storageFilePath, List<String> lines) {
        try {
            Files.write(storageFilePath, lines);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}
