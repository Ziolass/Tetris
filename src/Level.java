import java.awt.*;
import java.io.Serializable;

/**
 * Created by Michau on 16.05.2015.
 */
public class Level implements Serializable {
    /**
     * Serializable property determining object version
     */
    static final long serialVersionUID = 1;
    /**
     * Height of a board
     */
    private int boardHeight;
    /**
     * Width of a board
     */
    private int boardWidth;
    /**
     * Speed amplification
     */
    private int speedAmp;
    /**
     * Score needed for ending particular level
     */
    private int score;
    /**
     * Number of obstacles in level
     */
    private int obstacles;
    /**
     * {@link Color} table 
     */
    private Color[] colorsTable;

    /**
     * Parametrised constructor of a {@link Level}
     * @param boardHeight_ height of a board
     * @param boardWidth_ width of a board
     * @param speedAmp_ amplification of a shape
     * @param points points needed to end game
     * @param obst number of obstacles in level
     * @param table table of colors
     */
    public Level(int boardHeight_, int boardWidth_, int speedAmp_, int points, int obst,
                 Color[] table) {

        boardHeight = boardHeight_;
        boardWidth = boardWidth_;
        speedAmp = speedAmp_;
        score=points;
        obstacles=obst;
        colorsTable = table;
    }

    /**
     * Getter for height
     * @return height of a board
     */
    public int getBoardHeight() {
        return boardHeight;
    }

    /**
     * Getter for width
     * @return width of a board
     */
    public int getBoardWidth() {
        return boardWidth;
    }

    /**
     * Getter for score
     * @return score of a level
     */
    public int getScore() {
        return score;
    }

    /**
     * Getter for number of obstacles
     * @return number of obstacles
     */
    public int getObstacles() {
        return obstacles;
    }
    public int getSpeedAmp() {
        return speedAmp;
    }
    public Color[] getColorsTable() {
        return colorsTable;
    }

    /**
     * Setter for Colors table background
     * @param a new Color
     */
    public void setColor(Color a) {
        colorsTable[0] = a;
    }

    /**
     * Returning colors table to string
     * @return String instance of Colors 
     */
    public String getColorsToString()
    {
        String returnable="";

        for (int i = 0; i < 8; i++) {
            String temp;
            Color c = colorsTable[i];
            temp = c.getRGB()+"_";
            returnable+=temp;
        }
        return returnable;
    }

    /**
     * Overridden toString method
     * @return String format of a Level
     */
    public String toNapis() {
        String returnable;
        returnable = boardHeight + "_"+boardWidth+"_"+speedAmp+"_"+score+"_"+obstacles+"_"+getColorsToString();
        return returnable;
    }

    public static void main(String[] args) {

    }
}
