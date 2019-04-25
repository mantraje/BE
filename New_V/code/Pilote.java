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
	private static JButton take = new JButton("take");
	private static JButton drop = new JButton("drop");
	private static JButton stop = new JButton("STOP");
	private static JPanel TD = new JPanel();
	private static JPanel STOPL = new JPanel();
	private String suivi="Position robot : ";

	public Pilote(int posRobotPilotePrevious, int init, Graphe g, String last, int capaciteCofrre) {
		super(posRobotPilotePrevious,init, g,last,capaciteCofrre);
		suivi = suivi + currentPos;
		text.setText(suivi);
		new Thread(()-> {
			communication = new Bluetooth("Thorn");
			communication.connexion();
		}).run();
		startInterface();
	}




	private void startInterface() {
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
		STOPL.setLayout(layTakeDrop);
		Dimension d = new Dimension();
		d.setSize(220, 60);
		take.setPreferredSize(d);
		drop.setPreferredSize(d);
		stop.setPreferredSize(d);
		Font font = new Font(Font.DIALOG, Font.BOLD, 15);
		take.setFont(font);
		drop.setFont(font);
		stop.setFont(font);
		TD.add(take);
		TD.add(drop);
		STOPL.add(stop);


		//text ino + take et drop + Stop
		takeDrop.setLayout(new BorderLayout());
		takeDrop.add(TD, BorderLayout.CENTER);
		takeDrop.add(STOPL, BorderLayout.SOUTH);
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
		fenetre.setSize(500, 600);
		fenetre.setLocationRelativeTo(null);
		fenetre.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		boolean RverifDrop = verifDrop();
		if (source==drop) {
			if (RverifDrop && coffre!=0) {
				communication.sent("20d\n");
				coffre--;
				suivi = "Position robot : "+currentPos + "  Victime bien déposé !";
			}else {
				if(!RverifDrop) {
					suivi = "Vous ne pouvez pas déposez, la case "+currentPos+" n'a pas d'hopital...";
				}else {
					suivi = "Position robot : "+currentPos + "  Vous n'avez pas de victimes à déposer !";
				}
			}
		}else if (source==take) {
			if (verifTake()) {
				rammasser(currentPos);
				communication.sent("20t\n");
				suivi = "Position robot : "+currentPos + "  Victime ramassée !";
			}else {
				suivi = "Position robot : "+currentPos + "  Il n'y a pas de victime sur cette case !";
			}
		}else if (source==stop){
			communication.deconnexion();
			fenetre.dispose();
		}else {
			if (isEffectuer(source)) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
				suivi = "Position robot : "+currentPos;
			}else {
				suivi = "\n MOUVEMENT IMPOSSIBLE || " +"Position robot : "+currentPos+ "\n || MOUVEMENT IMPOSSIBLE";
				}
			}
		text.setText(suivi);
		communication.receive();
		}

	private boolean isEffectuer(Object source) {
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
				effectuer = true;
			}
		}
		if (source==right) {
			//String message = tournerDroite(take, drop)
			//fonction sent to robot (message)
			if (graphe.estIntersec(next)){
				msg = this.mouvement("r",next);
				communication.sent(msg);
				effectuer = true;
			}
		}
		if (source==down) {
			//String message = demiTour(take, drop)
			//fonction sent to robot (message)
			if (graphe.estLigne(currentPos)) {
				msg = this.mouvement("u",next);
				communication.sent(msg);
				effectuer = true;
			}

		}
		return effectuer;
	}
}