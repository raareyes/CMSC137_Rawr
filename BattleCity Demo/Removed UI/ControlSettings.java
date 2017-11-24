import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

import java.awt.event.*;
import java.awt.image.*;

public class ControlSettings extends JPanel {
	static BufferedImage content,back1,back2;
	private JLabel back;
	
	public ControlSettings() {
	//	this.setPrefferedSize(new Dimension(800,700));
		this.setLayout(new BorderLayout());
		
		JPanel backPanel = new JPanel();
		backPanel.setOpaque(false);
		
		try {
			content = ImageIO.read(new File("images/frame_settings.png"));
			back1 = ImageIO.read(new File("images/backarrow_1.png"));
			back2 = ImageIO.read(new File("images/backarrow_2.png"));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		back = new JLabel();
		back.setIcon(new ImageIcon(back2));
		
		back.addMouseListener(new MouseListener() {
			public void mouseEntered(MouseEvent e){
				back.setIcon(new ImageIcon(back1));
			}
			public void mouseExited(MouseEvent e){
				back.setIcon(new ImageIcon(back2));
			}
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){
				back.setIcon(new ImageIcon(back2));
			}
			public void mouseReleased(MouseEvent e){
				back.setIcon(new ImageIcon(back1));
				BattleCity1.changeMenu(1);
				BattleCity1.clickButton();
			}
		});
		
		add(backPanel,BorderLayout.PAGE_END);
		backPanel.add(back);
	}
	
	public void paintComponent(Graphics g) {
		ImageIcon icon = new ImageIcon("images/frame_settings.png");
		Image img = icon.getImage();
		
		super.paintComponent(g);
		g.drawImage(img,0,0,null);
		
	}
}





