package it.polimi.ingsw.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

public class BlocksTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getBlockId() {
        assertEquals(0, Blocks.EMPTY.getBlockId());
        assertEquals(1, Blocks.LEVEL1.getBlockId());
        assertEquals(2, Blocks.LEVEL2.getBlockId());
        assertEquals(3, Blocks.LEVEL3.getBlockId());
        assertEquals(4, Blocks.DOME.getBlockId());
    }

    @Test
    public void getBlock() {
        assertEquals(Blocks.getBlock(0), Blocks.EMPTY);
        assertEquals(Blocks.getBlock(1), Blocks.LEVEL1);
        assertEquals(Blocks.getBlock(2), Blocks.LEVEL2);
        assertEquals(Blocks.getBlock(3), Blocks.LEVEL3);
        assertEquals(Blocks.getBlock(4), Blocks.DOME);
    }

    @Test
    public void getBlockException(){
        exception.expect(IllegalArgumentException.class);
        Blocks blocks = Blocks.getBlock(10);
    }
}