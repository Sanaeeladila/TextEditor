package projet_javaTextEditor;

import java.util.HashMap;
import java.util.Map;


public class CharacterFlyweightFactory {
	
    private Map<Character, ICharacterFlyweight> characters = new HashMap<>();

    public ICharacterFlyweight getCharacterFlyweight(char character) {
        if (!characters.containsKey(character)) {
            characters.put(character, new CharacterFlyweight(character));
        }
        return characters.get(character);
    }
}

