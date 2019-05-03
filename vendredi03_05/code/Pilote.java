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
import java.util.concurrent.Semaphore;

import javax.swing.*;

public class Pilote extends Robot implements ActionListener {
	private static JFrame fenetre = new JFrame();
	private static JSplitPane layout = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
	private static JPanel takeDrop = new JPanel();
	private static JPanel direction = new JPanel();
	private static JLabel text = new JLabel("",SwingConstants.CENTER);
	private static JButton up = new JButton(new ImageIcon("src/image/fleche-haut.jpg"));
	private static JButton right = new JButton(new ImageIcon("src/image/fleche-droite.jpg"));
	private static JButton left = new JButton(new ImageIcon("src/image/fleche-gauche.jpg"));
	private static JButton down = new JButton(new ImageIcon("src/image/tourner-fleche.jpg"));
	private static JButton take = new JButton("TAKE");
	private static JButton drop = new JButton("DROP");
	private static JButton stop = new JButton("STOP");
	private static JPanel TD = new JPanel();
	private static JPanel STOPL = new JPanel();
	private String suivi="Position robot : ";

	public Pilote(String monNXT, int posRobotPilotePrevious, int init, Graphe g, String last, int capaciteCofrre, String nomC, Bluetooth b, Semaphore sem) {
		super(monNXT,posRobotPilotePrevious,init, g,last,capaciteCofrre,nomC,b,sem);
		start();
	}

	public void run() {
		suivi = suivi + currentPos;
		text.setText(suivi);
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
		boolean RverifTake = verifTake();
		if (source==drop) {
			if (RverifDrop && coffre!=0) {
				sent("20d");
				coffre--;
				suivi = "Position robot : "+currentPos + "  Victime bien d�pos� !";
			}else {
				if(!RverifDrop) {
					suivi = "Vous ne pouvez pas d�posez, la case "+currentPos+" n'a pas d'hopital...";
				}else {
					suivi = "Position robot : "+currentPos + "  Vous n'avez pas de victimes � d�poser !";
				}
			}
		}else if (source==take) {
			if (RverifTake && coffre<capaciteCofrre) {
				rammasser(currentPos);
				sent("20t");
				suivi = "Position robot : "+currentPos + "  Victime ramass�e !";
			}else {
				if (!RverifTake) {
					suivi = "Position robot : "+currentPos + "  Il n'y a pas de victime sur cette case !";
				}
				else {
					suivi = "Position robot : "+currentPos+" Vous n'avez plus la place pour prendre cette victime !";
				}
			}
		}else if (source==stop){
			log.close();
			fenetre.dispose();
		}else {
			if (isEffectuer(source)) {
				suivi = "Position robot : "+currentPos;
			}else {
				suivi = "\n MOUVEMENT IMPOSSIBLE || " +"Position robot : "+currentPos+ "\n || MOUVEMENT IMPOSSIBLE";
				}
			}
		text.setText(suivi);
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
				sent(msg);
				System.out.println(msg);
				effectuer = true;
			}
		}
		if (source==left) {
			//String message = tournerGauche(take, drop)
			//fonction sent to robot (message)
			if (graphe.estIntersec(next)){
				msg = this.mouvement("l",next);
				sent(msg);
				effectuer = true;
			}
		}
		if (source==right) {
			//String message = tournerDroite(take, drop)
			//fonction sent to robot (message)
			if (graphe.estIntersec(next)){
				msg = this.mouvement("r",next);
				sent(msg);
				effectuer = true;
			}
		}
		if (source==down) {
			//String message = demiTour(take, drop)
			//fonction sent to robot (message)
			if (graphe.estLigne(currentPos)) {
				msg = this.mouvement("u",next);
				sent(msg);
				effectuer = true;
			}

		}
		return effectuer;
	}
}