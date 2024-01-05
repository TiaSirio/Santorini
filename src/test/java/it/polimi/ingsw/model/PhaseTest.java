package it.polimi.ingsw.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;

public class PhaseTest {

        @Rule
        public final ExpectedException exception = ExpectedException.none();

        @Test
        public void getPhaseId() {
            assertEquals(-5, Phase.WAIT_PLAYERS.getPhaseId());
            assertEquals(-4, Phase.DRAWCARD.getPhaseId());
            assertEquals(-3, Phase.PICK_CARD.getPhaseId());
            assertEquals(-2, Phase.SETWORKER1.getPhaseId());
            assertEquals(-1, Phase.SETWORKER2.getPhaseId());
            assertEquals(0, Phase.BEGINNING.getPhaseId());
            assertEquals(1, Phase.MOVE.getPhaseId());
            assertEquals(2, Phase.BUILD.getPhaseId());
            assertEquals(5, Phase.WAIT_GOD_ANSWER.getPhaseId());
            assertEquals(6, Phase.PROMETHEUS_WORKER.getPhaseId());
            assertEquals(10, Phase.END_GAME.getPhaseId());
        }

        @Test
        public void getPhase() {
            assertEquals(Phase.getPhase(-5), Phase.WAIT_PLAYERS);
            assertEquals(Phase.getPhase(-4), Phase.DRAWCARD);
            assertEquals(Phase.getPhase(-3), Phase.PICK_CARD);
            assertEquals(Phase.getPhase(-2), Phase.SETWORKER1);
            assertEquals(Phase.getPhase(-1), Phase.SETWORKER2);
            assertEquals(Phase.getPhase(0), Phase.BEGINNING);
            assertEquals(Phase.getPhase(1), Phase.MOVE);
            assertEquals(Phase.getPhase(2), Phase.BUILD);
            assertEquals(Phase.getPhase(5), Phase.WAIT_GOD_ANSWER);
            assertEquals(Phase.getPhase(6), Phase.PROMETHEUS_WORKER);
            assertEquals(Phase.getPhase(10), Phase.END_GAME);
        }

        @Test
        public void getPhaseException(){
            exception.expect(IllegalArgumentException.class);
            Phase phase = Phase.getPhase(11);
        }

    @Test
    public void getNextPhaseException(){
        exception.expect(IllegalArgumentException.class);
        Phase phase = Phase.END_GAME;
        phase = Phase.next(Phase.getPhase(5));
    }

        @Test
        public void testNext() {
            assertEquals(Phase.DRAWCARD, Phase.next(Phase.getPhase(-5)));
            assertEquals(Phase.PICK_CARD, Phase.next(Phase.getPhase(-4)));
            assertEquals(Phase.SETWORKER1, Phase.next(Phase.getPhase(-3)));
            assertEquals(Phase.SETWORKER2, Phase.next(Phase.getPhase(-2)));
            assertEquals(Phase.MOVE, Phase.next(Phase.getPhase(-1)));
            assertEquals(Phase.MOVE, Phase.next(Phase.getPhase(0)));
            assertEquals(Phase.BUILD, Phase.next(Phase.getPhase(1)));
            assertEquals(Phase.MOVE, Phase.next(Phase.getPhase(2)));
            assertEquals(Phase.BUILD, Phase.next(Phase.getPhase(1)));
            assertEquals(Phase.BUILD, Phase.next(Phase.getPhase(6)));
            assertEquals(Phase.END_GAME, Phase.next(Phase.getPhase(10)));

        }
}