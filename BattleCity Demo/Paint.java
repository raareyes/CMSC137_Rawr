import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
//import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.HashMap;
import java.awt.image.BufferedImage;


public class Paint extends JPanel implements Runnable, KeyListener {

	/* Game Attributes */
	static DrawMap map = new DrawMap();
	//receives the data read from Map.txt by get DrawMap class
	static int[][] mp = map.getMap();
	static int playerCount;
	static ArrayList<Tank> tanks = new ArrayList<Tank>();
	static ArrayList<Thread> tankThreads = new ArrayList<Thread>();
	static ArrayList<Block> blocks = map.getBlocks();
	static int gameState = Paint.INITIALIZATION;
	private static String server = "localhost";
	private static DatagramSocket socket;
	private static int serverPort;
	private static BufferedImage offscreen;

	//client attributes
	private ArrayList<Integer> pressedKeys = new ArrayList<Integer>();
	private JFrame frame;
	private Thread receiver;
	private boolean connected=false;
	private String serverData;
	private String name;
	private int timer = 1;
	private int port;
	
	private int id;
	private int lastPressed = 99999;
	private int type;

	public static final Image WATERICON1 = new ImageIcon("Block/water2.png").getImage();
	public static final Image WATERICON2 = new ImageIcon("Block/water3.png").getImage();
	public static final Image BLOCKICON = new ImageIcon("Block/brick2.png").getImage();
	public static final Image METALICON = new ImageIcon("Block/metal2.png").getImage();
	public static final Image GRASSICON = new ImageIcon("Block/grass.png").getImage();
	public static final int INITIALIZATION = 0;
	public static final int GAMEON = 11;
	public static final int KILLED = 22;
	public static final int RESULTS = 33;


//For debugging and testing game only
	public static void main(String[] args){
		if (args.length < 4){
			System.out.println("Usage: java Paint <server-address> <name> <server-port> <Choose: 15-Ninja|25-Samurai>");
			System.exit(1);
		}
		new Paint(args[0],args[1],Integer.parseInt(args[2]),Integer.parseInt(args[3]));
		
	}
	//Constructors
	//Using Paint as Client
	public Paint(String server, String name,int serverPort, int type) {

		
		frame = new JFrame("Shinobi & Bushido");
		frame.setSize(600,600);
		//when we add this option to true (resizable), 
		//we should also be able to dynamically change the size of the window
		frame.setResizable(false);					
		frame.setFocusable(true);
		frame.setIconImage((new ImageIcon ("Weapons/Sword/WeaponDown.png")).getImage());
		this.setBackground(Color.BLACK);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(this);
		frame.getContentPane().add(this);
		this.type = type;

    //merged conflict here
		frame.setGlassPane(new Chat(0, 1234, server));
    
		this.server = server;
		this.name = name;
		this.serverPort = serverPort;

		boolean randomizePort = true;
		while(randomizePort){
			try{
				Random rand = new Random();
				this.port = 5000 + rand.nextInt(2000);
		    	this.socket = new DatagramSocket(port);
				this.socket.setSoTimeout(100);
				randomizePort=false;
			}catch(IOException e){
				System.err.println(e);
	            System.err.println("Could not listen on port: "+port);
	            System.exit(-1);}
	          catch(Exception e){};
		}
		//notify the server
		Paint.send("CONNECT "+this.name+" "+this.type);
		this.connect();

		
	}

	//Using Paint for Server
	public Paint(int playerCount, ArrayList<Player> players){
		Paint.playerCount = playerCount;
		for (Player player:players){
			this.addTank(new Tank(player.getTank(), player.getName(), player.getType()));
		}
		this.runTankThread();
	}


	//Creating an Empty Working Paint
	public Paint() {
		frame = new JFrame("Shinobi & Bushido");
		frame.setSize(600,600);
		frame.setResizable(false);
		frame.setFocusable(true);
		//frame.setIconImage((new ImageIcon ("Tank/Tank.png")).getImage());
		this.setBackground(Color.BLACK);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(this);
		frame.getContentPane().add(this);		
	}

		//Network Connection
	public String receiveData(DatagramSocket socket){
		String data;
		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try{
 			socket.receive(packet);
		}catch(Exception ioe){}
		data=new String(buf);
		
		//remove excess bytes
		data = data.trim();

		if (!data.equals("")){
			System.out.println("Data: "+data);
		}
		return data;
	}
	
	public void connect(){
		boolean starting = false;
		boolean generating = false;
		while (true){

			String data = this.receiveData(this.socket);
			if(!data.startsWith("GENERATING") && !generating){
				continue;
			}
			else if (data.startsWith("GENERATING") && !generating){
				System.out.println("START GENERATING");
				generating = true;
				//System.out.println(data.split(" ")[1]);
				Paint.playerCount = (Integer)Integer.parseInt(data.split(" ")[1]);
				System.out.println(Paint.playerCount);
				this.id = (Integer)Integer.parseInt(data.split(" ")[2]);
				continue;
			} 
				// NEW PLAYER NAME TANKID TYPE X Y
				else if (data.startsWith("NEW PLAYER")) {
				System.out.println("GENERATE NEW TANK");
				String[] playerInfo = data.split(" ");
				Paint.addTank( new Tank(Integer.parseInt(playerInfo[3]),playerInfo[2],Integer.parseInt(playerInfo[5]),Integer.parseInt(playerInfo[6]),Integer.parseInt(playerInfo[4])));
				System.out.println("NEW PLAYER COUNT "+ Paint.tanks.size() +"/"+Paint.playerCount);
				//System.out.println("STARTING "+ (data.startsWith("STARTING"));// && counter >= Paint.playerCount));
				continue;
			} else if(Paint.tanks.size() == Paint.playerCount) {
				System.out.println("GENERATE BOARD");
				frame.setVisible(true);
				(new Thread(this)).start();
				Paint.runTankThread();
				Paint.offscreen=(BufferedImage)this.createImage(600, 600);
				this.receiver = new Thread (new ClientReceiver(this,this.port,this.socket));
				receiver.start();
				Paint.gameState = Paint.GAMEON;
				starting = true;
				break;
			}
			
		}
	}
	
	public static void send(String msg){
		try{
			byte[] buf = msg.getBytes();
			InetAddress address = InetAddress.getByName(Paint.server);
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, Paint.serverPort);
			Paint.socket.send(packet);
		} catch(Exception e) {}
		
	}

//Tank Stuffs
	public static void addTank(Tank tank){
		Paint.tanks.add(tank);
		Paint.tankThreads.add(new Thread(tank));
	}

	public static ArrayList<Tank> getTanks(){
			return tanks;
		}

	public static void runTankThread(){
		for (Thread trid: tankThreads)
			trid.start();
	}


	//UI Section

	/* 
	 * this sets and adds the elements needed in the game
	*/
	private void setDrawing(Graphics g){
		int x, y;
		int h = Map.BOARD_HEIGHT/Map.BLOCK_HEIGHT, w = Map.BOARD_WIDTH/Map.BLOCK_WIDTH;
		g.clearRect(0, 0, 600,600);
		for(int j=0;j<mp.length;j++){
			y = j%w;
			for(int i=0;i<mp[0].length;i++){
				x = i%w;
				g.drawImage(Paint.GRASSICON,x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
				if (!(blocks.get(j+i).isDead())){
					switch(mp[j][i]){
						case Sprite.BRICK:
								g.drawImage(Paint.BLOCKICON,x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
								break;
						case Sprite.WATER:
								// if ((x+y)%2 == 1)
								// 	g.drawImage(this.timer<5?Paint.WATERICON1:Paint.WATERICON2,x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
								// else
									g.drawImage(this.timer<25?Paint.WATERICON2:Paint.WATERICON1,x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);

								break;
						case Sprite.METAL:
								g.drawImage(Paint.METALICON,x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
								break;
					}
				}
			}
		}

		for(Tank tank: Paint.tanks) {
			//Range
			if (!tank.isAlive()|| (!tank.isVisible() && tank.getPlayer() != this.id))
				continue;
			g.setColor(Color.WHITE);
			g.drawOval(tank.getXPos()-tank.getRange(),tank.getYPos()-tank.getRange(),tank.getRange()*2+tank.getWidth(),tank.getRange()*2+tank.getHeight());
			// HashMap<String,Integer> weap = tank.getWeaponPos();
			// g.drawImage(tank.getWeapon(),weap.get("x"),weap.get("y"),weap.get("r"),weap.get("r"),this);
			tank.drawWeapon(g,this);
			//Player
			g.setColor(tank.getColor());
			g.drawString(tank.getName(),tank.getXPos()-10,tank.getYPos()+30);
			g.fillOval(tank.getXPos() ,tank.getYPos() ,tank.getWidth(),tank.getHeight());        	
		}

		for(int j=0;j<mp.length;j++) {
			y = j%w;
			for(int i=0;i<mp[0].length;i++){
				x = i%w;
				switch(mp[j][i]){
					case Sprite.VINE:
							g.drawImage(new ImageIcon("Block/vine2.png").getImage(),x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
							break;
				}
			}
		}
	}

	/* 
	 * enables the drawing of component in the Paint class
	*/
	public void paintComponent(Graphics g){
		//super.paintComponent(g);
		g.drawImage(Paint.offscreen, 0, 0, null);
	}
//Useless for now, this kills blocks
	public static void updateBlock(Sprite object){
		ArrayList<Block> sp = map.getBlocks();
    	for(int i=0;i<sp.size();i++){
    		if (object.getType() == Sprite.MISSILE && sp.get(i).getType() == Sprite.WATER)
    			continue;
    		if(sp.get(i).isVisible() && !(sp.get(i).isDead()) && (sp.get(i).canCollide())){
    			if (object.collisionCheck((Block)sp.get(i))){
    				if(!(object.getType() == Sprite.MISSILE) && (sp.get(i).getType()==PowerUp.AMMO_UPGRADE||sp.get(i).getType()==PowerUp.GRENADE || sp.get(i).getType()==PowerUp.NEW_LIFE || sp.get(i).getType()==PowerUp.SPEED_BOOSTER)){
	    				((PowerUp)sp.get(i)).addEffect((Tank)object);
	    				map.removeBlock(sp.get(i));
	    				continue;
	    			}else if((object.getType() == Sprite.MISSILE) && (sp.get(i).getType()==PowerUp.AMMO_UPGRADE ||sp.get(i).getType()==PowerUp.GRENADE|| sp.get(i).getType()==PowerUp.NEW_LIFE || sp.get(i).getType()==PowerUp.SPEED_BOOSTER)){
	    				object.collide((Block)sp.get(i));
	    				map.removeBlock(sp.get(i));
	    				continue;
	    			}
    				object.collide((Block)sp.get(i));
	    		}
	    	}
    		else if(!(sp.get(i).getType()==Sprite.VINE)){
		    			map.removeBlock(sp.get(i));
	    	}
    	}
	}

/* UI Thread */
	public void run(){
		System.out.println("BOARD IS RUNNING");
		while(true){
			try{
				Thread.sleep(20);
			}catch(Exception e){
				System.out.println(e.getMessage());
			}

			if (Paint.gameState == RESULTS){
				//int rank = 1;
				for (Tank tank:Paint.tanks){
					System.out.println(tank.getName()+"\nStats:\n"+(tank.getLife()>0?("LIVES LEFT: "+tank.getLife()):("KILLED IN ACTION"))+" SCORED "+ tank.getScore()+" KILLS");
				}
				break;
			}


			if (Paint.tanks.get(this.id).getLife() == 0){
				Paint.send("PLAYER OUT "+this.id);
				Paint.gameState = Paint.KILLED;
			}
			else if (Paint.tanks.get(this.id).isDead()){
				Paint.send("PLAYER DIED "+this.id);
			}


			this.timer = this.timer != 50? this.timer+1: 1;
			setDrawing(Paint.offscreen.getGraphics());
			this.repaint();
		}
	}

	/* 
   * Key listeners	
	*/
	public void keyPressed(KeyEvent key){

		if(key.getKeyCode() == KeyEvent.VK_ESCAPE){
			frame.getGlassPane().setVisible(false);
		}
		if(key.getKeyCode() == KeyEvent.VK_ENTER){
			frame.getGlassPane().setVisible(true);
			frame.getGlassPane().requestFocusInWindow();
		}
		
		//stops sending redundant commands
		if (pressedKeys.size() != 0 && pressedKeys.contains(key.getKeyCode()))
			return;
		Paint.send("PLAYER "+this.id+" PRESSED "+ key.getKeyCode());
		pressedKeys.add(key.getKeyCode());
	}

	public void keyReleased(KeyEvent key){
			Paint.send("PLAYER "+this.id+" RELEASED "+ key.getKeyCode());
			//releases the key
			pressedKeys.remove((Object)key.getKeyCode());
	}

	public void keyTyped(KeyEvent key){
	}

} 

/*References

UDP Connection
Circle Wars Laboratory Example

Game Mechanics
Battle City
Gang Beasts


*/