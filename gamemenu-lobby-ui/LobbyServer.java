import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LobbyServer extends JPanel implements ActionListener {
  /* Declarations */
  private static JButton startGame;
  private static JButton connect;
  private static JLabel ipField;
  private static JLabel portField;
  public static JRadioButton ninjaSelector;
  public static JRadioButton samuraiSelector;
  private static JComboBox<String> lives;
  private static JComboBox<String> wins;
  private static JPanel classSelectorContainer;
  private static JPanel gameSettings;
  private static JPanel chatContainer;
  private static JTextField ip;
  private static JTextField port;
  private static Connections connections;

  //insert instance of chat with ui chat here

  /* final values */

  public static void main(String[] args) {
    //call the constructor to initialize class
  }

  // public LobbyServer(int string) {
  public LobbyServer(String mode){
    //initializations
    super.setLayout(null);
    classSelectorContainer = new JPanel();
    gameSettings = new JPanel();
    chatContainer = new JPanel();
    startGame = new JButton("Start Game!");
    connect = new JButton("Connect");
    if(mode == "Create a Lobby"){
      ip = new JTextField("127.0.0.1");
    } else {
      ip = new JTextField();
    }
    port = new JTextField();
    ipField = new JLabel("Enter IP");
    portField = new JLabel("Enter Port");
    connections = new Connections();

    /* instantiate components */

    //class selector
    ninjaSelector = new JRadioButton("Ninja");
    samuraiSelector = new JRadioButton("Samurai");
    classSelectorContainer.setBorder(BorderFactory
      .createTitledBorder(BorderFactory.createEtchedBorder(), "Select Class"));
    //game settings
    lives = new JComboBox<String> (new String[] { "3", "4", "5" });
    wins = new JComboBox<String>(new String[] {"3", "4", "5"});
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
    startGame.setBounds(470, 660, 200, 40);
    ip.setBounds(40, 100, 150, 30);
    port.setBounds(220, 100, 150, 30);
    ipField.setLocation(90, 70);
    ipField.setSize(60, 30);
    portField.setLocation(260, 70);
    portField.setSize(90, 30);
    connect.setBounds(130, 140, 150, 30);
    connections.setBounds(40, 200, 350, 350);

    //customize JPanels
    classSelectorContainer.setBounds(400, 95, 150, 80);
    classSelectorContainer.setVisible(true);
    gameSettings.setBounds(400, 200, 150, 80);
    gameSettings.setVisible(true);
    chatContainer.setBackground(Color.BLACK);
    chatContainer.setBounds(400, 300, 350, 350);
    chatContainer.setVisible(true);

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
    startGame.addActionListener(this);

    // add to cosntainers
    super.add(classSelectorContainer);
    super.add(gameSettings);
    super.add(chatContainer);
    super.add(startGame);
    super.add(ip);
    super.add(port);
    super.add(ipField);
    super.add(portField);
    super.add(connect);
    super.add(connections);

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


/* 
 * references 
 * - java docs
*/