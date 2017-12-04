//package ph.edu.uplb.ics.cmsc137;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.awt.Color;


public class UDPServer implements Runnable{
	public static boolean isStarting = false;
	boolean gameON =false;
	String playerData;
	int playerCount=0;
	String host;
    DatagramSocket serverSocket = null;
    Paint game;
	private int id;
	//int gameStage=WAITING_FOR_PLAYERS;
	int numPlayers;
	int port;
	int lives;
	ArrayList<Player> players = new ArrayList<Player>();
	Thread t = new Thread(this);
	public UDPServer(String name,int numPlayers,int port, int lives){
		try {
            serverSocket = new DatagramSocket(port);
			serverSocket.setSoTimeout(100);
		} catch (IOException e) {
            System.err.println("Could not listen on port: "+port);
            System.exit(-1);
		}catch(Exception e){}
		//Create the game state
	//	game = new GameState();
		this.host = name;
		this.numPlayers = numPlayers;
		this.port = port;
		this.lives = lives;
		//Start the game server thread
		t.start();
		System.out.println("Game created...");
		
	}
	
	/**
	 * Helper method for broadcasting data to all players
	 * @param msg
	 */
	public void broadcast(String msg){
		for(Player player : players){		
			send(player,msg);	
		}
	}


	/**
	 * Send a message to a player
	 * @param player
	 * @param msg
	 */
	public void send(Player player, String msg){
		DatagramPacket packet;	
		byte buf[] = msg.getBytes();		
		packet = new DatagramPacket(buf, buf.length, player.getAddress(),player.getPort());
		try{
			serverSocket.send(packet);
		}catch(IOException ioe){
			ioe.printStackTrace();
		}
	}

	private void initGame(){
		for(Player player : players){		
			send(player,("GENERATING "+playerCount+" "+player.getUnit()));	
		}
		
		game = new Paint(playerCount, players, lives);
		for(Unit player : game.getUnits()){
			// NEW PLAYER NAME TANKID X Y
			broadcast("NEW "+players.get(player.getPlayer()).toString()+" "+player.getXPos()+" "+player.getYPos() + " " + lives);
		}
		broadcast("STARTING");
	}

	private void updateState(String data){
		if (!data.equals("") && !data.startsWith("PLAYER DIED")){
			String[] dataStream = data.split(" ");
			int playerid = Integer.parseInt(dataStream[1]);
			int keyid = Integer.parseInt(dataStream[3]);
			Unit player = game.players.get(playerid);
			//System.out.println("IT RECIEVED!!");
			if (dataStream[2].equals("SCORES")){
				player.addScore(keyid);
			//	System.out.println("IT PRESSED!!");
			}
			else if (dataStream[2].equals("PRESSED")){
				player.keyPressed(keyid,playerid);
			//	System.out.println("IT PRESSED!!");
			}
			else if (dataStream[2].equals("RELEASED")){
				// System.out.println("IT RELEASED!!");
				player.keyReleased(keyid,playerid);
			}
		}
	}

	private void sychronizePosition(){
		for(Unit player : game.getUnits()){
			// NEW PLAYER NAME TANKID X Y
			broadcast("SYNCING "+player.toString());
		}
	}

	private boolean checkEnd(){
		int counter = 0;
		for(Unit player : game.getUnits()){
			// NEW PLAYER NAME TANKID X Y
			if (player.isAlive())
				counter++;
		}
		return counter<2;
	}
	
	/**
	 * The juicy part
	 */
	public void run(){
		int counter = 0;
		System.out.println("Listening at port "+this.port);
		while(true){
			counter= counter != 1000?counter+1:1;	
			// Get the data from players
			byte[] buf = new byte[256];
			DatagramPacket packet = new DatagramPacket(buf, buf.length);
			try{
     			serverSocket.receive(packet);
			}catch(Exception ioe){}
			
			/**
			 * Convert the array of bytes to string
			 */
			playerData=new String(buf);
			
			//remove excess bytes
			playerData = playerData.trim();

			String tokens[] = playerData.split(" ");
			if (playerData.startsWith("CONNECT")){
				players.add(new Player(tokens[1],packet.getAddress(),packet.getPort(),playerCount,Integer.parseInt(tokens[2])));
				System.out.println("Player connected: "+tokens[1]);
				broadcast("CONNECTED "+tokens[1]);
				playerCount++;
				System.out.println("Player Count: "+playerCount+"/"+numPlayers);
			}
			//System.out.println("Player Data: "+playerData);
			else if (playerData.startsWith("PLAYER OUT")){

				broadcast("KILL "+Integer.parseInt(tokens[2]));
				game.players.get(Integer.parseInt(tokens[2])).kill();
				continue;
			}
			else if (playerData.startsWith("PLAYER DIED")){
				int id = Integer.parseInt(tokens[2]);
				Unit player = game.getUnits().get(id);
				player.randCoor();
				player.spawn(player.getXPos(),player.getYPos());
				broadcast("RESPAWN "+id+" "+player.getXPos()+" "+player.getYPos());
			}

			else if (playerData.startsWith("PLAYER")){
				if (tokens[2].equals("SCORES")){
					if (players.get(Integer.parseInt(tokens[1])).getAddress().equals(packet.getAddress()) && players.get(Integer.parseInt(tokens[1])).getPort() == packet.getPort())
						broadcast(playerData);
					continue;
				}
				// System.out.println(tokens[2].equals("SCORES") +" "+ players.get(Integer.parseInt(tokens[1])).getAddress().equals(packet.getAddress()) +" "+ (players.get(Integer.parseInt(tokens[1])).getPort() == packet.getPort()));
				// System.out.println("Packet:"+packet.getAddress()+" "+packet.getPort());
				// System.out.println("Player:"+players.get(Integer.parseInt(tokens[1])).getAddress()+" "+players.get(Integer.parseInt(tokens[1])).getPort());
				// System.out.println("Player "+packet.getAddress()+" Data: "+players.get(Integer.parseInt(tokens[1])).getAddress() +" "+playerData);
				updateState(playerData);
				broadcast(playerData);
			}

			if (playerCount == numPlayers && !gameON || UDPServer.isStarting){
				initGame();
				gameON = true;
				UDPServer.isStarting = false;
			}
			//tries to sychronize the players' position throughout the clients
			if (counter%100==0)
				sychronizePosition();
			if (checkEnd() && gameON)
				broadcast("FINISH");
		}

	}	
	public void setLives(int lives){
		this.lives = lives;
	}
	
	
	public static void main(String args[]){
		if (args.length <3 ){
			System.out.println("Usage: java Server <name> <number of players> <port> <lives>");
			System.exit(1);
		}
		
		UDPServer s = new UDPServer(args[0],Integer.parseInt(args[1]),Integer.parseInt(args[2]),Integer.parseInt(args[3]));
		//new Paint("localhost",args[0],4000);
	}
}

