import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

import java.awt.event.*;
import java.awt.image.*;

public class Credits extends JPanel {
	static BufferedImage helpTitle,helpContent,back1,back2;
	private JLabel title,content,back;
	
	public void paintComponent(Graphics g) {
		ImageIcon icon = new ImageIcon("images/credits.png");
		Image img = icon.getImage();
		
		super.paintComponent(g);
		g.drawImage(img,0,0,null);
	}
}