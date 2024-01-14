package com.tankwar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TankTest {

    Tank t;
    GamePanel g;

    @Test
    public void testTank() {
        g = new GamePanel();
        t = new Tank("yellow", 50, 50, 1, g);
        Tank t1 = new Tank("yellow", 50, 50, 1, null);
        Tank t2 = new Tank();
        assertEquals("yellow", t1.color);
        assertEquals(30, t2.coolDownTime);
    }

    @Test
    public void testMany() {
        g = new GamePanel();
        t = new Tank("yellow", 50, 50, 1, g);

        t.attack();
        assertEquals(false, t.coolDown);

        t.attackCD();
        assertEquals(1, t.timer);

        t.timer += 30;
        t.attackCD();
        assertEquals(true, t.coolDown);

        t.direction = "DOWN";
        t.attack();
        assertEquals(false, t.coolDown);

        t.direction = "RIGHT";
        t.attack();
        assertEquals(false, t.coolDown);

        t.direction = "LEFT";
        t.attack();
        assertEquals(false, t.coolDown);

        boolean x = t.hitBarrier(t.x, t.y);
        assertEquals(false, x);

        g.tankList.add(t);
        boolean y = t.hitBarrier(t.x, t.y);
        assertEquals(false, y);

        g.barrierList.add(new Barrier(null, 50, 50, "wall", g));
        boolean z = t.hitBarrier(t.x, t.y);
        assertEquals(true, z);

        t.upMove();
        assertEquals(50, t.y);

        t.downMove();
        assertEquals(50, t.y);
    }

}
