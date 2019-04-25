package ia;

import code.Graphe;

import java.util.List;

public class Couple implements Comparable<Couple> {

    private float g =0;
    private int sommet;
    private int previous;
    private Graphe graphe;
    private int but;
    private List<Integer> chemin;

    public Couple(float g, int sommet, Graphe graphe, int but, List<Integer> chemin,int previous) {
        this.g = g;
        this.sommet = sommet;
        this.graphe = graphe;
        this.but = but;
        this.chemin=chemin;
        this.previous=previous;
    }


    @Override
    public int compareTo(Couple o){
        float totolCur = this.g + (graphe.heuristique(sommet,but));
        float totolCompare = o.g + (graphe.heuristique(o.sommet,but));
        if (totolCur<totolCompare) return -1;
        else if (totolCur>totolCompare) return 1;
        else if (sommet == o.sommet) return 0;
        return -1;
    }

    public boolean equals(Object o){
        if (!(o.getClass()==Couple.class)){
            return false;
        }
        Couple compare =(Couple) o;
        float totolCur = this.g + (graphe.heuristique(sommet,but));
        float totolCompare = compare.g + (graphe.heuristique(compare.sommet,but));
        if (totolCur==totolCompare && sommet == compare.sommet) return true;
        return false;
    }

    public Integer getSommet() {
        return sommet;
    }

    public List<Integer> getChemin() {
        return chemin;
    }

    public float getG() {
        return g;
    }
    
    public float getPrevious() {
        return previous;
    }
    @Override
    public String toString() {
        return "Couple{" +
                "g=" + g +
                ", sommet=" + sommet +
                ", chemin=" + chemin +
                ", previous ="+ previous + 
                '}';
    }
}