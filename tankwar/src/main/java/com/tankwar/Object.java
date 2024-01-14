package com.tankwar;

import java.awt.*;

public abstract class Object {

    Image img;
    int width;
    int height;
    int x;
    int y;

    int speed;
    String direction;
    GamePanel gamePanel;

    public Object() {
    }

    public Object(String img, int x, int y, GamePanel gamePanel) {
        this.img = Toolkit.getDefaultToolkit().getImage(img);
        this.x = x;
        this.y = y;
        this.gamePanel = gamePanel;
    }

    public void setImg(String img) {
        this.img = Toolkit.getDefaultToolkit().getImage(img);
    }

    public abstract void paintSelf(Graphics g);

    public abstract Rectangle getRec();
}
