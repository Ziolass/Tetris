import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyListener;
import java.net.URL;

/**
 * Created by Michau on 12.06.2015.
 */
public class GamePanel extends  JPanel {
    /**
     * {@link JButton} instance used in making game's toolbar, allows to move back to initial screen
     */
    private  JButton back_Button;
    /**
     * {@link JButton} instance used in making game's toolbar, used for starting new game
     */
    private JButton newGameButton;
    /**
     * {@link JButton} instance used in making game's toolbar, used for showing game instructions
     */
    private JButton instructionsButton;
    /**
     * {@link JButton} instance used in making game's toolbar used for pausing the game
     */
    private JButton pauseButton;
    /**
     * {@link JPanel} instance used for storing game information
     */
    private JPanel rightPanel;
    /**
     * {@link JFrame} instance used for adjusting the size of a initial frame
     */
    private JFrame frame;
    /**
     * {@link JPanel} instance used for changing current view
     */
    private JPanel card;
    /**
     * {@link RXCardLayout} instance used for changing current view
     */
    RXCardLayout layout;
    /**
     * {@link Board} instance, where the game takes place
     */
    private Board board;

    /**
     * {@link Board} getter
     * @return Current board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Immediately starts the game
     */
    public void start() {
        board.start();
    }

    /**
     * Parametrised {@link GamePanel} constructor
     * @param b board in which the game will take place
     * @param cardpanel {@link CardLayout} element
     * @param rxCardLayout {@link RXCardLayout} current layout
     * @param fr currently used {@link JFrame}
     */
    public GamePanel(Board b,JPanel cardpanel, RXCardLayout rxCardLayout,JFrame fr) {

        try {
            // Set cross-platform Java L&F (also called "Metal")
            UIManager.setLookAndFeel(
                    UIManager.getCrossPlatformLookAndFeelClassName());
        }
        catch (Exception e) {
        }
        card=cardpanel;
        frame=fr;
        layout=rxCardLayout;
        board=b;
        init();
    }

    /**
     * Funtion initalising all {@link GamePanel} GUI elements
     */
    private void init() {

        try {
            final JToolBar toolBar = new JToolBar();
            toolBar.setBackground(Color.black);
            toolBar.setFloatable(false);
            toolBar.setRollover(true);
            back_Button = makeNavigationButton("Icons/arrow_left.png", "Back", "Go back to previous screen");
            back_Button.setFocusable(false);
            back_Button.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SoundController.play("Sounds/click.wav");
                    if (!board.checkIfPaused()) {
                        board.pause();
                    }

                    layout.show(card, "INIT");
                    SoundController.playContinously("Sounds/theme.wav");
                    frame.setSize(400, 300);

                }
            });

            newGameButton = makeNavigationButton("Icons/new_game.png", "New game", "Starts new game");
            newGameButton.setFocusable(false);
            newGameButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SoundController.play("Sounds/click.wav");
                    start();
                    revalidate();
                }
            });
            instructionsButton = makeNavigationButton("Icons/info.png", "Instruction", "Game instructions");
            instructionsButton.setFocusable(false);
            instructionsButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    SoundController.play("Sounds/click.wav");
                    if (!board.checkIfPaused()) {
                        board.pause();
                    }
                    layout.show(card, "INSTRUCTIONS");
                    frame.setSize(450, 300);


                }
            });

            pauseButton = makeNavigationButton("Icons/pause.png", "Pause", "Pause game");
            pauseButton.setFocusable(false);
            pauseButton.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    board.pause();
                    //SoundController.play("Sounds/click.wav");
                }
            });


            rightPanel = board.getRightPanel();


            toolBar.add(back_Button);
            toolBar.addSeparator();
            toolBar.add(newGameButton);
            toolBar.addSeparator();
            toolBar.add(instructionsButton);
            toolBar.addSeparator();
            toolBar.add(pauseButton);
            setLayout(new GridBagLayout());

            GridBagConstraints c = new GridBagConstraints();
            setBackground(Color.black);
            c.fill = GridBagConstraints.HORIZONTAL;
            c.gridx = 0;
            c.gridy = 0;
            c.weightx = 0;
            c.weighty = 0;
            c.gridwidth = 1;
            add(toolBar, c);

            c.gridwidth = 5;
            c.gridy = 1;
            c.weighty = c.weightx = 1;
            c.fill = GridBagConstraints.BOTH;
            add(board, c);
            c.gridwidth = 1;

            c.gridx = 8;
            c.weightx = 0;
            add(rightPanel, c);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    protected JButton makeNavigationButton(String path, String actionCommand, String toolTipText) {
        JButton button = null;
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(path));
             button = new JButton(icon);
            button.setActionCommand(actionCommand);
            button.setToolTipText(toolTipText);
        } catch (Exception e) {
            System.out.println(e);
        }
        return button;
    }
}
