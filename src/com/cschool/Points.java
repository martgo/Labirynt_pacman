package com.cschool;

import java.awt.*;

public class Points extends Rectangle {

    public Points(int x, int y){
        setBounds(x+12, y+12, 8,8);
    }

    public void render(Graphics g){
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, width, height);
    }
}