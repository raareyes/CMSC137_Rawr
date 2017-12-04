//package ph.edu.uplb.ics.cmsc137;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
//import java.awt.event.*;

public class ClientReceiver implements Runnable{

  DatagramSocket socket = null;
	int port;
	Paint game;
	Thread t = new Thread(this);
	public ClientReceiver(Paint game, int port, DatagramSocket socket){
		this.socket = socket;
		this.port = port;
		this.game = game;
		//Start the game thread
		t.start();
		
	}

	public String receiveData(DatagramSocket socket){
		String data;
		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		try{
 			socket.receive(packet);
		}catch(Exception ioe){}
		
		//convert data 
		data=new String(buf);
		
		//clean unecessary data
		data = data.trim();

		if (!data.equals("")){
			System.out.println("Data: "+data);
		}
		return data;
	}

	public void run(){
		while(true){
			try{
				Thread.sleep(100);
			}catch(Exception e){
				System.out.println(e.getMessage());
			}
			String data = receiveData(this.socket);
			String[] dataStream = data.split(" ");
			if (data.startsWith("FINISH")){	//the game is done
				Paint.gameState = Paint.RESULTS;
				System.exit(0);
				break;
			}
			else if (data.startsWith("KILL")){	//a player was killed
				Paint.players.get(Integer.parseInt(dataStream[1])).kill();
			}
			else if (data.startsWith("SYNCING PLAYER")){
				int playerid = Integer.parseInt(dataStream[2]);
				int x = Integer.parseInt(dataStream[3]);
				int y = Integer.parseInt(dataStream[4]);
				Paint.players.get(playerid).setCoor(x,y);
			}
			else if (!data.equals("") && !data.startsWith("PLAYER OUT")){ //a player is out of the game
				int playerid = Integer.parseInt(dataStream[1]);
				if (data.startsWith("RESPAWN")){ //a player respawns a new character
					Unit player = Paint.players.get(playerid);
					player.spawn(Integer.parseInt(dataStream[2]),Integer.parseInt(dataStream[3]));
					player.setLife(player.getLife()-1);
				}
				int keyid = Integer.parseInt(dataStream[3]);
				Unit player = Paint.players.get(playerid);
				// System.out.println("IT RECIEVED!!");
				/*if (!((keyid == KeyEvent.VK_E) || (keyid == KeyEvent.VK_SPACE)))
	        		continue;*/
	        	if (dataStream[2].equals("SCORES")){ //this player scores
					player.addScore(keyid);
					// System.out.println("IT SCORED!!");
				}
				else if (dataStream[2].equals("PRESSED")){ // a player pressed a new key
					player.keyPressed(keyid,playerid);
					// System.out.println("IT PRESSED!!");
				}
				else if (dataStream[2].equals("RELEASED")){ // a player releases a key
					// System.out.println("IT RELEASED!!");
					player.keyReleased(keyid,playerid);
				}
			}
		}
	}	
}

