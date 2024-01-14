package com.tankwar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BarrierTest {
    GamePanel g;

    @Test
    public void test() {

    }

    @Test
    public void testSpawn() {
        g = new GamePanel();
        g.barrierList.add(new Barrier(null, 0, 0, "wall", g));
        g.barrierList.add(new Barrier(null, 0, 0, "riverUD", g));
        g.barrierList.add(new Barrier(null, 0, 0, "riverLR", g));
        g.barrierList.add(new Barrier(null, 0, 0, "iron", g));
        assertEquals(4, g.barrierList.size());
    }
}
