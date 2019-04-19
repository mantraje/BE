package ia;

import code.Bluetooth;
import code.Graphe;
import code.Robot;

import javax.swing.*;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class Ia extends Robot {
    private Bluetooth communication ;


    public Ia(int posRobotIAPrevious, int init, Graphe g, String last, int capaciteCofrre) throws InterruptedException {
        super(posRobotIAPrevious, init, g, last,capaciteCofrre);
        JOptionPane jop1,jop2;
        jop1 = new JOptionPane();
        jop1.showMessageDialog(null, "activer connexion", "Information", JOptionPane.INFORMATION_MESSAGE);
        new Thread(()-> {
            communication = new Bluetooth("Thorn");
            communication.connexion();
        }).run();
        jop1 = new JOptionPane();
        jop1.showMessageDialog(null, "Appuyer sur bouton", "Information", JOptionPane.INFORMATION_MESSAGE);
        lancerIA();
    }

    public void lancerIA() throws InterruptedException {
        boolean fini =false;
        List<Integer> listBut;
        int but,currentPosHypo=currentPos,previousPosHypo=previousPos;

        List<Integer> victimesHypo = new LinkedList<>(graphe.getVictimes());
        List<Integer> hopitauxHypo = new LinkedList<>(graphe.getHopitaux());

        listBut = victimesHypo;
        but =  fristListVictime(listBut);
        listBut.remove((Integer) but);

        List<Integer> cheminResult = new LinkedList<>();
        cheminResult.add(previousPosHypo);
        List<Integer> chemin = new LinkedList<>();
        chemin.add(currentPos);
        while (!fini) {
            TreeSet<Couple> attente = new TreeSet<>();

            attente.add(new Couple(0, currentPosHypo, graphe, but, chemin));

            cheminResult.addAll(AEtoile(attente, new LinkedList<Couple>(), but,previousPosHypo));
            previousPosHypo=currentPosHypo;
            currentPosHypo = cheminResult.get(cheminResult.size() - 1);
            if (graphe.estHopital(currentPosHypo)){
                if (coffre>0) {
                    coffre--;
                }
            }if (graphe.estVictime(currentPosHypo)){
                victimesHypo.remove(( Integer) currentPosHypo);
                coffre++;
            }
            if (victimesHypo.isEmpty()){
                if (coffre>0){
                    listBut = hopitauxHypo;
                    but = listBut.get(0);
                }else fini=true;
            }else if (coffre>=capaciteCofrre) {
                listBut = hopitauxHypo;
                but = listBut.get(0);
            }else {
                listBut = victimesHypo;
                but =  fristListVictime(listBut);
                listBut.remove((Integer) but);}
            chemin = new LinkedList<>();
        }
        System.out.println(cheminResult);
        int dernier = cheminResult.get(cheminResult.size()-1);
        int avantDernier = cheminResult.get(cheminResult.size()-2);
        cheminResult.add(graphe.autreVoisin(dernier,avantDernier));
        envoieChemin(cheminResult);
    }

    private void envoieChemin(List<Integer> cheminResult) throws InterruptedException {
        int i;
        String msg;
        for (i =2; i< cheminResult.size()-1; i++ ){
            msg = graphe.direction(cheminResult.get(i-1),cheminResult.get(i),cheminResult.get(i+1));

            takeOrDrop();

            if (msg=="u"){
                communication.sent("20s\n");
                System.out.println("s");
                Thread.sleep(1500);
                takeOrDrop();
                communication.sent("20u\n");
                Thread.sleep(1500);
                System.out.println("u");
                communication.sent("20s\n");
                Thread.sleep(1500);
                System.out.println("s");

            }else{
                communication.sent(construct(msg));
                Thread.sleep(2500);
            }

            System.out.println(" Avant pos " +currentPos);

            System.out.println(msg);
            previousPos=currentPos;
            currentPos= cheminResult.get(i);
            System.out.println(" New pos " +currentPos);
        }

        if (graphe.estHopital(currentPos)){
            while(coffre>0) {
                coffre--;
                communication.sent("20d\n");
                System.out.println("Drop");
            }
        }

    }

    private void takeOrDrop() throws InterruptedException {
        if (graphe.estHopital(currentPos)){
            while(coffre>0) {
                coffre--;
                communication.sent("20d\n");
                System.out.println("Drop");
            }
        }
        if (graphe.estVictime(currentPos)){
            rammasser(currentPos);
            communication.sent("20t\n");
            System.out.println("Take");
        }
        Thread.sleep(1500);
    }

    private int fristListVictime(List<Integer> listBut){
        TreeSet<Integer> result =new TreeSet<>(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        if (graphe.heuristique(o1,graphe.getHopitaux().get(0)) < (graphe.heuristique(o2,graphe.getHopitaux().get(0)))){
                            return 1;
                        }
                        return -1;
                    }
                }
        );

        for (int cur : listBut){
            result.add(cur);
        }
        return result.first();
    }


    private List<Couple> creerFils(Couple current, int but, List<Couple> vu, int previous){
        List<Couple> result = new LinkedList<>();
        boolean contains;
        int curSommet = current.getSommet();
        float g;

        Couple fils;
        List<Integer> cheminPere;
        cheminPere = (current.getChemin());

        //System.out.println("        Je fais le fils de "+current+" mon pere est "+ previous);

        for (int curFils : graphe.getVoisin(curSommet)){
            float gfils;
            contains = false;
            g = current.getG();
            List<Integer> cheminFils =  new LinkedList<>(cheminPere);
            cheminFils.add(curFils);
            if (previous == curFils){
                gfils = g +3;
            }else gfils = g +1;
            if (graphe.estIntersec(curFils)) gfils+=0.25;
            if (curFils==-1) gfils+=100;
            fils = new Couple(gfils,curFils,graphe,but,cheminFils);

            for (Couple curCoupleVu : vu){
                if (curCoupleVu.getSommet()==fils.getSommet()) {
                    if (fils.compareTo(curCoupleVu) < 0) {
                        contains = true;
                    }else{vu.remove(curCoupleVu);}
                }
            }
            if (!contains){
                result.add(fils);
            }
        }
        return result;
    }

    private List<Integer> AEtoile(TreeSet<Couple> attente, List<Couple> vu, int but, int previous) {

        if (attente.isEmpty()){
            List<Integer> result = new LinkedList<>();
            result.add(-100);
            return result;
        }
        Couple current = attente.first();
        attente.remove(current);
        System.out.println("\n je prend : " + current);
        int curSommet = current.getSommet();
        if(curSommet==but){
            return current.getChemin();
        }
        List<Couple> filss = creerFils(current,but,vu,previous);
        System.out.println("    ses fils sont  : " + filss);
        for(Couple cur : filss){
            attente.add(cur);
        }
        System.out.println("\n      attente : " + attente+ "\n\n");

        return AEtoile(attente,vu,but,curSommet);
    }

}
