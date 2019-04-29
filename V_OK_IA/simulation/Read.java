package simulation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Read {
	private File fichier = popInterface();
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
	private List<ImageIcon> ssr;
	private List<ImageIcon> avecR;
	private List<List<Integer>> numSommet = new LinkedList<>();
	private int numero = 1;

	public Read() {
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
		avecR = new LinkedList<>();
		avecR.add(r1);
		avecR.add(r2);
		avecR.add(r3);
		avecR.add(r4);
		avecR.add(r5);
		avecR.add(r6);
		avecR.add(r7);
		avecR.add(r8);
		avecR.add(r9);
		avecR.add(r10);
	}
	
	public List<ImageIcon> getAvecR(){
		return avecR;
	}
	
	public List<ImageIcon> getSsr() {
		return ssr;
	}

	public void setSsr(List<ImageIcon> ssr) {
		this.ssr = ssr;
	}

	public List<List<Integer>> getNumSommet() {
		return numSommet;
	}

	public static File popInterface() {
		File fichierCur = null;
		JOptionPane pop = new JOptionPane();
		String chemin = pop.showInputDialog("Entrez le nom du fichier de configuration :");
		fichierCur = new File("src/configuration/" + chemin + ".txt");
		while (chemin != null && !fichierCur.exists()) {
			chemin = pop.showInputDialog("!! Fichier introuvable !! \n Entrez le nom du fichier de configuration :");
			fichierCur = new File(("src/configuration/" + chemin + ".txt"));
		}
		return fichierCur;
	}

	public List<List<ImageIcon>> initMatrice() {
		Scanner scanner;
		String ligne;
		List<ImageIcon> ligneList;
		List<List<ImageIcon>> res = new LinkedList<>();
		try {
			scanner = new Scanner(fichier);
		} catch (FileNotFoundException e) {
			return null;
		}
		while (scanner.hasNextLine()) {
			ligne = scanner.nextLine();
			ligneList = ListStringToImage(ligne.split(" "));
			res.add(ligneList);
		}
		return res;
	}

	private LinkedList<ImageIcon> ListStringToImage(String[] ligne) {
		LinkedList<ImageIcon> ligneIma = new LinkedList<>();
		List<Integer> lignePos = new LinkedList<>();
		int index;
		String numIm;
		int numSomm;
		for (String c : ligne) {
			numSomm = Integer.parseInt(c.split(",")[0]);
			numIm = c.split(",")[1];
			if(numIm.contains("r")) {
				numIm=numIm.substring(0, 1);
				index = Integer.parseInt(numIm);
				ligneIma.add(avecR.get(index-1));
			}
			else {
				index = Integer.parseInt(numIm);
				ligneIma.add(ssr.get(index));
			}
			lignePos.add(numSomm);
		}
		numSommet.add(lignePos);
		return ligneIma;
	}

}
