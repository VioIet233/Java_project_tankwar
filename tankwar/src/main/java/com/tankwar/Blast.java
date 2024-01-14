package com.tankwar;

import java.awt.*;

public class Blast extends Object {

    static Image[] imgs = new Image[8];

    int explodeCount = 0;

    static {
        for (int i = 0; i < 8; i++) {
            imgs[i] = Toolkit.getDefaultToolkit().getImage("src/main/resources/images/blast/blast" + i + ".png");
        }
    }

    public Blast() {
        super();
    }

    public Blast(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Blast(int x, int y, int explodeCount) {
        this.x = x;
        this.y = y;
        this.explodeCount = explodeCount;
    }

    @Override
    public void paintSelf(Graphics g) {
        if (explodeCount < 8) {
            g.drawImage(imgs[explodeCount], x, y, null);
            explodeCount++;
        }

    }

    @Override
    public Rectangle getRec() {
        return null;
    }
}
