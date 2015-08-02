package rgbpd;

import java.awt.*;

/**
 * Created by me0x847206 on 8/2/15.
 */
public class RGBPD {
    public static void main(String ... args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                Frame frame;
                try {
                    frame = new Frame();
                    frame.setDefaultCloseOperation(Frame.EXIT_ON_CLOSE);
                    frame.setVisible(true);
                } catch(AWTException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
