package test;


import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import code.*;


public class RobotTest {
	Graphe maquette;
	Pilote humain;
	Bluetooth blue;

  @Before
  public void beforeTest() {
		Map<Integer, List<Integer>> test = new LinkedHashMap<>();
		List<Integer> utils = new LinkedList<>();
		List<Integer> victime = new LinkedList<>();
		List<Integer> hopitaux = new LinkedList<>();

		victime.add(3);
		victime.add(13);

		hopitaux.add(10);

		// g�n�ration map
		utils.add(-1);
		utils.add(2);
		test.put(1, utils);

		utils = new LinkedList<>();
		utils.add(1);
		utils.add(7);
		utils.add(3);
		test.put(2, utils);

		utils = new LinkedList<>();
		utils.add(2);
		utils.add(4);
		test.put(3, utils);

		utils = new LinkedList<>();
		utils.add(3);
		utils.add(5);
		test.put(4, utils);

		utils = new LinkedList<>();
		utils.add(10);
		utils.add(6);
		utils.add(4);
		test.put(5, utils);

		utils = new LinkedList<>();
		utils.add(5);
		utils.add(11);
		test.put(6, utils);

		utils = new LinkedList<>();
		utils.add(2);
		utils.add(8);
		test.put(7, utils);

		utils = new LinkedList<>();
		utils.add(9);
		utils.add(7);
		utils.add(12);
		test.put(8, utils);

		utils = new LinkedList<>();
		utils.add(8);
		utils.add(10);
		test.put(9, utils);

		utils = new LinkedList<>();
		utils.add(9);
		utils.add(5);
		test.put(10, utils);

		utils = null;
		utils = new LinkedList<>();
		utils.add(6);
		utils.add(13);
		test.put(11, utils);

		utils = new LinkedList<>();
		utils.add(8);
		utils.add(13);
		test.put(12, utils);

		utils = new LinkedList<>();
		utils.add(11);
		utils.add(12);
		test.put(13, utils);
		blue = new Bluetooth("");
		maquette = new Graphe(test, victime, hopitaux);
	/*	humain = new Pilote(1, maquette, blue);
  }
  
	@Test
	public void testToutdroit() {
		assertEquals("20s\n", humain.toutDroit(false, false));
	}

	@Test
	public void testTournerGauche() {
		humain.toutDroit(false, false);
		assertEquals("30lt\n", humain.tournerGauche(true, false));
	}

	@Test
	public void testTournerDroite() {
		humain.toutDroit(false, false);
		assertEquals("20r\n", humain.tournerDroite(false, false));
	}

	 @Test
	 public void testDemiTour () {
		 humain.toutDroit(false, false);
		 assertEquals("20u\n",humain.demiTour(false, false));
	 }
	 
	 @Test
	 public void testMAJpositionETgraphe() {
		 humain.toutDroit(false, false);
		 humain.tournerDroite(false, false);
		 humain.toutDroit(false, false);
		 humain.tournerDroite(false, false);
		 humain.toutDroit(true, false);
		 List<Integer> test=new LinkedList<>();
		 test.add(3);
		 assertEquals(13,humain.getCurrentPos());
		 assertEquals(12,humain.getPreviousPos());
		 assertEquals(test,maquette.getVictimes());
	 }*/
  }
}
