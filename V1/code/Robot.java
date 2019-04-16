package code;

public abstract class Robot {
	protected int currentPos;
	protected int previousPos;
	protected int initPos;
	protected int coffre;
	protected Graphe graphe;

	public Robot(int posRobotPilotePrevious,int init, Graphe g) {
		this.initPos = init;
		this.currentPos = init;
		this.previousPos = posRobotPilotePrevious;
		this.graphe = g;
	}

	public int getCurrentPos() {
		return currentPos;
	}

	public int getPreviousPos() {
		return previousPos;
	}

	public int getInitPos() {
		return initPos;
	}

	public Graphe getGraphe() {
		return graphe;
	}

	// construit le message � envoyer au robot
	private String construct(boolean take, boolean drop, String action) {
		String toSent = action;
		if (!(take || drop)) {
			toSent += "\n";
			return "20" + toSent;
		}
		if (take) {
			toSent += "t\n";
		}
		if (drop) {
			toSent += "d\n";
		}
		return "30" + toSent;
	}

	private boolean verifTake(boolean take) {
		if (graphe.estVictime(currentPos) && take) {
			this.rammaser();
			return true;
		}
		return false;
	}

	private boolean verifDrop(boolean drop) {
		if (graphe.estHopital(currentPos) && drop) {
			coffre--;
			return true;
		}
		return false;
	}

	protected String mouvement(String direction, boolean take, boolean drop) {
		int finalPos;
		
		switch (direction){
			case "s":
				finalPos = graphe.toutDroit(previousPos, currentPos);
				break;
			case "l":
				finalPos = graphe.tournerGauche(previousPos, currentPos);
				break;
			case "r":
				finalPos = graphe.tournerDroite(previousPos, currentPos);
				break;
			case "u" :
				if (graphe.estLigne(currentPos)) {
					finalPos = graphe.toutDroit(currentPos,previousPos);
				}else {
					finalPos = previousPos;
				}
				break;
			default :
				return "";
		}

		previousPos = currentPos;
		currentPos = finalPos;

		boolean dropBis = verifDrop(drop);
		boolean takeBis = verifTake(take);

		return construct(takeBis, dropBis, direction);
	}

	private void rammaser() {
		graphe.rammasserVictime(currentPos);
		coffre++;
	}
}