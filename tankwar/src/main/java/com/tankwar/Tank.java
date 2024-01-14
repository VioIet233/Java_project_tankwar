package com.tankwar;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class Tank extends Object {
    private String upImage;
    private String downImage;
    private String rightImage;
    private String leftImage;

    String direction = "UP";

    boolean upMoving = false;
    boolean downMoving = false;
    boolean leftMoving = false;
    boolean rightMoving = false;
    String color;

    boolean coolDown = true;
    int coolDownTime = 30;

    int timer = 0;

    boolean alive = true;
    int friendly = 0;

    public Tank() {

    }

    public Tank(String color, int x, int y, int friendly, GamePanel gamePanel) {
        super("src/main/resources/images/tank/" + color + "Up.png", x, y, gamePanel);
        this.upImage = "src/main/resources/images/tank/" + color + "Up.png";
        this.downImage = "src/main/resources/images/tank/" + color + "Down.png";
        this.leftImage = "src/main/resources/images/tank/" + color + "Left.png";
        this.rightImage = "src/main/resources/images/tank/" + color + "Right.png";
        this.color = color;
        this.friendly = friendly;
        width = 40;
        height = 40;
        speed = 3;

    }

    public void upMove() {
        direction = "UP";
        setImg(upImage);
        if (canMove(x, y - speed))
            this.y -= speed;

    }

    public void downMove() {
        direction = "DOWN";
        setImg(downImage);
        if (canMove(x, y + speed))
            this.y += speed;
    }

    public void leftMove() {
        direction = "LEFT";
        setImg(leftImage);
        if (canMove(x - speed, y))
            this.x -= speed;

    }

    public void rightMove() {
        direction = "RIGHT";
        setImg(rightImage);
        if (canMove(x + speed, y))
            this.x += speed;
    }

    public Point getHeadPoint() {
        switch (direction) {
            case "UP":
                return new Point(x + width / 2, y);
            case "LEFT":
                return new Point(x, y + height / 2);
            case "DOWN":
                return new Point(x + width / 2, y + height);
            case "RIGHT":
                return new Point(x + width, y + height / 2);
            default:
                return null;
        }
    }

    public void attack() {
        Point p = getHeadPoint();
        switch (direction) {
            case "UP":
                p.y -= 10;
                break;
            case "DOWN":
                p.y += 10;
                break;
            case "LEFT":
                p.x -= 10;
                break;
            case "RIGHT":
                p.x += 10;
                break;
        }
        if (coolDown && alive) {
            Bullet bullet = new Bullet("src/main/resources/images/other/" + color + "Bullet.png", p.x - 5, p.y - 5, direction,
                    this.friendly, color, this.gamePanel);
            this.gamePanel.bulletList.add(bullet);
            coolDown = false;
        }
    }

    public void attackCD() {
        if (coolDown == false)
            timer++;
        if (timer > coolDownTime) {
            coolDown = true;
            timer = 0;
        }
    }

    public boolean hitBarrier(int x, int y) {
        Rectangle next = new Rectangle(x, y, width, height);

        List<Barrier> barriers = this.gamePanel.barrierList;
        List<Tank> tanks = this.gamePanel.tankList;
        for (Barrier b : barriers) {
            if (b.getRec().intersects(next)) {
                return true;
            }
        }
        for (Tank t : tanks) {
            if (t.x != x && t.y != y && t.getRec().intersects(next)) {
                return true;
            }
        }

        return false;
    }

    public boolean moveBorder(int x, int y) {
        if (x < 10 || x > this.gamePanel.getWidth() - width - 10) {
            return true;
        }
        if (y < 10 || y > this.gamePanel.getHeight() - height - 10) {
            return true;
        }
        return false;
    }

    public boolean canMove(int x, int y) {
        return !hitBarrier(x, y) && !moveBorder(x, y) && alive;
    }

    @Override
    public void paintSelf(Graphics g) {
    }

    @Override
    public Rectangle getRec() {
        return new Rectangle(x, y, width, height);
    }
}
