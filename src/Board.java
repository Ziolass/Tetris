import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.event.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Random;

/**
 * Created by Michau on 05.04.2015.
 */

    public class Board extends JPanel implements ActionListener{
   // <editor-fold
    // TEST SITE
    /**
     * List of currently available levels
     */
    LevelList list;

    /**
     * Function used for updating LevelList
     * @param l New LevelList
     */
    public void updateList(LevelList l) {
    list = l;
    }

    /**
     * {@link Boolean} instance indicating whether the game is online or not
     */
    private boolean offfline;

    /**
     * Setter for offline property
     * @param value new values of offline;
     */
    public void setOffline(boolean value) {
        offfline=value;
    }
    String playerName;
    int level_count=3;
    private boolean loop = false;
    int currentLevel = 0;
    private boolean online = false;
    public void setOnline() {
        online = true;
    }
    int score_needed = 0;
    int numberOfObstacles = 0;
    InternetProtocol prot;
    NextShapePanel rightUpperPanel = null;
    CustomPanel rightMiddlePanel = null;
    JPanel rightLowerPanel = null;
    JPanel rightPanel = null;
    final int rightHeight = 6;
    final int rightWidth = 6;
    private Shapes [] rightShapes;

    private void makeGameMoreDifficult(int obstacles) {
         Random random = new Random();
        for (int i = 0; i < obstacles; i++) {
            int obstacleIndex = random.nextInt(19) + 1;
            boardOfShapes[obstacleIndex] = Shapes.Square;
        }
    }
    // </ TEST SITE
    /**
     * Instance storing current height of a board
     */
    private int BoardHeight ;

    /**
     * Instance storing current width of a board
     */
    private int BoardWidth ;

    /**
     * Instance storing current speed amplifier
     */
    private int speed_amp ;
    /**
     * Instance storing current vertical position of a shape
     */
    private int CurrentPositionX = 0;
    /**
     * Floating point instance storing current horizontal position of a shape
     */
    private double CurrentPositionY = 0;
    /**
     * The {@link Timer} instance, used for generating events for board
     */
    private Timer timer;
    /**
     * The {@link JLabel} instance used for showing current information about the game
     */
    private JLabel status;
    /**
     * The {@link Shape} instance used for storing currently used shape
     */
    private Shape CurrentShape;
    /**
     * The {@link Shape} instance used for storing next shape
     */
    private Shape NextShape;
    /**
     * The {@link Shapes} array used for storing dropped shapes
     */
    private Shapes [] boardOfShapes;
    /**
     * The {@link Color} array used for storing Tetris blocks colours
     */
    private Color [] colors;

    /**
     * Return current colors
     * @return Table of current colors
     */
    public Color [] getColors() {
        return colors;
    }
    public Color  BackgroundColor;

    /**
     * Returns current vertical position of a shape
     * @return Vertical position
     */
    public int getCurrentPosistionX(){return  CurrentPositionX;}

    public boolean checkIfPaused() {
        return Pause;
    }
    /**
     * Returns current horizontal position of a shape
     * @return Horizontal position
     */
    public double getCurrentPositionY() {return CurrentPositionY;}

    /**
     * Boolean instance used for determining whether the current shape
     * has finished falling
     */
    private boolean FallingFinished = false;
    /**
     * Boolean instance determining whether the game is on
     */
    private boolean HasStarted = false;
    /**
     * Boolean instance determining wheter the game is paused
     */
    private boolean Pause = false;
    /**
     * Integer instance used for storing number of
     * lines removed during current game
     */
    private int LinesRemoved = 0;

    private int score=0;
// </editor-fold
    /**
     * Estimates current width of a square by
     * dividing current window size by defined width of the board
     * @return Current width of a square
     */
    private int SquareWidth () {return (int) getSize().getWidth()/BoardWidth; }

    /**
     * Estimates current height of a square by
     * dividing current window size by defined height of the board
     * @return Current height of a square
     */
    private double SquareHeight() {return  getSize().getHeight()/BoardHeight;}

    /**
     * Checks what subtype of a shape is in the corresponding place
     * @param x vertical coordinate
     * @param y horizontal coordinate
     * @return Returns {@link Shapes} from corresponding place
     */
    private Shapes ShapeAtPlace (int x, double y) {return boardOfShapes[(int)(StrictMath.floor(y)*BoardWidth) +x];}

    /**
     * Default board constructor
     */
    public Board() {
        colors = new Color[] {new Color(0,0,0), new Color(255,11,40), new Color(232,176,37), new Color(239,255,53),
                new Color(21,232,46), new Color(40,255,251), new Color(52,40,255), new Color(156,124,255)};
        BoardHeight = 25;
        BoardWidth = 10;
        speed_amp = 16;

        initBoard();

    }

    /**
     * Parametric board constructor
     * Enables reading instances from a file
     * @param affirmation Boolean instance as a confirmation of choice
     */
    public Board(boolean affirmation) {

        BackgroundColor = new Color(128,128,128);
        LoadFromFile();
        initBoard();
    }
    // TEST SITE
    public Board(boolean off, boolean on) {
        offfline=true;
        online=false;
        currentLevel = 0;
        level_count = list.levelList.size();
        Level l = (Level)list.levelList.get(currentLevel);
        updateValues(l);
        reinitOnline(l);
        start();
    }
    public void playLoop() {
        loop=true;
        currentLevel =0;
        level_count = list.levelList.size();
        Level l = (Level)list.levelList.get(currentLevel);
        updateValues(l);
        reinitOnline(l);
        Pause=false;
        SoundController.playContinously("Sounds/background.wav");
        start();
    }
    public void updateValues(Level level) {
        BoardHeight = level.getBoardHeight();
        BoardWidth = level.getBoardWidth();
        speed_amp = level.getSpeedAmp();
        score_needed = level.getScore();
        numberOfObstacles = level.getObstacles();
        colors = level.getColorsTable();


    }
    // TEST SITE
    /**
     * "Creates" new piece
     *  randomly sets its shape
     *  place it halfway up
     *  If can't move anymore, ends the game
     */
    private void newPiece()  {

        if(NextShape.getShape()==Shapes.None){
            CurrentShape.SetRandom();
            NextShape.SetRandom();
        }

        else {
            CurrentShape.setShape(NextShape.getShape());
            NextShape.SetRandom();

        }
        rightUpperPanel.setShape(NextShape);
        CurrentPositionX = BoardWidth /2 +1;
        CurrentPositionY = BoardHeight - 1 + CurrentShape.minY();

        if(!Move(CurrentShape,CurrentPositionX,CurrentPositionY)) {
            CurrentShape.setShape(Shapes.None);
            timer.stop();
            HasStarted = false;

            SoundController.stop();
            try {
                SoundController.play("Sounds/end.wav");
                getname();
                if (playerName.equals("")) {
                    playerName = "Unnamed";
                }
                if(online){
                prot.setHighscore(score, currentLevel,playerName);}
            } catch (Exception e) {

                System.out.println(e);
            }

        }
    }

    public LevelList getLevelList() {
        return list;
    }
    private void reinitOnline(Level level) {
        CurrentShape = new Shape();
        NextShape = new Shape();
        timer = new Timer(10, this);
        score = 0;
        rightMiddlePanel.setScore(score+"");
        rightMiddlePanel.repaint();

        rightUpperPanel.updateColors(level.getColorsTable());

        clear();

    }
    /**
     * Initialise the board,
     * Creates new instance of {@link Timer}
     * Add KeyListener {@link Board.KeyboardAdapter}
     * Then "clears" the board preparing it for new game
     */
    private void initBoard() {
        list = new LevelList();
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        Border blackline = BorderFactory.createLineBorder(Color.black);
        Border compound;
        compound = BorderFactory.createCompoundBorder(
                raisedbevel, loweredbevel);
        this.setBorder(compound);

        CurrentShape = new Shape();
        NextShape = new Shape();
        setFocusable(true);
        status = new JLabel();

        rightPanel = new JPanel();
        rightPanel.setBorder(compound);
        rightPanel.setLayout(new GridLayout(3, 1));

        rightUpperPanel = new NextShapePanel(colors, NextShape);
        rightUpperPanel.setBackground(Color.gray);
        TitledBorder border_next;
        border_next = BorderFactory.createTitledBorder(
                blackline, " NEXT ");
        border_next.setTitleJustification(TitledBorder.CENTER);
        rightUpperPanel.setBorder(border_next);


        rightMiddlePanel = new CustomPanel(score);
        rightMiddlePanel.setBackground(Color.gray.brighter());
        TitledBorder border_score;
        border_score = BorderFactory.createTitledBorder(
                blackline, " SCORE ");
        border_score.setTitleJustification(TitledBorder.CENTER);
        rightMiddlePanel.setBorder(border_score);

        rightLowerPanel = new JPanel();
        rightLowerPanel.setBackground(Color.gray);
        TitledBorder border_low;
        border_low = BorderFactory.createTitledBorder(
                blackline, " INFO ");
        border_score.setTitleJustification(TitledBorder.CENTER);
        rightMiddlePanel.setBorder(border_score);


        rightPanel.add(rightUpperPanel);
        rightPanel.add(rightMiddlePanel);
        rightPanel.add(rightLowerPanel);

        boardOfShapes = new Shapes[BoardWidth * BoardHeight];
        rightShapes = new Shapes[rightHeight * rightWidth];

        timer = new Timer(10, this);
        score = 0;

        addKeyListener(new KeyboardAdapter());
        clear();
    }

    /**
     * Method used when event occur:
     * Tries to move piece down as long as it is possible
     * Then the piece is dropped
     */
    private void OneLineDown()
    {
        if(!Move(CurrentShape, CurrentPositionX,CurrentPositionY-SquareHeight()/(speed_amp*BoardHeight)))
        {
            pieceDropped();
            return;
        }
    }

    /**
     * Saves fallen pieces to corresponding place in array
     */
    private void pieceDropped() {
        for (int i = 0; i < 4; ++i) {
            int x = CurrentPositionX + CurrentShape.getXfromRow(i);
            double y = CurrentPositionY - CurrentShape.getYfromRow(i);
            boardOfShapes[(int)(StrictMath.floor(y)*BoardWidth) +x] = CurrentShape.getShape();
        }
        SoundController.play("Sounds/kick.wav");
        removeLines();
        if(!FallingFinished) {newPiece();}
    }
    @Override
    /**
     * Tell what to do after being given an ActionEvent
     * As long as the falling is not finished
     * moves piece down
     */
    public void actionPerformed(ActionEvent e ) {
        if(FallingFinished)
        {
            FallingFinished = false;
            newPiece();

        }
        else
        OneLineDown();


    }

    private void DropBlock() {
        double newY = CurrentPositionY;
        while (newY > 0) {
            if (!Move(CurrentShape, CurrentPositionX, newY - SquareHeight() / (speed_amp * BoardHeight)))
                break;

            --newY;
        }
        pieceDropped();
    }
    /**
     *
     * @param g {@link Graphics} instance
     * @param x horizontal coordinate
     * @param y vertical coordinate
     * @param shape currently served shape
     */
    private void DrawSquare(Graphics g, int x, double y, Shapes shape) {

        Graphics2D g2 = (Graphics2D) g;
        Color color = colors[shape.ordinal()];
        g2.setColor(color.brighter());
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

    private void paintGrid(Graphics gr) {
        int k;
        Graphics2D g2d = (Graphics2D) gr;
        g2d.setColor(this.getBackground().darker());
        int widthOfRow = SquareWidth();
        double heightOfRow =  SquareHeight();

        for (k=0; k<getHeight(); k++)
            g2d.draw(new Line2D.Double(0, k*heightOfRow, BoardWidth*widthOfRow, k*heightOfRow));

        for (k=0; k<getWidth(); k++)
            g2d.draw(new Line2D.Double(k*widthOfRow, 0, k*widthOfRow, BoardHeight*heightOfRow));

    }
    /**
     * Print both shapes saved into array
     * as well as the currently moving one
     * @param g {@link Graphics} instance
     */
    private void doDrawing(Graphics g) {

        rightUpperPanel.repaint();
        rightMiddlePanel.repaint();
        Dimension size = getSize();

        double boardTop =  size.getHeight() - BoardHeight * SquareHeight();

        for (int i = 0; i < BoardHeight; ++i) {
            for (int j = 0; j < BoardWidth; ++j) {
                Shapes piece = ShapeAtPlace(j, BoardHeight - i - 1);

                if (piece != Shapes.None) {
                    DrawSquare(g,0+j*SquareWidth(),boardTop+i*SquareHeight(),piece);
                }
            }
        }
        if(CurrentShape.getShape()!= Shapes.None) {
            for (int i = 0; i < 4; ++i) {

                int x = CurrentPositionX + CurrentShape.getXfromRow(i);
                double y = CurrentPositionY - CurrentShape.getYfromRow(i);
                DrawSquare(g, 0 + x * SquareWidth(),
                    boardTop + (BoardHeight - y - 1) * SquareHeight(),
                    CurrentShape.getShape());

            }
        }
        paintGrid(g);

    }


    /**
     * "Clears" the board by filling an array
     * with None Shapes
     */
    private void clear() {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i) {
            boardOfShapes[i] = Shapes.None;
        }
        for (int i = 0; i < rightWidth * rightHeight; ++i) {
            rightShapes[i] = Shapes.None;
        }
    }

    /**
     * Move piece to selected location as long as its possible
     * @param piece Piece of Shape we want to move
     * @param dx New vertical coordinate
     * @param dy New horizontal coordinate
     * @return True if possible to move. False otherwise
     */
    private boolean Move(Shape piece, int dx, double dy) {

        for(int i = 0; i< 4 ; i++) {
            int x = dx + piece.getXfromRow(i);
            double y = dy - piece.getYfromRow(i);

            if(x < 0 || x >=BoardWidth || y<0 || y>= BoardHeight) {return false;}
            if(ShapeAtPlace(x,y)!= Shapes.None) {return false;}
        }

        CurrentShape = piece;
        CurrentPositionX = dx;
        CurrentPositionY = dy;
        repaint();
        return true;
    }
    @Override
    /**
     * Overriden painting method
     * Calls drawing methos
     * Draws background
     */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        //Image scaledImage = img.getScaledInstance(getSize().width,getSize().height,Image.SCALE_SMOOTH);
       // g.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        doDrawing(g);
    }

    /**
     * Initiates gameplay
     * Starts timer
     * clears the board
     */
    public void start() {
        if(Pause) {return;}
        score = 0;
        HasStarted = true;
        FallingFinished = false;
        LinesRemoved = 0;
        clear();
        makeGameMoreDifficult(numberOfObstacles);
        newPiece();
        rightMiddlePanel.repaint();
        timer.start();
        SoundController.playContinously("Sounds/background.wav");
    }
    public void getname() {
        JTextField username = new JTextField();
        Object [] message = {
                "Username:", username
        };
        String [] temp = {"That's my name!","Call me that!"};
        int option = JOptionPane.showOptionDialog(null, message, "Give name", JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE,null,
                temp,"That's my name!");
        if (option == 0 ) {
            playerName =username.getText();
        }
        else {playerName =username.getText();}
    }
    /**
     * Pauses game if it is on
     */
    public void pause() {
        if(!HasStarted) {return;}
        Pause = !Pause;

        if (Pause) {
            timer.stop();
            SoundController.stop();
            JOptionPane.showMessageDialog(new JFrame(), "Game paused, click 'p' again to unpause.");
            rightMiddlePanel.setScore("PAUSE");
            status.setText("PAUSE");

        }
        else {
            timer.start();
            status.setText(String.valueOf(score));
            rightMiddlePanel.setScore("" + score);
            SoundController.carryOn();
        }
        repaint();

    }

    /**
     * Loads data from .properties file
     * Throws exception if interrupted
     */
    private  void LoadFromFile()  {
        java.util.Properties properties = new java.util.Properties();
        InputStream input = null;
        try {
            input = new FileInputStream("config0.properties");
            properties.load(input);
            String _height = properties.getProperty("height");
            String _width = properties.getProperty("width");
            String _speed_amp = properties.getProperty("speed_amp");
            String _score = properties.getProperty("score");
            String _obst = properties.getProperty("obstacles");
            String[] kolory = new String[7];
            for (int i = 0; i < 7; i++) {
                kolory[i] = properties.getProperty("kolor"+(i+1));

            }
            BoardHeight = Integer.parseInt(_height);
            BoardWidth = Integer.parseInt(_width);
            speed_amp = Integer.parseInt(_speed_amp);
            score_needed = Integer.parseInt(_score);
            numberOfObstacles = Integer.parseInt(_obst);
            colors = new Color[kolory.length+1];
            colors[0] = BackgroundColor;
            for (int i = 1; i < 8; i++) {

                    String[] temp = kolory[(i-1)].split(",");


                colors[i] = new Color(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),Integer.parseInt(temp[2]));
            }
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }
    public   void LoadAllLevelsFromMemory()  {
        java.util.Properties properties = new java.util.Properties();
        InputStream input = null;
        list = new LevelList();
        for (int j = 0; j < level_count; j++) {


            try {
                input = new FileInputStream("level"+j+".properties");
                properties.load(input);
                String _height = properties.getProperty("height");
                String _width = properties.getProperty("width");
                String _speed_amp = properties.getProperty("speed_amp");
                String _score = properties.getProperty("score");
                String _obst = properties.getProperty("obstacles");
                String[] kolory = new String[7];
                for (int i = 0; i < 7; i++) {
                    kolory[i] = properties.getProperty("kolor" + (i + 1));

                }


                colors = new Color[kolory.length + 1];
                colors[0] = BackgroundColor;
                for (int i = 1; i < 8; i++) {

                    String[] temp = kolory[(i - 1)].split(",");


                    colors[i] = new Color(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
                }
                Level temp = new Level(Integer.parseInt(_height), Integer.parseInt(_width), Integer.parseInt(_speed_amp),
                      Integer.parseInt(_score),Integer.parseInt(_obst),  colors);
                list.levelList.add(temp);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

    }
    private void removeLast() {
        for (int i = 0; i < BoardWidth; ++i) {
            boardOfShapes[i] = Shapes.None;
        }

        for (int k = 0; k < BoardHeight - 1; ++k) {
            for (int j = 0; j < BoardWidth; ++j) {
                boardOfShapes[(k * BoardWidth) + j] = ShapeAtPlace(j, k + 1);
            }
        }
    }
    /**
     * Removes full lines od Shapes
     */
    private void removeLines() {
        int fullLines = 0;
        for (int i = BoardHeight - 1; i >= 0; i--) {
            boolean linefull = true;
            for (int j = 0; j < BoardWidth; ++j) {
                if (ShapeAtPlace(j,i) == Shapes.None) {
                    linefull = false;
                    break;
                }
            }
            if (linefull) {
                ++fullLines;
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j) {
                        boardOfShapes[(k * BoardWidth) + j] = ShapeAtPlace(j, k + 1);
                    }
                }
                }
            }
            if (fullLines > 0) {

                LinesRemoved += fullLines;
                score+= (int)(StrictMath.pow(2,fullLines)*100);

                status.setText(String.valueOf(score));
                rightMiddlePanel.setScore(""+score);
                FallingFinished = true;
                CurrentShape.setShape(Shapes.None);
                repaint();
                SoundController.play("Sounds/clear.wav");

            }
        // TEST SITE
        if (score >= score_needed) {
            timer.stop();
            JOptionPane.showMessageDialog(null, "LEVEL "+ (currentLevel+1)+" CLEARED!");


            if(currentLevel<level_count-1) {
                currentLevel++;

                JOptionPane.showMessageDialog(null, "GET READY FOR LEVEL "+(currentLevel+1));
                if (loop) {
                    Level l = (Level)list.levelList.get(currentLevel);
                    updateValues(l);
                    reinitOnline(l);
                    start();
                }
                else if (online) {
                    try {

                        Level l = prot.getLev(currentLevel);
                        list.levelList.add(l);
                        updateValues(l);
                        reinitOnline(l);
                        start();
                    } catch (Exception e) {
                        System.out.println(e);
                    }
                }

                else{

                try {
                    Level l = (Level) list.levelList.get(currentLevel);
                    updateValues(l);
                    reinitOnline(l);
                    start();
                } catch (Exception e) {
                    System.out.println(e);
                }
                }
            }

            else {

                SoundController.stop();
                SoundController.play("Sounds/win.wav");
                int tempscore = (currentLevel+1)*1000 + score;
                JOptionPane.showMessageDialog(null,"Congratulations, you won\n" +
                        "Your score is: "+tempscore);
                if(online)
                {
                try {
                    getname();
                    if (playerName.equals("")) {
                        playerName = "Unnamed";
                    }
                    prot.setHighscore(score, currentLevel, playerName);
                } catch (Exception e) {
                    System.out.println(e);
                }
                }
            }
        }
        }

    /**
     * Method called when power-up is being used, change current shape to next one if possible
     */
    public void changeShape() {
        if(score > 199){
        CurrentShape.setShape(NextShape.getShape());
        NextShape.SetRandom();
        rightUpperPanel.setShape(NextShape);
            score-=200;
        rightMiddlePanel.setScore(""+score);
        SoundController.play("Sounds/change.wav");}
        else {SoundController.play("Sounds/wrong.wav");}
    }

    /**
     * Method called before setting up online game
     * @param pro {@link InternetProtocol} instance used for communicating with Serwer
     * @param current current level's number
     * @param ifonline boolean if it is online
     * @param l tldr
     */
    public void prepareForOnline(InternetProtocol pro, int current, boolean ifonline, LevelList l) {
        list = l;
        prot = pro;
        currentLevel = current;
        online = ifonline;
    }

    /**
     * Getter for right panel
     * @return Right panel
     */
    public JPanel getRightPanel() {
        return rightPanel;
    }

    /**
     * Getter for status label
     * @return Status label
     */
    public JLabel getStatus() {
        return status;
    }

    /**
     * Inner class providing
     * Key input from keyboard
     */
    class KeyboardAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent event) {
            if (!HasStarted) {
                return;
            }
            int keycode = event.getKeyCode();
            if(keycode =='p' || keycode == 'P'){
                pause();
                return;
            }
            if(Pause) {return;}

            switch (keycode) {

                case KeyEvent.VK_RIGHT:
                    Move(CurrentShape, CurrentPositionX + 1, CurrentPositionY);
                    break;

                case KeyEvent.VK_LEFT:
                    Move(CurrentShape, CurrentPositionX - 1, CurrentPositionY);
                    break;

                case KeyEvent.VK_UP:
                    Move(CurrentShape.RotateLeft(), CurrentPositionX, CurrentPositionY);
                    SoundController.play("Sounds/rotate.wav");
                    break;

                case KeyEvent.VK_DOWN:
                    Move(CurrentShape.RotateRight(), CurrentPositionX, CurrentPositionY);
                    SoundController.play("Sounds/rotate.wav");
                    break;
                case KeyEvent.VK_SPACE:
                    DropBlock();
                    break;
                case  KeyEvent.VK_C:
                    changeShape();
                    break;
                case KeyEvent.VK_E:
                    removeLast();
                    break;

            }
        }
    }

}
