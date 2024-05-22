package projet_javaTextEditor;

public interface Observable {
	
	void addObserver(TextObserver observer);
    void removeObserver(TextObserver observer);
    void notifyObservers(String newText);
    
}


