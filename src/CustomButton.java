import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.RoundRectangle2D;

import javax.swing.ButtonModel;
import javax.swing.JButton;


/**
 * Class used for creating custom made {@link JButton}
 */
public class CustomButton extends JButton {
    /**
     * Local {@link Color} instance used during drawing the button
     */
    private Color startColor = new Color(192, 192, 192);
    /**
     * Local {@link Color} instance used during drawing the button
     */
    private Color endColor = new Color(82, 82, 82);
    /**
     * Local {@link Color} instance used during drawing the button
     */
    private Color rollOverColor = new Color(255, 143, 89);
    /**
     * Local {@link Color} instance used during drawing the button
     */
    private Color pressedColor = new Color(204, 67, 0);
    /**
     * Instance defining outer size of a button
     */
    private int outerRoundRectSize = 30;
    /**
     * Instance defining inner size of a button
     */
    private int innerRoundRectSize = 28;
    /**
     * {@link GradientPaint} instance used during drawing the button
     */
    private GradientPaint GP;

    /**
     * Constructor defining button's text
     * @param text New {@link CustomButton} text
     */
    public CustomButton(String text) {
        // default JButton constructor
        super();
        //setting chosen text
        setText(text);
        // disabling default drawing methods
        setContentAreaFilled(false);
        setBorderPainted(false);
        // creating custom font
        setFont(new Font("Thoma", Font.BOLD, 12));
        setForeground(Color.WHITE);
        setFocusable(false);

    }


    /**
     * Overridden paint method used for custom button painting
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();

        // renderinghints used for enabling better looking view
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int h = getHeight();
        int w = getWidth();

        ButtonModel model = getModel();
        // defines color of unfocused button
        if (!model.isEnabled()) {
            setForeground(Color.GRAY);
            GP = new GradientPaint(0, 0, new Color(192,192,192), 0, h, new Color(192,192,192),
                    true);
        // defines color of focused button
        }else{
            setForeground(Color.WHITE);
            if (model.isRollover()) {
                GP = new GradientPaint(0, 0, rollOverColor, 0, h, rollOverColor,
                        true);

            } else {
                GP = new GradientPaint(0, 0, startColor, 0, h, endColor, true);
            }
        }
        g2d.setPaint(GP);
        GradientPaint p1;
        GradientPaint p2;
        // change color after being pressed and make sound
        if (model.isPressed()) {
            SoundController.play("Sounds/click.wav");
            GP = new GradientPaint(0, 0, pressedColor, 0, h, pressedColor, true);
            g2d.setPaint(GP);
            p1 = new GradientPaint(0, 0, new Color(0, 0, 0), 0, h - 1,
                    new Color(100, 100, 100));
            p2 = new GradientPaint(0, 1, new Color(0, 0, 0, 50), 0, h - 3,
                    new Color(255, 255, 255, 100));
        } else {
            p1 = new GradientPaint(0, 0, new Color(100, 100, 100), 0, h - 1,
                    new Color(0, 0, 0));
            p2 = new GradientPaint(0, 1, new Color(255, 255, 255, 100), 0,
                    h - 3, new Color(0, 0, 0, 50));
            GP = new GradientPaint(0, 0, startColor, 0, h, endColor, true);
        }

        RoundRectangle2D.Float r2d = new RoundRectangle2D.Float(0, 0, w - 1,
                h - 1, outerRoundRectSize, outerRoundRectSize);
        Shape clip = g2d.getClip();
        g2d.clip(r2d);
        g2d.fillRect(0, 0, w, h);
        g2d.setClip(clip);
        g2d.setPaint(p1);
        g2d.drawRoundRect(0, 0, w - 1, h - 1, outerRoundRectSize,
                outerRoundRectSize);
        g2d.setPaint(p2);
        g2d.drawRoundRect(1, 1, w - 3, h - 3, innerRoundRectSize,
                innerRoundRectSize);
        g2d.dispose();

        super.paintComponent(g);
    }

}