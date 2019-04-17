package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import javax.swing.JOptionPane;

import code.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    private int posRobotPilote=-1;
    private int posRobotPilotePrevious=-1;
    private int posRobotIA=-1;
    private String deplacementInit = "";
    private String chemin = "";
    
    public Main () {
    	chemin = "src/configuration/"+this.popInterface();
    }
    
    public String popInterface() {
    	JOptionPane pop = new JOptionPane();
    	String chemin = pop.showInputDialog("Entrez le nom du fichier de configuration.");
    	return chemin;
    }
    public Graphe inialisationGraphe(){

        File file = new File (chemin);
        Map<Integer, List<Integer>> map = null;
        List<Integer> victimes= null;
        List<Integer> hopitaux=null;
        Scanner scanner;
        String current;
        int nbSommet;

        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }


        while (scanner.hasNextLine()) {
            current = scanner.nextLine();
            switch (current) {

                case ("S"):
                    current = scanner.nextLine();
                    nbSommet = Integer.parseInt(current);
                    map = new LinkedHashMap<>();
                    while (nbSommet > 0) {
                        current = scanner.nextLine();
                        String[] sommet = current.split(":");
                        String[] voisin = sommet[1].split(",");
                        map.put(Integer.parseInt(sommet[0]), construcListAvecString(voisin));
                        nbSommet--;
                    }
                break;
                case("V"):
                    current = scanner.nextLine();
                    victimes = construcListAvecString(current.split(","));
                break;
                case("H"):
                    current = scanner.nextLine();
                    hopitaux = construcListAvecString(current.split(","));
                break;
                case("RP"):
                    current = scanner.nextLine();
                    String[] posDouble = current.split(",");
                    posRobotPilote = Integer.parseInt(posDouble[1]);
                    posRobotPilotePrevious = Integer.parseInt(posDouble[0]);
                    deplacementInit=posDouble[2];
                break;
                case("RIA"):
                    current = scanner.nextLine();
                    posRobotIA = Integer.parseInt(current);
                break;
            }

        }
    return new Graphe(map,victimes,hopitaux);

    }

    private LinkedList<Integer> construcListAvecString (String[] voisinString){
        LinkedList<Integer> voisin = new LinkedList<>();
        for (String current : voisinString){
            voisin.add(Integer.parseInt(current));
        }
        return voisin;
    }

    public int getPosRobotPilote() {
        return posRobotPilote;
    }

    public int getPosRobotIA() {
        return posRobotIA;
    }
    
    public void lancerRobot() {
    	Graphe maquette = this.inialisationGraphe();
    	new Thread(()-> {
    		Pilote humain = new Pilote(posRobotPilotePrevious,posRobotPilote,maquette,deplacementInit);
    		humain.startInterface();
    	}).run();
    }
}
