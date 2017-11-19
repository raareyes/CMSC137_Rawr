//package ph.edu.uplb.ics.cmsc137;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * The main game server. It just accepts the messages sent by one player to
 * another player
 * @author Joseph Anthony C. Hermocilla
 *
 */

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
		
		/**
		 * Convert the array of bytes to string
		 */
		data=new String(buf);
		
		//remove excess bytes
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
			if (!data.equals("")){
				String[] dataStream = data.split(" ");
				int tankid = Integer.parseInt(dataStream[1]);
				int keyid = Integer.parseInt(dataStream[3]);
				Tank tank = Paint.tanks.get(tankid);
				System.out.println("IT RECIEVED!!");
				if (dataStream[2].equals("PRESSED")){
					tank.keyPressed(keyid,tankid);
					System.out.println("IT PRESSED!!");
				}
				else if (dataStream[2].equals("RELEASED")){
					System.out.println("IT RELEASED!!");
					tank.keyReleased(keyid,tankid);
				}
			}
		}
	}	
}

