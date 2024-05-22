package projet_javaTextEditor;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;


public class TextEditor extends JFrame implements ActionListener, Observable {
    private JTextArea textArea;
    private JScrollPane scrollPane;
    private JLabel fontLabel;
    private JSpinner fontSizeSpinner;
    private JButton fontColorButton;
    private JComboBox fontBox;
    
    
    JComboBox<TextFormattingStrategy> strategyBox;
   
    // Observateur pour l'enregistrement automatique
    private AutoSaveObserver autoSaveObserver;
    
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openItem;
    private JMenuItem saveItem;
    private JMenuItem exitItem;

    private CharacterFlyweightFactory flyweightFactory;

    TextEditor() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Text Editor");
        this.setSize(700, 700);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);

        flyweightFactory = new CharacterFlyweightFactory();  // Factory pour les caractères flyweight

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 20));

        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(650, 650));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        fontLabel = new JLabel("Font: ");

        fontSizeSpinner = new JSpinner();
        fontSizeSpinner.setPreferredSize(new Dimension(50, 25));
        fontSizeSpinner.setValue(20);
        fontSizeSpinner.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Font currentFont = textArea.getFont();
                textArea.setFont(new Font(currentFont.getFamily(), currentFont.getStyle(), (int) fontSizeSpinner.getValue()));
            }
        });

        fontColorButton = new JButton("Color");
        fontColorButton.addActionListener(this);

        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

        fontBox = new JComboBox(fonts);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Arial");
        
       
        // ComboBox pour sélectionner la stratégie
        strategyBox = new JComboBox<>();
        strategyBox.addItem(new BoldTextStrategy());
        strategyBox.addItem(new ItalicTextStrategy());
        strategyBox.addItem(new UnderlineTextStrategy());
        strategyBox.addItem(new NormalTextStrategy());
        strategyBox.addActionListener(this);        
        
        // ----- menubar -----

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");
        openItem = new JMenuItem("Open");
        saveItem = new JMenuItem("Save");
        exitItem = new JMenuItem("Exit");
        

        openItem.addActionListener(this);
        saveItem.addActionListener(this);
        exitItem.addActionListener(this);

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);
        menuBar.add(fileMenu);
        
        // ----- /menubar -----
        this.setJMenuBar(menuBar);
        this.add(fontLabel);
        this.add(fontSizeSpinner);
        this.add(fontColorButton);
        this.add(fontBox);
        this.add(strategyBox); // Ajout de la liste déroulante de la stratégie
        this.add(scrollPane);
        this.setVisible(true);
        
        // Ajout d'un écouteur d'événements pour détecter les modifications du texte
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                setText(textArea.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                setText(textArea.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                // Ne s'applique pas aux changements de style
            }
        });
        
        // Création de l'observateur pour l'enregistrement automatique
        autoSaveObserver = new AutoSaveObserver("autosave.txt");
        addObserver(autoSaveObserver); // Ajout de l'observateur à la liste
   
    }
         
    
    // Méthode pour ajouter un caractère au texte
    public void addCharacter(char character) {
        ICharacterFlyweight flyweight = flyweightFactory.getCharacterFlyweight(character);
        flyweight.display(textArea); // Passer le JTextArea pour afficher le caractère
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==fontColorButton) {
            JColorChooser colorChooser = new JColorChooser();
            Color color = colorChooser.showDialog(null, "Choose a color", Color.black);
            textArea.setForeground(color);
        }
        if(e.getSource()==fontBox) {
            textArea.setFont(new Font((String)fontBox.getSelectedItem(),Font.PLAIN,textArea.getFont().getSize()));
        }
        
        if (e.getSource() == strategyBox) { // L'utilisateur a sélectionné une nouvelle stratégie
            TextFormattingStrategy selectedStrategy = (TextFormattingStrategy) strategyBox.getSelectedItem();
            applyFormatting(selectedStrategy); // Appliquer la stratégie sélectionnée
        }
          
        if(e.getSource()==openItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text files", "txt");
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showOpenDialog(null);

            if(response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;

                try {
                    fileIn = new Scanner(file);
                    if(file.isFile()) {
                        while(fileIn.hasNextLine()) {
                            String line = fileIn.nextLine()+"\n";
                            textArea.append(line);
                        }
                    }
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } finally {
                    if (fileIn != null) {
                        fileIn.close();
                    }
                }
            }
        }
        if(e.getSource()==saveItem) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));

            int response = fileChooser.showSaveDialog(null);

            if(response == JFileChooser.APPROVE_OPTION) {
                File file;
                PrintWriter fileOut = null;

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    fileOut = new PrintWriter(file);
                    fileOut.println(textArea.getText());
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                } finally {
                    if (fileOut != null) {
                        fileOut.close();
                    }
                }
            }
        }
        if(e.getSource()==exitItem) {
            System.exit(0);
        }
    }
    
    private void applyFormatting(TextFormattingStrategy strategy) {
        // Récupérer les positions de début et de fin de la sélection
        int start = textArea.getSelectionStart();
        int end = textArea.getSelectionEnd();
        // Appliquer la stratégie de mise en forme au texte sélectionné
        if (start != end) { // Assurez-vous qu'il y a du texte sélectionné
            // Utiliser la stratégie de mise en forme appropriée
            strategy.applyStrategy(textArea);
        }
    }
    
    
    
 /* ----------------------Observer pattern design----------------------*/
    
    private java.util.List<TextObserver> observers = new java.util.ArrayList<>();
    private String text = "";

    public void addObserver(TextObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TextObserver observer) {
        observers.remove(observer);
    }

    public void setText(String newText) {
        this.text = newText;
        notifyObservers(newText);
    }

    public void notifyObservers(String newText) {
        for (TextObserver observer : observers) {
            observer.update(newText);
        }
    }

   

}
