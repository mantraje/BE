package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class Log {

    private String filName;
    private int conteur=0;
    private Robot robot;
    private LocalDateTime currentTime = LocalDateTime.now();
    private String monNxt;
    private Semaphore sem;

    public Log(Robot robot,Semaphore sem) {
        FileWriter log=null;
        this.sem = sem;
        this.robot = robot;
        this.monNxt = robot.getMonNxt();
        this.filName = "src/log/"+monNxt+"LOG.txt";
        System.out.println(monNxt);
        try {
			sem.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
            log = new FileWriter(filName,false);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            currentTime = LocalDateTime.now();
            log.write(currentTime+"@"+monNxt+"@"+robot.getPreviousPos()+"@"+robot.getCurrentPos()+"@"+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            log.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sem.release();
    }

    public void ecrire(String message){
        FileWriter log=null;
        try {
			sem.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
            log = new FileWriter(filName,true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            currentTime = LocalDateTime.now();
            log.write(currentTime+"@"+monNxt+"@"+robot.getPreviousPos()+"@"+robot.getCurrentPos()+"@"+message+"\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            log.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        conteur++;
        sem.release();
    }

    public int dernierPosCoop(String nomNXTRobot){
        if (nomNXTRobot==null){return -1;}
        Scanner scanner;
        String current = null;
        String []curSplit;
        int result=-1;
        File fichier = new File("src/log/"+nomNXTRobot+"LOG.txt");
        try {
			sem.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
            scanner = new Scanner(fichier);
        } catch (FileNotFoundException e) {
            return -1;
        }
        while (scanner.hasNextLine()) {
            current = scanner.nextLine();
        }
        if (current!=null) {
            curSplit =current.split("@");
            result = Integer.parseInt(curSplit[3]);
        }
        scanner.close();
        sem.release();
        return result ;
    }

    public int dernierPosPreviousCoop(String nomNXTRobot){
        if (nomNXTRobot==null){return -1;}
        Scanner scanner;
        String current = null;
        String []curSplit;
        int result=-1;
        File fichier = new File("src/log/"+nomNXTRobot+"LOG.txt");
        try {
			sem.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
            scanner = new Scanner(fichier);
        } catch (FileNotFoundException e) {
            return -1;
        }
        while (scanner.hasNextLine()) {
            current = scanner.nextLine();
        }
        if (current!=null) {
            curSplit =current.split("@");
            result = Integer.parseInt(curSplit[2]);
        }
        scanner.close();
        sem.release();
        return result ;
    }

    public void close(){
        FileWriter log=null;
        try {
			sem.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
            log = new FileWriter(filName,true);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            log.write(String.valueOf(conteur));
            log.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        sem.release();
    }

    public boolean estFiniCoop(String nomNXTRobot){
        if (nomNXTRobot==null){return true;}
        Scanner scanner;
        String current = null;
        String []curSplit;
        File fichier = new File("src/log/"+nomNXTRobot+"LOG.txt");
        try {
			sem.acquire();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        try {
            scanner = new Scanner(fichier);
        } catch (FileNotFoundException e) {
            return false;
        }
        while (scanner.hasNextLine()) {
            current = scanner.nextLine();
        }

        curSplit = current.split("@");
        sem.release();
        return curSplit.length==1;
    }


    public String lastDepCoop(String nomNXTRobot){
        if (nomNXTRobot==null){return "e";}
        Scanner scanner;
        String current = null;
        String []curSplit = new String[0];
        String []curSplit2 = new String[0];
        File fichier = new File("src/log/"+nomNXTRobot+"LOG.txt");
        try {
            sem.acquire();
        } catch (InterruptedException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        try {
            scanner = new Scanner(fichier);
        } catch (FileNotFoundException e) {
            return null;
        }
        while (scanner.hasNextLine()) {
            current = scanner.nextLine();
        }
        if (current!=null) {
            curSplit =current.split("@");
        }
        scanner.close();
        sem.release();
        return (curSplit[4].split("0"))[1] ;
    }

}