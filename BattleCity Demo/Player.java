import java.net.InetAddress;


/* 
 * class for a player object in the program
*/
public class Player {
	private InetAddress address;
	private int port;
	private String name;
	private int player;
	private int type;

	/**
	 * Constructor
	 * @param name
	 * @param address
	 * @param port
	 */
	public Player(String name,InetAddress address, int port, int player, int type){
		this.address = address;
		this.port = port;
		this.name = name;
		this.player = player;
		this.type = type;
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
	
	public int getUnit(){
		return player;
	}

	public int getType(){
		return type;
	}

	public String toString(){
		String retval="";
		retval+="PLAYER ";
		retval+=name+" ";
		retval+=player+" ";
		retval+=type;
		return retval;
	}
}
