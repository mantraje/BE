package simulation;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Interface {
	private static JFrame fenetre = new JFrame();
	private static Read map = new Read();
	private static List<List<ImageIcon>> mapIm = map.initMatrice();
	private static JPanel pan = new JPanel();
	private static List<List<Integer>> numSommet = map.getNumSommet();
	private static Dimension d = new Dimension(52,52);
	private static int currentPos=1;
	private static List<ImageIcon> imRobot = map.getAvecR();
	private static Dimension dPan = new Dimension(332,352);
	
	public static void main(String args[]) {
		FlowLayout lay = new FlowLayout();
		lay.setHgap(0);
		lay.setVgap(0);
		pan.setLayout(lay);
		for(int i=0;i<6;i++) {
			List<ImageIcon> ligne = mapIm.get(i);
			for(int j=0;j<6;j++) {
				JButton toAdd = new JButton(ligne.get(j));
				toAdd.setPreferredSize(d);
				toAdd.setEnabled(true);
				pan.add(toAdd);
			}
		}
		pan.setPreferredSize(dPan);
		fenetre.getContentPane().add(pan);
		fenetre.setSize(350,370);
		fenetre.setVisible(true);
		changeMap(imRobot.get(1),1);
		refresh();
	}
	
	public static void changeMap(ImageIcon newIm,int positionR) {
		int indexListe=-1;
		boolean trouve = false;
		while(!trouve) {
			indexListe++;
			List<Integer> ligne = numSommet.get(indexListe);
			trouve = ligne.contains(positionR);
		}
		int indexImage = numSommet.get(indexListe).indexOf(positionR);
		mapIm.get(indexListe).remove(indexImage);
		mapIm.get(indexListe).add(indexImage, newIm);
		
	}
	
	public static void refresh() {
		pan.removeAll();
		for(int i=0;i<6;i++) {
			List<ImageIcon> ligne = mapIm.get(i);
			for(int j=0;j<6;j++) {
				JButton toAdd = new JButton(ligne.get(j));
				toAdd.setPreferredSize(d);
				toAdd.setEnabled(true);
				pan.add(toAdd);
			}
		}
	}

}
