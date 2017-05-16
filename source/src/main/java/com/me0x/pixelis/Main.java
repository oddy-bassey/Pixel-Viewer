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

import java.awt.EventQueue;

import javax.swing.JFrame;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

/**
 * @author Teodor MAMOLEA
 */
public final class Main {

    public static void main(final String... args) {
        EventQueue.invokeLater(() -> {
            final JFrame frame = new PixelisFrame();

            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }

}
