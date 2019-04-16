package code;

public abstract class Robot {
	protected int currentPos;
	protected int previousPos;
	protected int initPos;
	protected int coffre;
	protected Graphe graphe;
	String lastDeplacement = "";

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
	private String construct(String action) {
		String toSent = action;
		return "20" + toSent;

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

	protected String mouvement(String direction,int next) {
		previousPos = currentPos;
		currentPos = next;
		return construct(direction);
	}

	private void rammaser() {
		graphe.rammasserVictime(currentPos);
		coffre++;
	}
}