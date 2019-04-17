package code;

public abstract class Robot {
	protected int currentPos;
	protected int previousPos;
	protected int initPos;
	protected int coffre;
	protected Graphe graphe;
	protected String lastDeplacement="";

	public Robot(int posRobotPilotePrevious,int init, Graphe g,String last) {
		this.initPos = init;
		this.currentPos = init;
		this.previousPos = posRobotPilotePrevious;
		this.graphe = g;
		this.lastDeplacement = last;
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
		return "20" + toSent+"\n";
	}

	public boolean verifTake() {
		System.out.println("VerifTake : pos robot "+currentPos+"; Res "+graphe.estVictime(currentPos)+"");
		return graphe.estVictime(currentPos); 
	}

	public boolean verifDrop() {
		 return graphe.estHopital(currentPos) ;
	}

	protected String mouvement(String direction,int next) {
		if(!(lastDeplacement=="u")) {
			if(direction=="u") {
				previousPos=next;
			}else {
				previousPos = currentPos;
				currentPos = next;
			}
			lastDeplacement=direction;
		}
		return construct(direction);
	}

	public void rammasser() {
		graphe.rammasserVictime(currentPos);
		coffre++;
	}
}