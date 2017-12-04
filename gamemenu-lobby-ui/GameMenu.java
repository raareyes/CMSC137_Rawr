
import java.io.IOException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;

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
  private static Runnable runnable;
  private static Runnable runnable2;
  private static JPanel classSelectorContainer;
  public static JRadioButton ninjaSelector;
  public static JRadioButton samuraiSelector;
  public static int type =15;

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

    gameWindow = new JFrame("Shinobi & Bushido");
    gameWindow.setSize(GameMenu.FRAME_WIDTH, GameMenu.FRAME_HEIGHT);
    gameWindow.setResizable(false);
    gameWindow.setFocusable(true);
    gameWindow.setIconImage(
      (new ImageIcon("Weapons/Sword/WeaponDown.png")).getImage());
    this.setBackground(Color.BLACK);
    gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    // gameWindow.pack();
    gameWindow.setVisible(true);

    //initialize misc
    classSelector = new ButtonGroup();
    classSelectorContainer = new JPanel();

    //initialize components
    playerName = new JTextField("Player");
    ip = new JTextField("127.0.0.1");
    port = new JTextField("3000");
    createLobby = new JButton("Create a Lobby");
    joinLobby = new JButton("Join Game");
    help = new JButton("Help");
    gameName = new JLabel("Shinobi & Bushido");
    playerNameLabel = new JLabel("Enter Player Name: ");
    ipLabel = new JLabel("Enter IP: ");
    portLabel = new JLabel("Enter Port: ");
    ninjaSelector = new JRadioButton("Ninja");
    samuraiSelector = new JRadioButton("Samurai");

    //place components
    classSelectorContainer
        .setBorder(BorderFactory.createTitledBorder(BorderFactory.
        createEtchedBorder(), "Select Class"));
    classSelectorContainer.setBounds(330, 395, 150, 80);
    classSelectorContainer.setVisible(true);
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
    ninjaSelector.setActionCommand("Ninja");
    samuraiSelector.setActionCommand("Samurai");
    ninjaSelector.setPreferredSize(new Dimension(100, 20));
    samuraiSelector.setPreferredSize(new Dimension(100, 20));


    classSelectorContainer.add(ninjaSelector);
    classSelectorContainer.add(samuraiSelector);

    //add actionlistener to buttons
    createLobby.addActionListener(this);
    joinLobby.addActionListener(this);
    help.addActionListener(this);
    ninjaSelector.addActionListener(this);
    samuraiSelector.addActionListener(this);

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
    super.add(classSelectorContainer);


    //add the JPanel into the JFrame
    gameWindow.getContentPane().add(this);
  }

  public void actionPerformed(ActionEvent e) {
    if(e.getSource() instanceof JRadioButton){
          JRadioButton radioButton = (JRadioButton) e.getSource();
          if(radioButton.isSelected()){
               if (radioButton.getText().equals("Ninja")){
                GameMenu.type = 15;
                GameMenu.samuraiSelector.setSelected(false);
               }
                else if (radioButton.getText().equals("Samurai")){
                  GameMenu.type = 25;
                  GameMenu.ninjaSelector.setSelected(false);
                }
          }
          return;
      }


    System.out.println(e.getActionCommand());
    super.invalidate();
    super.setVisible(false);
    super.removeAll();
    gameWindow.getContentPane().remove(this);

    //instantiate UDP here
    if(e.getActionCommand() == "Create a Lobby"){
      try {
      UDPServer server = new UDPServer(playerName.getText(),99,Integer.parseInt(port.getText())+1,3);
      LobbyServer serverMenu = new LobbyServer(e.getActionCommand(),
        playerName.getText(), InetAddress.getLocalHost().getHostAddress().toString(), port.getText(),true,server,null);
      classSelector.add(ninjaSelector);
      classSelector.add(samuraiSelector);
      gameWindow.getContentPane().add(serverMenu);

      int tcpPort = Integer.parseInt(port.getText());
      Server chatServer = new Server(tcpPort);

      runnable = new Runnable(){
      
        @Override
        public void run() {
          try {
            chatServer.run();
          } catch(Exception event) {}
        }
      };

      Thread tcp = new Thread(runnable);
      tcp.start();
      }
      catch(IOException j) {}
      catch(Exception l){}
    }else if(e.getActionCommand() == "Join Game") {
      String[] args = {ip.getText(),playerName.getText(),(Integer.parseInt(port.getText())+1)+"", GameMenu.type+""};
      JFrame frame = new JFrame("Shinobi & Bushido");;
      frame.setSize(600,600);
      //when we add this option to true (resizable), 
      //we should also be able to dynamically change the size of the window
      frame.setResizable(false);          
      frame.setFocusable(true);
      frame.setIconImage((new ImageIcon ("Weapons/Sword/WeaponDown.png")).getImage());
      LobbyServer serverMenu = new LobbyServer(e.getActionCommand(), 
        playerName.getText(), ip.getText(), port.getText(),false,null,new Paint(args[0],args[1],Integer.parseInt(args[2]),Integer.parseInt(args[3]),frame));
      classSelector.add(ninjaSelector);
      classSelector.add(samuraiSelector);
      //gameWindow.getContentPane().add(serverMenu);

      int tcpPort = Integer.parseInt(port.getText());
      Client chatClient = new Client(ip.getText(), tcpPort, playerName.getText());
      runnable2 = new Runnable() {

        @Override
        public void run() {
          try {
            chatClient.run();
          } catch(Exception event) {
            System.out.println(event);
          }
        }
      };

      Thread tcpClientThread = new Thread(runnable2);
      tcpClientThread.start();
    }
  }


  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
  }
  
}