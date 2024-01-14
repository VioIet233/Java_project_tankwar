package com.tankwar;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;

public class GamePanel extends JFrame {
    int width = 1000;
    int height = 720;
    private Image select = Toolkit.getDefaultToolkit().getImage("src/main/resources/images/other/star.png");
    int selectY = 220;
    int state = 0;
    // 0 主菜单 1点击继续游戏选择存档位置 2选完存档位置后进入游戏 3游戏结束

    Image offScreenImage = null;

    public List<byte[]> bytelList = new ArrayList<>();
    public List<Bullet> bulletList = new ArrayList<>();
    public List<Tank> tankList = new ArrayList<>();
    public List<Barrier> barrierList = new ArrayList<>();
    public List<Bullet> removeBulletList = new ArrayList<>();
    public List<Blast> blastList = new ArrayList<>();
    int timer = 0;
    int score = 0;
    boolean lose = true;
    boolean shutDown = false;

    KeyMonitor monitor = new KeyMonitor();
    Player player;
    Player playerA;
    Player playerB;
    Player playerC;
    EnemySpawn enemySpawn = new EnemySpawn();
    ExecutorService executorService = Executors.newFixedThreadPool(3);

    public void clearAll() {
        for (Tank tank : tankList)
            tank.alive = false;
        tankList.clear();
        bytelList.clear();
        bulletList.clear();
        barrierList.clear();
        removeBulletList.clear();
        blastList.clear();
    }

    public void init() {
        clearAll();
        player = new Player("yellow", 500, 660, this);
        tankList.add(player);
    }

    public class EnemySpawn extends Thread {
        boolean working = false;

        @Override
        public void run() {
            while (true) {

                while (!working) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if (state == 2 && tankList.size() <= 8) {
                    Random random = new Random();
                    int rnum = random.nextInt(800) + 100;
                    boolean canSpawn = true;
                    for (Tank tank : tankList) {
                        if (tank.getRec().intersects(new Rectangle(rnum, 60, 40, 40)))
                            canSpawn = false;
                    }
                    if (canSpawn) {
                        Enemy enemy = new Enemy("white", rnum, 60, GamePanel.this);
                        tankList.add(enemy);

                        Thread enemyThread = new Thread(enemy);
                        enemyThread.start();
                    }
                }
            }
        }
    }

    public void setKeyMonitor() {
        this.addKeyListener(monitor);
    }

    public void launch() {

        setTitle("简易坦克大战");
        setSize(width, height);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(3);
        setResizable(false);

        setVisible(true);
        setKeyMonitor();

        enemySpawn.start();
        init();
        while (true) {
            if (state == 2)
                enemySpawn.working = true;
            else
                enemySpawn.working = false;
            if (player.alive == false)
                state = 3;
            repaint();
            try {
                Thread.sleep(25);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (shutDown) {
                this.dispose();
                return;
            }
        }
    }

    private class imageWriter implements Runnable {
        private Image image;

        public imageWriter(Image image) {
            this.image = image;
        }

        @Override
        public void run() {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write((BufferedImage) image, "png", baos);
                byte[] by = baos.toByteArray();
                bytelList.add(by);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = this.createImage(width, height);
        }
        Graphics gImage = offScreenImage.getGraphics();

        gImage.setColor(Color.black);
        gImage.fillRect(0, 0, width, height);
        if (state == 0) {
            gImage.setFont(new Font("宋体", Font.BOLD, 80));

            gImage.setColor(Color.white);
            gImage.drawString("坦克大战", 340, 150);

            gImage.setFont(new Font("宋体", Font.BOLD, 40));
            gImage.drawString("开始游戏", 420, 250);
            gImage.drawString("继续游戏", 420, 350);
            gImage.drawString("退出游戏", 420, 450);

            gImage.drawImage(select, 380, selectY, null);
            gImage.drawImage(select, 585, selectY, null);
        } else if (state == 1) {
            gImage.setFont(new Font("宋体", Font.BOLD, 80));

            gImage.setColor(Color.white);
            gImage.drawString("坦克大战", 340, 150);

            gImage.setFont(new Font("宋体", Font.BOLD, 40));

            gImage.drawString("存档 一 ", 420, 250);
            gImage.drawString("存档 二 ", 420, 350);
            gImage.drawString("存档 三 ", 420, 450);

            gImage.drawImage(select, 380, selectY, null);
            gImage.drawImage(select, 585, selectY, null);
        } else if (state == 2) {
            for (Tank tank : tankList) {
                tank.paintSelf(gImage);
            }
            for (Barrier barrier : barrierList) {
                barrier.paintSelf(gImage);
            }
            for (Bullet bullet : bulletList) {
                bullet.paintSelf(gImage);
            }
            for (Blast blast : blastList) {
                blast.paintSelf(gImage);
            }
            bulletList.removeAll(removeBulletList);
            gImage.setColor(Color.white);
            gImage.setFont(new Font("宋体", Font.BOLD, 20));
            gImage.drawString("按下数字键1、2、3进行存档", 10, 710);
        } else if (state == 3) {
            gImage.setFont(new Font("宋体", Font.BOLD, 80));

            gImage.setColor(Color.white);
            gImage.drawString("坦克大战", 340, 150);

            gImage.setFont(new Font("宋体", Font.BOLD, 40));
            gImage.drawString("游戏结束", 420, 250);
            gImage.drawString("得分:" + score, 420, 350);
            gImage.setFont(new Font("宋体", Font.BOLD, 20));
            gImage.drawString("按下enter键返回主菜单", 10, 710);
        }

        timer += 1;
        g.drawImage(offScreenImage, 0, 0, null);

        executorService.submit(new imageWriter(offScreenImage));

    }

    public void save(String path) {
        FileWriter writer;
        try {
            writer = new FileWriter(path, false);
            writer.write("");
            writer.flush();

            writer = new FileWriter(path, true);
            String s;
            for (Bullet bullet : bulletList) {
                s = "bullet," + bullet.x + "," + bullet.y + "," + bullet.direction + "," + bullet.friendly + ","
                        + bullet.color + "\n";
                writer.write(s);
            }

            for (Tank tank : tankList) {
                s = "tank," + tank.x + "," + tank.y + "," + tank.color + "," + tank.friendly + "\n";
                writer.write(s);
            }

            for (Barrier barrier : barrierList) {
                s = barrier.type + "," + barrier.x + "," + barrier.y + "\n";
                writer.write(s);
            }

            for (Blast blast : blastList) {
                s = "blast," + blast.x + "," + blast.y + "," + blast.explodeCount + "\n";
                writer.write(s);
            }
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void load(String path) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path));
        for (String s : lines) {
            String[] splitS = s.split(",");
            int sx = Integer.parseInt(splitS[1]);
            int sy = Integer.parseInt(splitS[2]);
            if (splitS.length == 3) {
                barrierList
                        .add(new Barrier("src/main/resources/images/barrier/" + splitS[0] + ".png", sx, sy,
                                splitS[0], this));
            } else if (splitS.length == 4) {
                blastList.add(new Blast(sx, sy, Integer.parseInt(splitS[3])));
            } else if (splitS.length == 5) {
                int sfriendly = Integer.parseInt(splitS[4]);
                if (sfriendly == 1) {
                    player = new Player(splitS[3], sx, sy, GamePanel.this);
                    tankList.add(player);
                } else {
                    Enemy enemy = new Enemy("white", sx, sy, GamePanel.this);
                    tankList.add(enemy);
                    Thread enemyThread = new Thread(enemy);
                    enemyThread.start();
                }
            } else if (splitS.length == 6) {
                bulletList.add(
                        new Bullet("src/main/resources/images/other/" + splitS[3] + "Bullet.png", sx, sy, splitS[3],
                                Integer.parseInt(splitS[4]), splitS[5], GamePanel.this));
            }
        }

    }

    public void replay(String path) {
        Graphics g = this.getGraphics();

        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(path))) {
            byte[][] byArray = (byte[][]) ois.readObject();
            for (byte[] by : byArray) {
                ByteArrayInputStream bis = new ByteArrayInputStream(by);
                BufferedImage image = ImageIO.read(bis);
                g.drawImage(image, 0, 0, null);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void saveReplay(String path) {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(path))) {
            oos.writeObject(listTo2DArray(bytelList));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[][] listTo2DArray(List<byte[]> list) {
        int numRows = list.size();
        int numCols = 0;
        for (byte[] b : list) {
            if (numCols < b.length)
                numCols = b.length;
        }

        byte[][] twoDArray = new byte[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            twoDArray[i] = list.get(i);
        }

        return twoDArray;
    }

    public class KeyMonitor extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (state == 2) {
                player.keyPressed(e);
                return;
            }

            if (state == 3) {
                if (key == KeyEvent.VK_ENTER) {
                    state = 0;
                    init();
                }
                return;
            }

            switch (key) {
                case KeyEvent.VK_UP:
                    if (selectY > 220)
                        selectY -= 100;
                    break;
                case KeyEvent.VK_DOWN:
                    if (selectY < 420)
                        selectY += 100;
                    break;
                case KeyEvent.VK_ENTER: {
                    try {
                        if (state == 0) {
                            if (selectY == 220) {
                                init();
                                state = 2;
                                load("src\\main\\resources\\save\\save0");
                            } else if (selectY == 320)
                                state = 1;
                            else if (selectY == 420) {
                                GamePanel.this.dispose();
                                shutDown = true;
                            }
                        } else if (state == 1) {
                            enemySpawn.working = false;

                            if (selectY == 220) {
                                replay("src/main/resources/replay/replay1");
                                clearAll();
                                load("src\\main\\resources\\save\\save1");
                            } else if (selectY == 320) {
                                replay("src/main/resources/replay/replay2");
                                clearAll();
                                load("src\\main\\resources\\save\\save2");
                            } else if (selectY == 420) {
                                replay("src/main/resources/replay/replay3");
                                clearAll();
                                load("src\\main\\resources\\save\\save3");
                            }
                            state = 2;
                        }
                    } catch (Exception exce) {
                        exce.printStackTrace();
                    }
                    break;
                }
                case KeyEvent.VK_ESCAPE: {
                    state = 0;
                }
                default:

            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (state == 2) {
                player.keyReleased(e);
            }
        }
    }

    public static void main(String[] args) {
        GamePanel tankWar = new GamePanel();
        tankWar.launch();
    }
}