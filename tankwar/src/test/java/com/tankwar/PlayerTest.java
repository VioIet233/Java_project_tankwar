package com.tankwar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class PlayerTest {
    GamePanel g;

    @Test
    public void test() {
        g = new GamePanel();
        g.init();
        g.player = new Player();
        assertEquals(g.player.alive, true);
    }

    @Test
    public void testKey() {
        g = new GamePanel();
        g.init();
        g.player.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_ESCAPE, 's'));
        assertEquals(g.player.alive, false);

        g.player.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_W, 's'));
        assertEquals(g.player.upMoving, true);
        g.player.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_S, 's'));
        assertEquals(g.player.downMoving, true);
        g.player.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_A, 's'));
        assertEquals(g.player.leftMoving, true);
        g.player.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_D, 's'));
        assertEquals(g.player.rightMoving, true);

        g.player.keyReleased(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_W, 's'));
        assertEquals(g.player.upMoving, false);
        g.player.keyReleased(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_S, 's'));
        assertEquals(g.player.downMoving, false);
        g.player.keyReleased(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_A, 's'));
        assertEquals(g.player.leftMoving, false);
        g.player.keyReleased(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_D, 's'));
        assertEquals(g.player.rightMoving, false);

    }

}
