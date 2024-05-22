package projet_javaTextEditor;

import java.io.FileWriter;
import java.io.IOException;


public class AutoSaveObserver implements TextObserver {
    private String autoSaveFile;

    public AutoSaveObserver(String autoSaveFile) {
        this.autoSaveFile = autoSaveFile;
    }

    @Override
    public void update(String newText) {
        try (FileWriter writer = new FileWriter(autoSaveFile)) {
        	System.out.println("Auto saving");
            writer.write(newText);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



