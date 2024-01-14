package com.tankwar;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Player extends Tank {

    public Player() {

    }

    public Player(String color, int x, int y, GamePanel gamePanel) {
        super(color, x, y, 1, gamePanel);
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W:
                upMoving = true;
                break;
            case KeyEvent.VK_S:
                downMoving = true;
                break;
            case KeyEvent.VK_A:
                leftMoving = true;
                break;
            case KeyEvent.VK_D:
                rightMoving = true;
                break;
            case KeyEvent.VK_SPACE:
                attack();
                break;
            case KeyEvent.VK_ESCAPE:
                alive = false;
                break;
            case KeyEvent.VK_1:
                gamePanel.saveReplay("src/main/resources/replay/replay1");
                gamePanel.save("src\\main\\resources\\save\\save1");
                break;
            case KeyEvent.VK_2:
                gamePanel.saveReplay("src/main/resources/replay/replay2");
                gamePanel.save("src\\main\\resources\\save\\save2");
                break;
            case KeyEvent.VK_3:
                gamePanel.saveReplay("src/main/resources/replay/replay3");
                gamePanel.save("src\\main\\resources\\save\\save3");
                break;
            default:
                break;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_W:
                upMoving = false;
                break;
            case KeyEvent.VK_S:
                downMoving = false;
                break;
            case KeyEvent.VK_A:
                leftMoving = false;
                break;
            case KeyEvent.VK_D:
                rightMoving = false;
                break;
            default:
                break;
        }
    }

    public void move() {
        if (upMoving)
            upMove();
        else if (downMoving)
            downMove();
        else if (leftMoving)
            leftMove();
        else if (rightMoving)
            rightMove();
    }

    @Override
    public void paintSelf(Graphics g) {
        g.drawImage(img, x, y, null);
        move();
        attackCD();
    }
    // @Override
    // public Rectangle getRec() {
    // return new Rectangle(x, y, width, height);
    // }
}
