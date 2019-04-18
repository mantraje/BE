package ia;

import code.Graphe;

import java.util.LinkedList;
import java.util.List;

public class Triplet4Heuris {

    private int cout;
    private int sommetDepart;
    private Graphe g;
    private int but;

    public Triplet4Heuris(int sommetDepart, int but, Graphe g) {
        this.sommetDepart = sommetDepart;
        this.but = but;
        this.g=g;
        this.cout = this.calculCout();
    }

    private int calculCout(){
        List<Integer> vu = new LinkedList<>();
        return glouton(sommetDepart,but,vu);
    }

    private int glouton(int sommet, int but, List<Integer> vu){
        int max = 100;
        int coutBis = 0;
        vu.add(sommet);
        List<Integer> voisin =  g.voisinDansL1MaisPasDansL2(sommet,vu);
        if (sommet == but){
            return 0;
        }else if (voisin.contains(but)){
            return 1;
        }else {
            for ( int cur : voisin) {
                coutBis = glouton(cur, but,vu);
            if (coutBis < max) {
                max = coutBis;
                }
            }
        }
        return 1 + max;
    }

    public int getCout() {
        return cout;
    }

    public int getSommetDepart() {
        return sommetDepart;
    }

    public int getBut() {
        return but;
    }



}
