package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import ia.Ia;

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
    private static File repertoire = new File("src/configuration");
    private static File fichier = popInterface();
    private static boolean BRIA = false;
    private static boolean BRP = false;
    private static int capaciteCofrre = 1;

    public static void main(String args[]) {
        Graphe graphe = inialisationGraphe();
        if (!(graphe==null)) {
            if (BRIA) {
                lancerIA(graphe);
            } else if (BRP) {
                //lancerRobot(graphe);
            }
        }
    }
    
    public static File popInterface() {
    	JOptionPane pop = new JOptionPane();
        String[] listConfig = repertoire.list();
        String chemin = (String) pop.showInputDialog(null,
                "Entrez le nom du fichier de configuration :",
                "Paramètre",
                JOptionPane.QUESTION_MESSAGE,null,listConfig,null);
        File fichierCur = new File("src/configuration/" + chemin);
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
                case("C"):
                    current = scanner.nextLine();
                    capaciteCofrre = Integer.parseInt(current);
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
    		new Pilote(posRobotPilotePrevious,posRobotPilote,graphe,deplacementInit,capaciteCofrre);
    	}).run();
    }

    public static void lancerIA(Graphe graphe) {
        new Thread(()-> {
            try {
                new Ia(posRobotIAPrevious,posRobotIA,graphe,deplacementInitIA,capaciteCofrre);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).run();
    }
}
