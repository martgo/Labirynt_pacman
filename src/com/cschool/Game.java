package com.cschool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Game extends Canvas implements Runnable, KeyListener {

    private boolean isRunning = false;
    public static int width = 800, height = 680;
    public static JFrame window;
    private Thread thread;
    private Music music;
    public static Player player;
    public static Map map;
    public static SpriteSheet spriteSheet;
    public static SpriteSheet spriteSheet2;

    public Game(){
        Dimension dimension = new Dimension(Game.width, Game.height);
        setPreferredSize(dimension);
        setMinimumSize(dimension);
        setMaximumSize(dimension);

        addKeyListener(this);

        player = new Player(Game.width/2, Game.height/2+32);
        map = new Map("/resources/map2.png");
        spriteSheet = new SpriteSheet("/resources/spritesheet.png");
        spriteSheet2 = new SpriteSheet("/resources/duszek.png");
    }

    public synchronized void start(){
        music = new Music("src/resources/pacman_beginning.wav");
        music.play();
        if (isRunning) return;
        isRunning = true;
        thread = new Thread(this);
        thread.start();

        SwingUtilities.getWindowAncestor(this).addWindowStateListener(windowEvent -> {
//            System.out.println("Window state changed: " + windowEvent);
            if(windowEvent.getNewState() ==1){
                if(player.music != null)                player.music.stop();
                music.stop();
            } else if(windowEvent.getNewState() ==0) {
              if(player.music!=null)  player.music.play();
                music.play();
            }
        });
    }

    public synchronized void stop(){
        if (!isRunning) return;
        isRunning = false;
        music.stop();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void tick() throws FileNotFoundException {
        player.tick();
        map.tick();
    }

    private void render(){
        BufferStrategy bs = getBufferStrategy();
        if (bs == null){
            createBufferStrategy(2);
            return;
        }
        Graphics g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, Game.width, Game.height);
        player.render(g);
        map.render(g);

        g.dispose();
        bs.show();
    }

    @Override
    public void run() {

        requestFocus();
        int fps = 0;
        double timer = System.currentTimeMillis();
        long lastTime = System.nanoTime();
        double targetTick = 50.0;
        double delta = 0;
        double ns = 1000000000 / targetTick;

        while (isRunning){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;

            while (delta >= 1){
                try {
                    tick();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                render();
                fps++;
                delta--;
            }
            if (System.currentTimeMillis() - timer >=1000){
                System.out.println(fps);
                fps = 0;
                timer += 1000;
            }
        }
        stop();
    }

    public static void main(String[] args) throws IOException {
//        Music music = new Music("src/resources/pacman_beginning.wav");
        Game.window = new JFrame();
        JPanel startPagePanel = new StartSide();
        ImageIcon mazeIcon = new ImageIcon("/resources/mazeIcon.png");
        window.setIconImage(mazeIcon.getImage());
        window.add(startPagePanel);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setPreferredSize(new Dimension(600, 600));
        window.setLocation(50, 50);
        window.setVisible(true);
        window.setTitle("MAZE");
        window.pack();
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT)player.right = true;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) player.left = true;
        if (e.getKeyCode() == KeyEvent.VK_UP) player.up = true;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) player.down = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.right = false;
        if (e.getKeyCode() == KeyEvent.VK_LEFT) player.left = false;
        if (e.getKeyCode() == KeyEvent.VK_UP) player.up = false;
        if (e.getKeyCode() == KeyEvent.VK_DOWN) player.down = false;
    }
}