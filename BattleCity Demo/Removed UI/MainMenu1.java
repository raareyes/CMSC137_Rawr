import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

import java.awt.event.*;
import java.awt.image.*;

public class MainMenu1 extends JPanel {
	static BufferedImage player1,player2,settings1,settings2,tankPtr,highscore,credits1,credits2;
	private JLabel p1,p1Ptr,p2,p2Ptr,settings,credits;
	
	
	public MainMenu1() {
		setLayout(new BorderLayout());
		
		try {
			player1 = ImageIO.read(new File("images/p1.png"));
			player2 = ImageIO.read(new File("images/p2.png"));
			settings1 = ImageIO.read(new File("images/settings_1.png"));
			settings2 = ImageIO.read(new File("images/settings_2.png"));
			tankPtr = ImageIO.read(new File("images/tank_pointer.png"));
			credits1 = ImageIO.read(new File("images/credits1.png"));
			credits2 = ImageIO.read(new File("images/credits2.png"));
		} catch (Exception e) {}
		
		JPanel main = new JPanel();
		main.setLayout(new GridLayout(3,1));
		main.setOpaque(false);
		main.setPreferredSize(new Dimension(550, 200));
		
		JPanel playMenu = new JPanel();
		playMenu.setOpaque(false);
		playMenu.setLayout(new GridLayout(2,2,0,0));

		JPanel players = new JPanel();
		playMenu.setOpaque(false);
		
		p1 = new JLabel();
		p1.setIcon(new ImageIcon(player1));
		p1Ptr = new JLabel(new ImageIcon(tankPtr),JLabel.RIGHT);
		
		p2 = new JLabel();
		p2.setIcon(new ImageIcon(player2));
		p2Ptr = new JLabel(new ImageIcon(tankPtr),JLabel.RIGHT);
		p2Ptr.setVisible(false);
		
		p1.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){
				p1Ptr.setVisible(true);
				p2Ptr.setVisible(false);
			}
			public void mouseExited(MouseEvent e) {}
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
		});
		
		p2.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){
				p2Ptr.setVisible(true);
				p1Ptr.setVisible(false);
			}
			public void mouseExited(MouseEvent e) {}
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseReleased(MouseEvent e){
				BattleCity1.changeMenu(2);
				BattleCity1.clickButton();
			}
		});
/*		
		JPanel creditsPanel = new JPanel();
		creditsPanel.setOpaque(false);
	//	creditsPanel.setBackground(Color.RED);
		creditsPanel.setPreferredSize(new Dimension(50,50));
		credits = new JLabel(new ImageIcon(credits1),JLabel.RIGHT);
		credits.setPreferredSize(new Dimension(250,100));
		credits.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){
				credits.setIcon(new ImageIcon(credits2));
			}
			public void mouseExited(MouseEvent e) {
				credits.setIcon(new ImageIcon(credits1));
			}
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){}
			public void mouseReleased(MouseEvent e){}
		});
*/		
		JPanel settingsPanel = new JPanel();
		settingsPanel.setOpaque(false);
		settingsPanel.setLayout(new BorderLayout());
		settings = new JLabel();
		settings.setIcon(new ImageIcon(settings2));
		settings.addMouseListener(new MouseListener(){
			public void mouseEntered(MouseEvent e){
				settings.setIcon(new ImageIcon(settings1));
			}
			public void mouseExited(MouseEvent e) {
				settings.setIcon(new ImageIcon(settings2));
			}
			public void mouseClicked(MouseEvent e){}
			public void mousePressed(MouseEvent e){
				settings.setIcon(new ImageIcon(settings2));
			}
			public void mouseReleased(MouseEvent e){
				settings.setIcon(new ImageIcon(settings1));
				BattleCity1.changeMenu(5);
				BattleCity1.clickButton();
			}
		});
		
	//	hiscore = new JLabel(new ImageIcon(highscore));
	
		JPanel upper = new JPanel();
		upper.setOpaque(false);
		upper.setPreferredSize(new Dimension(100,150));
			
		add(main,BorderLayout.EAST);
		add(upper,BorderLayout.NORTH);
		playMenu.add(p1Ptr);
		playMenu.add(p1);
		playMenu.add(p2Ptr);
		playMenu.add(p2);
		main.add(playMenu);
	//	creditsPanel.add(credits);
	//	add(creditsPanel,BorderLayout.SOUTH);
		settingsPanel.add(settings,BorderLayout.LINE_END);
	 	add(settingsPanel,BorderLayout.PAGE_END);
	}
	
	public void paintComponent(Graphics g) {
		ImageIcon bgMenu = new ImageIcon("images/menu_bg.png");
		Image icon = bgMenu.getImage();
		
		super.paintComponent(g);
		g.drawImage(icon,0,0,null);
	}
	
}