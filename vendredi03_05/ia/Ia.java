package ia;

import code.Bluetooth;
import code.Graphe;
import code.Robot;

import javax.swing.*;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

public class Ia extends Robot {

    public Ia(String monNXT, int posRobotIAPrevious, int init, Graphe g, String last, int capaciteCofrre, String nomC, Bluetooth b, Semaphore sem)  {
        super(monNXT,posRobotIAPrevious, init, g, last,capaciteCofrre,nomC,b,sem);
        start();
    }

    public void run() {
        JOptionPane jop1,jop2,jop3;
        jop1 = new JOptionPane();
        jop1.showMessageDialog(null, "activer connexion", "Information", JOptionPane.INFORMATION_MESSAGE);
        jop1 = new JOptionPane();
        jop1.showMessageDialog(null, "Appuyer sur bouton", "Information", JOptionPane.INFORMATION_MESSAGE);
        lancementIA();
        jop3 = new JOptionPane();
        jop3.showMessageDialog(null, "Fini", "Information", JOptionPane.INFORMATION_MESSAGE);
        log.close();
    }

    //permet l'envoie de message en recalculant tout le chemin toute les deux envoie de mouvement
    private void lancementIA(){
        int i;
        //permet de differencier le premier coup car U differe selon le premier coup ou pas
        boolean frist=false;
        String msg = lastDeplacement;
        List<Integer> cheminResult=calculCheminEntier(!frist);
        System.out.println(cheminResult);
        for (i = 1; i < cheminResult.size() - 1; i++) {
            //evite colision si pilote sur but;
            while((cheminResult.get(i + 1) == log.dernierPosCoop(nomCoop))){
                    try {
						sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                    cheminResult=calculCheminEntier(!frist);
            }
            //envoie le bon message
            envoieMouvement(i, frist, cheminResult);
            i=1;
            frist=true;
            cheminResult=calculCheminEntier(!frist);
            System.out.println(cheminResult);
        }

        currentPos = graphe.next(previousPos, previousPos = currentPos, msg);
        if (graphe.estIntersec(currentPos)){
            sent("20l");
            lastDeplacement="l";
        }else {
            sent("20s");
            lastDeplacement="s";
        }
        takeOrDrop();
        esquivePilote();
    }

    private void envoieMouvement(int i, boolean frist, List<Integer> cheminResult) {
        String msg;
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
                sent("20s");
            }
            sent("20u");
            sent("20s");

        } else {
            if (frist) {
                sent(construct(msg));
            }
        }
        lastDeplacement=msg;

        System.out.println(" New pos " + currentPos);
    }

    private void takeOrDrop() {
        if (graphe.estHopital(currentPos)){
            while(coffre>0) {
                coffre--;
                sent("20d");
            }
        }

        if ( verifTake() && coffre<capaciteCofrre){
            rammasser(currentPos);
            sent("20t");
        }
    }

    //permet de calcul les chemin entier de l'ia avec tout les but
    public List<Integer> calculCheminEntier(boolean fristB){
        boolean fini =false;
        List<Integer> listBut;
        int but,currentPosHypo=currentPos,previousPosHypo=previousPos,nextFrist,coffreHypo=coffre;

        List<Integer> victimesHypo = new LinkedList<>(graphe.getVictimes());
        List<Integer> hopitauxHypo = new LinkedList<>(graphe.getHopitaux());
        System.out.println("Pour pos : "+ currentPosHypo);
        but =  -1;
        //choix premier but
        if (victimesHypo.isEmpty()){
            if (coffreHypo>0){
                listBut = hopitauxHypo;
                but = fristListHopital(listBut,currentPosHypo);;
            }else fini=true;
        }else if (coffreHypo>=capaciteCofrre) {
            listBut = hopitauxHypo;
            but = fristListHopital(listBut,currentPosHypo);;
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
        List<Couple> vu = new LinkedList<>();
        nextFrist = graphe.next(previousPosHypo,currentPosHypo,lastDeplacement);

        //***********
        //serie de A* pour calculer les chemin but a but
        boucleCalculChemin(fristB, fini, but, currentPosHypo, previousPosHypo, nextFrist, coffreHypo, victimesHypo, hopitauxHypo, cheminResult, chemin, vu);
        //***********
        return cheminResult;
    }

    //calcul tout le chemin en faisant une hypothese des positions et des buts
    public void boucleCalculChemin(boolean fristB, boolean fini, int but, int currentPosHypo, int previousPosHypo, int nextFrist, int coffreHypo, List<Integer> victimesHypo, List<Integer> hopitauxHypo, List<Integer> cheminResult, List<Integer> chemin, List<Couple> vu) {
        List<Integer> listBut;
        while (!fini) {

            TreeSet<Couple> attente = new TreeSet<>();

            if (fristB){
                Couple frist = new Couple(0, currentPosHypo, graphe, but, chemin,previousPosHypo);

                List<Couple> filss = creerFils(frist,but,vu);
                for(Couple cur : filss){
                    if ((cur.getSommet()==nextFrist) || (cur.getSommet()==previousPosHypo)) {
                        attente.add(cur); }
                }
                vu.add(frist);
            }else {
                attente.add(new Couple(0, currentPosHypo, graphe, but, chemin, previousPosHypo));
            }
            cheminResult.addAll(AEtoile(attente, vu , but));
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
            //choix but suivant
            if (victimesHypo.isEmpty()){
                if (coffreHypo>0){
                    listBut = hopitauxHypo;
                    but = fristListHopital(listBut,currentPosHypo);
                }else fini=true;
            }else if (coffreHypo>=capaciteCofrre) {
                listBut = hopitauxHypo;
                but = fristListHopital(listBut,currentPosHypo);
            }else {
                listBut = victimesHypo;
                but =  fristListVictime(listBut,currentPosHypo);
                listBut.remove((Integer) but);}
            chemin = new LinkedList<>();
        }
    }

    private int fristListHopital(List<Integer> listBut, int currentPosHyp){
        final float[] malus1 = {0};
        final float[] malus2 = { 0 };
        TreeSet<Integer> result =new TreeSet<>(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        if (o1==log.dernierPosCoop(nomCoop)){ malus1[0] =100;}
                        if (o2==log.dernierPosCoop(nomCoop)){ malus2[0] =100;}
                        if ((graphe.heuristique(currentPosHyp, o1)+ malus1[0])< (malus2[0] +(graphe.heuristique(currentPosHyp, o2)))) {
                            return 1;
                        }
                        return -1;
                    }
                }
        );
        for (int cur : listBut){
            result.add(cur);
        }
        System.out.println("                        "+result);
        return result.first();
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
                if (graphe.estIntersec(curSommet)) { gfils = gfils +1000;}
            }else gfils = g +1;
            if (graphe.estIntersec(curFils)) gfils+=0.25;
            if (curFils==-1) gfils+=100;
            if (curFils==(log.dernierPosCoop(nomCoop))&&((!graphe.estHopital(curFils)&&(!graphe.estVictime(log.dernierPosCoop(nomCoop)))))){gfils+=100;}
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

    private List<Integer> AEtoile(TreeSet<Couple> attente, List<Couple> vu, int but) {
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
        return AEtoile(attente,vu,but);
    }

    private void esquivePilote(){
        List<Integer> cheminEsquive = new LinkedList<>();
        List<Integer> cheminPilote  = new LinkedList<>();
        boolean frist=false,fini;
        int i;

        fini = log.estFiniCoop(nomCoop);

        while(!fini) {
            TreeSet<Couple> attente = new TreeSet<>();
            cheminPilote  = new LinkedList<>();
            int currentPosPilote = log.dernierPosCoop(nomCoop);
            int currentPosPreviousPilote = log.dernierPosPreviousCoop(nomCoop);
            attente.add(new Couple(0, currentPosPilote, graphe, fristListHopital(graphe.getHopitaux(), currentPosPilote), new LinkedList<>(), currentPosPreviousPilote));
            cheminPilote.addAll(AEtoile(attente, new LinkedList<>(), fristListHopital(graphe.getHopitaux(), currentPosPilote)));
            
            if (cheminPilote.contains(currentPos)) {
            	List<Integer> voisinCur = graphe.voisinDansL1MaisPasDansL2(currentPos,cheminPilote);
            	int voisinCool = voisinCur.get(0);
            	TreeSet<Couple> attenteBis = new TreeSet<>();
            	attenteBis.add(new Couple(0, currentPos, graphe, voisinCool,new LinkedList<>(), previousPos));
            	cheminEsquive = new LinkedList<>();
            	cheminEsquive.addAll(AEtoile(attenteBis, new LinkedList<>(), voisinCool));
            	System.out.println(cheminEsquive);
                for (i = 1; i < cheminEsquive.size() - 1; i++) {
                    envoieMouvement(i, !frist, cheminEsquive);
                    frist = false;
                    i=1;
                }
                
            }
            fini = log.estFiniCoop(nomCoop);
        }
        log.close();
    }

}