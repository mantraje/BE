package code;



import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class Bluetooth {
	private NXTComm conn;
    private DataOutputStream dos;
    private DataInputStream dis;
    private boolean connexion = false;
    private String monNxt;
    private FileWriter log;
    Robot robot;
    private int conteur=0;
    LocalDateTime currentTime = LocalDateTime.now();
    private byte[] buffer = new byte[1024];

    public Bluetooth(String monNxt,Robot robot)  {

        this.monNxt = monNxt;
        this.robot = robot;
            try {
				log = new FileWriter("src/log/"+monNxt+"LOG.txt");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            System.out.println(log==null);
            try {
				log.write("ROBOT "+monNxt+ "\n");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        
    }

    public void connexion() {

        try {
			conn = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
		} catch (NXTCommException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

            // Adresse de Thorn : 001653161388
            System.out.println("Tentative de connexion � " + monNxt);
            NXTInfo info = new NXTInfo(NXTCommFactory.BLUETOOTH, monNxt ,"001653161388");
            try {
				connexion = conn.open(info);
			} catch (NXTCommException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            if (!connexion) {
                System.err.println("Failed to connect to any NXT");
            } else {
                System.out.println("Connexion OK");
                dos = new DataOutputStream(conn.getOutputStream());
                dis = new DataInputStream(conn.getInputStream());
                if(dos  == null) {
                	System.out.println("pas de data output");
                }
                if(dis == null) {
                	System.out.println("pas de data input");
                }
                System.out.println("avant envoi nom ordi");
                this.sent("160DESKTOP-J4PVGUT\n");
                
            }

    }

    public void sent(String message) {
        if(!(conteur==0)) {
	    	try {
	        	currentTime = LocalDateTime.now();
	            log.write(currentTime+" . "+monNxt+" . "+robot.getCurrentPos()+" . "+message.toCharArray()[2]+"\n");
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
        }
        conteur++;
    	if(connexion) {
            try {
                dos.writeBytes(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {System.out.println("plus connecte au nxt");}
    }    
    
    public void receive() {
    	if(connexion) {
    		try {
				//dis.readFully(buffer, 0, 4);
				dis.read(buffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    		System.out.println("message recu");
    	}
    	else {
    		System.out.println("vous etes deconnecte");
    	}
    }
    
    public void deconnexion() {
        try {
            log.write(String.valueOf(conteur));
            log.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (connexion) {
            try {
                dis.close();
                dos.close();
                conn.close();

            } catch (IOException ioe) {
                System.out.println("IOException closing connection:");
                System.out.println(ioe.getMessage());
            }
            connexion = false;
            System.out.println("D�connexion OK");
        } else {
            System.out.println("Vous n'�tes pas connect� au NXT");
        }
    }


}