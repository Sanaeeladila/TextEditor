package projet_javaTextEditor;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTextArea;


public class UnderlineTextStrategy implements TextFormattingStrategy {
	
    @Override
    public void applyStrategy(JTextArea textArea) {
        int start = textArea.getSelectionStart();
        int end = textArea.getSelectionEnd();
        
        if (start != end) {
            Font currentFont = textArea.getFont();
            Map<TextAttribute, Object> attributes = new HashMap<>(currentFont.getAttributes());
            attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
            Font newFont = new Font(attributes);
            textArea.setFont(newFont);
        }
    }
    
    @Override
    public String toString() {
        return "Underline";
    }	
    
}