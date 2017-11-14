import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import javax.sound.sampled.*;

public class Missile extends Sprite{
	static final int SPEED = 5;
	private int damage;
	private int player;
	private Tank tank;
	public Missile(int posx, int posy, int lastKey,int damage,Tank tank){
		super(posx,posy,5,10,Sprite.MISSILE);
		this.tank = tank;
		this.player = this.tank.getPlayer();
		this.damage = damage;
		//super.setDirection(1,0);
		if (lastKey == KeyEvent.VK_LEFT || lastKey == KeyEvent.VK_A) {
            this.setDirection(-1,0);
            if (this.getHeight() != 5){
            	this.swapSize();
            }
            	this.move(-25,0);
        	
        }

        else if (lastKey == KeyEvent.VK_RIGHT || lastKey == KeyEvent.VK_D) {
            this.setDirection(1,0);
            if (this.getHeight() != 5){
            	this.swapSize();
            }
            	this.move(20,0);
        }

        else if (lastKey == KeyEvent.VK_UP || lastKey == KeyEvent.VK_W) {
            this.setDirection(0,-1);
            if (this.getHeight() != 10){
            	this.swapSize();
            }
            	this.move(0,-25);
        }

        else if (lastKey == KeyEvent.VK_DOWN || lastKey == KeyEvent.VK_S) {
            this.setDirection(0,1);
            if (this.getHeight() != 10){
            	this.swapSize();
            }
            	this.move(0,20);
    	}
		this.setCollision(true);
		this.setSpeed(SPEED);
		this.setHealth(1);
	}

	public void run(){
		while(this.isVisible()){
			try{
				for (Tank opponent:Paint.tanks){
					if (opponent.getPlayer() == this.player)
						continue;
					this.getSprite().sleep(this.getSpeed());
					Paint.p.updateBlock(this);
					ArrayList missileList = opponent.getMissiles();

					

	       			for (int i = 0; i < missileList.size(); i++) {
	       				if (this.collisionCheck((Sprite)missileList.get(i))){
							this.collide((Sprite)missileList.get(i));
							/*this.health = 0;
							this.setVisibility(false);*/
	       				}
							
	       			}


					if (this.collisionCheck(opponent)){
						try{
							AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("Audio/metal.wav"));
							Clip clip = AudioSystem.getClip();
							clip.open(audioInputStream);
							clip.start();
						}catch(Exception e){}
						this.collide(opponent);
					}
					else
						this.notCollide(opponent);

					if (this.isDead() || !(this.isVisible())){
						this.tank.removeMissile(this);
						return;
					}
				}

			}catch(Exception e){
				System.out.println(e.getMessage());
			}
		}

	}

	
	
	public void move(){

		if (this.isDead() || this.getXPos() > 600 || this.getXPos() < 0 || this.getYPos() > 600 || this.getYPos() < 0){
			this.setVisibility(false);
		}
		
		if (!(this.isVisible()))
			return;
		

		super.move();


	}

	public void collide(Sprite object){
		object.addDamage(this.damage);
		System.out.println("Missile: "+this.getXPos()+ "," + this.getYPos());
		this.setVisibility(false);
	}

	public void notCollide(Sprite object){
		this.move();
	}
}