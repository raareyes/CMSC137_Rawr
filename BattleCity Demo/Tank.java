import java.awt.event.*;
import java.awt.*;
//import java.awt.geom;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import javax.sound.sampled.*;
import java.util.Random;
import java.lang.Math;

public class Tank extends Sprite{
	public static final int HEIGHT=15, WIDTH=15;
	private Character direction;
	private boolean alive = true;
	private int lastKey;
	private int player;
	private int cooldown;
	private CooldownTimer skillTimer;
	private int reloadDelay;
	private int reloaded;
	private int animationState;
	private int life;
	private int damage;
	private int range;
	private int score;
	private String name;
	private Color color;
	private int playerType;
	public JLabel lives = new JLabel();

//Constructors
	//For Client
	public Tank(int player,String name, int x, int y,int playerType){
		super(x,y,HEIGHT,WIDTH,Sprite.TANK);
		this.score = 0;
		this.player = player;
		this.setCollision(true);
		this.life = 3;
		this.spawn(x,y);
		this.name = name;
		this.color = new Color(x%255,y%255,Math.abs(x-y)%255);
		this.setCoor(x,y);
		this.lastKey = KeyEvent.VK_DOWN;
		this.playerType = playerType;
		if (this.playerType == Ninja.TYPE){
			this.range = Ninja.RANGE;
			this.cooldown = Ninja.CD;
			this.skillTimer = new CooldownTimer(Ninja.CD,Ninja.CD/2,this);
			super.setSpeed(Ninja.SPEED);
			super.origSpeed(Ninja.SPEED);
		}
		else if(this.playerType == Samurai.TYPE){
			this.range = Samurai.RANGE;
			this.cooldown = Samurai.CD;
			this.skillTimer = new CooldownTimer(Samurai.CD,Samurai.CD/100,this);
			super.setSpeed(Samurai.SPEED);
			super.origSpeed(Samurai.SPEED);
		}
	}
	//For Server
	public Tank(int player,String name,int playerType){
		super(player,player,HEIGHT,WIDTH,Sprite.TANK);
		this.score = 0;
		this.range = 15;
		this.player = player;
		this.setCollision(true);
		this.life = 3;
		this.randCoor();
		this.spawn(super.getXPos(),super.getYPos());
		this.name = name;
		this.color = new Color(super.getXPos()%255,super.getYPos()%255,Math.abs(super.getXPos()-super.getYPos())%255);
		this.lastKey = KeyEvent.VK_DOWN;
		this.playerType = playerType;
		if (this.playerType == Ninja.TYPE){
			this.range = Ninja.RANGE;
			this.cooldown = Ninja.CD;
			this.skillTimer = new CooldownTimer(Ninja.CD,Ninja.CD/2,this);
			super.setSpeed(Ninja.SPEED);
			super.origSpeed(Ninja.SPEED);
		}
		else if(this.playerType == Samurai.TYPE){
			this.range = Samurai.RANGE;
			this.cooldown = Samurai.CD;
			this.skillTimer = new CooldownTimer(Samurai.CD,Samurai.CD/8,this);
			super.setSpeed(Samurai.SPEED);
			super.origSpeed(Samurai.SPEED);
		}
		//this.setImage = null;//	new ImageIcon("Tank/P2/upS1.png").getImage();
	}

	//setting the direction of the movement
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

	
	//randomizing the coordinates
	public void randCoor(){
		boolean intersecting = false;
		int randX, randY;
		while(true){
			boolean flag = false;
			Random rand = new Random();
			randX = rand.nextInt(570);
			randY = rand.nextInt(570);
			super.setCoor(randX,randY);

			Rectangle thisBounds = new Rectangle(randX,randY,Tank.HEIGHT,Tank.WIDTH);
			for (Sprite object : Paint.map.getBlocks()){
				if (collisionCheck(object))
					flag = true;
			}

			if (flag)
				continue;

			for (Tank object : Paint.tanks){
				if (this.player == object.getPlayer())
					continue;
				else if (collisionCheck(object))
					flag = true;
			}

			if (flag)
				continue;

			return;
		}

	}

	//spawning a character
	public void spawn(int x, int y){
		this.setCoor(x,y);
		this.setHealth(1);
		this.resetSpeed();
		this.reloadDelay = 50;
		this.reloaded = 0;
		this.animationState = 1;
		this.damage = 1;
	}

	//animation
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

//Pseudo key listeners
	public void keyPressed(int key, int player) {

				if (!(this.player == (player)))
					return;
				if (key == KeyEvent.VK_E){
					this.skill();
				}
				else if ((key == KeyEvent.VK_SPACE)
	        		&& this.isReloaded()) {
	        			this.attack();
	        			this.reloaded = this.reloadDelay;
	        	}

			    else if ((key == KeyEvent.VK_LEFT) || (key == KeyEvent.VK_A)) {
		            this.setDirection(-1,0);
   		            this.lastKey = key;
	            	this.direction = 'a';
	            	this.changeImageState();
		        }

		        else if ((key == KeyEvent.VK_RIGHT) || (key == KeyEvent.VK_D)) {
		            this.setDirection(1,0);
		            this.lastKey = key;
	            	this.direction = 'd';
	            	this.changeImageState();
	           
		        }

		        else if ((key == KeyEvent.VK_UP) || (key == KeyEvent.VK_W)) {
		            this.setDirection(0,-1);
		            this.lastKey = key;
	            	this.direction = 'w';
	            	this.changeImageState();
		        }

		        else if ((key == KeyEvent.VK_DOWN) || (key == KeyEvent.VK_S)) {
		            this.setDirection(0,1);
		            this.lastKey = key;
	            	this.direction = 's';
	        	}

    			
	}

	public void keyReleased(int key, int player) {

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

	//Tries to all enemies
	public void attack(){
		for (Tank tank : Paint.getTanks()){
			if (tank.getPlayer() == this.player)
				continue;
			if (this.slash(tank)){
    			tank.addDamage(this.damage);
    			if (tank.isDead()){
    				Paint.send("PLAYER "+this.player+" SCORES "+1);
    			}
			}
		}

	}

	//damages those in range
	public boolean slash(Sprite object){

    	//checks if in range
		Rectangle thisBounds = new Rectangle(super.getXPos()-this.range,super.getYPos()-this.range,this.range*2+super.getWidth(),this.range*2+super.getHeight());
    	Rectangle objectBounds = new Rectangle(object.getXPos(),object.getYPos(),object.getHeight(),object.getWidth());

    	if (thisBounds.intersects(objectBounds)){
    		return true;
    	}

		//checks if facing the right direction
		if ((this.lastKey == KeyEvent.VK_LEFT || this.lastKey == KeyEvent.VK_A) &&object.getXPos() > this.getXPos()) {
			return false;
        }
        else if ((this.lastKey == KeyEvent.VK_RIGHT || this.lastKey == KeyEvent.VK_D) && object.getXPos() < this.getXPos()) {
        	return false;
        }
        else if ((this.lastKey == KeyEvent.VK_UP || this.lastKey == KeyEvent.VK_W) && object.getYPos() > this.getYPos()) {
        	return false;
        }	

        else if ((this.lastKey == KeyEvent.VK_DOWN || this.lastKey == KeyEvent.VK_S) && object.getYPos() < this.getYPos()) {
    		return false;
    	}

    	return false;
	}

	public void skill(){
		this.skillTimer.activate();
	}

	//Player thread
	public void run(){
		System.out.println("THE THREAD STARTED");
		while (alive){
			try{
				this.reload();
				this.getSprite().sleep(this.getSpeed());
				Paint.updateBlock(this);
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
				
				//Life UI changer
				/*switch(this.life) {
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
				}*/

			Thread.sleep(this.getSpeed());
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}
		System.out.println("DEAD");
	}

//getters and setters
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
		this.damage = damage;
	}

	public String getName(){
		return this.name;
	}

	public int getRange(){
		return this.range;
	}
	public void setRange(int range){
		this.range = range;
	}

	public int getLife(){
		return this.life;
	}

	public int getPlayerType(){
		return this.playerType;
	}

	public void setLife(int life){
		this.life = life;
	}

	public boolean isAlive(){
		return this.alive;
	}

	public void kill(){
		this.alive =false;
	}

	public void revive(){
		this.alive = true;
	}

	public int getScore(){
		return this.score;
	}

	public void addScore(int score){
		this.score += score;
	}

	public boolean isReloaded(){
		return reloaded==0?true:false;
	}

	public void reload(){
		this.reloaded = reloaded == 0? 0:reloaded-1;
	}

	public String toString(){
		String retval="";
		retval+="PLAYER ";
		retval+=player+" ";
		retval+=getXPos()+" ";
		retval+=getYPos();
		return retval;
	}
}

