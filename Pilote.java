package code;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.awt.*;

import javax.swing.*;

public class Pilote extends Robot implements ActionListener {
	private Bluetooth communication ;
	private static JFrame fenetre = new JFrame();
	private static JSplitPane layout = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private static JPanel takeDrop = new JPanel();
	private static JPanel direction = new JPanel();
	private static JLabel text = new JLabel("",SwingConstants.CENTER);
	private static JButton up = new JButton(new ImageIcon("src/image/fleche-haut.jpg"));
	private static JButton right = new JButton(new ImageIcon("src/image/fleche-droite.jpg"));
	private static JButton left = new JButton(new ImageIcon("src/image/fleche-gauche.jpg"));
	private static JButton down = new JButton(new ImageIcon("src/image/tourner-fleche.jpg"));
	private static JRadioButton take = new JRadioButton("take",false);
	private static JRadioButton drop = new JRadioButton("drop",false);
	private static JButton stop = new JButton("STOP");
	private static JPanel TD = new JPanel();
	private String suivi="Position robot : ";

	public Pilote(int posRobotPilotePrevious, int init, Graphe g) {
		super(posRobotPilotePrevious,init, g);
		suivi = suivi + currentPos;
		text.setText(suivi);
		new Thread(()-> {
			communication = new Bluetooth("Thorn");
			try {
				communication.connexion();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}).run();
	}

	public void startInterface() {
		up.addActionListener(this);
		left.addActionListener(this);
		right.addActionListener(this);
		down.addActionListener(this);
		take.addActionListener(this);
		drop.addActionListener(this);
		stop.addActionListener(this);
		
		layout.setTopComponent(direction);
		layout.setBottomComponent(takeDrop);
		layout.setDividerSize(5);
		layout.setDividerLocation(400);

		// bouton take et drop
		FlowLayout layTakeDrop = new FlowLayout();
		TD.setLayout(layTakeDrop);
		Dimension d = new Dimension();
		d.setSize(100, 60);
		take.setPreferredSize(d);
		drop.setPreferredSize(d);
		stop.setPreferredSize(d);
		Font font = new Font(Font.DIALOG, Font.BOLD, 15);
		take.setFont(font);
		drop.setFont(font);
		stop.setFont(font);
		TD.add(take);
		TD.add(drop);
		TD.add(stop);
		//text info


		//text ino + take et drop
		takeDrop.setLayout(new BorderLayout());
		takeDrop.add(TD, BorderLayout.SOUTH);
		takeDrop.add(text, BorderLayout.NORTH);


		// bouton de direction
		Dimension d2 = new Dimension();
		d2.setSize(100, 100);
		up.setPreferredSize(d2);
		left.setPreferredSize(d2);
		right.setPreferredSize(d2);
		down.setPreferredSize(d2);
		direction.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;
		c.weighty = 2.0;
		c.weightx = 0.0;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 0;
		direction.add(up, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridwidth = 1;
		c.gridx = 0;
		c.gridy = 1;
		direction.add(left, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridwidth = 1;
		c.gridx = 3;
		c.gridy = 1;
		direction.add(right, c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.ipady = 40;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridwidth = 1;
		c.gridx = 1;
		c.gridy = 2;
		direction.add(down, c);
		
		fenetre.add(layout);
		fenetre.setSize(500, 550);
		fenetre.setVisible(true);
	}


	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source==drop) {

		}else if (source==take) {

		}else if (source==stop){
			communication.deconnexion();
		}else {
			try {
				if (!isEffectuer(source)){
						suivi = "\n MOUVEMENT IMPOSSIBLE || " +"Position robot : "+currentPos+ "\n || MOUVEMENT IMPOSSIBLE";
					}
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
			text.setText(suivi);
		}

	private boolean isEffectuer(Object source) throws IOException, InterruptedException {
		String msg;
		boolean effectuer = false;
		int next = graphe.next(previousPos,currentPos,lastDeplacement);
		if (source==up) {
			//String message = toutDroit(take, drop)
			//fonction sent to robot (message)
			if (graphe.estLigne(next)){
				msg = this.mouvement("s",next);
				communication.sent(msg);
				System.out.println(msg);
				effectuer = true;
			}
		}
		if (source==left) {
			//String message = tournerGauche(take, drop)
			//fonction sent to robot (message)
			if (graphe.estIntersec(next)){
				msg = this.mouvement("l",next);
				communication.sent(msg);
				System.out.println(msg);
				effectuer = true;
			}
		}
		if (source==right) {
			//String message = tournerDroite(take, drop)
			//fonction sent to robot (message)
			if (graphe.estIntersec(next)){
				msg = this.mouvement("r",next);
				communication.sent(msg);
				System.out.println(msg);
				effectuer = true;
			}
		}
		if (source==down) {
			//String message = demiTour(take, drop)
			//fonction sent to robot (message)
			if (graphe.estLigne(previousPos)) {
				communication.sent("20u\n");
				System.out.println("20u\n");
				effectuer = true;
			}

		}

		return effectuer;
	}

}