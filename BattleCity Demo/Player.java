import java.net.InetAddress;


/* 
 * abstraction for a player object in the program
*/
public class Player {
	private InetAddress address;
	private int port;
	private String name;
	private int tank;

	/**
	 * Constructor
	 * @param name
	 * @param address
	 * @param port
	 */
	public Player(String name,InetAddress address, int port, int tank){
		this.address = address;
		this.port = port;
		this.name = name;
		this.tank = tank;
	}

	public InetAddress getAddress(){
		return address;
	}

	public int getPort(){
		return port;
	}
	public String getName(){
		return name;
	}
	
	public int getTank(){
		return tank;
	}

	public String toString(){
		String retval="";
		retval+="PLAYER ";
		retval+=name+" ";
		retval+=tank;
		return retval;
	}
}
