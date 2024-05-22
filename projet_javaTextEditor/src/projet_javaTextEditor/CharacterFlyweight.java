package projet_javaTextEditor;

import javax.swing.JTextArea;


public class CharacterFlyweight implements ICharacterFlyweight {
    private char character;

    public CharacterFlyweight(char character) {
        this.character = character;
    }

    @Override
    public void display(JTextArea textArea) {
        textArea.append(String.valueOf(character)); // Ajout du caractère à la JTextArea
    }
}