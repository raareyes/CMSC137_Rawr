public class CooldownTimer implements Runnable{

	private int cooldown;
	private int skillCooldown;
	private boolean ready;
	private Unit player;

	public CooldownTimer(int cooldown, int skillCooldown, Unit player){
		this.cooldown = cooldown;
		this.skillCooldown = skillCooldown;
		this.player = player;
		this.ready = true;
	}

//Contains the usage of skill and 
	public void run(){
		//this is the skill of ninja
		if (this.player.getPlayerType() == Ninja.TYPE){
			this.player.setVisibility(false); //turns invisible
			try{
				Thread.sleep(this.skillCooldown);
			}catch(Exception e){
				System.out.println(e.getMessage());
			}	
			this.player.setVisibility(true); //returns visibility
		}
		//this is the skill of sumurai
		else if (this.player.getPlayerType() == Samurai.TYPE){
			int counter = 5;
			this.player.setSpeed(5); // charging speed (speed buff)
			while (counter > 0){	//attacks 20 times with half second difference
				this.player.unload();
				this.player.attack();
				try{
					Thread.sleep(this.skillCooldown);
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
				counter --;
			}
			this.player.resetSpeed();
		}
		try{
		Thread.sleep(this.cooldown);	//the skill cooldown
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
		this.ready = true;
	}

	public boolean isReady(){
		return this.ready;
	}

	public void activate(){
		if (this.ready){
			this.ready = false;
			(new Thread (this)).start();
		}

	}
}