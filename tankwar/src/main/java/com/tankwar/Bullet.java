package com.tankwar;

import java.awt.*;
import java.util.List;

public class Bullet extends Object {
    private int width = 5;
    private int height = 5;
    private int speed = 7;
    String direction;
    int friendly = 0;
    String color;

    public Bullet(String img, int x, int y, String direction, int friendly, String color, GamePanel gamePanel) {
        super(img, x, y, gamePanel);
        this.direction = direction;
        this.friendly = friendly;
        this.color = color;
    }

    public void move() {
        switch (direction) {
            case "UP":
                y -= speed;
                moveBorder();
                break;
            case "DOWN":
                y += speed;
                moveBorder();
                break;
            case "LEFT":
                x -= speed;
                moveBorder();
                break;

            case "RIGHT":
                x += speed;
                moveBorder();
                break;
        }
    }

    public void hitTank() {
        Rectangle next = this.getRec();
        List<Tank> tanks = this.gamePanel.tankList;
        for (Tank tank : tanks) {
            if (tank.getRec().intersects(next)) {
                if (this.friendly != tank.friendly) {
                    this.gamePanel.tankList.remove(tank);
                    this.gamePanel.blastList.add(new Blast(tank.x, tank.y));
                    tank.alive = false;
                    if (tank.friendly == 0)
                        this.gamePanel.score++;
                }
                this.gamePanel.removeBulletList.add(this);
                break;
            }
        }
    }

    public void hitBarrier() {
        Rectangle next = this.getRec();
        List<Barrier> barriers = this.gamePanel.barrierList;
        for (Barrier b : barriers) {
            if (b.getRec().intersects(next)) {
                if (b.destructible == true)
                    this.gamePanel.barrierList.remove(b);
                if (b.traversable == false)
                    this.gamePanel.removeBulletList.add(this);
                break;
            }
        }
    }

    public void moveBorder() {
        if (x < 0 || x > this.gamePanel.getWidth()) {
            this.gamePanel.removeBulletList.add(this);
        }
        if (y < 0 || y > this.gamePanel.getHeight()) {
            this.gamePanel.removeBulletList.add(this);
        }
    }

    @Override
    public void paintSelf(Graphics g) {
        g.drawImage(img, x, y, null);
        move();

        hitTank();
        hitBarrier();
        moveBorder();
    }

    @Override
    public Rectangle getRec() {
        return new Rectangle(x, y, width, height);
    }
}
