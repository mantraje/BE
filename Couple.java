package robot;

import java.util.List;

import graphe.Case;
import graphe.Graphe;

public class Couple implements Comparable<Couple> {

    private float g =0;
    private Case sommet;
    private Case previous;
    private Graphe graphe;
    private Case but;
    private List<String> chemin;

    public Couple(float g, Case sommet, Graphe graphe, Case but, List<String> chemin,Case previous) {
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

    public Case getSommet() {
        return sommet;
    }

    public List<String> getChemin() {
        return chemin;
    }

    public float getG() {
        return g;
    }
    
    public Case getPrevious() {
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
