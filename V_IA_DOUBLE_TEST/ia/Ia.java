package ia;

import code.Bluetooth;
import code.Graphe;
import code.Robot;
import simulation.Interface;

import javax.swing.*;

import java.io.IOException;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

public class Ia extends Robot {
    private List<Integer> cheminResul;

    public Ia(String monNXT,int posRobotIAPrevious, int init, Graphe g, String last, int capaciteCofrre,String nomC) throws InterruptedException, IOException {
        super(monNXT,posRobotIAPrevious, init, g, last,capaciteCofrre,nomC);
        JOptionPane jop1,jop2,jop3;
        jop1 = new JOptionPane();
        jop1.showMessageDialog(null, "activer connexion", "Information", JOptionPane.INFORMATION_MESSAGE);
        jop1 = new JOptionPane();
        jop1.showMessageDialog(null, "Appuyer sur bouton", "Information", JOptionPane.INFORMATION_MESSAGE);
        envoieChemin();
        jop3 = new JOptionPane();
        jop3.showMessageDialog(null, "Fini", "Information", JOptionPane.INFORMATION_MESSAGE);
        log.close();
        
        
    }
    public List<Integer> lancerIA(boolean fristB) throws InterruptedException, IOException {
        System.out.println("lalalalala");
        boolean fini =false;
        List<Integer> listBut;
        int but,currentPosHypo=currentPos,previousPosHypo=previousPos,nextFrist,coffreHypo=coffre;

        List<Integer> victimesHypo = new LinkedList<>(graphe.getVictimes());
        List<Integer> hopitauxHypo = new LinkedList<>(graphe.getHopitaux());


        System.out.println("Pour pos : "+ currentPosHypo);
        but =  -1;
        if (victimesHypo.isEmpty()){
            if (coffreHypo>0){
                listBut = hopitauxHypo;
                but = listBut.get(0);
            }else fini=true;
        }else if (coffreHypo>=capaciteCofrre) {
            listBut = hopitauxHypo;
            but = listBut.get(0);
        }else {
            listBut = victimesHypo;
            System.out.println("Pour pos : "+ currentPosHypo);
            but =  fristListVictime(listBut,currentPosHypo);
            listBut.remove((Integer) but);}

        List<Integer> cheminResult = new LinkedList<>();
        cheminResult.add(previousPosHypo);
        List<Integer> chemin = new LinkedList<>();
        chemin.add(currentPos);

        // pour cas intersect et victime a cote premiere iteration
        List<Couple> vu = new LinkedList<Couple>();
        nextFrist = graphe.next(previousPosHypo,currentPosHypo,lastDeplacement);

        while (!fini) {

            TreeSet<Couple> attente = new TreeSet<>();

            if (fristB){
                Couple frist = new Couple(0, currentPosHypo, graphe, but, chemin,previousPosHypo);

                List<Couple> filss = creerFils(frist,but,vu);
                System.out.println("# fils " + filss);
                for(Couple cur : filss){
                    if ((cur.getSommet()==nextFrist) || (cur.getSommet()==previousPosHypo)) {
                        attente.add(cur);
                    }
                }
                vu.add(frist);
            }else {

                attente.add(new Couple(0, currentPosHypo, graphe, but, chemin, previousPosHypo));
            }
            System.out.println("### attente : " +attente);
            cheminResult.addAll(AEtoile(attente, vu , but,previousPosHypo));
            previousPosHypo=cheminResult.get(cheminResult.size() - 2);
            currentPosHypo = cheminResult.get(cheminResult.size() - 1);
            if (graphe.estHopital(currentPosHypo)){
                if (coffreHypo>0) {
                    coffreHypo--;
                }
            }if (graphe.estVictime(currentPosHypo)){
                victimesHypo.remove(( Integer) currentPosHypo);
                coffreHypo++;
            }
            if (victimesHypo.isEmpty()){
                if (coffreHypo>0){
                    listBut = hopitauxHypo;
                    but = listBut.get(0);
                }else fini=true;
            }else if (coffreHypo>=capaciteCofrre) {
                listBut = hopitauxHypo;
                but = listBut.get(0);
            }else {
                listBut = victimesHypo;
                System.out.println("Pour pos : "+ currentPosHypo);
                but =  fristListVictime(listBut,currentPosHypo);
                listBut.remove((Integer) but);}
            chemin = new LinkedList<>();
        }

        return cheminResult;
    }

	private void envoieChemin() throws InterruptedException, IOException {
        int i;
        boolean frist=false;
        String msg = lastDeplacement;
        List<Integer> cheminResult=lancerIA(!frist);
        System.out.println(cheminResult);
        for (i = 1; i < cheminResult.size() - 1; i++) {
            msg = graphe.direction(cheminResult.get(i - 1), cheminResult.get(i), cheminResult.get(i + 1));

            takeOrDrop();
            System.out.println(" Avant pos " + currentPos);
            System.out.println(msg);
            if (frist) {
                previousPos = currentPos;
                currentPos = cheminResult.get(i);
            }

            if (msg == "u") {


                if (frist) {
                    sent("s");
                    //Thread.sleep(4000);
                    System.out.println("s");
                }

                sent("20u");

                //Thread.sleep(4000);
                System.out.println("u");
                sent("s");
                //Thread.sleep(4000);
                System.out.println("s");

            } else {
                if (frist) {
                    sent(construct(msg));
                    //Thread.sleep(4000);
                }
                //Thread.sleep(1000);
            }

            System.out.println(" New pos " + currentPos);
            i=1;
            frist=true;
            cheminResult=lancerIA(!frist);
            System.out.println(cheminResult);
        }

        currentPos = graphe.next(previousPos, previousPos = currentPos, msg);
        sent("s");
        takeOrDrop();
    }

    private void takeOrDrop() throws InterruptedException, IOException {
        if (graphe.estHopital(currentPos)){
            while(coffre>0) {
                coffre--;
                sent("d");
                //Thread.sleep(3000);
                System.out.println("Drop");
            }
        }
        if ( verifTake() && coffre<capaciteCofrre){
            rammasser(currentPos);
            sent("t");
            //Thread.sleep(3000);
            System.out.println("Take");
        }
    }

    private int fristListVictime(List<Integer> listBut, int currentPosHyp){
        TreeSet<Integer> result =new TreeSet<>(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                    	if (graphe.heuristique(currentPosHyp,o1) == (graphe.heuristique(currentPosHyp,o2))){
		                    if (graphe.heuristique(currentPosHyp,o1) < (graphe.heuristique(currentPosHyp,o2))){
		                        return 1;
		                    }
		                    return -1;
                    	}else {
                    	    //System.out.println( "   Pour "+ o1 + "h : " +graphe.heuristique(currentPosHyp,o1) + " < "+" Pour "+ o2 + "h : "+(graphe.heuristique(currentPosHyp,o2)));
                    		 if ((graphe.heuristique(currentPosHyp,o1)-graphe.heuristique((log.dernierPosCoop(nomCoop)),o1)) < (graphe.heuristique(currentPosHyp,o2))-graphe.heuristique((log.dernierPosCoop(nomCoop)),o2)){
 		                        return -1;
 		                    }
 		                    return 1;}
                    }
                }
        );

        for (int cur : listBut){
            result.add(cur);
        }
        System.out.println("                        "+result);
        return result.first();
    }


    private List<Couple> creerFils(Couple current, int but, List<Couple> vu){
        List<Couple> result = new LinkedList<>();
        boolean contains;
        int curSommet = current.getSommet();
        float g;

        Couple fils;
        List<Integer> cheminPere;
        cheminPere = (current.getChemin());


        for (int curFils : graphe.getVoisin(curSommet)){
            float gfils;
            contains = false;
            g = current.getG();
            List<Integer> cheminFils =  new LinkedList<>(cheminPere);
            cheminFils.add(curFils);
            if (current.getPrevious() == curFils){
                gfils = g +2;
                if (graphe.estIntersec(curSommet)) { gfils = gfils +100;}
            }else gfils = g +1;
            if (graphe.estIntersec(curFils)) gfils+=0.25;
            if (curFils==-1) gfils+=100;
            if (curFils==(log.dernierPosCoop(nomCoop))){gfils+=1000;}
            fils = new Couple(gfils,curFils,graphe,but,cheminFils,curSommet);

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
        int curSommet = current.getSommet();
        if(curSommet==but){
            return current.getChemin();
        }
        List<Couple> filss = creerFils(current,but,vu);
        //System.out.println("\n je suis en "+ curSommet + "mon but est : " + but + "je viens de : "+ previous);
        //System.out.println("	fils de" + current + " : " + filss);
        for(Couple cur : filss){
            attente.add(cur);
        }
        
        //System.out.println("attente : "+ attente+ "\n");

        return AEtoile(attente,vu,but,curSommet);
    }

}