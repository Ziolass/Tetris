import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

/**
 * Created by Michau on 08.06.2015.
 */
public class NextShapePanel extends JPanel {
    /**
     * Currently used {@link Shape} used for drawing next shape
     */
    private Shape shape= null;
    /**
     * Table of {@link Color} used for drawing different shapes in different colours
     */
    private Color [] colours = null;
    /**
     * Vertical size of a panel
     */
    private final int height=7;
    /**
     * Horizontal size of a panel
     */
    private final int width = 7;

    /**
     * Parametrised {@link NextShapePanel} constructor
     * @param colors Board of {@link Color} used for drawing
     * @param shape1 Next shape
     */
    public NextShapePanel(Color[] colors,Shape shape1) {

        shape=shape1;
        colours = colors;

    }

    /**
     * Function used for setting new next shape
     * @param s New next shape
     */
    public void setShape(Shape s) {
        shape=s;
    }

    /**
     * Function used for setting new set of {@link Color}
     * @param tab table of colors
     */
    public void updateColors(Color[] tab) {
        colours=tab;
    }

    /**
     * Estimates current height of a square by
     * dividing current window size by defined height of the board
     * @return Current width of a square
     */
    private int SquareWidth () {return (int) getSize().getWidth()/width; }

    /**
     * Estimates current height of a square by
     * dividing current window size by defined height of the board
     * @return Current height of a square
     */
    private double SquareHeight() {return  getSize().getHeight()/height;}


    protected void paintComponent(Graphics g) {

       int CurrentPositionX = width /2 +1;
       int CurrentPositionY = height - 2 + shape.minY();

        if (!isOpaque()) {
            super.paintComponent(g);
            return;
        }


        Dimension size = this.getSize();
        double boardTop =  size.getHeight() - height* SquareHeight();

        for (int i = 0; i < 4; ++i) {
            int x = CurrentPositionX+shape.getXfromRow(i)-1;
            double y = CurrentPositionY-shape.getYfromRow(i)-1;
            DrawSquare(g,x*SquareWidth(),boardTop+(height-y-1)*SquareHeight(),shape.getShape());

        }




        setOpaque(false);
        super.paintComponent(g);
        setOpaque(true);
    }
    private void DrawSquare(Graphics g, int x, double y, Shapes shape) {



        Graphics2D g2 = (Graphics2D) g;
        Color color = colours[shape.ordinal()];
        g2.setColor(color);
        g2.fill(new Rectangle.Double(x + 1, y + 1, SquareWidth() - 2, SquareHeight() - 2));

        g2.setColor(color.brighter());
        g2.draw(new Line2D.Double(x, y + SquareHeight() - 1, x, y));
        g2.draw(new Line2D.Double(x, y, x + SquareWidth() - 1, y));

        g2.setColor(color.darker());
        g2.draw(new Line2D.Double(x + 1, y + SquareHeight() - 1,
                x + SquareWidth() - 1, y + SquareHeight() - 1));
        g2.draw(new Line2D.Double(x + SquareWidth() - 1, y + SquareHeight() - 1,
                x + SquareWidth() - 1, y + 1));
    }
    }
