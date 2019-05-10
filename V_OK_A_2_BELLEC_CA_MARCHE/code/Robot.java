package code;

import java.util.concurrent.Semaphore;

public abstract class Robot extends Thread {
	protected int currentPos;
	protected int previousPos;
	protected int initPos;
	protected String monNxt;
	protected int coffre = 0 ;
	protected int capaciteCofrre;
	protected static Graphe graphe;
	protected String lastDeplacement="";
	protected Log log;
	protected String nomCoop;
	protected Bluetooth communication;
	protected Simulation simu;
	protected Semaphore sem;

	public Robot(String monNXT,int posRobotPilotePrevious, int init, Graphe g, String last, int capaciteCofrre,String nomCoop,Bluetooth b,Semaphore sem,Simulation simu) {
		this.monNxt=monNXT;
		this.initPos = init;
		this.currentPos = init;
		this.previousPos = posRobotPilotePrevious;
		this.graphe = g;
		this.lastDeplacement = last;
		this.capaciteCofrre = capaciteCofrre;
		this.nomCoop=nomCoop;
		this.log = new Log(this,sem);
		this.communication=b;
		this.sem=sem;
		this.simu=simu;
		if (communication!=null) {
			communication.connexion();
		}
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
		return "20" + toSent;
	}


	public String getMonNxt() {
		return monNxt;
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

					lastDeplacement=direction;
				}
			}else {
				if (lastDeplacement=="u"){
					lastDeplacement= "s";
				}else {
					previousPos = currentPos;
					currentPos = next;
				}

				lastDeplacement=direction;
			}
		return construct(direction);
	}

	public void rammasser(int cur) {
		graphe.rammasserVictime(cur);
		coffre++;
	}

	public void sent(String msg){
		if (communication!=null) {
			communication.sent(msg+"\n");
			communication.receive();
		}
		if (simu!=null) {
			simu.sent(msg,currentPos,previousPos,lastDeplacement,this.getClass().equals(Pilote.class));
		}
		log.ecrire(msg);
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}