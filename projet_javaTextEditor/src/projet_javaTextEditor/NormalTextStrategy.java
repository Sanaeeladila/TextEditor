package projet_javaTextEditor;

import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTextArea;


public class NormalTextStrategy implements TextFormattingStrategy {

	@Override
	public void applyStrategy(JTextArea textArea) {
        int start = textArea.getSelectionStart();
        int end = textArea.getSelectionEnd();
        
        if (start != end) {
            // Reset any previous formatting
            textArea.setFont(textArea.getFont().deriveFont(Font.PLAIN));
            
            // Remove underline if present
            Map<TextAttribute, Object> attributes = new HashMap<>(textArea.getFont().getAttributes());
            attributes.remove(TextAttribute.UNDERLINE);
            Font newFont = new Font(attributes);
            textArea.setFont(newFont);
        }
    }
	
	@Override
    public String toString() {
        return "Normal";
    }
	
}

