import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import javax.sound.sampled.*;
import java.util.Random;

public class Tank extends Sprite implements KeyListener{
	public static final int HEIGHT=15, WIDTH=15;
	private ArrayList<Missile> mL = new ArrayList<Missile>();
	private Character direction;
	private int lastKey;
	private int player;
	private int reloadDelay;
	private int reloaded;
	private int animationState;
	private int life;
	private int damage;
	private int range;
	private Color color;
	public JLabel lives = new JLabel();
	
	public Tank(int player){
		super(player,player,HEIGHT,WIDTH,Sprite.TANK);
		this.range = 15;
		this.player = player;
		this.setCollision(true);
		this.life = 3;
		this.spawn();
		Random rand = new Random();
		this.color = new Color(rand.nextFloat(),rand.nextFloat(),rand.nextFloat());
		this.lastKey = KeyEvent.VK_DOWN;
		//this.setImage = null;//	new ImageIcon("Tank/P2/upS1.png").getImage();
	}
	public void run(){
		while (life != 0){
			try{
				this.reload();
				this.getSprite().sleep(this.getSpeed());
				Paint.p.updateBlock(this);
				boolean flag = false;
				for (int players = 0;players<Paint.playerCount;players++){
					if (players == this.player)
						continue;
					else if(this.collisionCheck(Paint.tanks.get(players))){
						flag = true;
					}
				}
				if(!flag){
					this.move();
				}
				if(this.isDead()){
		   			this.spawn();	
		   			this.life --;
				}
				
				switch(this.life) {
					case 0: 	this.lives.setIcon(null);
					
					
					
									break;
					case 1:	switch(this.getHealth()) {
										case 2:	this.lives.setIcon(new ImageIcon("images/life/life1_4.png"));
														break;
										case 4:	this.lives.setIcon(new ImageIcon("images/life/life1_3.png"));
														break;
										case 6:	this.lives.setIcon(new ImageIcon("images/life/life1_2.png"));
														break;
										case 8:	this.lives.setIcon(new ImageIcon("images/life/life1_1.png"));
														break;
										case 10:	this.lives.setIcon(new ImageIcon("images/life/life1.png"));
														break;		
										default:	break;
									}
									break;
					case 2:	switch(this.getHealth()) {
										case 2:	this.lives.setIcon(new ImageIcon("images/life/life2_4.png"));
														break;
										case 4:	this.lives.setIcon(new ImageIcon("images/life/life2_3.png"));
														break;
										case 6:	this.lives.setIcon(new ImageIcon("images/life/life2_2.png"));
														break;
										case 8:	this.lives.setIcon(new ImageIcon("images/life/life2_1.png"));
														break;
										case 10:	this.lives.setIcon(new ImageIcon("images/life/life2.png"));
														break;		
										default:	break;
									}
									break;
					case 3:	switch(this.getHealth()) {
										case 2:	this.lives.setIcon(new ImageIcon("images/life/life3_4.png"));
														break;
										case 4:	this.lives.setIcon(new ImageIcon("images/life/life3_3.png"));
														break;
										case 6:	this.lives.setIcon(new ImageIcon("images/life/life3_2.png"));
														break;
										case 8:	this.lives.setIcon(new ImageIcon("images/life/life3_1.png"));
														break;
										case 10:	this.lives.setIcon(new ImageIcon("images/life/life3.png"));
														break;		
										default:	break;
									}
									break;
					default: break;
				}
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		System.out.println("DEAD");
		//BattleCity1.changeMenu(3);
		//BattleCity1.clickButton();
	}

	public boolean isReloaded(){
		return reloaded==0?true:false;
	}

	public void reload(){
		this.reloaded = reloaded == 0? 0:reloaded-1;
	}

	
	public void move(){




		if (this.getXPos() + Tank.WIDTH + this.getDX() > 600){
			this.setDirection(0,this.getDY());
		}
		if (this.getYPos() + Tank.HEIGHT + this.getDY() > 600){
			this.setDirection(this.getDX(),0);
		}
		if (this.getYPos() + this.getDY() < 0){
			this.setDirection(this.getDX(),0);
		}
		if (this.getXPos() + this.getDX() < 0){
			this.setDirection(0,this.getDY());
		}

    	
			super.move();
			
    	

    	if (this.getDX() != 0 || this.getDY() != 0){
    		this.changeImageState();
	    	this.animationState = this.animationState != 10? this.animationState += 1:1;
    	}
	    

	}

	private void fire(){
		Missile missile = new Missile(this.getXPos()+WIDTH/2 - 2,this.getYPos()+HEIGHT/2 - 2,this.lastKey, this.damage,this);
		this.mL.add(missile);
		missile.getSprite().start();
		try{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("Audio/fire.wav"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}catch(Exception e){}
	}

	public ArrayList<Missile> getMissiles(){
		return this.mL;
	}

	public void removeMissile(Missile m){
		mL.remove(m);
	}

	public void keyPressed(KeyEvent e) {
				//System.out.println(this.getXPos());
				int key = e.getKeyCode();

			        if ((key == KeyEvent.VK_LEFT) || (key == KeyEvent.VK_A)) {
		            this.setDirection(-1,0);
   		            this.lastKey = e.getKeyCode();
	            	this.direction = 'a';
	            	this.changeImageState();
   		           
					
		        }

		        else if ((key == KeyEvent.VK_RIGHT) || (key == KeyEvent.VK_D)) {
		            this.setDirection(1,0);
		            this.lastKey = e.getKeyCode();
	            	this.direction = 'd';
	            	this.changeImageState();
	           
		        }

		        else if ((key == KeyEvent.VK_UP) || (key == KeyEvent.VK_W)) {
		            this.setDirection(0,-1);
		            this.lastKey = e.getKeyCode();
	            	this.direction = 'w';
	            	this.changeImageState();
		        }

		        else if ((key == KeyEvent.VK_DOWN) || (key == KeyEvent.VK_S)) {
		            this.setDirection(0,1);
		            this.lastKey = e.getKeyCode();
	            	this.direction = 's';
	        	}

    			
	}
	public void spawn(){
		this.randCoor();
		//this.setCoor(this.player,this.player);
		this.setHealth(10);
		this.resetSpeed(10);
		this.reloadDelay = 50;
		this.reloaded = 0;
		this.animationState = 1;
		this.damage = 1;
	}

	public void changeImageState(){
		/*switch (this.direction){
			case 'w':
				if (this.animationState < 5)
            		//this.setImage(this.player == CP1? (new ImageIcon("Tank/P1/upS1.png").getImage()):(new ImageIcon("Tank/P2/upS1.png").getImage()));
            	else
            		//this.setImage(this.player == CP1? (new ImageIcon("Tank/P1/upS2.png").getImage()):(new ImageIcon("Tank/P2/upS2.png").getImage()));
			break;
			case 'a':
	            if (this.animationState < 5)
            		//this.setImage(this.player == CP1? (new ImageIcon("Tank/P1/leftS1.png").getImage()):(new ImageIcon("Tank/P2/leftS1.png").getImage()));
            	else
            		//this.setImage(this.player == CP1? (new ImageIcon("Tank/P1/leftS2.png").getImage()):(new ImageIcon("Tank/P2/leftS2.png").getImage()));

			break;
			case 's':
				if (this.animationState < 5)
            		//this.setImage(this.player == CP1? (new ImageIcon("Tank/P1/downS1.png").getImage()):(new ImageIcon("Tank/P2/downS1.png").getImage()));
            	else
            		//this.setImage(this.player == CP1? (new ImageIcon("Tank/P1/downS2.png").getImage()):(new ImageIcon("Tank/P2/downS2.png").getImage()));
			break;
			case 'd':
				 if (this.animationState < 5)
	            	//this.setImage(this.player == CP1? (new ImageIcon("Tank/P1/rightS1.png").getImage()):(new ImageIcon("Tank/P2/rightS1.png").getImage()));
	           	else
	           		//this.setImage(this.player == CP1? (new ImageIcon("Tank/P1/rightS2.png").getImage()):(new ImageIcon("Tank/P2/rightS2.png").getImage()));
			break;
		}*/

		
	}


	public void keyReleased(KeyEvent e) {
	        
		        int key = e.getKeyCode();

		        if (((key == KeyEvent.VK_E)
	        		|| (key == KeyEvent.VK_SPACE))
	        		&& this.isReloaded()) {
	        			this.fire();
	        			this.reloaded = this.reloadDelay;
	        	}

		        if (key != this.lastKey)
		        	return;

		        else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
		            this.setDirection(0,0);
		        }

		        else if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
		            this.setDirection(0,0);
		        }

		        else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
		            this.setDirection(0,0);
		        }

		        else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
		            this.setDirection(0,0);
	        	}
	        	
	}

	public void keyTyped(KeyEvent ke) {}

	public void collide(Sprite object){
		this.setDirection(0,0);
	}

	public void notCollide(Sprite object){
		this.move();

	}

	public int getPlayer(){
		return this.player;
	}

	public int getDamage(){
		return this.damage;
	}

	public Color getColor(){
		return this.color;
	}

	public void setDamage(int damage){
		this.damage += damage;
	}



}

