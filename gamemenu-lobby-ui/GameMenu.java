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
  private static ButtonGroup classSelector;
  // private static JPanel serverMenu = new JPanel("server menu");

  // final values
  private static final int FRAME_HEIGHT = 800;
  private static final int FRAME_WIDTH = 800;

  public static void main(String[] args) {
    //call the constructor to initialize class
    new GameMenu();
  }

  //constructor
  public GameMenu() {
    gameWindow = new JFrame("Ninjas & Samurai");
    gameWindow.setSize(GameMenu.FRAME_WIDTH, GameMenu.FRAME_HEIGHT);
    gameWindow.setResizable(false);
    gameWindow.setFocusable(true);
    gameWindow.setIconImage(
      (new ImageIcon("../BattleCity Demo/Tank/Tank.png")).getImage());
    this.setBackground(Color.BLACK);
    gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gameWindow.setVisible(true);

    //initialize misc
    classSelector = new ButtonGroup();

    //initialize components
    createLobby = new JButton("Create a Lobby");
    joinLobby = new JButton("Join a Lobby");
    help = new JButton("Help");
    gameName = new JLabel("Ninjas & Samurai");

    //place components
    createLobby.setBounds(300, 500, 200, 40);
    joinLobby.setBounds(300, 550, 200, 40);
    help.setBounds(300, 600, 200, 40);
    gameName.setBounds(220, 300, 400, 60);
    gameName.setFont(new Font("Serif", Font.PLAIN, 40));
    gameName.setForeground(Color.WHITE); 

    //add actionlistener to buttons
    createLobby.addActionListener(this);
    joinLobby.addActionListener(this);
    help.addActionListener(this);

    //add components to panel
    super.add(createLobby);
    super.add(joinLobby);
    super.add(help);
    super.add(gameName);

    //set panel layout to null so that one can set the coordinates 
    //of a component
    super.setLayout(null);

    //add the JPanel into the JFrame
    gameWindow.getContentPane().add(this);
  }

  public void actionPerformed(ActionEvent e) {
    System.out.println(e.getActionCommand());
    super.invalidate();
    super.setVisible(false);
    super.removeAll();
    gameWindow.getContentPane().remove(this);

    if(e.getActionCommand() == "Create a Lobby"){
      LobbyServer serverMenu = new LobbyServer();
      classSelector.add(serverMenu.ninjaSelector);
      classSelector.add(serverMenu.samuraiSelector);
      gameWindow.getContentPane().add(serverMenu);
    }
  }


  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
  }
  
}