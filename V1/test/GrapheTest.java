package test;

import code.Graphe;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.List;

import static org.junit.Assert.*;

public class GrapheTest {
    private static Graphe testG;

    @Before
    public void initialisation() {

        Map<Integer, List<Integer>> test = new LinkedHashMap<>();
        List<Integer> victime = new LinkedList<>();
        List<Integer> hopitaux = new LinkedList<>();

        victime.add(3);
        victime.add(13);

        hopitaux.add(10);

        //génération map
        LinkedList<Integer> utils = new LinkedList<>();
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

        testG = new Graphe(test, victime, hopitaux);
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
    }
}
