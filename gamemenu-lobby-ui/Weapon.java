
import javax.swing.ImageIcon;
import java.awt.Image;
public class Weapon{
	public static final String WD = "WeaponDown.png";
	public static final String WU = "WeaponUp.png";
	public static final String WL = "WeaponLeft.png";
	public static final String WR = "WeaponRight.png";
	public static final String AD1 = "Attack1Down.png";
	public static final String AU1 = "Attack1Up.png";
	public static final String AL1 = "Attack1Left.png";
	public static final String AR1 = "Attack1Right.png";
	public static final String AD2 = "Attack2Down.png";
	public static final String AU2 = "Attack2Up.png";
	public static final String AL2 = "Attack2Left.png";
	public static final String AR2 = "Attack2Right.png";

	private Image image;
	private int width;
	private int height;
	private int xPos;
	private int yPos;
	private int origWidth;
	private int origHeight;

	public Weapon(String image,int width,int height, int x, int y){
		this.image = new ImageIcon(image).getImage();
		this.width = width;
		this.height = height;
		this.origWidth = width;
		this.origHeight = height;
		this.xPos = x;
		this.yPos = y;
	}

	public void setImage(String image){
		this.image = new ImageIcon(image).getImage();
	}

	public Image getImage(){
		return this.image;
	}

	public int getWidth(){
		return this.width;
	}

	public int getHeight(){
		return this.height;
	}

	public void setWidth(int width){
		this.width = width;
	}


	public void setHeight(int height){
		this.height = height;
	}

	public void resetDimension(){
		this.height = this.origHeight;
		this.width = this.origWidth;
	}
	public void setCoord(int x, int y){
		this.xPos = x;
		this.yPos = y;
	}

	public int getXPos(){
		return this.xPos;
	}

	public int getYPos(){
		return this.yPos;
	}
}