package graphe;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import robot.Couple;

public class Graphe {
	private List<Triplet4Heuris> Hvictimes;
	private List<Triplet4Heuris> Hhopitaux;
	private Case[][] map;
	private List<Case> victimes;
	private List<Case> hopitaux;
	
	public Graphe(int tailleX,int tailleY,Case[][] matrice,List<Case> vic,List<Case> hop) {
		this.map = matrice;
		this.victimes=vic;
		this.hopitaux=hop;
	}
	
	private List<Case> matriceToList(){
		List<Case> matPlate = new LinkedList<>();
		for(int i=0;i<map.length;i++) {
			for(Case c : map[i]) {
				matPlate.add(c);
			}
		}
		return matPlate;
	}
	
	private Case getCase(int x,int y) {
		return map[x][y];
	}
	
    private List<Triplet4Heuris> initHeuristique(List<Case> listBut){
        List<Case> caseGraphe = matriceToList();
        List<Triplet4Heuris> lTriplet =  new LinkedList<>();
        for (Case cur : listBut){
            for(Case som : caseGraphe) {
                lTriplet.add(new Triplet4Heuris(som,cur,this));
            }
        }
        return lTriplet;
    }
    
    public void  rammasserVictime (int x, int y){
        if (victimes.contains(getCase(x,y))){
            victimes.remove(getCase(x,y));
        }
    }

    public boolean estHopital(int x,int y){
        return hopitaux.contains(getCase(x,y));
    }
    
    public boolean estVictime(int x,int y){
        return victimes.contains(getCase(x,y));
    }

    public List<Case> getVictimes() {
        return victimes;
    }

    public List<Case> getHopitaux() {
        return hopitaux;
    }

    public String toString (){
        return map.toString();
    }
    
    public List<Case> getVoisinCouple(Couple currentPos) {
        return currentPos.getSommet().getVoisin();
    }
    
    public float heuristique(Case sommet, Case but) {
        List<Triplet4Heuris> listBut;

        if (victimes.contains(but)){
            listBut = Hvictimes;
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
    
    public List<Case> voisinPasVu(Case current,List<Case> vu){
    	return current.voisinNonVu(vu);
    }
    
    public String inverse(String dir) {
    	switch (dir) {
    		case ("N"):
    			return "S";
    		case ("S"):
    			return "N";
    		case ("O"):
    			return "E";
    		case ("E"):
    			return "O";
    		default:
    			return null;
    	}
    }
    
    public String nextOrientation(int x, int y,String dir ,String mouv) {
    	Case current = map[x][y];
    	Case next = current.next(dir);
    	return next.newOrientation(inverse(dir),mouv );
    }
    
    public String nextMouv(int x, int y, String dir,String dirSortie) {
    	Case current = map[x][y];
    	Case next = current.next(dir);
    	return next.messageToSent(inverse(dir), dirSortie);
    }
    
    public boolean isMouvementPossible(int x, int y, String dir, String mouvement) {
    	Case current = map[x][y];
    	Case next = current.next(dir);
    	return next.mouvementPossible(inverse(dir), mouvement);
    }
    
}
