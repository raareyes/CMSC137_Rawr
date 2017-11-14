import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class BattleCity1 extends JFrame {
	private static int menu = 1;
	private static JButton button = new JButton();
	static JFrame frame = new BattleCity1();

	public BattleCity1() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(new Dimension(800,700));
		this.setTitle("BATTLE CITY");
		this.setIconImage((new ImageIcon ("images/tank_pointer.png")).getImage());

		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				switchMenu();
			}
		});
		button.doClick();

		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public static void clickButton() {
		button.doClick();
	}
	
	public static void changeMenu(int i) {
		menu = i;
	}
	
	public void switchMenu() {
		switch(menu) {
		
		case 1: 
			getContentPane().removeAll();
			getContentPane().add(new MainMenu1());
			revalidate();
			break;
		case 2: 
			getContentPane().removeAll();
			getContentPane().add(new Game());
		//	Game.requestFocus();
			revalidate();
			break;
		case 3:
			getContentPane().removeAll();
			getContentPane().add(new Credits());
			revalidate();
			break;
		case 5:
			getContentPane().removeAll();
			getContentPane().add(new ControlSettings());
			revalidate();
			break;
		default: 
			break;
		}
	}
	
	public static void main(String[] args) {
		Thread t1 = new Thread();
		
		frame.addKeyListener((KeyListener) Paint.tank);
		frame.addKeyListener((KeyListener) Paint.tank2);
		frame.requestFocus();
		
		t1.start();
		while(true) {
			try{
				Thread.sleep(1);
			}catch (Exception e) {}
		}
		
		
	}
	
}