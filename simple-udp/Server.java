import java.net.*;
import java.io.*;
import java.util.*;

public class Server implements Runnable {
  Thread thread = new Thread(this);
  DatagramSocket dataReceiver;
  String playerData;
  // ArrayList<Client> clientContainer = new ArrayList<Client>();

  public static void main(String args[]) throws Exception {
    new Server();
  }

  public Server() {
    try {
      dataReceiver = new DatagramSocket(8080);
      dataReceiver.setSoTimeout(100);
    } catch(IOException e) {
      System.err.println("Could not listen to this port: " + 8080);
      System.exit(-1);
    } catch(Exception e) {}
    
    System.out.println("server created");

    thread.start();
  }

  //copy paste from sample udp
  public void run() {
    while(true) {

      byte[] buf = new byte[1024];
      DatagramPacket packet = new DatagramPacket(buf, buf.length);
      try { 
        dataReceiver.receive(packet);
      } catch(Exception e) {
        //preferably dont print exception everytime since this is continous 
        //and when it does not receive a packet, the exception will print

        // System.out.println(e);
      }

      playerData = new String(buf);

      playerData = playerData.trim();

      System.out.println(playerData);

      try {
        thread.sleep(500);
      } catch(Exception e) {
        System.out.println(e);
      }
    }
  }
}