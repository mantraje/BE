package test;

import code.Graphe;
import code.Main;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class GrapheTestInitConfig1 {

    Main m;
    Graphe testG ;
    @Before
    public void initialisation() {

        m = new Main();
        testG=m.inialisationGraphe();
    }

    @Test
    public final void testEstHopitauxFalse() {
        assertFalse(testG.estHopital(12));
    }

    @Test
    public final void testEstHopitauxTrue() {
        assertTrue(testG.estHopital(5));
    }

    @Test
    public final void testEstVictimeFalse() {
        assertFalse(testG.estVictime(5));
    }

    @Test
    public final void testEstVictimeTrue() {
        assertTrue(testG.estVictime(4));
    }
/*
    @Test
    public final void testDirectionDroit() {
        char curr = testG.direction(1, 2, 4);
        assertEquals('s', curr);
    }

    @Test
    public final void testToutDroit() {
        int curr = testG.toutDroit(1, 2);
        assertEquals(4, curr);
    }

    @Test
    public final void testToutDroitIntersec() {
        int curr = testG.toutDroit(2, 3);
        assertEquals(-1, curr);
    }

    @Test
    public final void testToutDroitLigne() {
        int curr = testG.toutDroit(4, 2);
        assertEquals(1, curr);
    }

    @Test
    public final void testTournerGaucheIntersecD() {
        int curr = testG.tournerGauche(6, 4);
        assertEquals(3, curr);
    }

    @Test
    public final void testTournerGaucheIntersecG() {
        int curr = testG.tournerGauche(5, 3);
        assertEquals(1, curr);
    }

    @Test
    public final void testTournerGaucheIntersecN() {
        int curr = testG.tournerGauche(3, 4);
        assertEquals(2, curr);
    }

    @Test
    public final void testTournerDroiteIntersecN() {
        int curr = testG.tournerDroite(3, 4);
        assertEquals(6, curr);
    }

    @Test
    public final void testTournerDroiteIntersecG() {
        int curr = testG.tournerDroite(2, 4);
        assertEquals(3, curr);
    }

    @Test
    public final void testTournerDroiteIntersecD() {
        int curr = testG.tournerDroite(6, 4);
        assertEquals(2, curr);
    }*/
}
