import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
//import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Paint extends JPanel implements Runnable, KeyListener{

	//game attributes
	static DrawMap map = new DrawMap();
	static int[][] mp = map.getMap();
	static int playerCount;
	static ArrayList<Tank> tanks = new ArrayList<Tank>();
	static ArrayList<Thread> tankThreads = new ArrayList<Thread>();
	static ArrayList<Block> blocks = map.getBlocks();

	//client attributes
	private ArrayList<Integer> pressedKeys = new ArrayList<Integer>();
	private String server = "localhost";
	private JFrame frame;
	private Thread receiver;
	private boolean connected=false;
    private DatagramSocket socket;
	private String serverData;
	private String name;
	private int timer = 1;
	private int port;
	private int id;
	private int lastPressed = 99999;

	public static final Image WATERICON1 = new ImageIcon("Block/water2.png").getImage();
	public static final Image WATERICON2 = new ImageIcon("Block/water3.png").getImage();
	public static final Image BLOCKICON = new ImageIcon("Block/brick2.png").getImage();
	public static final Image METALICON = new ImageIcon("Block/metal2.png").getImage();

//For debugging and testing game only
	public static void main(String[] args){
		if (args.length != 3){
			System.out.println("Usage: java Paint <server-address> <name> <port>");
			System.exit(1);
		}
		new Paint(args[0],args[1],Integer.parseInt(args[2]));
		
	}
//Constructors
	//Using Paint as Client
	public Paint(String server, String name,int port) {
		frame = new JFrame("Battle City");
		frame.setSize(600,600);
		frame.setResizable(false);
		frame.setFocusable(true);
		frame.setIconImage((new ImageIcon ("Tank/Tank.png")).getImage());
		this.setBackground(Color.BLACK);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addKeyListener(this);
		frame.getContentPane().add(this);

		this.server = server;
		this.name = name;
		this.port = port;

		try{
	    	this.socket = new DatagramSocket(port);
			this.socket.setSoTimeout(100);
		}catch(IOException e){
			System.err.println(e);
            System.err.println("Could not listen on port: "+port);
            System.exit(-1);}
          catch(Exception e){};

        //check for connection
		this.send("CONNECT "+this.name);
		serverData = receiveData(this.socket);
		while (!serverData.startsWith("CONNECTED "+this.name)){	
			this.send("CONNECT "+this.name);
			serverData = receiveData(this.socket);
		}

		this.connect();

		
	}

	//Using Paint for Server
	public Paint(int playerCount, ArrayList<Player> players){
			Paint.playerCount = playerCount;
			for (Player player:players)
				Paint.tanks.add(new Tank(player.getTank(),player.getName()));
		}


	//Creating an Empty Working Paint
	public Paint() {
		frame = new JFrame("Battle City");
		frame.setSize(600,600);
		frame.setResizable(false);
		frame.setFocusable(true);
		frame.setIconImage((new ImageIcon ("Tank/Tank.png")).getImage());
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
			if(!data.startsWith("GENERATING") && !generating)
				continue;

			else if (data.startsWith("GENERATING") && !generating){
				System.out.println("START GENERATING");
				generating = true;
				//System.out.println(data.split(" ")[1]);
				Paint.playerCount = (Integer)Integer.parseInt(data.split(" ")[1]);
				System.out.println(Paint.playerCount);
				this.id = (Integer)Integer.parseInt(data.split(" ")[2]);
				continue;
			}
			// NEW PLAYER NAME TANKID X Y
			else if (data.startsWith("NEW PLAYER")){
				System.out.println("GENERATE NEW TANK");
				String[] playerInfo = data.split(" ");
				Paint.addTank( new Tank(Integer.parseInt(playerInfo[3]),playerInfo[2],Integer.parseInt(playerInfo[4]),Integer.parseInt(playerInfo[5])));
				System.out.println("NEW PLAYER COUNT "+ Paint.tanks.size() +"/"+Paint.playerCount);
				//System.out.println("STARTING "+ (data.startsWith("STARTING"));// && counter >= Paint.playerCount));
				continue;
			}
			else if (Paint.tanks.size() == Paint.playerCount){
				System.out.println("GENERATE BOARD");
				frame.setVisible(true);
				(new Thread(this)).start();
				Paint.runTankThread();
				this.receiver = new Thread (new ClientReceiver(this,this.port,this.socket));
				receiver.start();
				starting = true;
				break;
			}
			

		}
	}
	
	public void send(String msg){
		try{
			byte[] buf = msg.getBytes();
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 3000);
        	socket.send(packet);
        }catch(Exception e){}
		
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

//UI Stuffs
	private void setDrawing(Graphics g){
		int x, y;
		int h = Map.BOARD_HEIGHT/Map.BLOCK_HEIGHT, w = Map.BOARD_WIDTH/Map.BLOCK_WIDTH;
		for(int j=0;j<mp.length;j++){
			y = j%w;
			for(int i=0;i<mp[0].length;i++){
				x = i%w;
				if (!(blocks.get(j+i).isDead())){
					switch(mp[j][i]){
						case Sprite.BRICK:
								g.drawImage(Paint.BLOCKICON,x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
								break;
						case Sprite.WATER:
								if ((x+y)%2 == 1)
									g.drawImage(this.timer<5?Paint.WATERICON1:Paint.WATERICON2,x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
								else
									g.drawImage(this.timer<5?Paint.WATERICON2:Paint.WATERICON1,x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);

								break;
						case Sprite.METAL:
								g.drawImage(Paint.METALICON,x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
								break;
					}
				}
			}
		}

        for(Tank tank: Paint.tanks){
        	//Range
        	g.setColor(Color.WHITE);
			g.fillOval(tank.getXPos()-tank.getRange(),tank.getYPos()-tank.getRange(),tank.getRange()*2+tank.getWidth(),tank.getRange()*2+tank.getHeight());
			//Player
			g.setColor(tank.getColor());
			g.fillOval(tank.getXPos() ,tank.getYPos() ,tank.getWidth(),tank.getHeight());        	


			ArrayList <Missile> ms = tank.getMissiles();

		 	for (Object m1 : ms) {
	            Missile m = (Missile) m1;
	            g.fillRect(m.getXPos() ,m.getYPos() ,m.getWidth(),m.getHeight());
	        }
        }

		for(int j=0;j<mp.length;j++){
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

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		setDrawing(g);
	}

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

//UI Thread
	public void run(){
		System.out.println("BOARD IS RUNNING");
		while(true){
			try{
				Thread.sleep(100);
			}catch(Exception e){
				System.out.println(e.getMessage());
			}

			if (Paint.tanks.get(this.id).getLife() == 0)
				this.send("PLAYER OUT "+this.id);
			else if (Paint.tanks.get(this.id).isDead()){
				this.send("PLAYER DIED "+this.id);
			}

				this.timer = this.timer != 10? this.timer+1: 1;
				this.repaint();
		}
	}

//Key Listeners
	public void keyPressed(KeyEvent key){


			//stops sending redundant commands
			if (pressedKeys.size() != 0 && pressedKeys.contains(key.getKeyCode()))
				return;
			this.send("PLAYER "+this.id+" PRESSED "+ key.getKeyCode());
			pressedKeys.add(key.getKeyCode());
	}
	public void keyReleased(KeyEvent key){
			this.send("PLAYER "+this.id+" RELEASED "+ key.getKeyCode());
			//releases the key
			pressedKeys.remove((Object)key.getKeyCode());
	}
	public void keyTyped(KeyEvent key){
	}

} 

/*References

Semi-Circle
https://stackoverflow.com/questions/8061420/how-to-draw-semi-circle

UDP Connection
Circle Wars Laboratory Example

Game Mechanics
Battle City
Gang Beasts


*/