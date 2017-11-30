import javax.sound.sampled.*;

/*
 * This class is not yet implemented
*/
public class PowerUp extends Block{
	public final static int SPEED_BOOSTER = 100;
	public final static int AMMO_UPGRADE = 101;
	public final static int NEW_LIFE = 102;
	public final static int MAX_LIFE = 10;
	public final static int MAX_SPEED = 7;
	public final static int MAX_DAMAGE = 3;
	public final static int GRENADE = 103;

	public PowerUp(int xpos,int ypos,int height,int width, int type){
		super(xpos, ypos, height, width,type);
	}

	public void addEffect(Tank tank){

		System.out.println("GGG");
		if(this.getType()==SPEED_BOOSTER){
			if(tank.getSpeed()>MAX_SPEED){
				tank.setSpeed(-1);
			}
		}else if(this.getType()==AMMO_UPGRADE){
			if(tank.getDamage()+1<=MAX_DAMAGE){
				tank.setDamage(1);
				}
			
		}else if(this.getType()==NEW_LIFE){
			if(tank.getHealth()+1 <= 10){
					tank.setHealth(1);
			}
		}
		else if(this.getType()==GRENADE){
			try{
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("Audio/explode.wav"));
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
			}catch(Exception e){}

			for (Tank opponent:Paint.tanks){
				if (opponent.getPlayer() == tank.getPlayer())
					continue;
				opponent.addDamage(3);
			}
		}
	}

	public void collide(Sprite object){}

	public void notCollide(Sprite object){}
}