package it.polimi.ingsw.model;

import it.polimi.ingsw.model.gods.*;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class GodsTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getSimpleGodId() {
        assertEquals(0, Gods.APOLLO.getGodId());
        assertEquals(1, Gods.ARTEMIS.getGodId());
        assertEquals(2, Gods.ATHENA.getGodId());
        assertEquals(3, Gods.ATLAS.getGodId());
        assertEquals(4, Gods.DEMETER.getGodId());
        assertEquals(5, Gods.HEPHAESTUS.getGodId());
        assertEquals(6, Gods.MINOTAUR.getGodId());
        assertEquals(7, Gods.PAN.getGodId());
        assertEquals(8, Gods.PROMETHEUS.getGodId());
    }

    @Test
    public void getGod() {
        assertEquals(Gods.getGod(0), new Apollo());
        assertEquals(Gods.getGod(1), new Artemis());
        assertEquals(Gods.getGod(2), new Athena());
        assertEquals(Gods.getGod(3), new Atlas());
        assertEquals(Gods.getGod(4), new Demeter());
        assertEquals(Gods.getGod(5), new Hephaestus());
        assertEquals(Gods.getGod(6), new Minotaur());
        assertEquals(Gods.getGod(7), new Pan());
        assertEquals(Gods.getGod(8), new Prometheus());
    }

    @Test
    public void getSimpleGodException() {
        exception.expect(IllegalArgumentException.class);
        Gods.getGod(15);
    }
}