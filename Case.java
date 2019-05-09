package graphe;

import java.util.LinkedList;
import java.util.List;

public class Case {
	private Case nord=null;
	private Case sud=null;
	private Case ouest=null;
	private Case est=null;
	private String[] voisin;
	
	public Case (Case nord,Case ouest,String[] voisin) {
		this.nord = nord;
		this.ouest=ouest;
		this.voisin = voisin;
	}

	public void setSud(Case sud) {
		this.sud = sud;
	}

	public void setEst(Case est) {
		this.est = est;
	}
	
	//retourne tous les voisins de cette Case
	public List<Case> getVoisin(){
		List<Case> vois = new LinkedList<>();
		for(String v : voisin) {
			switch (v) {
				case ("N"):
					vois.add(nord);
					break;
				case ("S"):
					vois.add(sud);
					break;
				case ("O"):
					vois.add(ouest);
					break;
				case ("E"):
					vois.add(est);
					break;
			}
		}
		return vois;
	}
	
	//retourne les voisin possible selon une direction 
	public List<Case> getVoisinPossible(String dir){
		List<Case> vois = new LinkedList<>();
		if(isIntersect()) {
			Case next = next(dir);
			vois.add(next);
			return vois;
		}else {
			vois.add(trad(dir));
			vois.add(trad(autreVoisin(dir)));
		}
		return vois;
	}
	
	public boolean isIntersect() {
		return voisin.length==3;
	}
	
	//Uniquement pour ligneDroite/virage : retourne le voisin qui n'est pas "v"
	private String autreVoisin(String v) {
		if(!isIntersect()) {
			for(String vois:voisin) {
				if(v!=vois) {
					return vois;
				}
			}
		}
		return null;
	}
	
	//retourne les voisins qui ne sont pas dans vu
	public List<Case> voisinNonVu(List<Case> vu) {
		List<Case> retour = getVoisin();
		retour.removeAll(vu);
		return retour;
	}
	
	//traduit une string (O/E/S/N) vers une case
	private Case trad(String dir) {
		switch (dir) {
		case ("N"):
			return nord;
		case ("S"):
			return sud;
		case ("O"):
			return ouest;
		case ("E"):
			return est;
		}
		return null;
	}
	
	//traduit une case vers un string
	private String tradCase(Case x) {
		if(x.equals(nord)) {return "N";}
		if(x.equals(sud)) {return "S";}
		if(x.equals(ouest)) {return "O";}
		if(x.equals(est)) {return "E";}
		return "e";
	}
	
	//retourne la prochaine case selon une direction
	public Case next(String dir) {
		for(String v : voisin) {
			if(v.equals(dir)) {
				return trad(dir);
			}
		}
		return null;
	}
	
	//retourne la direction qui se situe un quart à gauche de dir
	public Case quartGauche(String dir) {
		switch (dir) {
			case ("N"):
				return trad("O");
			case ("S"):
				return trad("E");
			case ("O"):
				return trad("S");
			case ("E"):
				return trad("N");
		}
		return null;
	}
	
	//retourne la direction qui se situe un quart a droite de dir
	public Case quartDroite(String dir) {
		switch (dir) {
			case ("N"):
				return trad("E");
			case ("S"):
				return trad("O");
			case ("O"):
				return trad("N");
			case ("E"):
				return trad("S");
		}
		return null;
	}
	
	//Pour IA: retourne le message à envoyer selon le voisin d'entree et de sortie
	public String messageToSent(String entree,String sortie) {
		if(isIntersect()) {
			//arrive par neutral
			if(quartGauche(entree)!=null && quartDroite(entree)!=null) {
				if(trad(sortie).equals(quartDroite(entree))) {
					return "l";
				}else {
					return "r";
				}
			}else {
				//arrive par la gauche
				if(quartGauche(entree)!=null) {
					if(trad(sortie)==quartDroite(entree)) {
						return "l";
					}else {
						return "r";
					}
				//arrive par la droite
				}else {
					if(trad(sortie)==quartGauche(entree)) {
						return "r";
					}else {
						return "l";
					}
				}
			}
		}else {
			return "s";
		}
	}
	
	//Pour pilote: retourne la nouvelle orientation du robot apres avoir effectué mouvement en arrivant par entree
	public String newOrientation(String entree,String mouvement) {
		if(isIntersect()) {
			if(mouvement.equals("u")||mouvement.equals("s")) {return "e";}
			//arrive par neutral
			if(quartDroite(entree)!=null && quartGauche(entree)!=null) {
				if(mouvement.equals("l")) {return tradCase(quartDroite(entree));}
				else {return tradCase(quartGauche(entree));}
			}else {
				//arrive par gauche
				if(quartGauche(entree)!=null) {
					if(mouvement.equals("r")) {return tradCase(quartGauche(entree));}
				//arrive par droite
				}else {
					if(mouvement.equals("l")) {return tradCase(quartDroite(entree));}
				}
				//inverse de entrée
				return tradCase(quartGauche(tradCase(quartGauche(entree))));
			}
		}else {
			if(mouvement.equals("l")||mouvement.equals("r")) {return "e";}
			return autreVoisin(entree);
		}
	}
	
	//retourne true si le mouvement est possible
	public boolean mouvementPossible(String entree,String mouvement) {
		return newOrientation(entree,mouvement)!="e";
	}
}
