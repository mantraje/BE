package code;



import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTConnector;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException; 

public class Bluetooth {
	private NXTConnector conn;
    private DataOutputStream dos;
    private DataInputStream dis;
    private boolean connexion = false;
    private String monNxt;

    public Bluetooth(String monNxt) {
        this.monNxt = monNxt;
    }
		
    public void connexion() {

        conn = new NXTConnector();

            // Adresse de Thorn : 001653161388
            System.out.println("Tentative de connexion � " + monNxt);
            connexion = conn.connectTo(monNxt,"001653161388", NXTCommFactory.BLUETOOTH);
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
    
    public void receive() throws IOException, InterruptedException {
    	if(connexion) {
    		System.out.println(dis.available());
    		dis.readAllBytes();
    		System.out.println("message recu");
    	}
    	else {
    		System.out.println("vous etes deconnecte");
    	}
    }
    
    public void deconnexion() {
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