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
        JOptionPane jop1;
        jop1 = new JOptionPane();
        jop1.showMessageDialog(null, "C'est parti !!", "Information", JOptionPane.INFORMATION_MESSAGE);
        new Thread(()-> {
            communication = new Bluetooth("Thorn");
            communication.connexion();
        }).run();
        lancerIA();
    }

    public void lancerIA() throws InterruptedException {
        boolean fini =false;
        List<Integer> listBut;
        int but,currentPosHypo=currentPos;

        List<Integer> victimesHypo = new LinkedList<>(graphe.getVictimes());
        List<Integer> hopitauxHypo = new LinkedList<>(graphe.getHopitaux());

        listBut = victimesHypo;
        but =  fristListVictime(listBut);

        List<Integer> cheminResult = new LinkedList<>();
        List<Integer> chemin = new LinkedList<>();
        chemin.add(currentPos);
        while (!fini) {
            System.out.println("But : "+but );
            TreeSet<Couple> attente = new TreeSet<>();

            attente.add(new Couple(0, currentPosHypo, graphe, but, chemin));

            cheminResult.addAll(AEtoile(attente, new LinkedList<Couple>(), but));
            currentPosHypo = cheminResult.get(cheminResult.size() - 1);
            if (graphe.estHopital(currentPosHypo)){
                coffre--;
            }if (graphe.estVictime(currentPosHypo)){
                victimesHypo.remove(( Integer) currentPosHypo);
                coffre++;
            }

            listBut = victimesHypo;
            System.out.println("listbut : "+listBut);
            if (listBut.isEmpty()){
                if (coffre>0){
                    listBut = hopitauxHypo;
                    but = listBut.get(0);
                }else fini=true;
            }else if (coffre>=capaciteCofrre) {
                listBut = hopitauxHypo;
                but = listBut.get(0);
            }else {but =  fristListVictime(listBut);}
            chemin = new LinkedList<>();
        }

        cheminResult.add(0,previousPos);
        envoieChemin(cheminResult);
    }

    private void envoieChemin(List<Integer> cheminResult) throws InterruptedException {
        int i;
        String msg= "s";
        for (i =1; i< cheminResult.size()-1; i++ ){
            msg = graphe.direction(cheminResult.get(i-1),cheminResult.get(i),cheminResult.get(i+1));
            if (msg=="u"){
                communication.sent("20u\n");
                communication.sent("20s\n");
            }else{
                communication.sent(construct(msg));
            }
            currentPos= cheminResult.get(i);
            if (graphe.estHopital(currentPos)){
                coffre--;
                communication.sent("20d\n");

            }
            System.out.println(graphe.estVictime(currentPos));
            if (graphe.estVictime(currentPos)){
                rammasser(currentPos);
                communication.sent("20t\n");
            }

            Thread.sleep(1500);
        }
        int next = graphe.next(cheminResult.get(i-1),cheminResult.get(i),msg);

        if (graphe.estLigne(next)){
            msg="s";
        }else{
            msg = "r";
        }
        communication.sent(construct(msg));

        currentPos=cheminResult.get(i);
        if (graphe.estHopital(currentPos)){
            coffre--;
            communication.sent("20d\n");
        }

    }

    private int fristListVictime(List<Integer> listBut){
        TreeSet<Integer> result =new TreeSet<>(
                new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        if (graphe.heuristique(o1,graphe.getHopitaux().get(0)) < (graphe.heuristique(o2,graphe.getHopitaux().get(0)))){
                            return -1;
                        }
                        return 1;
                    }
                }
        );

        for (int cur : listBut){
            result.add(cur);
        }
        return result.first();
    }


    private List<Couple> creerFils(Couple current, int but, List<Couple> vu){
        List<Couple> result = new LinkedList<>();
        boolean contains;
        int curSommet = current.getSommet();

        Couple fils;
        List<Integer> cheminPere;
        cheminPere = (current.getChemin());


        for (int curFils : graphe.getVoisin(curSommet)){
            contains = false;
            List<Integer> cheminFils =  new LinkedList<>(cheminPere);
            cheminFils.add(curFils);
            fils = new Couple(current.getG()+1,curFils,graphe,but,cheminFils);

            for (Couple curCoupleVu : vu){
                if (fils.compareTo(curCoupleVu)>0){
                    contains = true;
                }
            }
            if (!contains){
                result.add(fils);
            }
            fils=null;
        }
        return result;
    }

    private List<Integer> AEtoile(TreeSet<Couple> attente, List<Couple> vu, int but){

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
        for(Couple cur : filss){
            attente.add(cur);
        }
        vu.add(current);
        return AEtoile(attente,vu,but);
    }

}
