package com.cschool;

import java.awt.*;

public class Tile extends Rectangle {

    public Tile(int x, int y){
        setBounds(x, y, 32, 32);
    }

    public void render(Graphics g){
        g.setColor(new Color(33, 0, 127));
        g.fillRect(x, y, width, height);
    }
}
