package com.cschool;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class StartSide extends JPanel implements ActionListener {

    static String userName;
    JLabel gameName, jUserName, titleLabel;
    JTextField jAreaUserName;
    JButton playButton, quitButton;
    Color color = new Color(80,36,7);
    Color color2 = new Color(126,42,9);
    static Game game;
//        private BufferedImage backgroundImage;
    private Image backgroundPicture;
    private ImageIcon titleImage;

    public StartSide() throws IOException {
        backgroundPicture = Toolkit.getDefaultToolkit().createImage("src/resources/space3.gif");
        setLayout(null);
        titleImage = new ImageIcon("src/resources/rsz_title.png");
        titleLabel = new JLabel(titleImage);
        titleLabel.setBounds(-50,200,700,60);
        add(titleLabel);

         jUserName = new JLabel("Enter Username", SwingConstants.CENTER);
        jUserName.setFont(new Font("Phosphate", Font.BOLD, 24));
        jUserName.setBounds(165, 275, 300, 100);
        jUserName.setForeground(Color.RED);
        //jUserName.setOpaque(true);
        add(jUserName);

        jAreaUserName = new JTextField(SwingConstants.CENTER);
        jAreaUserName.setFont(new Font("Phosphate", Font.BOLD, 32));
        jAreaUserName.setHorizontalAlignment(JTextField.CENTER);
        jAreaUserName.setBounds(210, 350, 200, 50);
        jAreaUserName.setBorder(BorderFactory.createLineBorder(color, 2));
        jAreaUserName.setForeground(Color.black);
        jAreaUserName.setOpaque(true);
        add(jAreaUserName);

        playButton = new JButton("PLAY");
        playButton.setFont(new Font("Phosphate", Font.BOLD, 25));
        playButton.setBounds(260, 420, 100, 50);
        playButton.setHorizontalAlignment(JButton.CENTER);
        playButton.addActionListener(this);
        add(playButton);


        quitButton = new JButton("QUIT");
        quitButton.setFont(new Font("Phosphate", Font.BOLD, 25));
        quitButton.setBounds(260,475,100,50);
        quitButton.setHorizontalAlignment(JButton.CENTER);
        quitButton.setForeground(Color.BLACK);
        quitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        add(quitButton);

    }


    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        if(backgroundPicture != null){
            g.drawImage(backgroundPicture,0,0,this);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("PLAY") && !jAreaUserName.getText().equals("")){
            game = new Game();
            JFrame frame = new JFrame();
            ImageIcon mazeIcon = new ImageIcon("src/resources/mazeIcon.png");
            frame.setIconImage(mazeIcon.getImage());
            frame.setTitle("MAZE");
            frame.add(game);
            frame.setResizable(false);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            game.start();
            Game.window.dispose();
            userName = jAreaUserName.getText();
        }
        else {
            JFrame windowZleHaslo = new JFrame();
            windowZleHaslo.setVisible(true);
            windowZleHaslo.setBackground(Color.black);
            windowZleHaslo.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
            windowZleHaslo.setSize(400, 200);
            windowZleHaslo.setLocation(400, 100);
            JLabel komunikatRozneHasla = new JLabel("Nie podano nazwy uzytkownika", SwingConstants.CENTER);
            komunikatRozneHasla.setFont(new Font("Phosphate", Font.BOLD, 20));
            komunikatRozneHasla.setForeground(Color.RED);
            windowZleHaslo.add(komunikatRozneHasla);
        }
    }

    public static void gameStop(){
        game.stop();

    }
}