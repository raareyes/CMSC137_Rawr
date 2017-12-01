import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LobbyServer extends JPanel implements ActionListener {
  /* Declarations */
  private static JButton startGame;
  public static JRadioButton ninjaSelector;
  public static JRadioButton samuraiSelector;
  private static JComboBox<String> lives;
  private static JComboBox<String> wins;
  private static JTextArea chatArea;
  private static JPanel classSelectorContainer;
  private static JPanel gameSettings;

  /* final values */

  public static void main(String[] args) {
    //call the constructor to initialize class
    new LobbyServer().getPanel();
  }

  public LobbyServer() {
    super.setLayout(null);

    //instantiate components
    ninjaSelector = new JRadioButton("Ninja");
    samuraiSelector = new JRadioButton("Samurai");
    classSelectorContainer = new JPanel();
    classSelectorContainer.setBorder(BorderFactory
      .createTitledBorder(BorderFactory.createEtchedBorder(), "Select Class"));
    lives = new JComboBox<String> (new String[] { "3", "4", "5" });
    wins = new JComboBox<String>(new String[] {"3", "4", "5"});
    gameSettings = new JPanel();
    gameSettings.setBorder(BorderFactory
      .createTitledBorder(BorderFactory.createEtchedBorder(), "Game Settings"));

    //set component properties 
    ninjaSelector.setActionCommand("Ninja");
    samuraiSelector.setActionCommand("Samurai");
    ninjaSelector.setPreferredSize(new Dimension(100, 20));
    samuraiSelector.setPreferredSize(new Dimension(100, 20));
    lives.setActionCommand("lives setting");
    lives.setEditable(true);
    wins.setActionCommand("wins setting");
    wins.setEditable(true);
    lives.setPreferredSize(new Dimension(100, 20));
    wins.setPreferredSize(new Dimension(100, 20));

    //customize JPanels
    classSelectorContainer.setBounds(400, 95, 150, 80);
    classSelectorContainer.setVisible(true);
    gameSettings.setBounds(400, 200, 150, 80);
    gameSettings.setVisible(true);

    //place components
    classSelectorContainer.add(ninjaSelector);
    classSelectorContainer.add(samuraiSelector);
    gameSettings.add(lives);
    gameSettings.add(wins);

    //add action listeners
    ninjaSelector.addActionListener(this);
    samuraiSelector.addActionListener(this);
    lives.addActionListener(this);
    wins.addActionListener(this);
    
    // add to containers


    super.add(classSelectorContainer);
    super.add(gameSettings);
    // super.add(ninjaSelector);
    // super.add(samuraiSelector);
  }

  public JPanel getPanel() {
    return this;
  }

  public void actionPerformed(ActionEvent e) {
    System.out.println(e.getActionCommand());
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
  }
}