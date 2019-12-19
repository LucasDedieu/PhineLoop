package fr.dauphine.javaavance.phineloops.view;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Window extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int WIDTH_SIZE = 600;
	public static final int HEIGHT_SIZE = 600;
	public static final int MATRIX_SIZE = 5;
	
	private JButton[][] buttons;
	public Window(int rows, int columns) {
		// TODO Auto-generated constructor stub
		super();
		this.setSize(WIDTH_SIZE,HEIGHT_SIZE);
		this.setTitle("INFINITY LOOP GAME");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		setLayout(new GridLayout(rows, columns));
		
		
		buttons = new JButton[MATRIX_SIZE][MATRIX_SIZE];
		for(int i=0; i<buttons.length; i++) {
			for(int j=0; j<buttons[0].length; j++) {
				
				JButton cells = new JButton();
				buttons[i][j] = cells;
				add(cells);
				cells.addActionListener(this);
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JButton button = new JButton();
		button = (JButton) e.getSource();
		
		switch(button.getName()) {
		 //???
		}
	}
	
	public static void main(String[] args) {
		Window window = new Window(5, 5);
		window.setVisible(true);
	}
}
