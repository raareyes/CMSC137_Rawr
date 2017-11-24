public class Block extends Sprite{
	public Block(int xpos,int ypos,int height,int width,int type){
		super(xpos,ypos,height,width,type);
		this.setHealth(1);
		if (type == Sprite.VINE)
			this.setCollision(false);
		else
			this.setCollision(true);
		if (this.getType() == Sprite.METAL)
			this.setHealth(Sprite.INFINITE);
	}

	public void run(){
	}

	public void collide(Sprite object){
	}

	public void notCollide(Sprite object){
	}
}