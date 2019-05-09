package graphe;

import java.util.LinkedList;
import java.util.List;

public class Triplet4Heuris {

    private float cout=0;
    private Case sommetDepart;
    private Graphe g;
    private Case but;

    public Triplet4Heuris(Case sommetDepart, Case but, Graphe g) {
        this.sommetDepart = sommetDepart;
        this.but = but;
        this.g=g;
        this.cout = this.calculCout();
    }

    private float calculCout(){
        return glouton(sommetDepart,but,new LinkedList<>());
    }

    private float glouton(Case sommet, Case but, List<Case> vu){
        float max = 100;
        float coutBis = 0;
        //car probleme adresse vu
        List<Case> vuBis = new LinkedList<>(vu);
        vuBis.add(sommet);
        List<Case> voisin = g.voisinPasVu(sommet,vuBis);
        if (sommet.equals(but)){
            return 0;
        }else if (voisin.contains(but)){
            return 1;
        }else {
            for ( Case cur : voisin) {
                vuBis.add(cur);
                coutBis = glouton(cur, but, vuBis);
                if (coutBis < max) {
                    max = coutBis;
                }
                vuBis.remove(cur);

            }
        }
        return 1 + max;
    }

    public float getCout() {
        return cout;
    }

    public Case getSommetDepart() {
        return sommetDepart;
    }

    public Case getBut() {
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
