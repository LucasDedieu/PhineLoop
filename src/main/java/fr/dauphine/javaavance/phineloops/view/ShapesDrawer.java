package fr.dauphine.javaavance.phineloops.view;


import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

public class ShapesDrawer extends JFrame implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int WIDTH_SIZE = 400;
	public static final int HEIGHT_SIZE = 400;
	public static final int MATRIX_SIZE = 6;
	
	
	//public static GameDrawer game;
	
	private JButton[][] buttons;
	private ImageIcon imageBoutton1 = new ImageIcon(getClass().getResource("/images/inf_q1.png"));
	private ImageIcon imageBoutton2 = new ImageIcon(getClass().getResource("/images/inf_q2.png"));
	private ImageIcon imageBoutton3 = new ImageIcon(getClass().getResource("/images/inf_q3.png"));
	private ImageIcon imageBoutton4 = new ImageIcon(getClass().getResource("/images/inf_q4.png"));
	private ImageIcon imageBoutton5 = new ImageIcon(getClass().getResource("/images/inf_x1.png"));
	private ImageIcon imageBoutton6 = new ImageIcon(getClass().getResource("/images/inf_vide.png"));
	private ImageIcon imageBoutton7 = new ImageIcon(getClass().getResource("/images/inf_l1.png"));
	private ImageIcon imageBoutton8 = new ImageIcon(getClass().getResource("/images/inf_l2.png"));
	private ImageIcon imageBoutton9 = new ImageIcon(getClass().getResource("/images/inf_ll1.png"));
	private ImageIcon imageBoutton10 = new ImageIcon(getClass().getResource("/images/inf_i1.png"));
	private ImageIcon imageBoutton11 = new ImageIcon(getClass().getResource("/images/inf_t1.png"));
	

	
	private ImageIcon imageBouttons[] = {imageBoutton1,imageBoutton2,imageBoutton3,imageBoutton4,imageBoutton5,imageBoutton6,
			                             imageBoutton7,imageBoutton8,imageBoutton9,imageBoutton10,imageBoutton11};

	public ShapesDrawer(int rows, int columns) {

		
		super();
		this.setSize(WIDTH_SIZE,HEIGHT_SIZE);
		this.setTitle("INFINITY LOOP GAME");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		
		
		setLayout(new GridLayout(rows, columns));
		int k= 0;
		buttons = new JButton[MATRIX_SIZE][MATRIX_SIZE];
		for(int i=0; i<buttons.length; i++) {
			
			for(int j=0; j<buttons[0].length; j++) {
				k=(int)(Math.random()*10); 
				//System.out.println(k);
				JButton cells = new JButton(imageBouttons[k]);
				buttons[i][j] = cells;
				
				add(cells);
				cells.addActionListener(this);
				//cells.setVisible(false);
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
		ShapesDrawer windowPanel = new ShapesDrawer(Constants.MATRIX_SIZE, Constants.MATRIX_SIZE);
		//game = new Game();
		//window.setContentPane(game);
		windowPanel.setVisible(true);
	}
}
