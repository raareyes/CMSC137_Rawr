import java.awt.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;

import java.awt.event.*;
import java.awt.image.*;

public class Game extends JPanel {
	private BufferedImage tank1,tank2,logo1,logo2,heart;
	Paint gamePanel = new Paint();	
	Thread board = new Thread(gamePanel);
	static JLabel p1Life = new JLabel();
	
	public Game() {
		setSize(new Dimension(800,700));
		setLayout(new BorderLayout());
		
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.setPreferredSize(new Dimension(800,40));
		JPanel bot = new JPanel();
		bot.setPreferredSize(new Dimension(800,20));
		
		JPanel right = new JPanel();
		right.setPreferredSize(new Dimension(80,700));
		right.setLayout(new BorderLayout());
		
		JPanel left = new JPanel();
		left.setPreferredSize(new Dimension(80, 700));
		left.setLayout(new BorderLayout());
		
		top.setBackground(Color.DARK_GRAY);
		left.setBackground(Color.DARK_GRAY);
		right.setBackground(Color.DARK_GRAY);
		bot.setBackground(Color.DARK_GRAY);
		
		add(top,BorderLayout.NORTH);
		add(left,BorderLayout.EAST);
		add(right,BorderLayout.WEST);
		add(bot,BorderLayout.SOUTH);
		
		try {
			tank1 = ImageIO.read(new File("images/tank1.png"));
			tank2 = ImageIO.read(new File("images/tank2.png"));
		//	logo1 = ImageIO.read(new File("images/player1.png"));
		//	logo2 = ImageIO.read(new File("images/player2.png"));
			heart = ImageIO.read(new File("images/life0.png"));
		} catch(Exception e) {}
		
		JPanel p1 = new JPanel();
		p1.setOpaque(false);
		p1.setLayout(new GridLayout(2,0));
		
		JLabel p1Tank = new JLabel(new ImageIcon(tank1));

		p1.add(Paint.tank.lives);
		p1.add(p1Tank);	
		right.add(p1,BorderLayout.PAGE_END);
	
		
		JPanel p2 = new JPanel();
		p2.setOpaque(false);
		p2.setLayout(new GridLayout(2,0));
		
		JLabel p2Tank = new JLabel(new ImageIcon(tank2));

		p2.add(Paint.tank2.lives);
		p2.add(p2Tank);
		left.add(p2,BorderLayout.PAGE_END);
		
		gamePanel.setBackground(Color.BLACK);		
		// addKeyListener((KeyListener) Paint.tank);
		// addKeyListener((KeyListener) Paint.tank2);		
		
		add(gamePanel,BorderLayout.CENTER);
		
		board.start();
		Paint.tank1Thread.start();
		Paint.tank2Thread.start();
	} 
	
}