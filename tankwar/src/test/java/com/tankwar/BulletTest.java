package com.tankwar;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BulletTest {
    GamePanel g;

    @Test
    public void test() {

    }

    @Test
    public void testHit() {
        g = new GamePanel();
        Bullet b = new Bullet(null, 50, 50, "UP", 0, "yellow", g);

        g.clearAll();
        g.bulletList.add(b);
        g.tankList.add(new Tank("yellow", 50, 50, 1, g));
        b.hitTank();
        assertTrue(g.removeBulletList.contains(b));

        g.clearAll();
        g.bulletList.add(b);
        g.barrierList.add(new Barrier(null, 50, 50, "wall", g));
        b.hitBarrier();
        assertTrue(g.removeBulletList.contains(b));
    }

    @Test
    public void testMove() {
        g = new GamePanel();
        Bullet b = new Bullet(null, 50, 50, "UP", 0, "yellow", g);

        b.direction = "UP";
        b.move();
        assertEquals(b.y, 43);

        b.direction = "DOWN";
        b.move();
        assertEquals(b.y, 50);

        b.direction = "LEFT";
        b.move();
        assertEquals(b.x, 43);

        b.direction = "RIGHT";
        b.move();
        assertEquals(b.x, 50);

    }
}
