package code;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Log {

    private String filName;
    private int conteur=0;
    private Robot robot;
    private LocalDateTime currentTime = LocalDateTime.now();
    private String monNxt;

    public Log(Robot robot) {
        FileWriter log=null;
        this.robot = robot;
        this.monNxt = robot.getMonNxt();
        this.filName = "src/log/"+monNxt+"LOG.txt";
        System.out.println(monNxt);
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
    }

    public void ecrire(String message){
        FileWriter log=null;
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
    }

    public int dernierPosCoop(String nomNXTRobot){
        Scanner scanner;
        String current = null;
        String []curSplit;
        int result=-1;
        File fichier = new File("src/log/"+nomNXTRobot+"LOG.txt");
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
        return result ;
    }

    public int dernierPosPreviousCoop(String nomNXTRobot){
        Scanner scanner;
        String current = null;
        String []curSplit;
        int result=-1;
        File fichier = new File("src/log/"+nomNXTRobot+"LOG.txt");
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
        return result ;
    }

    public void close(){
        FileWriter log=null;
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
    }

    public boolean estFiniCoop(String nomNXTRobot){
        Scanner scanner;
        String current = null;
        String []curSplit;
        int result=-1;
        File fichier = new File("src/log/"+nomNXTRobot+"LOG.txt");
        try {
            scanner = new Scanner(fichier);
        } catch (FileNotFoundException e) {
            return -1;
        }
        while (scanner.hasNextLine()) {
            current = scanner.nextLine();
        }

        curSplit = current.split("@");

        return curSplit.length==1;

    }


}
