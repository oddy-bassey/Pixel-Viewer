package rgbpd;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;

/**
 * Created by me0x847206 on 8/2/15.
 */
public class Frame extends JFrame {
    public Frame() throws AWTException {
        setTitle("Get RGB Pixel by me0x847206");

        robot = new Robot(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());

        JPanel southPanel = new JPanel();
        JPanel southPanelCenter = new JPanel();
        JPanel southPanelSouth = new JPanel();

        southPanel.setLayout(new BorderLayout());
        southPanelCenter.setLayout(new GridLayout(3, 2));
        southPanelSouth.setLayout(new GridLayout(1, 2));

        southPanelCenter.add(red = new LabelTextFieldPanel("R", "", Color.RED));
        southPanelCenter.add(html = new LabelTextFieldPanel("H", "", Color.BLACK, "HTML Style"));
        southPanelCenter.add(green = new LabelTextFieldPanel("G", "", Color.GREEN));
        southPanelCenter.add(xCursor = new LabelTextFieldPanel("X", "", Color.BLACK, "X value for Cursor position"));
        southPanelCenter.add(blue = new LabelTextFieldPanel("B", "", Color.BLUE));
        southPanelCenter.add(yCursor = new LabelTextFieldPanel("Y", "", Color.BLACK, "Y value for Cursor position"));
        southPanelSouth.add(button1 = new JButton("Select Pixel"));
        southPanelSouth.add(button2 = new JButton("Top " + (windowOnTop ? "(on)" : "(off)" )));
        //southPanelSouth.add(button3 = new JButton("B3"));

        southPanel.add(southPanelCenter);
        southPanel.add(southPanelSouth, BorderLayout.SOUTH);

        imageLabel = new Label("", SwingConstants.CENTER);
        imageLabel.setToolTipText("Move mouse pointer to `" + button1.getText() + "` button and press it. "
                + "Drag the pointer and take the pixel color.");
        imageLabel.setPreferredSize(new Dimension(260, 260));

        KeyboardAndMouseListener listener = new KeyboardAndMouseListener();
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(listener);
        button1.addMouseMotionListener(listener);
        button2.addActionListener(e -> {
            windowOnTop = !windowOnTop;
            Frame.this.setAlwaysOnTop(windowOnTop);
            button2.setText("Top " + (windowOnTop ? "(on)" : "(off)" ));
        });

        add(imageLabel);
        add(southPanel, BorderLayout.SOUTH);

        setResizable(false);
        setAlwaysOnTop(windowOnTop);
        pack();

        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        setLocation(p.x - (getWidth() >> 1), p.y - (getHeight() >> 1));
    }

    private class KeyboardAndMouseListener extends MouseMotionAdapter implements KeyEventDispatcher {
        private int centralX, centralY;

        public KeyboardAndMouseListener() {
            update();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            update();
        }

        private void update() {
            Point centralPoint = MouseInfo.getPointerInfo().getLocation();
            centralX = (int) centralPoint.getX();
            centralY = (int) centralPoint.getY();

            Color c = robot.getPixelColor((int) centralPoint.getX(), (int) centralPoint.getY());

            xCursor.setText("" + centralX);
            yCursor.setText("" + centralY);

            blue.setText("" + c.getBlue());
            red.setText("" + c.getRed());
            green.setText("" + c.getGreen());
            html.setText("#" + Integer.toHexString(c.getRGB()).substring(2));

            imageLabel.setIcon(
                    new ImageIcon(robot
                            .createScreenCapture(new Rectangle( centralX - 6, centralY - 6, 13, 13))
                            .getScaledInstance(260, 260, Image.SCALE_DEFAULT)
                    )
            );
        }

        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if(e.getID() == e.KEY_PRESSED) {
                int keyCode = e.getKeyCode();
                boolean needUpdate;
                if(needUpdate = keyCode == e.VK_UP) {
                    centralY--;
                } else if(needUpdate = keyCode == e.VK_DOWN) {
                    centralY++;
                } else if(needUpdate = keyCode == e.VK_LEFT) {
                    centralX--;
                } else if(needUpdate = keyCode == e.VK_RIGHT) {
                    centralX++;
                }

                if(needUpdate) {
                    robot.mouseMove(centralX, centralY);
                    update();
                }
            }

            return false;
        }
    }

    private class LabelTextFieldPanel extends JPanel {

        public LabelTextFieldPanel(String labelText, String fieldText, Color color) {
            this(labelText, fieldText, color, "");
        }

        public LabelTextFieldPanel(String labelText, String fieldText, Color color, String description) {
            label = new JLabel(labelText, SwingConstants.CENTER);
            field = new JTextField(fieldText, 7);

            field.setComponentPopupMenu(new PopupMenu(field));
            field.setEditable(false);
            field.setHorizontalAlignment(SwingConstants.CENTER);

            label.setForeground(color);
            field.setForeground(color);

            setLayout(new FlowLayout());

            add(label);
            add(field);
            this.setToolTipText(description);
            field.setToolTipText(description);
            label.setToolTipText(description);

            field.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    if(e.getButton() == MouseEvent.BUTTON2_MASK) {
                        field.getComponentPopupMenu().show(null, e.getX(), e.getY());
                    } else
                        field.selectAll();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    field.select(0, 0);
                }
            });

        }
        public void setText(String text) {
            field.setText(text);
        }

        private class PopupMenu extends javax.swing.JPopupMenu {

            public PopupMenu(final JTextField textField) {
                JMenuItem toClipboard = new JMenuItem("To Clipboard");
                add(toClipboard);

                toClipboard.addActionListener(e -> Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new StringSelection(textField.getText()), null));

            }
        }

        private JLabel label = null;
        private JTextField field = null;
    }

    private final Label imageLabel;
    private final LabelTextFieldPanel red, green, blue, html, xCursor, yCursor;

    private final JButton button1, button2;//, button3;

    private final Robot robot;

    private boolean windowOnTop = false;
}
