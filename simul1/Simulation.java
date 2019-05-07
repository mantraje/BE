package simulation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Simulation {
	private JFrame fenetre = new JFrame();
	private List<List<Integer>> numSommet;
	private List<List<ImageIcon>> mapIm ;
	private JPanel pan = new JPanel();
	private Dimension d = new Dimension(56,56);
	private List<ImageIcon> imRobot ;
	private List<ImageIcon> ssr;
	private String lastdeplacement="";
	private Dimension dPan = new Dimension(348,368);
	private List<Integer> victimes;
	private List<Integer> hopitaux;
	private ImageIcon sr0 = new ImageIcon("src/image/blanc0.png");
	private ImageIcon sr1 = new ImageIcon("src/image/droite1.png");
	private ImageIcon sr2 = new ImageIcon("src/image/droite2.png");
	private ImageIcon sr3 = new ImageIcon("src/image/tour3.png");
	private ImageIcon sr4 = new ImageIcon("src/image/tour4.png");
	private ImageIcon sr5 = new ImageIcon("src/image/tour5.png");
	private ImageIcon sr6 = new ImageIcon("src/image/tour6.png");
	private ImageIcon sr7 = new ImageIcon("src/image/inter7.png");
	private ImageIcon sr8 = new ImageIcon("src/image/inter8.png");
	private ImageIcon sr9 = new ImageIcon("src/image/inter9.png");
	private ImageIcon sr10 = new ImageIcon("src/image/inter10.png");
	private ImageIcon r1 = new ImageIcon("src/image/droite1R.png");
	private ImageIcon r2 = new ImageIcon("src/image/droite2R.png");
	private ImageIcon r3 = new ImageIcon("src/image/tour3R.png");
	private ImageIcon r4 = new ImageIcon("src/image/tour4R.png");
	private ImageIcon r5 = new ImageIcon("src/image/tour5R.png");
	private ImageIcon r6 = new ImageIcon("src/image/tour6R.png");
	private ImageIcon r7 = new ImageIcon("src/image/inter7R.png");
	private ImageIcon r8 = new ImageIcon("src/image/inter8R.png");
	private ImageIcon r9 = new ImageIcon("src/image/inter9R.png");
	private ImageIcon r10 = new ImageIcon("src/image/inter10R.png");
	private ImageIcon ri1 = new ImageIcon("src/image/droite1Ri.png");
	private ImageIcon ri2 = new ImageIcon("src/image/droite2Ri.png");
	private ImageIcon ri3 = new ImageIcon("src/image/tour3Ri.png");
	private ImageIcon ri4 = new ImageIcon("src/image/tour4Ri.png");
	private ImageIcon ri5 = new ImageIcon("src/image/tour5Ri.png");
	private ImageIcon ri6 = new ImageIcon("src/image/tour6Ri.png");
	private ImageIcon ri7 = new ImageIcon("src/image/inter7Ri.png");
	private ImageIcon ri8 = new ImageIcon("src/image/inter8Ri.png");
	private ImageIcon ri9 = new ImageIcon("src/image/inter9Ri.png");
	private ImageIcon ri10 = new ImageIcon("src/image/inter10Ri.png");

	public Simulation(List<List<Integer>> numSommet, List<List<String>> image,List<Integer> vic,List<Integer> hop) {
		this.victimes = vic;
		this.hopitaux = hop;
		this.numSommet=numSommet;
		ssr = new LinkedList<>();
		ssr.add(sr0);
		ssr.add(sr1);
		ssr.add(sr2);
		ssr.add(sr3);
		ssr.add(sr4);
		ssr.add(sr5);
		ssr.add(sr6);
		ssr.add(sr7);
		ssr.add(sr8);
		ssr.add(sr9);
		ssr.add(sr10);
		imRobot = new LinkedList<>();
		imRobot.add(r1);
		imRobot.add(r2);
		imRobot.add(r3);
		imRobot.add(r4);
		imRobot.add(r5);
		imRobot.add(r6);
		imRobot.add(r7);
		imRobot.add(r8);
		imRobot.add(r9);
		imRobot.add(r10);
		imRobot.add(ri1);
		imRobot.add(ri2);
		imRobot.add(ri3);
		imRobot.add(ri4);
		imRobot.add(ri5);
		imRobot.add(ri6);
		imRobot.add(ri7);
		imRobot.add(ri8);
		imRobot.add(ri9);
		imRobot.add(ri10);
		this.mapIm = initMat(image);
		FlowLayout lay = new FlowLayout();
		lay.setHgap(0);
		lay.setVgap(0);
		pan.setLayout(lay);
		pan.setPreferredSize(dPan);
		fenetre.getContentPane().add(pan);
		fenetre.setSize(360,375);
		int numSom = 0;
		for(int i=0;i<6;i++) {
			List<ImageIcon> ligne = mapIm.get(i);
			for(int j=0;j<6;j++) {
				numSom=numSommet.get(i).get(j);
				JButton toAdd = new JButton(ligne.get(j));
				for(Integer v : victimes) {
					if (numSom==v) {
						toAdd.setBackground(Color.red);
					}
				}
				for(Integer h : hopitaux) {
					if (numSom==h) {
						toAdd.setBackground(Color.green);
					}
				}
				toAdd.setPreferredSize(d);
				pan.add(toAdd);
			}
		}
		fenetre.setVisible(true);
	}

	private List<List<ImageIcon>> initMat(List<List<String>> indexI){
		int row;
		int index;
		List<String> ligne;
		List<ImageIcon> ligneIma;
		List<List<ImageIcon>> map = new LinkedList<>();
		for(row=0;row<6;row++) {
			ligneIma = new LinkedList<>();
			ligne = indexI.get(row);
			for(String c : ligne) {
				if(c.endsWith("i")) {
					index = Integer.parseInt(c.substring(0, c.length()-2));
					ligneIma.add(imRobot.get(index+9));
				}else if(c.endsWith("R")) {
					index = Integer.parseInt(c.substring(0,c.length()-1));
					ligneIma.add(imRobot.get(index-1));
				}else {
					ligneIma.add(ssr.get(Integer.parseInt(c)));
				}
			}
			map.add(ligneIma);
		}
		return map;
	}

	public void sent(String mess,int positionR, int previousPos, String lastdep,boolean pilote) {
		List<Integer> coord = coordSommet(previousPos);
		ImageIcon avecR = mapIm.get(coord.get(0)).get(coord.get(1));
		List<Integer> coord2 = coordSommet(positionR);
		ImageIcon ssR = mapIm.get(coord2.get(0)).get(coord2.get(1));
		int indexR = imRobot.indexOf(avecR);
		int index = ssr.indexOf(ssR);
		int rob = pilote?index-1:index+9;
		int sansr = pilote?indexR+1:indexR-9 ;
		System.out.println("dans sent = "+mess);
		if((positionR!=previousPos) && !(mess.contains("u")) && lastdeplacement!="u" && mess!="20t" && mess!="20d") {
			int y = coord.get(1);
			int y2 = coord2.get(1);
			mapIm.get(coord.get(0)).remove(y);
			mapIm.get(coord.get(0)).add(coord.get(1), ssr.get(sansr));
			mapIm.get(coord2.get(0)).remove(y2);
			mapIm.get(coord2.get(0)).add(coord2.get(1), imRobot.get(rob));
		}
		this.lastdeplacement=lastdep;
		refresh();
	}

	public List<Integer> coordSommet(int pos){
		int i=0;
		int index=0;
		boolean trouve = false;
		List<Integer> coord = new LinkedList<>();
		while(!trouve) {
			List<Integer> ligneSom = numSommet.get(i);
			if (ligneSom.contains(pos)) {
				index=ligneSom.indexOf(pos);
				trouve = true;
				coord.add(i);
				coord.add(index);
				return coord;
			}
			i++;
		}
		return null;
	}

	public void refresh() {
		pan.removeAll();
		int numSom = 0;
		for(int i=0;i<6;i++) {
			List<ImageIcon> ligne = mapIm.get(i);
			for(int j=0;j<6;j++) {
				numSom=numSommet.get(i).get(j);
				JButton toAdd = new JButton(ligne.get(j));
				for(Integer v : victimes) {
					if (numSom==v) {
						toAdd.setBackground(Color.red);
					}
				}
				for(Integer h : hopitaux) {
					if (numSom==h) {
						toAdd.setBackground(Color.green);
					}
				}
				toAdd.setPreferredSize(d);
				pan.add(toAdd);
			}
		}
		fenetre.setVisible(true);
	}

}

case ("SI"):
								String[] ligne;
								List<List<Integer>> numSommet = new LinkedList<>();
								List<List<String>> indexIma = new LinkedList<>();
								List<Integer> lignSomm;
								List<String> lignIma;
								for(int i = 0 ;i<6;i++) {
									current = scanner.nextLine();
									ligne = current.split(" ");
									lignSomm = new LinkedList<>();
									lignIma = new LinkedList<>();
									for (String c : ligne) {
										lignSomm.add(Integer.parseInt(c.split(",")[0]));
										lignIma.add(c.split(",")[1]);
									}
									numSommet.add(lignSomm);
									indexIma.add(lignIma);
								}
								simul = new Simulation(numSommet,indexIma,victimes,hopitaux);
					}
