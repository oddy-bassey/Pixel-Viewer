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
import java.awt.FlowLayout;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static java.awt.Toolkit.getDefaultToolkit;
import static java.awt.event.InputEvent.BUTTON2_MASK;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;

import static javax.swing.SwingConstants.CENTER;

/**
 * @author Teodor MAMOLEA
 */
class LabelTextFieldPanel extends JPanel {

    private final JTextField field;

    LabelTextFieldPanel(final String labelText, final Color color, final String description) {
        setLayout(new FlowLayout());
        setToolTipText(description);

        field = new JTextField("", 7);
        field.setComponentPopupMenu(new PopupMenu(field));
        field.setEditable(false);
        field.setHorizontalAlignment(CENTER);
        field.setForeground(color);
        field.setToolTipText(description);
        add(field);

        final JLabel label = new JLabel(labelText, CENTER);
        label.setForeground(color);
        label.setToolTipText(description);
        add(label);

        setMouseListener();
    }

    private void setMouseListener() {
        field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                if (e.getButton() == BUTTON2_MASK) {
                    field.getComponentPopupMenu().show(null, e.getX(), e.getY());
                } else {
                    field.selectAll();
                }
            }

            @Override
            public void mouseExited(final MouseEvent e) {
                field.select(0, 0);
            }
        });
    }

    void setText(final String text) {
        field.setText(text);
    }

    void setText(final int value) {
        field.setText("" + value);
    }

    private static class PopupMenu extends JPopupMenu {
        PopupMenu(final JTextField textField) {
            add(new JMenuItem("COPY") {{
                addActionListener(e -> getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(textField.getText()), null));
            }});
        }
    }
}
