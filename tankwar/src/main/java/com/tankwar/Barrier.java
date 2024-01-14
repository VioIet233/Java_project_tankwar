package com.tankwar;

import java.awt.*;

public class Barrier extends Object {

    String type;
    boolean destructible;
    boolean traversable;

    Barrier(String img, int x, int y, String type, GamePanel gamePanel) {
        super(img, x, y, gamePanel);
        this.type = type;
        width = 36;
        height = 36;
        switch (type) {
            case "wall":
                destructible = true;
                traversable = false;
                break;
            case "iron":
                destructible = false;
                traversable = false;
                break;
            case "riverLR":
                destructible = false;
                traversable = true;
                break;
            case "riverUD":
                destructible = false;
                traversable = true;
                break;
            default:
                break;
        }
    }

    @Override
    public void paintSelf(Graphics g) {
        g.drawImage(img, x, y, null);
    }

    @Override
    public Rectangle getRec() {
        return new Rectangle(x, y, width, height);
    }
}
