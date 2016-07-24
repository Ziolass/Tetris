import javax.swing.*;
import java.awt.*;

/**
 * Created by Michau on 05.06.2015.
 */

/**
 * Overridden {@link JPanel} used for displaying text  / images
 */
public class CustomPanel extends JPanel {
    /**
     * Instance of an {@link Image} used as a background
     */
    private Image img = null;
    /**
     * Instance of an {@link String} used for drawing score
     */
    private String score=null;

    /**
     * Parametrised {@link CustomPanel} constructor
     * Used for displaying image in the back of a panel
     * @param image_name Desired image URL
     */
    public CustomPanel(String image_name) {
        img = Toolkit.getDefaultToolkit().createImage(image_name);

    }

    /**
     * Parametrised {@link CustomPanel} constructor
     * Used for displaying text in panel
     * @param score value to be updated
     */
    public CustomPanel(int score) {
        this.score=""+score;
    }

    /**
     * Funtion used for updating panel's text
     * @param s Desired new text
     */
    public void setScore(String s) {
        score=s;
    }

    /**
     * Overridden painting method used for customizing panel's look
     * @param g {@link Graphics} instance
     */
    protected void paintComponent(Graphics g) {

        if (!isOpaque()) {
            super.paintComponent(g);
            return;
        }
        int w = getWidth();
        int h = getHeight();

        // if image was initialised draw it
        if (img != null) {
            g.drawImage(img, 0, 0, w, h, this);

        }
        // else draw String
        else {

           g.drawString(""+score,w/2-score.length()/2,h/2);
        }
        setOpaque(false);
        super.paintComponent(g);
        setOpaque(true);
    }
}
