package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import code.Graphe;
import code.Main;

public class GrapheTestInit {
	Main m;
	Graphe testG ;
	@Before
    public void initialisation() {

		m = new Main();
		testG=m.inialisationGraphe();
	}
	
	@Test
    public final void testEstHopitauxFalse() {
        assertFalse(testG.estHopital(13));
    }

    @Test
    public final void testEstHopitauxTrue() {
        assertTrue(testG.estHopital(10));
    }

    @Test
    public final void testEstVictimeFalse() {
        assertFalse(testG.estVictime(5));
    }

    @Test
    public final void testEstVictimeTrue() {
        assertTrue(testG.estVictime(13));
    }
/*
    @Test
    public final void testDirectionDroit() {
        char curr = testG.direction(2, 3, 4);
        assertEquals('s', curr);
    }

    @Test
    public final void testToutDroit() {
        int curr = testG.toutDroit(2, 3);
        assertEquals(4, curr);
    }

    @Test
    public final void testToutDroitIntersec() {
        int curr = testG.toutDroit(7, 8);
        assertEquals(-1, curr);
    }

    @Test
    public final void testToutDroitLigne() {
        int curr = testG.toutDroit(6, 11);
        assertEquals(13, curr);
    }

    @Test
    public final void testTournerGaucheIntersecN() {
        int curr = testG.tournerGauche(10, 5);
        assertEquals(4, curr);
    }

    @Test
    public final void testTournerGaucheIntersecG() {
        int curr = testG.tournerGauche(4, 5);
        assertEquals(6, curr);
    }

    @Test
    public final void testTournerGaucheIntersecD() {
        int curr = testG.tournerGauche(6, 5);
        assertEquals(10, curr);
    }

    @Test
    public final void testTournerDroiteIntersecN() {
        int curr = testG.tournerDroite(1, 2);
        assertEquals(7, curr);
    }

    @Test
    public final void testTournerDroiteIntersecG() {
        int curr = testG.tournerDroite(7, 2);
        assertEquals(3, curr);
    }

    @Test
    public final void testTournerDroiteIntersecD() {
        int curr = testG.tournerDroite(3, 2);
        assertEquals(1, curr);
    }*/
}
