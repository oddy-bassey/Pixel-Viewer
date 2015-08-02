package rgbpd;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;

/**
 * Created by me0x847206 on 8/2/15.
 */
public class Frame extends javax.swing.JFrame {
    public Frame() throws AWTException {
        setTitle("Get RGB Pixel by me0x847206");

        screen = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        robot = new Robot( screen );

        JPanel southPanel = new JPanel();
        JPanel southPanelCenter = new JPanel();
        JPanel southPanelSouth = new JPanel();

        southPanel.setLayout( new BorderLayout() );
        southPanelCenter.setLayout( new GridLayout( 3, 2 ) );
        southPanelSouth.setLayout( new GridLayout( 1, 2 ) );

        southPanelCenter.add( red = new LabelTextFieldPanel("R", "", Color.RED) );
        southPanelCenter.add( html = new LabelTextFieldPanel("H", "", Color.BLACK, "HTML Style") );
        southPanelCenter.add( green = new LabelTextFieldPanel("G", "", Color.GREEN) );
        southPanelCenter.add( xCursor = new LabelTextFieldPanel("X", "", Color.BLACK, "Y value for Cursor position") );
        southPanelCenter.add( blue = new LabelTextFieldPanel("B", "", Color.BLUE) );
        southPanelCenter.add( yCursor = new LabelTextFieldPanel("Y", "", Color.BLACK, "Y value for Cursor position") );
        southPanelSouth.add( button1 = new JButton("Select Pixel") );
        southPanelSouth.add( button2 = new JButton("Top " + (windowOnTop ? "(on)" : "(off)" )) );
        //southPanelSouth.add( button3 = new JButton("B3") );

        southPanel.add( southPanelCenter );
        southPanel.add( southPanelSouth, BorderLayout.SOUTH );

        imageLabel = new Label("", SwingConstants.CENTER);
        imageLabel.setToolTipText("Go mouse pointer to `" + button1.getText() + "` Button and press it. "
                + "Dragg the pointer and take pixel color until the mouse button is not released.");
        imageLabel.setPreferredSize(new Dimension( 260, 260));

        button1.addMouseMotionListener( new MouseMotionListener() );
        button2.addActionListener(e -> {
            windowOnTop = !windowOnTop;
            Frame.this.setAlwaysOnTop( windowOnTop );
            button2.setText("Top " + (windowOnTop ? "(on)" : "(off)" ) );
        });

        add( imageLabel);
        add( southPanel, BorderLayout.SOUTH );

        setResizable( false );
        setAlwaysOnTop( windowOnTop );
        pack();

        Point p = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint();
        setLocation( p.x - getWidth() / 2, p.y - getHeight() / 2);
    }

    private class MouseMotionListener extends java.awt.event.MouseMotionAdapter {

        public MouseMotionListener() {
            set();
        }

        @Override
        public void mouseDragged(java.awt.event.MouseEvent e) {
            set();
        }

        private void set() {
            Point p = MouseInfo.getPointerInfo().getLocation();
            Color c = robot.getPixelColor( (int) p.getX(), (int) p.getY());

            xCursor.setText("" + (int) p.getX() );
            yCursor.setText("" + (int) p.getY() );

            blue.setText("" + c.getBlue());
            red.setText("" + c.getRed());
            green.setText("" + c.getGreen());
            html.setText("#" + Integer.toHexString( c.getRGB() ).substring(2) );

            int x = (int) p.getX() - 6;
            int y = (int) p.getY() - 6;

            imageLabel.setIcon(
                    new ImageIcon(robot
                            .createScreenCapture( new Rectangle( x, y, 13, 13) )
                            .getScaledInstance(260, 260, Image.SCALE_DEFAULT)
                    )
            );
        }
    }

    private class LabelTextFieldPanel extends JPanel {

        public LabelTextFieldPanel(String labelText, String fieldText, Color color) {
            this( labelText, fieldText, color, "");
        }

        public LabelTextFieldPanel(String labelText, String fieldText, Color color, String description) {
            label = new JLabel(labelText, SwingConstants.CENTER);
            field = new JTextField(fieldText, 7);

            field.setComponentPopupMenu(new PopupMenu( field ));
            field.setEditable( false );
            field.setHorizontalAlignment( SwingConstants.CENTER );

            label.setForeground(color);
            field.setForeground(color);

            setLayout( new FlowLayout() );

            add( label );
            add( field );
            this.setToolTipText( description );
            field.setToolTipText( description );
            label.setToolTipText( description );

            field.addMouseListener( new MouseAdapter() {
                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    if(e.getButton() == java.awt.event.MouseEvent.BUTTON2_MASK) {
                        field.getComponentPopupMenu().show( null, e.getX(), e.getY());
                    } else
                        field.selectAll();
                }
                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    field.select( 0, 0);
                }
            });

        }
        public void setText(String text) {
            field.setText( text );
        }

        private class PopupMenu extends javax.swing.JPopupMenu {

            public PopupMenu(final JTextField textField) {
                JMenuItem toClipboard = new JMenuItem("To Clipboard");
                add( toClipboard );

                toClipboard.addActionListener(e -> Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents( new StringSelection(textField.getText()), null));

            }
        }

        private JLabel label = null;
        private JTextField field = null;
    }

    private final Label imageLabel;
    private final LabelTextFieldPanel red, green, blue, html, xCursor, yCursor;

    private final JButton button1, button2;//, button3;

    private final GraphicsDevice screen;
    private final Robot robot;

    private boolean windowOnTop = false;
}
