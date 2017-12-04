import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameMenu extends JPanel implements ActionListener {
  /* Declarations */
  private static JFrame gameWindow;
  private static JButton createLobby;
  private static JButton joinLobby;
  private static JButton help;
  private static JLabel gameName;
  private static JLabel playerNameLabel;
  private static JLabel ipLabel;
  private static JLabel portLabel;
  private static ButtonGroup classSelector;
  private static JTextField playerName;
  private static JTextField ip;
  private static JTextField port;

  // final values
  private static final int FRAME_HEIGHT = 800;
  private static final int FRAME_WIDTH = 800;

  public static void main(String[] args) {
    //call the constructor to initialize class
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        new GameMenu();
      }
    });
  }

  //constructor
  public GameMenu() {
    setLayout(null);
    setVisible(true);

    gameWindow = new JFrame("Ninjas & Samurai");
    gameWindow.setSize(GameMenu.FRAME_WIDTH, GameMenu.FRAME_HEIGHT);
    gameWindow.setResizable(false);
    gameWindow.setFocusable(true);
    gameWindow.setIconImage(
      (new ImageIcon("../BattleCity Demo/Tank/Tank.png")).getImage());
    this.setBackground(Color.BLACK);
    gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // gameWindow.pack();
    gameWindow.setVisible(true);

    //initialize misc
    classSelector = new ButtonGroup();

    //initialize components
    playerName = new JTextField("Player");
    ip = new JTextField("127.0.0.1");
    port = new JTextField("3000");
    createLobby = new JButton("Create a Lobby");
    joinLobby = new JButton("Join a Lobby");
    help = new JButton("Help");
    gameName = new JLabel("Ninjas & Samurai");
    playerNameLabel = new JLabel("Enter Player Name: ");
    ipLabel = new JLabel("Enter IP: ");
    portLabel = new JLabel("Enter Port: ");

    //place components
    playerName.setBounds(400, 300, 150, 30);
    ip.setBounds(260, 350, 100, 30);
    port.setBounds(475, 350, 100, 30);
    createLobby.setBounds(300, 500, 200, 40);
    joinLobby.setBounds(300, 550, 200, 40);
    help.setBounds(300, 600, 200, 40);
    gameName.setBounds(220, 200, 400, 60);
    gameName.setFont(new Font("Serif", Font.PLAIN, 40));
    gameName.setForeground(Color.WHITE); 
    playerNameLabel.setBounds(250, 300, 150, 30);
    playerNameLabel.setForeground(Color.WHITE);
    ipLabel.setBounds(190, 350, 100, 30);
    ipLabel.setForeground(Color.WHITE);
    portLabel.setBounds(390, 350, 100, 30);
    portLabel.setForeground(Color.WHITE);

    //add actionlistener to buttons
    createLobby.addActionListener(this);
    joinLobby.addActionListener(this);
    help.addActionListener(this);

    //add components to panel
    super.add(playerName);
    super.add(ip);
    super.add(port);
    super.add(createLobby);
    super.add(joinLobby);
    super.add(help);
    super.add(gameName);
    super.add(playerNameLabel);
    super.add(ipLabel);
    super.add(portLabel);

    //add the JPanel into the JFrame
    gameWindow.getContentPane().add(this);
    System.out.println(gameWindow);
  }

  public void actionPerformed(ActionEvent e) {
    System.out.println(e.getActionCommand());
    super.invalidate();
    super.setVisible(false);
    super.removeAll();
    gameWindow.getContentPane().remove(this);

    //instantiate UDP here
    if(e.getActionCommand() == "Create a Lobby"){
      LobbyServer serverMenu = new LobbyServer(e.getActionCommand(),
        playerName.getText(), ip.getText(), port.getText());
      classSelector.add(serverMenu.ninjaSelector);
      classSelector.add(serverMenu.samuraiSelector);
      gameWindow.getContentPane().add(serverMenu);
    }else if(e.getActionCommand() == "Join a Lobby") {
      LobbyServer serverMenu = new LobbyServer(e.getActionCommand(), 
        playerName.getText(), ip.getText(), port.getText());
      classSelector.add(serverMenu.ninjaSelector);
      classSelector.add(serverMenu.samuraiSelector);
      gameWindow.getContentPane().add(serverMenu);
    }
  }


  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
  }
  
}