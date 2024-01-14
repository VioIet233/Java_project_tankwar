package com.tankwar;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.event.KeyEvent;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.tankwar.GamePanel.KeyMonitor;

public class GamePanelTest {
    GamePanel g;

    @Test
    public void test() {

    }

    @Test
    public void testKey1() {
        g = new GamePanel();
        g.setKeyMonitor();
        KeyMonitor k = g.monitor;
        g.init();
        g.state = 2;
        k.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_ENTER, 's'));
        assertEquals(g.state, 2);

        g.state = 3;
        k.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_ENTER, 's'));
        assertEquals(g.state, 0);

        k.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_DOWN, 's'));
        assertEquals(g.selectY, 320);

        k.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_DOWN, 's'));
        assertEquals(g.selectY, 420);

    }

    @Test
    public void testKey2() {
        g = new GamePanel();
        g.setKeyMonitor();
        KeyMonitor k = g.monitor;
        g.init();
        k.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_DOWN, 's'));
        k.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_DOWN, 's'));
        k.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_DOWN, 's'));
        k.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_UP, 's'));
        k.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_UP, 's'));
        k.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_UP, 's'));
        k.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_UP, 's'));
        assertEquals(g.selectY, 220);

        k.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_ENTER, 's'));
        assertEquals(g.state, 2);

        g.init();
        g.state = 0;
        g.selectY = 320;

        k.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_ENTER, 's'));
        assertEquals(g.state, 1);

        g.init();
        g.state = 0;
        g.selectY = 420;

        k.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_ENTER, 's'));
        assertEquals(g.shutDown, true);

        k.keyPressed(new KeyEvent(g, 0, 0, 0, KeyEvent.VK_ESCAPE, 's'));
        assertEquals(g.state, 0);

    }

    @Test
    public void testReply() {
        g = new GamePanel();
        g.shutDown = true;
        g.launch();
        g.saveReplay("src/test/resources/replay/replay1");
        assertEquals(0, g.state);

        g.replay("src/test/resources/replay/replay1");
        assertEquals(0, g.state);
    }

    @Test
    public void testLanuch0() {
        g = new GamePanel();
        g.shutDown = true;
        g.launch();
        assertEquals(0, g.state);
    }

    @Test
    public void testLanuch1() {
        g = new GamePanel();
        g.shutDown = true;
        g.state = 1;
        g.launch();
        assertEquals(1, g.state);
    }

    @Test
    public void testLanuch2() {
        g = new GamePanel();
        g.shutDown = true;
        g.state = 2;
        g.launch();
        assertEquals(2, g.state);
    }

    @Test
    public void testLanuch3() {
        g = new GamePanel();
        g.shutDown = true;
        g.state = 3;
        g.launch();
        assertEquals(3, g.state);
    }

    @Test
    public void testLoadSave() {
        g = new GamePanel();
        g.tankList.add(new Tank("yellow", 0, 0, 1, g));
        g.barrierList.add(new Barrier(null, 0, 0, "wall", g));
        g.blastList.add(new Blast());
        g.bulletList.add(new Bullet(null, 0, 0, "UP", 0, "yellow", g));
        g.save("src\\test\\resources\\save\\save1");
        g.clearAll();
        try {
            g.load("src\\test\\resources\\save\\save1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertEquals(1, g.tankList.size());
        assertEquals(1, g.barrierList.size());
        assertEquals(1, g.bulletList.size());
        assertEquals(1, g.blastList.size());
    }

    @Test
    public void testClear() {
        g = new GamePanel();
        g.tankList.add(new Tank());
        g.init();

        assertEquals(1, g.tankList.size());
    }

    @Test
    public void testEnemySpawn() {
        g = new GamePanel();
        g.enemySpawn.start();
        g.state = 2;
        assertEquals(0, g.tankList.size());

        g.enemySpawn.working = true;
        try {
            Thread.sleep(1700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(1, g.tankList.size());
    }

}
