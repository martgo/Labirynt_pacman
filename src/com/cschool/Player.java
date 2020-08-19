package com.cschool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class Player extends Rectangle implements ActionListener {

    public boolean right, left, up, down;
    private int speed = 4;
    private int punkty;
    private int iloscZyc;
    private String nazwa = StartSide.userName;
    JFrame oknoKoncowe;
    JPanel panelKoncowy, p;
    JTextArea ranking;
    JLabel gameName, titleLabel, quitLabel, scoreLabel;
    private Image gameOverImage;
    private BufferedImage gameOvrIMG;
    JButton quitButton, scoreButton;
    private ImageIcon titleImage;
    Music music;
    private GameOverMusic musicGameOver;

    List<Player> playerList = new ArrayList<>();

    public Player(int x, int y) {
        nazwa = StartSide.userName;
        setBounds(x, y, 32, 32);
        punkty = 0;
        iloscZyc = 3;
        init();
    }

    public Player(int x, int y, int punkty, int iloscZyc) {
        nazwa = StartSide.userName;
        setBounds(x, y, 32, 32);
        this.punkty = punkty;
        this.iloscZyc = iloscZyc;
        init();
    }

    public Player(String nazwa, int punkty) {
        this.nazwa = nazwa;
        this.punkty = punkty;
        init();
    }

    private void init() {

    }


    public void tick() throws FileNotFoundException {
        makeMove();
        Map map = Game.map;
        eatApples(map);
        render();

        if (Map.points.size() == 0 && Map.enemies.size() == 8) {
            Game.player = new Player(Game.width / 2, Game.height / 2 + 32, punkty, iloscZyc);
            Game.map = new Map("/resources/map3.png");
            for (int i = 0; i < Map.enemies.size(); i++) {
                Map.enemies.get(i).setSpd(Map.enemies.get(i).getSpd() + 2);
            }
        } else if (map.points.size() == 0 && Map.enemies.size() == 6) {
            Game.player = new Player(Game.width / 2, Game.height / 2 + 32, punkty, iloscZyc);
            Game.map = new Map("/resources/map3.png");
        } else if (map.points.size() == 0) {
            Game.player = new Player(Game.width / 2, Game.height / 2 + 32, punkty, iloscZyc);
            Game.map = new Map("/resources/map.png");
        }
    }

    private void makeMove() {
        if (right && canMove(x + speed, y)) x += speed;
        if (left && canMove(x - speed, y)) x -= speed;
        if (up && canMove(x, y - speed)) y -= speed;
        if (down && canMove(x, y + speed)) y += speed;
    }

    private void eatApples(Map map) {
        for (int i = 0; i < map.points.size(); i++) {
            if (this.intersects(map.points.get(i))) {
                map.points.remove(i);
                punkty++;
                break;
            }
        }
    }

    private void render() {
        for (int i = 0; i < Game.map.enemies.size(); i++) {
            Enemy en = Game.map.enemies.get(i);

            if (en.intersects(this) && iloscZyc == 1) {
                renderGameOver();
            } else if (en.intersects(this) && (iloscZyc > 0)) {
                renderEnemies();
            }
        }
    }

    private void renderGameOver() {
        System.out.println(Game.player);

        try (Writer zapis = new BufferedWriter(new FileWriter("Ranking.txt", true))) {
            zapis.append(StartSide.userName + " - " + Game.player.punkty + "\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        showGameOverScreen();
    }

    private void showGameOverScreen() {
        JFrame gameOver = new JFrame();
        titleImage = new ImageIcon("src/resources/GameOver.png");
        titleLabel = new JLabel(titleImage);
        titleLabel.setBounds(40, 100, 200, 30);
        gameOver.add(titleLabel);
        p = new JPanel();
        quitButton = new JButton("QUIT");
        quitButton.setFont(new Font("Phosphate", Font.BOLD, 17));
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        scoreButton = new JButton(" HIGHEST SCORES");
        scoreButton.setFont(new Font("Phosphate", Font.BOLD, 17));
        scoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JFrame scores = new JFrame();
                scores.setVisible(true);
                scores.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
                scores.setSize(300, 285);
                scores.setLocation(400, 100);

        JList<HighScoreEntry> scoreList = createHighScoreList();
        scoreList.setFont(new Font("Din Condensed", Font.BOLD, 20));
//        scoreList(new Dimension(200,200));
//        scoreList.setBounds(700,600,100,200);
        scoreList.setVisibleRowCount(10);
        scores.add(scoreList);
            }

            private void add(JScrollPane scrollPane) {
            }
        });
        music = new Music("src/resources/pacman_beginning.wav");
        music.stop();
        musicGameOver = new GameOverMusic("src/resources/pacman_death.wav");
        musicGameOver.play();
        p.add(quitButton);
        p.add(scoreButton);
        p.setBounds(75, 200, 175, 175);
        gameOver.add(p);
        gameOver.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        gameOver.setSize(300, 400);
        gameOver.setLocation(400, 100);
        JLabel points = new JLabel("Your score:  " + punkty, SwingConstants.CENTER);
        points.setFont(new Font("Phosphate", Font.BOLD, 25));
        points.setBounds(100, 600, 600, 300);
        gameOver.add(points);
        gameOver.setVisible(true);
        StartSide.gameStop();
    }

    private JList<HighScoreEntry> createHighScoreList() {
        List<HighScoreEntry> entries = null;
        try (BufferedReader reader = new BufferedReader(new FileReader("Ranking.txt"))) {
            entries = reader
                    .lines()
                    .filter(line -> line.contains(" - "))
                    .map(Player::createHighScoreEntryFromLine)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.sort(entries);


        JList<HighScoreEntry> scoreList = new JList<>();
        scoreList.setListData(new Vector<>(entries));
        return scoreList;
    }

    private static HighScoreEntry createHighScoreEntryFromLine(String line) {
        String[] values = line.split(" - ");
        return new HighScoreEntry(values[0], Integer.parseInt(values[1]));
    }

    static class HighScoreEntry implements Comparable<HighScoreEntry> {
        public final String nickName;
        public final int score;

        HighScoreEntry(String nickName, int score) {
            this.nickName = nickName;
            this.score = score;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            HighScoreEntry that = (HighScoreEntry) o;
            return score == that.score &&
                    Objects.equals(nickName, that.nickName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(nickName, score);
        }

        @Override
        public int compareTo(HighScoreEntry highScoreEntry) {
            int out = Integer.compare(highScoreEntry.score, score);
            if (out == 0) {
                return nickName.compareTo(highScoreEntry.nickName);
            }
            return out;
        }

        @Override
        public String toString() {
            return nickName + ":" + score;
        }
    }

    private void renderEnemies() {
        Game.player.setBounds(Game.width / 2, Game.height / 2 - 52, 32, 32);

        Map.enemies.get(0).setBounds(32, 32, 32, 32);
        Map.enemies.get(1).setBounds(32, 576, 32, 32);
        Map.enemies.get(2).setBounds(768 - 32, 32, 32, 32);
        Map.enemies.get(3).setBounds(768 - 32, 608 - 32, 32, 32);

        if (Map.enemies.size() == 6) {
            Map.enemies.get(4).setBounds(400, 32, 32, 32);
            Map.enemies.get(5).setBounds(400, 576, 32, 32);
        }

        iloscZyc--;
    }

    private boolean canMove(int nextx, int nexty) {
        Rectangle bounds = new Rectangle(nextx, nexty, width, height);
        Map map = Game.map;

        for (int xx = 0; xx < map.tiles.length; xx++) {
            for (int yy = 0; yy < map.tiles[0].length; yy++) {
                if (map.tiles[xx][yy] != null) {
                    if (bounds.intersects(map.tiles[xx][yy])) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public void render(Graphics g) {

        SpriteSheet sheet = Game.spriteSheet;
        if (left) {
            g.drawImage(sheet.getSprite(16, 0), x, y, 32, 32, null);
        } else if (down) {
            g.drawImage(sheet.getSprite(16, 16), x, y, 32, 32, null);
        } else if (up) {
            g.drawImage(sheet.getSprite(0, 16), x, y, 32, 32, null);
        } else {
            g.drawImage(sheet.getSprite(0, 0), x, y, 32, 32, null);
        }
        g.setFont(new Font("Curier New", Font.CENTER_BASELINE, 20));
        g.setColor(Color.yellow);
        g.drawString("SCORES: " + punkty, 600, 665);
        g.drawString("LIFES: " + iloscZyc, 350, 665);
        g.drawString("PLAYER: " + StartSide.userName, 100, 665);

    }


    @Override
    public String toString() {
        return "Player{" +
                "punkty=" + punkty +
                ", nazwa='" + nazwa + '\'' +
                '}';
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

    }
}