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

/**
 * The main game server. It just accepts the messages sent by one player to
 * another player
 * @author Joseph Anthony C. Hermocilla
 *
 */

public class Server implements Runnable{

	boolean gameON =false;
	String playerData;
	int playerCount=0;
	String host;
    DatagramSocket serverSocket = null;
    Paint game;
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
	
	/**
	 * The juicy part
	 */
	public void run(){
		while(true){
						
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

			if (playerData.startsWith("CONNECT")){
				String tokens[] = playerData.split(" ");
				players.add(new Player(tokens[1],packet.getAddress(),packet.getPort(),playerCount));
				System.out.println("Player connected: "+tokens[1]);
				broadcast("CONNECTED "+tokens[1]);
				playerCount++;
				System.out.println("Player Count: "+playerCount+"/"+numPlayers);
			}
			//System.out.println("Player Data: "+playerData);

			else if (!playerData.equals("")){
				System.out.println("Player Data:"+playerData);
				broadcast(playerData);
			}

			if (playerCount == numPlayers && !gameON){
				initGame();
				gameON = true;
			}
		
			// process
			/*switch(gameStage){
				  case WAITING_FOR_PLAYERS:
						//System.out.println("Game State: Waiting for players...");
						if (playerData.startsWith("CONNECT")){
							String tokens[] = playerData.split(" ");
							NetPlayer player=new NetPlayer(tokens[1],packet.getAddress(),packet.getPort());
							System.out.println("Player connected: "+tokens[1]);
							game.update(tokens[1].trim(),player);
							broadcast("CONNECTED "+tokens[1]);
							playerCount++;
							if (playerCount==numPlayers){
								gameStage=GAME_START;
							}
						}
					  break;	
				  case GAME_START:
					  System.out.println("Game State: START");
					  broadcast("START");
					  gameStage=IN_PROGRESS;
					  break;
				  case IN_PROGRESS:
					  //System.out.println("Game State: IN_PROGRESS");
					  
					  //Player data was received!
					  if (playerData.startsWith("PLAYER")){
						  //Tokenize:
						  //The format: PLAYER <player name> <x> <y>
						  String[] playerInfo = playerData.split(" ");					  
						  String pname =playerInfo[1];
						  int x = Integer.parseInt(playerInfo[2].trim());
						  int y = Integer.parseInt(playerInfo[3].trim());
						  //Get the player from the game state
						  NetPlayer player=(NetPlayer)game.getPlayers().get(pname);					  
						  player.setX(x);
						  player.setY(y);
						  //Update the game state
						  game.update(pname, player);
						  //Send to all the updated game state
						  broadcast(game.toString());
					  }
					  break;
			}				  
		*/}

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

