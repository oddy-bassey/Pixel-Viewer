/*
 * Copyright (C) 2017 Teodor MAMOLEA <Teodor.Mamolea@gmail.com>
 *
 * ******************************************************************************
 *
 * DOWHATYOUWANTTODO
 *
 * ******************************************************************************
 */
package com.me0x.pixelis;

import java.awt.Color;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import static java.awt.Image.SCALE_DEFAULT;
import static java.awt.event.KeyEvent.KEY_PRESSED;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_UP;
import static java.lang.Integer.toHexString;

import javax.swing.ImageIcon;

import static com.me0x.pixelis.ImagePanel.IMAGE_SIZE;

/**
 * @author Teodor MAMOLEA
 */
class TtyInput extends MouseMotionAdapter implements KeyEventDispatcher {

    private static final int SCREEN_CAPTURED_SIZE = IMAGE_SIZE / 20;
    private static final int SCREEN_CAPTURED_RADIUS = SCREEN_CAPTURED_SIZE / 2;

    private final Robot robot;

    private final ImagePanel imagePanel;

    private final LabelTextFieldPanel red;
    private final LabelTextFieldPanel green;
    private final LabelTextFieldPanel blue;
    private final LabelTextFieldPanel html;
    private final LabelTextFieldPanel xCursor;
    private final LabelTextFieldPanel yCursor;

    private int centralX, centralY;

    TtyInput(final Robot robot, final ImagePanel imagePanel, final LabelTextFieldPanel red, final LabelTextFieldPanel green, final LabelTextFieldPanel blue, final LabelTextFieldPanel html, final LabelTextFieldPanel xCursor, final LabelTextFieldPanel yCursor) {
        this.robot = robot;
        this.imagePanel = imagePanel;
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.html = html;
        this.xCursor = xCursor;
        this.yCursor = yCursor;

        update();
    }

    @Override
    public void mouseDragged(final MouseEvent event) {
        update();
    }

    private void update() {
        final Point centralPoint = MouseInfo.getPointerInfo().getLocation();
        centralX = (int) centralPoint.getX();
        centralY = (int) centralPoint.getY();

        imagePanel.setIcon(new ImageIcon(captureScreen()));

        final Color pixelColor = robot.getPixelColor(centralX, centralY);
        red.setText(pixelColor.getRed());
        green.setText(pixelColor.getGreen());
        blue.setText(pixelColor.getBlue());

        html.setText("#" + toHexString(pixelColor.getRGB()).substring(2));

        xCursor.setText(centralX);
        yCursor.setText(centralY);
    }

    private Image captureScreen() {
        final Rectangle rectangle = new Rectangle(centralX - SCREEN_CAPTURED_RADIUS, centralY - SCREEN_CAPTURED_RADIUS, SCREEN_CAPTURED_SIZE, SCREEN_CAPTURED_SIZE);
        return robot.createScreenCapture(rectangle).getScaledInstance(IMAGE_SIZE, IMAGE_SIZE, SCALE_DEFAULT);
    }

    @Override
    public boolean dispatchKeyEvent(final KeyEvent e) {
        _ifs:
        if (e.getID() == KEY_PRESSED) {
            final int keyCode = e.getKeyCode();

            if (keyCode == VK_UP) {
                centralY--;
            } else if (keyCode == VK_DOWN) {
                centralY++;
            } else if (keyCode == VK_LEFT) {
                centralX--;
            } else if (keyCode == VK_RIGHT) {
                centralX++;
            } else {
                break _ifs;
            }

            robot.mouseMove(centralX, centralY);

            update();
        }

        return false;
    }

}
