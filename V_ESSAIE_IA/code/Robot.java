package code;

public abstract class Robot {
	protected int currentPos;
	protected int previousPos;
	protected int initPos;
	protected int coffre;
	protected int capaciteCofrre;
	protected static Graphe graphe;
	protected String lastDeplacement="";

	public Robot(int posRobotPilotePrevious, int init, Graphe g, String last, int capaciteCofrre) {
		this.initPos = init;
		this.currentPos = init;
		this.previousPos = posRobotPilotePrevious;
		this.graphe = g;
		this.lastDeplacement = last;
		this.capaciteCofrre = this.capaciteCofrre;
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
	protected String construct(String action) {
		String toSent = action;
		return "20" + toSent+"\n";
	}

	public boolean verifTake() { return graphe.estVictime(currentPos); }

	public boolean verifDrop() {
		 return graphe.estHopital(currentPos) ;
	}

	protected String mouvement(String direction,int next) {
			if(direction=="u") {
				if (lastDeplacement=="u"){
					previousPos=graphe.next(previousPos,currentPos,"s");
					lastDeplacement= "s";
				}else {
					previousPos = next;
				}
			}else {
				previousPos = currentPos;
				currentPos = next;
			}
		lastDeplacement=direction;
		return construct(direction);
	}

	public void rammasser(int cur) {
		graphe.rammasserVictime(cur);
		coffre++;
	}
}