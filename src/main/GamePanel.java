package main;

import entity.Player;
import object.SuperObject;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

/* Inherits JPanel Class
so that main.GamePanel has all functions of JPanel */
public class GamePanel extends JPanel implements Runnable {
    // SCREEN SETTINGS
    final int originalTileSize = 16; // 16x16 tile
    final int scale = 3; // For scaling up

    public final int tileSize = originalTileSize * scale; // 48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; // 768 px
    public final int screenHeight = tileSize * maxScreenRow; // 576 px

    // WORLD SETTINGS
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    // FPS
    int FPS = 60;

    // SYSTEM
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Sound music = new Sound();
    Sound se = new Sound();
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public UI ui = new UI(this);
    Thread gameThread;

    // ENTITY AND OBJECT
    public Player player = new Player(this, keyH);
    public SuperObject[] obj = new SuperObject[10];

    public GamePanel() {
        // Sets the size of this class (JPanel)
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        /*
        If set to true, all the drawing from this
        component will be done in an offscreen painting buffer.
        In short, enabling this can improve game's rendering performance.
        */
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        // With this, this main.GamePanel can be "focused" to receive key input.
        this.setFocusable(true);
    }

    public void setupGame() {
        aSetter.setObject();
        playMusic(0);
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

//    @Override
//    public void run() {
//        double drawInterval = 1000000000.0 / FPS; // 0.01666 secs
//        // nanoTime(): returns the current system time
//        double nextDrawTime = System.nanoTime() + drawInterval;
//
//        // GAME LOOP
//        while (gameThread != null) {
//
//            // UPDATE: update information such as character position
//            update();
//            // DRAW: draw the screen with updated information
//            repaint();
//
//            try {
//                double remainTime = nextDrawTime - System.nanoTime();
//                remainTime = remainTime / 1000000; // nano to millis
//
//                if(remainTime < 0) remainTime = 0;
//
//                Thread.sleep((long) remainTime);
//                nextDrawTime += drawInterval;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }

// More accurate method: DELTA METHOD
@Override
public void run() {
    double drawInterval = 1000000000.0 / FPS; // 0.01666 secs
    double delta = 0;
    long lastTime = System.nanoTime();
    long currentTime;
    long timer = 0;
    int drawCount = 0;

    // GAME LOOP
    while (gameThread != null) {
        currentTime = System.nanoTime();
        delta += (currentTime - lastTime) / drawInterval;
        timer += (currentTime - lastTime);
        lastTime = currentTime;

        if(delta >= 1) {
            update();
            repaint();
            delta--;
            drawCount++;
        }
        if(timer >= 1000000000) {
//            System.out.println("FPS: " + drawCount);
            drawCount = 0;
            timer = 0;
        }
    }
}

    public void update() {
        player.update();
    }

    /*
    Graphics class: A class that has many functions to draw objects on the screen.
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        /*
        Graphics2D class extends the Graphics class
        to provide more sophisticated control over geometry,
        coordinate transformations, color management, and text layout.
         */
        Graphics2D g2 = (Graphics2D) g;

        // TILE
        tileM.draw(g2);

        // OBJECT
        for(int i = 0; i < obj.length; i++) {
            if(obj[i] != null) {
                obj[i].draw(g2, this);
            }
        }

        // PLAYER
        player.draw(g2);

        // UI
        ui.draw(g2);

        /*
         Dispose of this graphics context
         and release any system resources that it is using.
        */
        g2.dispose();
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    // SE: Sound Effects
    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }
}
