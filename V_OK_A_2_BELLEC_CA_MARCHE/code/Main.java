package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import ia.Ia;

import javax.swing.JOptionPane;

import code.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.Semaphore;

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
    private static String nomIA=null,nomPilote=null,portIA=null,portPilote=null;
    private static boolean blue=false;
    private static Semaphore sem = new Semaphore(1);
    private static Simulation simu = null;

    public static void main(String args[]) {
        Graphe graphe = inialisationGraphe();
        if (!(graphe==null)) {
            if (BRIA) {
                lancerIA(graphe);
            }
             if (BRP) {
                lancerRobot(graphe);
            }

        }
    }
    
    public static File popInterface() {
    	JOptionPane pop = new JOptionPane();
        String[] listConfig = repertoire.list();
        String chemin = (String) pop.showInputDialog(null,
                "Entrez le nom du fichier de configuration :",
                "Parametre",
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
        String[] posTriple, sommet, voisin, donneBlue;


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
                    donneBlue=current.split(",");
                    nomPilote=donneBlue[0];
                    portPilote=donneBlue[1];
                    current = scanner.nextLine();
                    posTriple = current.split(",");
                    posRobotPilote = Integer.parseInt(posTriple[1]);
                    posRobotPilotePrevious = Integer.parseInt(posTriple[0]);
                    deplacementInit=posTriple[2];
                    BRP = true;
                break;
                case("RIA"):
                    current = scanner.nextLine();
                    donneBlue=current.split(",");
                    nomIA=donneBlue[0];
                    portIA=donneBlue[1];
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
                case("B"):
                	blue=true;
                    break;
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
                    simu = new Simulation(numSommet,indexIma,victimes,hopitaux);
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
    		if (blue) {
                new Pilote(nomPilote,posRobotPilotePrevious,posRobotPilote,graphe,deplacementInit,capaciteCofrre,nomIA,new Bluetooth(nomPilote,portPilote),sem,simu);
            }else {
                new Pilote(nomPilote, posRobotPilotePrevious, posRobotPilote, graphe, deplacementInit, capaciteCofrre, nomIA,null, sem,simu);
            }
    	}).start();
    }

    public static void lancerIA(Graphe graphe) {
        if (blue) {
            new Ia(nomIA, posRobotIAPrevious, posRobotIA, graphe, deplacementInitIA, capaciteCofrre, nomPilote, new Bluetooth(nomIA, portIA), sem,simu);
        }else {
            new Ia(nomIA, posRobotIAPrevious, posRobotIA, graphe, deplacementInitIA, capaciteCofrre, nomPilote,null, sem,simu);
        }

    }
}