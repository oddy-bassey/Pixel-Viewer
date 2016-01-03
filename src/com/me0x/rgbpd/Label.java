package com.me0x.rgbpd;

import javax.swing.*;
import java.awt.*;

/**
 * Created by me0x847206 on 8/2/15.
 */
public class Label extends JLabel {
    public Label(String name, int i) {
        super(name, i);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setPaint(Color.RED);

        int x = (getWidth() - 19) / 2;
        int y = (getHeight() - 19) / 2;

        g2.drawRect(x, y, 19, 19);
    }
}
