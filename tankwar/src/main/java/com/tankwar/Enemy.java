package com.tankwar;

import java.awt.*;
import java.util.Random;

public class Enemy extends Tank implements Runnable {

    public Enemy() {

    }

    public Enemy(String color, int x, int y, GamePanel gamePanel) {
        super(color, x, y, 0, gamePanel);
        direction = randomDirection();
        coolDownTime = 50;
    }

    @Override
    public void run() {
        while (alive) {
            move();
            attack();
            attackCD();
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void move() {
        Random r = new Random();
        int rnum = r.nextInt(1000);
        if (rnum % 100 == 0)
            direction = randomDirection();

        switch (direction) {
            case "UP":
                upMove();
                break;
            case "DOWN":
                downMove();
                break;
            case "LEFT":
                leftMove();
                break;
            case "RIGHT":
                rightMove();
                break;

        }
    }

    public String randomDirection() {
        Random r = new Random();
        int rnum = r.nextInt(4);
        switch (rnum) {
            case 0:
                return "UP";
            case 1:
                return "DOWN";
            case 2:
                return "LEFT";
            default:
                return "RIGHT";
        }
    }

    @Override
    public void paintSelf(Graphics g) {
        g.drawImage(img, x, y, null);

    }

    // @Override
    // public Rectangle getRec() {
    // return new Rectangle(x, y, width, height);
    // }
}
