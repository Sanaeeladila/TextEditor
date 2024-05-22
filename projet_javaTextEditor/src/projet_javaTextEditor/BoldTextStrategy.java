package projet_javaTextEditor;

import java.awt.Font;
import javax.swing.JTextArea;



public class BoldTextStrategy implements TextFormattingStrategy {

	@Override
    public void applyStrategy(JTextArea textArea) {
        int start = textArea.getSelectionStart();
        int end = textArea.getSelectionEnd();
        
        if (start != end) {
            textArea.setFont(textArea.getFont().deriveFont(Font.BOLD));
        }
    }
	
    @Override
    public String toString() {
        return "Bold";
    }

}


