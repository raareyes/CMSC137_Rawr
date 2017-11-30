import java.util.Random;
import java.awt.event.*;
import java.awt.*;

/* 
 *This super class of the elements within the program
*/
public abstract class Sprite implements Runnable{
	static final int SPACE = 0;
	static final int TANK = 1;
	static final int MISSILE = 2;
	static final int BRICK = 3;
	static final int METAL = 4;
	static final int VINE = 5;
	static final int WATER = 6;
	static final int POWERUP = 7;

	static final int INFINITE = 9999999;

	private int xPos, dx;
	private int yPos, dy;
	private int speed;
	private int origSpeed;
	private int height;
	private int width;
	private int health;
	private int level;
	private int type;
	private boolean collision;
	private boolean visibility;
	private Image image = null;
	private Thread sprite;
	//private Timer tm;

	public Sprite(int xPos,int yPos,int height,int width,int type){
		this.xPos = xPos;
		this.yPos = yPos;
		this.height = height;
		this.width = width;
		this.type = type;
		this.visibility = true;
		this.sprite = new Thread (this);
		this.image = image;
		this.setDirection(0,0);
		//this.tm = new Timer(1, this);
	}

	public void setLevel(int level){
		this.level = level;
	}

	public Thread getSprite(){
		return this.sprite;
	}

	public void move(int dx,int dy){
		this.xPos += dx;
		this.yPos += dy;
	}

	public void setImage(Image image){
		this.image = image;
	}

	public void setSpeed(int speed){
		this.speed = speed;
	}
	
	public void origSpeed(int speed){
		this.origSpeed = speed;
	}

	public void resetSpeed(){
		this.speed = 10;
	}

	public int getSpeed(){
		return this.speed;
	}	

	public void setCollision(boolean collision){
		this.collision = collision;
	}

	public void setVisibility(boolean visibility){
		this.visibility = visibility;
	}

	public void setCoor(int xPos,int yPos){
		this.xPos = xPos;
		this.yPos = yPos;
	}

	public void setDirection(int dx,int dy){
		this.dx = dx;
		this.dy = dy;
	}

	public void setHealth(int health){
		this.health = health;
	}

	public int getHealth(){
		return this.health;
	}

	public int getXPos(){
		return this.xPos;
	}

	public int getDX(){
		return this.dx;
	}

	public int getDY(){
		return this.dy;
	}

	public Image getImage(){
		return this.image;
	}

	public int getYPos(){
		return this.yPos;
	}

	public int getHeight(){
		return this.height;
	}

	public int getWidth(){
		return this.width;
	}

	public boolean isDead(){
		return this.health<=0?true:false;
	}
	
	public int getType(){
		return this.type;
	}

	public boolean isVisible(){
		return this.visibility;
	}

	public boolean canCollide(){
		return this.collision;
	}

	public void move(){
		this.xPos += this.dx;
		this.yPos += this.dy;
	}

	public void swapSize(){
		int temp = height;
		this.height = this.width;
		this.width = temp;
	}

	public void addDamage(int damage){
		this.health -= damage;	
		//System.out.println(this.health);
	}

	public boolean collisionCheck(Sprite object){
		Rectangle thisBounds = new Rectangle(this.getXPos() + this.dx,this.getYPos() + this.dy,this.height,this.width);
    Rectangle objectBounds = new Rectangle(object.getXPos(),object.getYPos(),object.getHeight(),object.getWidth());
   
    return (this.collision && thisBounds.intersects(objectBounds));
	}

	public abstract void collide(Sprite object);

	public abstract void notCollide(Sprite object);



}