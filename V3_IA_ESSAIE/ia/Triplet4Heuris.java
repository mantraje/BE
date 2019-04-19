package ia;

import code.Graphe;

import java.util.LinkedList;
import java.util.List;

public class Triplet4Heuris {

    private float cout=0;
    private int sommetDepart;
    private Graphe g;
    private int but;

    public Triplet4Heuris(int sommetDepart, int but, Graphe g) {
        this.sommetDepart = sommetDepart;
        this.but = but;
        this.g=g;
        this.cout = this.calculCout();
    }

    private float calculCout(){
        return glouton(sommetDepart,but,new LinkedList<>());
    }

    private float glouton(int sommet, int but, List<Integer> vu){
        float max = 100;
        float coutBis = 0;
        //car probleme adresse vu
        List<Integer> vuBis = new LinkedList<>(vu);
        vuBis.add(sommet);
        List<Integer> voisin =  g.voisinDansL1MaisPasDansL2(sommet,vuBis);
        if (sommet == but){
            return 0;
        }else if (voisin.contains(but)){
            return 1;
        }else {
            for ( int cur : voisin) {
                vuBis.add(cur);
                coutBis = glouton(cur, but, vuBis);
                if (coutBis < max) {
                    max = coutBis;
                }
                vuBis.remove((Integer)cur);

            }
        }
        return 1 + max;
    }

    public float getCout() {
        return cout;
    }

    public int getSommetDepart() {
        return sommetDepart;
    }

    public int getBut() {
        return but;
    }

    @Override
    public String toString() {
        return "{" +
                "cout=" + cout +
                ", sommetDepart=" + sommetDepart +
                ", but=" + but +
                "} \n";
    }
}
