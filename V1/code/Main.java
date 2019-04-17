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

    private static int posRobotPilote=-1;
    private static int posRobotPilotePrevious=-1;
    private static int posRobotIA=-1;
    private static int posRobotIAPrevious=-1;
    private static String deplacementInit = "";
    private static String deplacementInitIA = "";
    private static File fichier = popInterface();
    private static boolean BRIA = false;
    private static boolean BRP = false;

    public static void main(String args[]) {
        Graphe graphe = inialisationGraphe();
        if (!(graphe==null)) {
            if (BRIA) {
                System.out.println(("Ya pas IA ta race"));
            } else if (BRP) {
                lancerRobot(graphe);
            }
        }
    }
    
    public static File popInterface() {
        File fichierCur = null;
    	JOptionPane pop = new JOptionPane();
        String chemin = pop.showInputDialog("Entrez le nom du fichier de configuration :");
        fichierCur = new File("src/configuration/"+chemin);
    	while (chemin!=null && !fichierCur.exists()){
            chemin = pop.showInputDialog("!! Fichier introuvable !! \n Entrez le nom du fichier de configuration :");
            fichierCur = new File(("src/configuration/"+chemin));
        }
    	return fichierCur;
    }
    public static Graphe inialisationGraphe(){

        Map<Integer, List<Integer>> map = null;
        List<Integer> victimes= null;
        List<Integer> hopitaux=null;
        Scanner scanner;
        String current;
        String[] posTriple, sommet, voisin;


        int nbSommet;

        try {
            scanner = new Scanner(fichier);
        } catch (FileNotFoundException e) {
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
                        sommet = current.split(":");
                        voisin = sommet[1].split(",");
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
                    posTriple = current.split(",");
                    posRobotPilote = Integer.parseInt(posTriple[1]);
                    posRobotPilotePrevious = Integer.parseInt(posTriple[0]);
                    deplacementInit=posTriple[2];
                    BRP = true;
                break;
                case("RIA"):
                    current = scanner.nextLine();
                    posTriple = current.split(",");
                    posRobotIA = Integer.parseInt(posTriple[1]);
                    posRobotIAPrevious = Integer.parseInt(posTriple[0]);
                    deplacementInitIA=posTriple[2];
                    BRIA=true;

                break;
            }

        }
    return new Graphe(map,victimes,hopitaux);

    }

    private static LinkedList<Integer> construcListAvecString(String[] voisinString){
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
    
    public static void lancerRobot(Graphe graphe) {
    	new Thread(()-> {
    		Pilote humain = new Pilote(posRobotPilotePrevious,posRobotPilote,graphe,deplacementInit);
    		humain.startInterface();
    	}).run();
    }
}
