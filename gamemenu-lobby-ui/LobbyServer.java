import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LobbyServer extends JPanel implements ActionListener {
  /* Declarations */
  private static JButton startGame;
  private static JLabel ipField;
  private static JLabel portField;
  private JLabel playerName;
  public static JRadioButton ninjaSelector;
  public static JRadioButton samuraiSelector;
  private static JComboBox<String> lives;
  private static JComboBox<String> wins;
  private static JPanel classSelectorContainer;
  private static JPanel gameSettings;
  private static JPanel chatContainer;
  private JTextField ip;
  private JTextField port;
  private JTextField nickName;
  private static Connections connections;

  //insert instance of chat with ui chat here

  /* final values */
  UDPServer server;
  public static void main(String[] args) {
    //call the constructor to initialize class

  }

  // public LobbyServer(int string) {
  public LobbyServer(String mode, String playerName, String ip, String port, boolean host, UDPServer server, Paint game){
    //initializations
    // LobbyServer.playerName = playerName;
    this.server = server;
    super.setLayout(null);
    classSelectorContainer = new JPanel();
    gameSettings = new JPanel();
    chatContainer = new JPanel();
    startGame = new JButton("Start Game!");
    this.ip = new JTextField(ip);
    this.port = new JTextField(port);
    this.nickName = new JTextField(playerName);
    ipField = new JLabel("User IP");
    portField = new JLabel("User Port");
    this.playerName = new JLabel("Player Name: ");
    connections = new Connections();

    /* instantiate components */

    //class selector
    ninjaSelector = new JRadioButton("Ninja");
    samuraiSelector = new JRadioButton("Samurai");
    classSelectorContainer.setBorder(BorderFactory
      .createTitledBorder(BorderFactory.createEtchedBorder(), "Select Class"));
    //game settings
    if (host){
      lives = new JComboBox<String> (new String[] { "3", "4", "5" });
    wins = new JComboBox<String>(new String[] {"3", "4", "5"});
      gameSettings.setBorder(BorderFactory
        .createTitledBorder(BorderFactory.createEtchedBorder(), "Game Settings"));
    }


    //set component properties 
    ninjaSelector.setActionCommand("Ninja");
    samuraiSelector.setActionCommand("Samurai");
    ninjaSelector.setPreferredSize(new Dimension(100, 20));
    samuraiSelector.setPreferredSize(new Dimension(100, 20));
    if (host){
      lives.setActionCommand("lives setting");
      lives.setEditable(true);
      wins.setActionCommand("wins setting");
      wins.setEditable(true);
      lives.setPreferredSize(new Dimension(100, 20));
      wins.setPreferredSize(new Dimension(100, 20));
      startGame.setBounds(470, 660, 200, 40);
    }
    this.ip.setBounds(40, 100, 150, 30);
    this.ip.setEditable(false);
    this.port.setBounds(220, 100, 150, 30);
    this.port.setEditable(false);
    this.nickName.setBounds(140, 135, 150, 30);
    this.nickName.setEditable(false);
    ipField.setLocation(90, 70);
    ipField.setSize(60, 30);
    portField.setLocation(260, 70);
    portField.setSize(90, 30);
    this.playerName.setLocation(40, 135);
    this.playerName.setSize(150, 30);
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
    if (host){
      gameSettings.add(lives);
      gameSettings.add(wins);
    }

    //add action listeners
    ninjaSelector.addActionListener(this);
    samuraiSelector.addActionListener(this);
    if (host){
      lives.addActionListener(this);
      wins.addActionListener(this);
      startGame.addActionListener(this);
    }

    // add to cosntainers
    super.add(classSelectorContainer);
    super.add(gameSettings);
    super.add(chatContainer);
    super.add(startGame);
    super.add(this.ip);
    super.add(this.port);
    super.add(this.nickName);
    super.add(ipField);
    super.add(portField);
    super.add(this.playerName);
    super.add(connections);

    if (!host){
      game.connect();
    }

  }

  public JPanel getPanel() {
    return this;
  }

  //TODO: intanstiate game here
  public void actionPerformed(ActionEvent e) {
    System.out.println(e.getActionCommand());
    if(e.getActionCommand() == "Start Game!") {
      //server.setLives((Integer)LobbyServer.lives.getSelectedItem());
      UDPServer.isStarting = true;
    }
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
  }

  public String[] getConnectionDetails(){
    String[] connection = new String[2];
    connection[0] = ip.getText();
    connection[1] = ip.getText();

    return connection;
  }
}


/* 
 * references 
 * - java docs
*/