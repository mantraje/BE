package code;

import ia.Couple;
import ia.Triplet4Heuris;

import java.util.*;

public class Graphe {

    private Map<Integer, List<Integer>> map;
    private List<Integer> victimes;
    private List<Integer> hopitaux;
    private List<Triplet4Heuris> Hvictime;
    private List<Triplet4Heuris> Hhopitaux;



    public Graphe(Map<Integer, List<Integer>> mapC, List<Integer> victimesC, List<Integer> hopitauxC) {
        //((sommet,[origine,droite,gauche]) ou (sommet,[origine,suivant])) list
        if (mapC!=null){
            this.map = mapC;
            List<Integer> lnull = new LinkedList<>();
            lnull.add(-1);
            lnull.add(-1);
            lnull.add(-1);
            map.put(-1,lnull);
        }else{this.map = new LinkedHashMap<>(); }
        if (victimesC!=null) {
            this.victimes = victimesC;
        } else{this.victimes = new LinkedList<>(); }
        if (hopitauxC!=null) {
            this.hopitaux = hopitauxC;
        }else{this.hopitaux = new LinkedList<>(); }

        Hvictime =  initHeuristique(victimes);
        Hhopitaux =  initHeuristique(hopitaux);
    }

    private List<Triplet4Heuris> initHeuristique(List<Integer> listBut){

        Set<Integer> sommetsGraphe = map.keySet();
        List<Triplet4Heuris> lTriplet =  new LinkedList<>();
        for (int cur : listBut){
            for(int som : sommetsGraphe) {
                lTriplet.add(new Triplet4Heuris(som,cur,this));
            }
        }
        return lTriplet;
    }


    public String direction (int previousPos, int currentPos, int nextPos){
        // TODO: 2019-04-09 verife voisin differant de null
        List<Integer> voisin = map.get(currentPos);
        System.out.println("p :" + previousPos);
        System.out.println("c :" + currentPos);
        System.out.println("n :" + nextPos);
        //cas ligne droit
        if (estLigne(currentPos)){
            if (previousPos==nextPos) {
                return "u";
            }else if (voisin.contains(nextPos)) {
                return "s";
            }else{return "e";}
        }
        //cas intersection
        //arrive par origine
        if (previousPos==voisin.get(0)) {
            if (nextPos == voisin.get(1)) {
                return "r";
            } else if (nextPos == voisin.get(2)) {
                return "l";
            } else {
                return "e";
            }
        }
        //arrive par la droite
        if (previousPos==voisin.get(1)) {
            if (nextPos == voisin.get(2)) {
                return "r";
            } else if (nextPos == voisin.get(0)) {
                return "l";
            } else {
                return "e";
            }
        }
        //arrive par la gauche
        if (previousPos==voisin.get(2)) {
            if (nextPos == voisin.get(0)) {
                return "r";
            } else if (nextPos == voisin.get(1)) {
                return "l";
            } else {
                return "e";
            }
        }
        return "e";
    }

    public void  rammasserVictime (int posVictime){
        if (victimes.contains(posVictime)){
            victimes.remove((Integer) posVictime);
            //possible bug car posVictime est un int /!\
        }
    }

    public boolean estHopital(int currentPos){
        return hopitaux.contains(currentPos);
    }
    public boolean estVictime(int currentPos){
        return victimes.contains(currentPos);
    }

    public List<Integer> getVictimes() {
        return victimes;
    }

    public List<Integer> getHopitaux() {
        return hopitaux;
    }


    public boolean estIntersec(int currentPos){
        List<Integer> voisin = map.get(currentPos);
        return (voisin.size() == 3);
    }

    public boolean estLigne(int currentPos){
        List<Integer> voisin = map.get(currentPos);
        return (voisin.size() == 2);
    }


    public String toString (){
        return map.toString();
    }

    public int next(int previousPos,int currentPos,String lastDep) {
        List<Integer> voisin = map.get(currentPos);
        if(lastDep=="u") {
        	return currentPos;
        }
        if (estLigne(currentPos)) {
            if (previousPos == voisin.get(0)) {
                return voisin.get(1);
            } else {
                return voisin.get(0);
            }
        } else {
            switch (lastDep) {
                case "r":
                    if (previousPos == voisin.get(0)) {
                        return voisin.get(1);
                    }
                    //arrive par la droite
                    if (previousPos == voisin.get(1)) {
                        return voisin.get(2);
                    }
                    //arrive par la gauche
                    if (previousPos == voisin.get(2)) {
                        return voisin.get(0);
                    }
                    break;
                case "l":
                    if (previousPos == voisin.get(0)) {
                        return voisin.get(2);
                    }
                    //arrive par la droite
                    if (previousPos == voisin.get(1)) {
                        return voisin.get(0);
                    }
                    //arrive par la gauche
                    if (previousPos == voisin.get(2)) {
                        return voisin.get(1);
                    }
                    break;
            }
        }
        return -1;
    }


    public List<Integer> getVoisin(int currentPos) {
        if (currentPos==-1){
            return new LinkedList<Integer>();
        }
        return map.get(currentPos);
    }

    public List<Integer> getVoisinCouple(Couple currentPos) {
        return map.get(currentPos.getSommet());
    }

    public int heuristique(int sommet, int but) {
        List<Triplet4Heuris> listBut;

        if (victimes.contains(but)){
            listBut = Hvictime;
        }else{
            listBut = Hhopitaux;
        }
        for (Triplet4Heuris c : listBut){
            if (c.getBut()==but){
                if (c.getSommetDepart()==sommet){
                    return c.getCout();
                }
            }
        }
        return 100;
    }

    public int autreVoisin (int currentPos,int diff){
        List<Integer> voisin = map.get(currentPos);
        if (diff == voisin.get(0)) {
            return voisin.get(1);
        } else {
            return voisin.get(0);
        }
    }

    public List<Integer> voisinDansL1MaisPasDansL2 (int current, List<Integer>L2) {
        List<Integer> result = new LinkedList<>();
        if (this.getVoisin(current).isEmpty() || L2.isEmpty()){
            return result;
        }
        for (int cur : this.getVoisin(current)) {
            if (!L2.contains(cur)) {
                result.add(cur);
            }
        }
        return result;
    }

}