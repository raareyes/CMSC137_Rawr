import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Paint extends JPanel implements Runnable{
	/*static int x=50,y=50,dx = 0,dy = 0,lastKey;
	*/
	//Timer tm = new Timer(1, this);
	static Paint p = new Paint();
	static Thread board = new Thread (p);

	static DrawMap map = new DrawMap();
	static JFrame frame;

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
	
	static String server = "localhost";
	static boolean connected=false;
    static DatagramSocket socket;
	static String serverData;
	static String name;

	Image water1 = null;
	Image water2 = null;
	int timer = 1;
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.water1 = new ImageIcon("Block/water2.png").getImage();
		this.water2 = new ImageIcon("Block/water3.png").getImage();
		
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
								g.drawImage(this.timer<500?this.water1:this.water2,x*Map.BLOCK_HEIGHT,y*Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,Map.BLOCK_WIDTH,this);
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


/*
		g.setColor(tank.getColor());
		ArrayList <Missile> ms = tank.getMissiles();

		 for (Object m1 : ms) {
            Missile m = (Missile) m1;
            g.fillRect(m.getXPos() ,m.getYPos() ,m.getWidth(),m.getHeight());
        }

        g.setColor(tank2.getColor());
        ArrayList <Missile> mss = tank2.getMissiles();

		 for (Object m2 : mss) {
            Missile mz = (Missile) m2;
            g.fillRect(mz.getXPos() ,mz.getYPos() ,mz.getWidth(),mz.getHeight());
        }
*/


        for(int i=0;i<playerCount;i++){
			g.setColor(Paint.tanks.get(i).getColor());
			g.fillOval(Paint.tanks.get(i).getXPos() ,Paint.tanks.get(i).getYPos() ,Paint.tanks.get(i).getWidth(),Paint.tanks.get(i).getHeight());        	


			ArrayList <Missile> ms = Paint.tanks.get(i).getMissiles();

		 	for (Object m1 : ms) {
	            Missile m = (Missile) m1;
	            g.fillRect(m.getXPos() ,m.getYPos() ,m.getWidth(),m.getHeight());
	        }
        }


/*
        g.setColor(tank.getColor());
		g.fillOval(tank.getXPos() ,tank.getYPos() ,tank.getWidth(),tank.getHeight());//drawImage(tank.getImage(),tank.getXPos() ,tank.getYPos() ,tank.getWidth(),tank.getHeight(),this);
		g.setColor(tank2.getColor());
		g.fillOval(tank2.getXPos() ,tank2.getYPos() ,tank2.getWidth(),tank2.getHeight());//drawImage(tank2.getImage(),tank2.getXPos() ,tank2.getYPos() ,tank2.getWidth(),tank2.getHeight(),this);
		*/
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


	

	public void run(){
		
		try{
			while(true){
			Thread.sleep(1);
			

			this.timer = this.timer != 1000? this.timer+1: 1;
			repaint();

			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public void updateBlock(Sprite object){
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
    				if(sp.get(i).getType()==Sprite.METAL){
    					try{
						AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("Audio/metal.wav"));
						Clip clip = AudioSystem.getClip();
						clip.open(audioInputStream);
						clip.start();
						}catch(Exception e){}
    				}
	    		}
	    	}
    		else if(!(sp.get(i).getType()==Sprite.VINE)){
		    			map.removeBlock(sp.get(i));
	    	}
    	}
	}

	public static void send(String msg){
		try{
			byte[] buf = msg.getBytes();
        	InetAddress address = InetAddress.getByName(server);
        	DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 3000);
        	socket.send(packet);
        }catch(Exception e){}
		
	}



	public static void main(String[] args){
//	public Paint() {
		frame = new JFrame("Battle City");
		frame.setSize(600,600);
		frame.setResizable(false);
		frame.setFocusable(true);
		frame.setIconImage((new ImageIcon ("Tank/Tank.png")).getImage());
		p.setBackground(Color.BLACK);		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(p);
		

		try{
	    	Paint.socket = new DatagramSocket();
		}catch(Exception e){};

		Paint.server = args[0];
		Paint.name = args[1];

		Paint.playerCount = Integer.parseInt(args[2]);
		for (int i =0;i<Paint.playerCount;i++){
			Paint.tanks.add(new Tank(i,"player"+i));
			Paint.tankThreads.add(new Thread(Paint.tanks.get(i)));
			frame.addKeyListener((KeyListener)Paint.tanks.get(i));
			Paint.tankThreads.get(i).start();
		}

/*
		frame.addKeyListener((KeyListener) tank);
		frame.addKeyListener((KeyListener) tank2);		

		
		//audiThread.start();
		tank1Thread.start();
		tank2Thread.start();*/
		Paint.send("CONNECT "+Paint.name);
		frame.addKeyListener((KeyListener) new KeyHandler());
		board.start();
		frame.setVisible(true);
	}

} 

class MouseMotionHandler extends MouseMotionAdapter{
	public void mouseMoved(MouseEvent mouse){
		Paint.send("PLAYER "+Paint.name+" MOUSE "+ mouse);
	}
}

class KeyHandler extends KeyAdapter{
	public void keyPressed(KeyEvent key){
		Paint.send("PLAYER "+Paint.name+" PRESSED "+ key);	
	}
	public void keyReleased(KeyEvent key){
		Paint.send("PLAYER "+Paint.name+" RELEASED "+ key);
	}
}
