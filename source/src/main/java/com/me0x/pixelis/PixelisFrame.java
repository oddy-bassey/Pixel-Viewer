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

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Robot;

import static java.awt.Color.BLACK;
import static java.awt.Color.BLUE;
import static java.awt.Color.GREEN;
import static java.awt.Color.RED;
import static java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import static javax.swing.SwingConstants.CENTER;

/**
 * @author Teodor MAMOLEA
 */
class PixelisFrame extends JFrame {

    private ImagePanel imagePanel;

    private LabelTextFieldPanel red;
    private LabelTextFieldPanel green;
    private LabelTextFieldPanel blue;
    private LabelTextFieldPanel html;
    private LabelTextFieldPanel xCursor;
    private LabelTextFieldPanel yCursor;

    private JButton button1;
    private JButton button2;
    //private JButton button3;

    private Robot robot;

    private boolean windowOnTop;

    PixelisFrame() {
        try {
            init();
        } catch (final AWTException e) {
            throw new RuntimeException(e);
        }

        setTitle("Pixelis by me0x847206");
    }

    private void init() throws AWTException {
        robot = new Robot(getLocalGraphicsEnvironment().getDefaultScreenDevice());

        initButtons();
        initPanels();

        initTty();

        add(imagePanel, BorderLayout.CENTER);
        add(southPanel(), BorderLayout.SOUTH);

        setResizable(false);
        setAlwaysOnTop(windowOnTop);

        pack();

        Point centralPoint = getLocalGraphicsEnvironment().getCenterPoint();
        setLocation(centralPoint.x - (getWidth() >> 1), centralPoint.y - (getHeight() >> 1));
    }

    private void initButtons() {
        button1 = new JButton("Select Pixel");
        button1.setForeground(Color.RED);

        button2 = new JButton("Top " + (windowOnTop ? "(on)" : "(off)"));
        //button3 = new JButton("B3");

        button2.addActionListener(e -> {
            windowOnTop = !windowOnTop;
            PixelisFrame.this.setAlwaysOnTop(windowOnTop);
            button2.setText("Top " + (windowOnTop ? "(on)" : "(off)"));
        });
    }

    private void initPanels() {
        html = new LabelTextFieldPanel("H", BLACK, "HTML Style");

        red = new LabelTextFieldPanel("R", RED, "RED");
        green = new LabelTextFieldPanel("G", GREEN, "GREEN");
        blue = new LabelTextFieldPanel("B", BLUE, "BLUE");

        xCursor = new LabelTextFieldPanel("X", BLACK, "X value for Cursor position");
        yCursor = new LabelTextFieldPanel("Y", BLACK, "Y value for Cursor position");

        imagePanel = new ImagePanel("", CENTER, button1.getName());
    }

    private void initTty() {
        final TtyInput ttyInput = new TtyInput(robot, imagePanel, red, green, blue, html, xCursor, yCursor);
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(ttyInput);
        button1.addMouseMotionListener(ttyInput);
    }

    private JPanel southPanel() {
        final JPanel panel = new JPanel();

        panel.setLayout(new BorderLayout());
        panel.add(southPanelCenter(), BorderLayout.CENTER);
        panel.add(southPanelSouth(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel southPanelCenter() {
        final JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(3, 2));
        panel.add(red);
        panel.add(html);
        panel.add(green);
        panel.add(xCursor);
        panel.add(blue);
        panel.add(yCursor);

        return panel;
    }

    private JPanel southPanelSouth() {
        final JPanel panel = new JPanel();

        panel.setLayout(new GridLayout(1, 2));
        panel.add(button1);
        panel.add(button2);
        //panel.add(button3);

        return panel;
    }
}
