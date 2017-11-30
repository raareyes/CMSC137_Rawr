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


public class Server implements Runnable{

	boolean gameON =false;
	String playerData;
	int playerCount=0;
	String host;
    DatagramSocket serverSocket = null;
    Paint game;
	private int id;
	//int gameStage=WAITING_FOR_PLAYERS;
	int numPlayers;
	ArrayList<Player> players = new ArrayList<Player>();
	Thread t = new Thread(this);
	public Server(String name,int numPlayers){
		try {
            serverSocket = new DatagramSocket(3000);
			serverSocket.setSoTimeout(100);
		} catch (IOException e) {
            System.err.println("Could not listen on port: "+3000);
            System.exit(-1);
		}catch(Exception e){}
		//Create the game state
	//	game = new GameState();
		this.host = name;
		this.numPlayers = numPlayers;
		//Start the game thread
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
			send(player,("GENERATING "+playerCount+" "+player.getTank()));	
		}
		
		game = new Paint(playerCount, players);
		for(Tank tank : game.getTanks()){
			// NEW PLAYER NAME TANKID X Y
			broadcast("NEW "+players.get(tank.getPlayer()).toString()+" "+tank.getXPos()+" "+tank.getYPos());
		}
		broadcast("STARTING");
	}

	private void updateState(String data){
		if (!data.equals("") && !data.startsWith("PLAYER DIED")){
			String[] dataStream = data.split(" ");
			int tankid = Integer.parseInt(dataStream[1]);
			int keyid = Integer.parseInt(dataStream[3]);
			Tank tank = game.tanks.get(tankid);
			//System.out.println("IT RECIEVED!!");
			if (dataStream[2].equals("SCORES")){
				tank.addScore(keyid);
			//	System.out.println("IT PRESSED!!");
			}
			else if (dataStream[2].equals("PRESSED")){
				tank.keyPressed(keyid,tankid);
			//	System.out.println("IT PRESSED!!");
			}
			else if (dataStream[2].equals("RELEASED")){
				// System.out.println("IT RELEASED!!");
				tank.keyReleased(keyid,tankid);
			}
		}
	}

	private void sychronizePosition(){
		for(Tank tank : game.getTanks()){
			// NEW PLAYER NAME TANKID X Y
			broadcast("SYNCING "+tank.toString());
		}
	}

	private boolean checkEnd(){
		int counter = 0;
		for(Tank tank : game.getTanks()){
			// NEW PLAYER NAME TANKID X Y
			if (tank.isAlive())
				counter++;
		}
		return counter<2;
	}
	
	/**
	 * The juicy part
	 */
	public void run(){
		int counter = 0;
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
				players.add(new Player(tokens[1],packet.getAddress(),packet.getPort(),playerCount));
				System.out.println("Player connected: "+tokens[1]);
				broadcast("CONNECTED "+tokens[1]);
				playerCount++;
				System.out.println("Player Count: "+playerCount+"/"+numPlayers);
			}
			//System.out.println("Player Data: "+playerData);
			else if (playerData.startsWith("PLAYER OUT")){

				broadcast("KILL "+Integer.parseInt(tokens[2]));
				game.tanks.get(Integer.parseInt(tokens[2])).kill();
				continue;
			}
			else if (playerData.startsWith("PLAYER DIED")){
				int id = Integer.parseInt(tokens[2]);
				Tank tank = game.getTanks().get(id);
				tank.randCoor();
				tank.spawn(tank.getXPos(),tank.getYPos());
				broadcast("RESPAWN "+id+" "+tank.getXPos()+" "+tank.getYPos());
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

			if (playerCount == numPlayers && !gameON){
				initGame();
				gameON = true;
			}
			//tries to sychronize the players' position throughout the clients
			if (counter%100==0)
				sychronizePosition();
			if (checkEnd() && gameON)
				broadcast("FINISH");
		}

	}	
	
	
	public static void main(String args[]){
		/*if (args.length != 1){
			System.out.println("Usage: java -jar circlewars-server <number of players>");
			System.exit(1);
		}*/
		
		Server s = new Server(args[0],Integer.parseInt(args[1]));
		//new Paint("localhost",args[0],4000);
	}
}
