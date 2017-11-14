import java.applet.*;
import javax.swing.*;
import java.net.*;
import java.util.ArrayList;
public class Audio extends JApplet implements Runnable{
	public static final int FIRE = 222, MOVE = 333, BRICK = 444, METAL = 555, EXPLODE = 777;
	private static ArrayList<AudioClip> audio;
	private static URL fire;
	private static URL move;
	private static URL brick;
	private static URL metal;
	private static URL explode;
	public Audio(){
		try{
			 fire = new URL("/Audio/fire.wav");
			 move = new URL("/Audio/move.wav");
			 brick = new URL("/Audio/brick.wav");
			 metal = new URL("/Audio/metal.wav");
			 explode = new URL("/Audio/explode.wav");
		}catch(Exception e){}

	}
	

	public void run(){
		try{
			while(true){
				for (AudioClip sound : audio)
					sound.play();
			}
		}catch(Exception e){

		}
	}

	public static void playSound(int sound){
		switch(sound){
			case FIRE:
				audio.add(Applet.newAudioClip(fire));
				break;
			case MOVE:
				audio.add(Applet.newAudioClip(move));
				break;
			case BRICK:
				audio.add(Applet.newAudioClip(brick));
				break;
			case METAL:
				audio.add(Applet.newAudioClip(metal));
				break;
			case EXPLODE:
				audio.add(Applet.newAudioClip(explode));
				break;
		}
	}

	
}