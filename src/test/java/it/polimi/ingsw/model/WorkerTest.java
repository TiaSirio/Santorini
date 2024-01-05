package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class WorkerTest {

    @Test
    public void getCell() {
        Cell cell = new Cell(1,1);
        Worker worker = new Worker(cell);
        assertEquals(cell, worker.getCell());
    }

    @Test
    public void setCell() {
        Cell cell = new Cell(1,1);
        Worker worker = new Worker(cell);
        Cell cell2 = new Cell(2,2);
        worker.setCell(cell2);
        assertEquals(cell2,worker.getCell());
    }

    @Test
    public void getStatus() {
        Cell cell = new Cell(1,1);
        Worker worker = new Worker(cell);
        assertTrue(worker.getStatus());
    }

    @Test
    public void setStatus() {
        Cell cell = new Cell(1,1);
        Worker worker = new Worker(cell);
        worker.setStatus(false);
        assertFalse(worker.getStatus());
    }


}