import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.awt.*;
import javax.swing.*;
import java.util.ArrayList;
import java.io.FileNotFoundException;

public class Map{
	public final static int BOARD_WIDTH = 600;
	public final static int BOARD_HEIGHT = 600;
	public final static int BLOCK_WIDTH = 15;
	public final static int BLOCK_HEIGHT = 15;
	

	private int [][] map;

	public Map(int height, int width){
		map = new int[height][width];
	}

	public int[][] getMap(){
		return this.map;
	}

	public static Map getFromFile(String file){
		Map layer = null;

		ArrayList<ArrayList<Integer>> temp = new ArrayList<>();
		try{
			BufferedReader br = new BufferedReader(new FileReader(file));
			String currentLine;

			while((currentLine = br.readLine()) != null){
				if(currentLine.isEmpty()){
					continue;
				}

				ArrayList<Integer> row = new ArrayList<>();
				String[] data = currentLine.split(" ");

				for(String str:data){
					if(!(str.isEmpty())){
						int block = Integer.parseInt(str);
						row.add(block);
					}
				}
				temp.add(row);
			}
		}catch(Exception e){

		}

			int width = temp.get(0).size();
			int height = temp.size();
			layer = new Map(height, width);
			for(int x=0;x<height;x++){
				for(int y=0;y<width;y++){
					layer.map[x][y] = temp.get(x).get(y);
				}
			}
		return layer;
	}

	public ArrayList<Block> setLayer(){
		ArrayList<Block> temp = new ArrayList<Block>();
		int x =0 ,y=0, h = BOARD_HEIGHT/BLOCK_HEIGHT, w = BOARD_WIDTH/BLOCK_WIDTH;
		Block s;

		for(int j=0;j<this.map.length;j++){
			y = j % h;
			for(int i=0;i<this.map[0].length;i++){
				x = i%w;
				switch(map[j][i]){
					case Sprite.BRICK:
							s = new Block(x*BLOCK_HEIGHT,y*BLOCK_WIDTH,BLOCK_HEIGHT,BLOCK_WIDTH,Sprite.BRICK);
							temp.add(s);
							//s.sprite.start();
							break;
					case Sprite.VINE:
							s = new Block(x*BLOCK_HEIGHT,y*BLOCK_WIDTH,BLOCK_HEIGHT,BLOCK_WIDTH,Sprite.VINE);
							temp.add(s);
							//s.sprite.start();
							break;
					case Sprite.WATER:
							s = new Block(x*BLOCK_HEIGHT,y*BLOCK_WIDTH,BLOCK_HEIGHT,BLOCK_WIDTH,Sprite.WATER);
							temp.add(s);
							//s.sprite.start();
							break;
					case Sprite.METAL:
							s = new Block(x*BLOCK_HEIGHT,y*BLOCK_WIDTH,BLOCK_HEIGHT,BLOCK_WIDTH,Sprite.METAL);
							temp.add(s);
							break;
				}
			}
		}
	return temp;
	}

	public void removeBlock(int x, int y){
		map[x][y] = 0;
	}

	public void replaceBlock(int x, int y, int type){
		map[x][y] = type;
	}	
}