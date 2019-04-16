package test;

import java.io.IOException;

import code.*;

public class BluetoothTest {
	public static void main(String args[]) throws IOException, InterruptedException {
		Bluetooth test = new Bluetooth("Thorn");
		test.connexion();
		test.sent("20r\n");
		test.receive();
		test.sent("20l\n");
	}
}
