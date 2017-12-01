public class CooldownTimer implements Runnable{

	private int cooldown;
	private int skillCooldown;
	private boolean ready;
	private Tank tank;

	public CooldownTimer(int cooldown, int skillCooldown, Tank tank){
		this.cooldown = cooldown;
		this.skillCooldown = skillCooldown;
		this.tank = tank;
		this.ready = true;
	}

	public void run(){
		if (this.tank.getPlayerType() == Ninja.TYPE){
			this.tank.setVisibility(false);
			try{
				Thread.sleep(this.skillCooldown);
			}catch(Exception e){
				System.out.println(e.getMessage());
			}	
			this.tank.setVisibility(true);
		}
		else if (this.tank.getPlayerType() == Samurai.TYPE){
			int counter = 20;
			this.tank.setSpeed(5);
			while (counter > 0){
				this.tank.attack();
				try{
					Thread.sleep(this.skillCooldown);
				}catch(Exception e){
					System.out.println(e.getMessage());
				}
				counter --;
			}
			this.tank.resetSpeed();
		}
		try{
		Thread.sleep(this.cooldown);
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