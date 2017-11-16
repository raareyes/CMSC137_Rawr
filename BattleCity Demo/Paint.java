import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Paint extends JPanel implements Runnable, KeyListener{
	/*static int x=50,y=50,dx = 0,dy = 0,lastKey;
	*/
	//Timer tm = new Timer(1, this);
	private Thread board;

	static DrawMap map = new DrawMap();
	private JFrame frame;

	static ArrayList<Tank> tanks = new ArrayList<Tank>();
	static ArrayList<Thread> tankThreads = new ArrayList<Thread>();

/*	static Tank tank = new Tank(Tank.CP1);
	static Tank tank2 = new Tank(Tank.CP2);

	static Thread tank1Thread = new Thread(tank);
	static Thread tank2Thread = new Thread(tank2);
*/
	static int playerCount;
	static int[][] mp = map.getMap();
	static ArrayList<Block> blocks = map.getBlocks();
	
	private String server = "localhost";
	private boolean connected=false;
    private DatagramSocket socket;
	private String serverData;
	private String name;
	private int id;
	private int port;
	private BufferedImage offscreen;

	private Image water1 = new ImageIcon("Block/water2.png").getImage();
	private Image water2 = new ImageIcon("Block/water3.png").getImage();
	int timer = 1;
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//g.drawImage(offscreen, 0, 0, null);
		setDrawing(g);
		
		
	}

	public String receiveData(){
		String data;
		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try{
 			socket.receive(packet);
		}catch(Exception ioe){}
		
		/**
		 * Convert the array of bytes to string
		 */
		data=new String(buf);
		
		//remove excess bytes
		data = data.trim();

		if (!data.equals("")){
			System.out.println("Data: "+data);
		}
		return data;
	}

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
								// g.setColor(Color.ORANGE);
								// g.fillRect(x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH);
								g.drawImage(new ImageIcon("Block/brick2.png").getImage(),x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
								break;
						case Sprite.WATER:
								g.drawImage(this.timer<50?this.water1:this.water2,x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
								break;
						case Sprite.METAL:
								// g.setColor(Color.WHITE);
								// g.fillRect(x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH);
								g.drawImage(new ImageIcon("Block/metal2.png").getImage(),x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
								break;
						case PowerUp.SPEED_BOOSTER:
								// g.setColor(Color.RED);
								// g.fillRect(x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH);
								g.drawImage(new ImageIcon("Block/speed.png").getImage(),x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
								break;
						case PowerUp.AMMO_UPGRADE:
								// g.setColor(Color.RED);
								// g.fillRect(x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH);
								g.drawImage(new ImageIcon("Block/ammo.png").getImage(),x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
								break;
						case PowerUp.NEW_LIFE:
								// g.setColor(Color.YELLOW);
								// g.fillRect(x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH);
								g.drawImage(new ImageIcon("Block/newlife.png").getImage(),x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
								break;
						case PowerUp.GRENADE:
								// g.setColor(Color.YELLOW);
								// g.fillRect(x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH);
								g.drawImage(new ImageIcon("Block/k.png").getImage(),x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
								break;
					}
				}
			}
		}

        for(Tank tank: Paint.tanks){
        	//System.out.println(tank.toString());
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
							// g.setColor(Color.GREEN);
							// g.fillRect(x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH);
							g.drawImage(new ImageIcon("Block/vine2.png").getImage(),x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
							break;
				}
			}
		}
	}

	public void connect(){
		boolean starting = false;
		boolean generating = false;
		while (true){

			String data = this.receiveData();
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
				Tank tank = new Tank(Integer.parseInt(playerInfo[3]),playerInfo[2],Integer.parseInt(playerInfo[4]),Integer.parseInt(playerInfo[5]));
				Paint.tanks.add(tank);
				Paint.tankThreads.add(new Thread(tank));
				System.out.println("NEW PLAYER COUNT "+ Paint.tanks.size() +"/"+Paint.playerCount);
				//System.out.println("STARTING "+ (data.startsWith("STARTING"));// && counter >= Paint.playerCount));
				continue;
			}
			else if (Paint.tanks.size() == Paint.playerCount){
				System.out.println("GENERATE BOARD");
				frame.setVisible(true);
				(new Thread(this)).start();
				for (Thread trid: tankThreads)
					trid.start();
				
				starting = true;
				break;
			}
			

		}
	}

	

	public void run(){
		
		System.out.println("BOARD IS RUNNING");
		while(true){
			try{
				Thread.sleep(1);
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
				
			String data = receiveData();
			if (!data.equals("")){
				String[] dataStream = data.split(" ");
				for(Tank tank: Paint.tanks){
					if (!(tank.getPlayer() == (Integer.parseInt(dataStream[1]))))
						continue;
					else
						System.out.println("IT RECIEVED!!");
					if (dataStream[2].equals("PRESSED")){
						tank.keyPressed(Integer.parseInt(dataStream[3]),Integer.parseInt(dataStream[1]));
						System.out.println("IT PRESSED!!");
					}
					if (dataStream[2].equals("RELEASED")){
						System.out.println("IT RELEASED!!");
						tank.keyReleased(Integer.parseInt(dataStream[3]),Integer.parseInt(dataStream[1]));
					}
	
				}
			}
			this.timer = this.timer != 100? this.timer+1: 1;
			this.repaint();

		}
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
    				//sounds
    				/*if(sp.get(i).getType()==Sprite.METAL){
    					try{
						AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("Audio/metal.wav"));
						Clip clip = AudioSystem.getClip();
						clip.open(audioInputStream);
						clip.start();
						}catch(Exception e){}
    				}*/
	    		}
	    	}
    		else if(!(sp.get(i).getType()==Sprite.VINE)){
		    			map.removeBlock(sp.get(i));
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



	public static void main(String[] args){
		
		new Paint(args[0],args[1],Integer.parseInt(args[2]));
		
	}

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


		this.send("CONNECT "+this.name);
		this.connect();

		
	}
	public Paint(int playerCount, ArrayList<Player> players){
			Paint.playerCount = playerCount;
			for (Player player:players)
				Paint.tanks.add(new Tank(player.getTank(),player.getName()));
		}
	public ArrayList<Tank> getTanks(){
			return tanks;
		}
	public void keyPressed(KeyEvent key){
		this.send("PLAYER "+this.id+" PRESSED "+ key.getKeyCode());	
	}
	public void keyReleased(KeyEvent key){
		this.send("PLAYER "+this.id+" RELEASED "+ key.getKeyCode());
	}
	public void keyTyped(KeyEvent key){
	}
	/*public void mouseMoved(MouseEvent mouse){
		this.send("PLAYER "+this.name+" MOUSE "+ mouse);
	}*/

} 

