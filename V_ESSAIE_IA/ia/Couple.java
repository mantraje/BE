package ia;

import code.Graphe;

import java.util.LinkedList;
import java.util.List;

public class Couple implements Comparable<Couple> {

    private int g =0;
    private int sommet;
    private Graphe graphe;
    private int but;
    private List<Integer> chemin;

    public Couple(int g, int sommet, Graphe graphe, int but, List<Integer> chemin) {
        this.g = g;
        this.sommet = sommet;
        this.graphe = graphe;
        this.but = but;
        this.chemin=chemin;
    }


    @Override
    public int compareTo(Couple o){
        int totolCur = this.g + (graphe.heuristique(sommet,but));
        int totolCompare = o.g + (graphe.heuristique(o.sommet,but));

        if (totolCur==totolCompare) return 0;
        else if (totolCur<totolCompare) return -1;
        else if (sommet == o.sommet) return 1;
        return -1;
    }

    public boolean equals(Object o){
        if (!(o.getClass()==Couple.class)){
            return false;
        }
        Couple compare =(Couple) o;
        int totolCur = this.g + (graphe.heuristique(sommet,but));
        int totolCompare = compare.g + (graphe.heuristique(compare.sommet,but));
        if (totolCur==totolCompare && sommet == compare.sommet) return true;
        return false;
    }

    public Integer getSommet() {
        return sommet;
    }

    public List<Integer> getChemin() {
        return chemin;
    }

    public int getG() {
        return g;
    }

    @Override
    public String toString() {
        return "Couple{" +
                "g=" + g +
                ", sommet=" + sommet +
                ", chemin=" + chemin +
                '}';
    }
}
