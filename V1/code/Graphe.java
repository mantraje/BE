package code;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Graphe {

    private Map<Integer, List<Integer>> map;
    private List<Integer> victimes;
    private List<Integer> hopitaux;


    public Graphe(Map<Integer, List<Integer>> mapC, List<Integer> victimesC, List<Integer> hopitauxC) {
        //((sommet,[origine,droite,gauche]) ou (sommet,[origine,suivant])) list
        if (mapC!=null){
            this.map = mapC;
        }else{this.map = new LinkedHashMap<>(); }
        if (victimesC!=null) {
            this.victimes = victimesC;
        } else{this.victimes = new LinkedList<>(); }
        if (hopitauxC!=null) {
            this.hopitaux = hopitauxC;
        }else{this.hopitaux = new LinkedList<>(); }
    }


    public char direction (int previousPos, int currentPos, int nextPos){
        // TODO: 2019-04-09 verife voisin differant de null

        List<Integer> voisin = map.get(currentPos);
        //cas ligne droit
        if (voisin.size()==2){
            if (previousPos==nextPos) {
                return 'u';
            }else if (voisin.contains(nextPos)) {
                return 's';
            }else{return 'e';}
        }
        //cas intersection
        //arrive par origine
        if (currentPos==voisin.get(0)) {
            if (nextPos == voisin.get(1)) {
                return 'r';
            } else if (nextPos == voisin.get(2)) {
                return 'l';
            } else {
                return 'e';
            }
        }
        //arrive par la droite
        if (currentPos==voisin.get(1)) {
            if (nextPos == voisin.get(2)) {
                return 'r';
            } else if (nextPos == voisin.get(0)) {
                return 'l';
            } else {
                return 'e';
            }
        }
        //arrive par la gauche
        if (currentPos==voisin.get(2)) {
            if (nextPos == voisin.get(0)) {
                return 'r';
            } else if (nextPos == voisin.get(1)) {
                return 'l';
            } else {
                return 'e';
            }
        }
        return 'e';
    }

    public void  rammasserVictime (int posVictime){
        if (victimes.contains(posVictime)){
            victimes.remove((Integer) posVictime);
            //possible bug car posVictime est un int /!\
        }
    }

    // renvoie -1 si erreur ou intersection (peut etre changer si cas pas defalt est defini),
    // renvoie le seul voisin sur prevouis = -1
    public int toutDroit (int previousPos,int currentPos) {
        List<Integer> voisin = map.get(currentPos);
        //verife que c'est pas une intersection
        if (voisin.size() != 2) {
            return -1;
        } else{
            if (previousPos == voisin.get(0)) {
                return voisin.get(1);
            } else if (previousPos == voisin.get(1)) {
                return voisin.get(0);
            }
        }
        return -1;
    }

    public boolean estHopital(int currentPos){
        return hopitaux.contains(currentPos);
    }
    public boolean estVictime(int currentPos){
        return victimes.contains(currentPos);
    }

    public int tournerGauche (int previousPos, int currentPos){
        List<Integer> voisin = map.get(currentPos);
        if (previousPos==voisin.get(0)) {
            return voisin.get(2);
        }
        //arrive par la droite
        if (previousPos==voisin.get(1)) {
            return voisin.get(0);
        }
        //arrive par la gauche
        if (previousPos==voisin.get(2)) {
            return voisin.get(1);
        }
        return -1;

    }

    public List<Integer> getVictimes() {
        return victimes;
    }

    public List<Integer> getHopitaux() {
        return hopitaux;
    }

    public int tournerDroite (int previousPos, int currentPos){
        List<Integer> voisin = map.get(currentPos);

        if (previousPos==voisin.get(0)) {
            return voisin.get(1);
        }
        //arrive par la droite
        if (previousPos==voisin.get(1)) {
            return voisin.get(2);
        }
        //arrive par la gauche
        if (previousPos==voisin.get(2)) {
            return voisin.get(0);
        }
        return -1;

    }
    public boolean estIntersec(int currentPos){
        List<Integer> voisin = map.get(currentPos);
        return (voisin.size() == 3);
    }

    public boolean estLigne(int currentPos){
        List<Integer> voisin = map.get(currentPos);
        return (voisin.size() == 2);
    }

    public void deposerVictime(int currentPos) {
    }

    public String toString (){
        return map.toString();
    }

    public int DemiTour(int previousPos, int currentPos) {
        if (this.estLigne(previousPos)){
            return this.toutDroit(currentPos,previousPos);
        }else {
            return previousPos;
        }
    }
}
