import com.sun.rowset.internal.Row;

import java.util.Random;

/**
 * Created by Michau on 05.04.2015.
 *
 */

/**
 * Enumeration storing possible Tetris shapes
 */
enum Shapes { None, Zet, S, Line, T, Square, L, MirroredL
}

public class Shape {
    /**
     * The 2-dimensional array which stores
     * current coordination of the specific shape
     */
    private int [][] Coordinations;
    /**
     * The 3-dimensional array which stores
     * all the possible shapes of a Tetris block
     */
    private int [][][] ShapeMatrix;
    /**
     * Instance of a currently used shape
     */
    private Shapes CurrentShape;

    /**
     * Default class constructor
     */
    public Shape() {

        Coordinations = new int[4][2];

        ShapeMatrix = new int[][][] {
                {{0, 0}, {0, 0}, {0, 0}, {0, 0}},
                {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}},
                {{0, -1}, {0, 0}, {1, 0}, {1, 1}},
                {{0, -1}, {0, 0}, {0, 1}, {0, 2}},
                {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},
                {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},
                {{1, -1}, {0, -1}, {0, 0}, {0, 1}}
        };

        CurrentShape = Shapes.None;

    }

    /**
     * Returns a vertical value of a shape
     * from given row
     * @param RowIndex absolute number defining matrix row
     * @return vertical value at row
     */
    public int getXfromRow(int RowIndex) {return Coordinations[RowIndex][0];}

    /**
     * Return a horizontal value of a shape
     * from given row
     * @param RowIndex absolute number defining matrix row
     * @return horizontal value at row
     */
    public int getYfromRow(int RowIndex) {return Coordinations[RowIndex][1];}

    /**
     * Sets a new value for vertical coordinate
     * at given row
     * @param index number of a row in matrix
     * @param x new value that is going to be set
     */
    private void setXonRow(int index, int x) {Coordinations[index][0] = x;}

    /**
     * Sets a new value for horizontal coordinate
     * at given row
     * @param index number of a row in matrix
     * @param y new value that is going to be set
     */
    private void setYonRow(int index, int y) {Coordinations[index][1] = y;}

    /**
     * Estimates minimal value that
     * is needed in order to fit given Shape
     * in screen properly
     * @return Minimal value from the top
     */
    public int minY() {
        int a = Coordinations[0][1];
        for (int i = 0; i < 4; i++) {
            a = Math.min(a, Coordinations[i][1]);
        }
        return a;
    }

    /**
     * Returns a currently used shape
     * @return Currently used shape
     */
    public Shapes getShape() { return CurrentShape;}

    /**
     * Changes current shape into the one given
     * @param shape New shape
     */
    public void setShape(Shapes shape) {

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                Coordinations [i][j] = ShapeMatrix[shape.ordinal()][i][j];
            }
        }
        CurrentShape = shape;
    }

    /**
     * Changes current shape into
     * a randomly picked one
     */
    public void SetRandom() {
        Random random = new Random();
        int seed = (Math.abs(random.nextInt()) % 7) + 1;
        Shapes [] values = Shapes.values();
        setShape(values[seed]);
    }

    /**
     * Rotates the shape clockwise
     * @return Returns self if is a square, rotated one in other cases
     */
    public Shape RotateRight() {
        if(CurrentShape == Shapes.Square) {
            return this;
        }
        Shape result = new Shape();
        result.CurrentShape = CurrentShape;
        for (int i = 0; i < 4; i++) {
            result.setXonRow(i,-getYfromRow(i));
            result.setYonRow(i, getXfromRow(i));
        }
        return result;
    }

    /**
     * Rotates the shape counter - clockwise
     * @return Returns self if is a square, rotated one in othes cases
     */
    public Shape RotateLeft() {
        if(CurrentShape == Shapes.Square) {
            return this;
        }

        Shape result = new Shape();
        result.CurrentShape = CurrentShape;

        for (int i = 0; i < 4; i++) {
            result.setXonRow(i,getYfromRow(i));
            result.setYonRow(i,-getXfromRow(i));
        }
return result;
    }


}
