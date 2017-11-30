/*
 * This class statically loads data from Map.txt file
 */
import java.util.ArrayList;
import java.util.Random;
import javax.sound.sampled.*;


public class DrawMap{
	private Map layer;
	private ArrayList<Block> blocks = new ArrayList<Block>();
	
	public DrawMap(){
		layer = Map.getFromFile("Map.txt");
		blocks = layer.setLayer();
	}

	public int[][] getMap(){
		return this.layer.getMap();
	}

	public ArrayList<Block> getBlocks(){
		return this.blocks;
	}

	public void removeBlock(Block block){
		if (block.getType() == Sprite.BRICK || block.getType()==PowerUp.AMMO_UPGRADE
			|| block.getType()==PowerUp.GRENADE || block.getType()==PowerUp.NEW_LIFE || block.getType()==PowerUp.SPEED_BOOSTER){
			this.blocks.remove(block);
			System.out.println("Block: "+block.getXPos()+ "," + block.getYPos());
			layer.removeBlock(block.getYPos()/Map.BLOCK_WIDTH,block.getXPos()/Map.BLOCK_HEIGHT);
			try{
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(this.getClass().getResource("Audio/brick.wav"));
				Clip clip = AudioSystem.getClip();
				clip.open(audioInputStream);
				clip.start();
			}catch(Exception e){}

		}
	}

	public void replaceBlock(Block block){
		if (block.getType() == Sprite.BRICK){
			this.blocks.remove(block);
			Random rand = new Random();
			int type = rand.nextInt(4)+100;

			this.blocks.add(new PowerUp(block.getXPos(),block.getYPos(),Map.BLOCK_WIDTH,Map.BLOCK_HEIGHT,type));
			layer.replaceBlock(block.getYPos()/Map.BLOCK_WIDTH,block.getXPos()/Map.BLOCK_HEIGHT,type);
		}
	}
}