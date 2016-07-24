import sun.applet.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Michau on 10.05.2015.
 */
public class MainView extends JFrame {
    private boolean loop = false;
    private boolean loggedIn = false;
    private InternetProtocol protocol;
    private boolean gameon = false;
    private boolean online = false;
    private Board board;
    private JPanel card = null;
    private JPanel initPanel = null;
    private CustomPanel aboutPanel = null;
    private GamePanel gamePanel = null;
    private JPanel loginPanel = null;
    private CustomPanel instructionPanel = null;
    private JPanel internetPanel = null;
    private LevelList list = null;

    public MainView() {
        init();
        SoundController.playContinously("Sounds/theme.wav");

    }
    public void init() {
        list = new LevelList();
        protocol = new InternetProtocol();
        // ******************* SETTING FRAME OPTIONS ***********************
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension dim = new Dimension(400, 300);
        setPreferredSize(dim);

        board = new Board(true);
        // ******************* SETTING MAIN LAYOUT **************************
        card = new JPanel(new RXCardLayout());
        initPanel = new CustomPanel("src/Images/frame_back.jpeg");
        initPanel.setLayout(new GridBagLayout());
        loginPanel = new JPanel();
        internetPanel = new JPanel();
        instructionPanel = new CustomPanel("src/Images/instructions.jpg");
        aboutPanel = new CustomPanel("src/Images/about.png");
        aboutPanel.setLayout(new GridBagLayout());
        RXCardLayout cardLayout = (RXCardLayout) card.getLayout();
        gamePanel = new GamePanel(board, card, cardLayout, this);
        // ******************** CARD INITIALISING **************************
        card.add("INIT", initPanel);
        card.add("ABOUT", aboutPanel);
        card.add("LOGIN", loginPanel);
        card.add("GAME", gamePanel);
        card.add("INTERNET", internetPanel);
        card.add("INSTRUCTIONS", instructionPanel);
        //
        add(card);

        cardLayout.show(card, "INIT");


        // ************************* INITIAL PANEL *************************
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        CustomButton online_button = new CustomButton("Online play");
        online_button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (loggedIn) {
                    cardLayout.show(card, "INTERNET");
                } else {
                    cardLayout.show(card, "LOGIN");
                }

            }
        });

        initPanel.add(online_button, gbc);

        CustomButton offline_button = new CustomButton("Offline play");
        offline_button.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {

                SoundController.stop();
                if(!gameon) {
                    gamePanel.getBoard().LoadAllLevelsFromMemory();
                    Level level = (Level) gamePanel.getBoard().getLevelList().levelList.get(0);

                    gamePanel.getBoard().updateValues(level);

                    setSize(300, 400);
                    loop = true;
                    gameon = true;
                    cardLayout.show(card, "GAME");

                    gamePanel.start();
                }

                else {cardLayout.show(card, "GAME");
                    setSize(300, 400);
                }
                }

        });
        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);
        initPanel.add(offline_button, gbc);

        CustomButton option_about = new CustomButton("About");
        option_about.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(card, "ABOUT");

            }
        });
        gbc.gridy++;
        gbc.insets = new Insets(10, 0, 0, 0);
        initPanel.add(option_about, gbc);
        // ************************* INITIAL PANEL *************************


        // ********************** LOGIN PANEL *******************************
        loginPanel.setBackground(Color.darkGray);
        loginPanel.setVisible(false);

        //loginPanel.setLayout(new GridLayout(4, 2, 20, 20));
        loginPanel.setLayout(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0;
        g.gridy = 0;
        JTextField login = new JTextField();
        JLabel login_d = new JLabel("Login");
        login_d.setForeground(Color.white);

        JTextField passwordField = new JPasswordField();
        JLabel pass_d = new JLabel("Password");
        pass_d.setForeground(Color.white);
        g.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(login_d, g);
        g.gridx = 1;
        loginPanel.add(login, g);
        g.gridx = 0;
        g.gridy = 1;
        loginPanel.add(pass_d, g);
        g.gridx = 1;
        loginPanel.add(passwordField, g);

        CustomButton back = new CustomButton("Back");
        CustomButton confirm = new CustomButton("Confirm");

        JLabel desc = new JLabel("Attempt will be made to connect server on port 4444");
        Font font = new Font("Arial", Font.ITALIC, 10);
        desc.setForeground(Color.white);
        desc.setFont(font);
        g.gridwidth = 2;
        g.gridx = 0;
        g.gridy = 2;
        loginPanel.add(desc, g);

        JButton register = new JButton("Don't have an account yet? Register");
        // making borderless, "transparent" button
        register.setOpaque(false);
        register.setContentAreaFilled(false);
        register.setBorderPainted(false);
        register.setFocusable(false);
        register.setForeground(Color.white);
        //
        g.gridy = 3;

        register.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField address = new JTextField();
                Object [] mes = {"Host address:",address};
                String host="localhost";
                int op = JOptionPane.showConfirmDialog(null, mes, "HOST", JOptionPane.OK_CANCEL_OPTION);

                if (op == JOptionPane.OK_OPTION)
                {
                    host = address.getText();
                }
                else {return;}

                protocol.InternetProtocolInit(host);
                if (protocol.getClientSocket() == null) {
                    JOptionPane.showMessageDialog(null, "Connection with server could not be established\n" +
                            "Please try again later");
                    return;
                }
                login.setText("");
                passwordField.setText("");
                JTextField username = new JTextField();
                JTextField password = new JTextField();
                Object[] message = {
                        "Username:", username,
                        "Password:", password
                };
                int option = JOptionPane.showConfirmDialog(null, message, "REGISTER", JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    if (username.getText().equals("") || password.getText().equals("")) {
                        JOptionPane.showMessageDialog(null, "Neither username, nor password can be nullified\n" +
                                "Please fill in all the boxes");
                        return;

                    }
                    if (username.getText().contains("_") || password.getText().contains("_")) {
                        JOptionPane.showMessageDialog(null, "Neither username, nor password may contain '_' symbol\n" +
                                "Please make sure none of them contain that symbol and try again");
                        return;
                    }
                    if (protocol.userRegister(username.getText(), password.getText()).getB()) {
                        JOptionPane.showMessageDialog(null, "User " + username.getText() + " successfully registered.");
                        return;
                    } else {
                        JOptionPane.showMessageDialog(null, "There is already someone with that name\n" +
                                "Try another one.");
                        return;
                    }
                } else {
                    return;
                }
            }
        });
        loginPanel.add(register, g);

        back.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login.setText("");
                passwordField.setText("");
                cardLayout.show(card, "INIT");
            }
        });
        g.gridwidth = 1;
        g.gridy = 4;

        loginPanel.add(back, g);

        confirm.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField address = new JTextField();
                Object [] mes = {"Host address:",address};
                String host="localhost";
                int op = JOptionPane.showConfirmDialog(null, mes, "HOST", JOptionPane.OK_CANCEL_OPTION);

                if (op == JOptionPane.OK_OPTION)
                {
                    host = address.getText();
                }
                else {return;}

                protocol.InternetProtocolInit(host);
                if (protocol.getClientSocket() == null) {
                    JOptionPane.showMessageDialog(null, "Connection with server could not be established\n" +
                            "Please try again later");
                    return;
                }
                String user = login.getText();
                String pass = passwordField.getText();

                if (user.contains("_") || pass.contains("_")) {
                    JOptionPane.showMessageDialog(null, "Neither username, nor password may contain '_' symbol\n" +
                            "Please make sure none of them contain that symbol and try again");
                    return;
                }

                Returnable r = protocol.userVerification(user, pass);
                if (r.getB()) {
                    cardLayout.show(card, "INTERNET");
                    loggedIn = true;
                    login.setText("");
                    passwordField.setText("");
                } else {
                    if (r.getE() != null) {
                        JOptionPane.showMessageDialog(null, "Verification process is unavailable at the moment\n" +
                                "due to an error:\n" + r.getE().toString() + "" +
                                "\nPlease try again later.");
                        return;
                    }
                    JOptionPane.showMessageDialog(null, "Provided credentials are not valid,\nplease try again.");
                }
            }
        });
        g.gridx = 1;
        loginPanel.add(confirm, g);
        // ********************** LOGIN PANEL *******************************

        // ********************** ONLINE PANEL ****************************
        internetPanel.setLayout(new GridLayout(3, 1));
        online = true;
        CustomButton onlineToMainButton = new CustomButton("BACK");
        onlineToMainButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(card, "INIT");

            }
        });
        CustomButton highscore = new CustomButton("SHOW HIGHSCORE");
        highscore.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String temp = protocol.getHighscores();
                String[] ranks = temp.split("_");

                JPanel scores = new JPanel();
                scores.setLayout(new BoxLayout(scores, BoxLayout.Y_AXIS));

                for (String s : ranks) {
                    JLabel a = new JLabel(s);
                    scores.add(a);
                }
                JOptionPane.showMessageDialog(null, scores);
            }
        });

        CustomButton getlevels = new CustomButton("GET LEVELS AND PLAY");
        getlevels.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                 if (loop) {
                     board.playLoop();
                     setSize(300, 400);
                     SoundController.stop();
                     cardLayout.show(card, "GAME");

                 }
                else{
                Level level = new Level(1, 1, 1, 1, 1, null);
                level = protocol.getLev(0);
                list.levelList.add(level);
                gamePanel.getBoard().prepareForOnline(protocol, 0, true,list);
                gamePanel.getBoard().updateValues(level);

                SoundController.stop();
                setSize(300, 400);
                loop = true;
                cardLayout.show(card, "GAME");
                gamePanel.start();}

            }

        });
        internetPanel.add(highscore);
        internetPanel.add(getlevels);
        internetPanel.add(onlineToMainButton);
        // ********************** ONLINE PANEL ****************************

        // ********************** ABOUT PANEL ****************************
        aboutPanel.setBackground(Color.gray.darker());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.gridx = 0;
        gbc2.gridy = 0;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        gbc2.anchor = GridBagConstraints.CENTER;
        // button with hyperlink
        CustomButton hyperlinkButton = new CustomButton("PROZE homepage");
        hyperlinkButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Desktop.isDesktopSupported()) {
                    try {
                        final URI uri = new URI("http://ztv.ire.pw.edu.pl:8080/proze/");
                        Desktop.getDesktop().browse(uri);
                    } catch (Exception ex) {
                        System.out.println(ex);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Browser could not be opened " +
                            "due to an error.\nPlease try again later. ");
                }
            }
        });
        aboutPanel.add(hyperlinkButton, gbc2);

        CustomButton backToMainButton = new CustomButton("Back");
        backToMainButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(card, "INIT");

            }
        });
        gbc2.gridy++;
        gbc2.insets = new Insets(10, 0, 0, 0);
        aboutPanel.add(backToMainButton, gbc2);

        // ********************** ABOUT PANEL ****************************

        // ********************** OFFLINE GAME ***************************

        // nothing to write really, took care of all of it in GamePanel

        // ********************** OFFLINE GAME ***************************

        // ********************** INSTRUCTION PANEL ***************************
        CustomButton backToGameView = new CustomButton("Back");
        backToGameView.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setSize(300, 400);
                cardLayout.show(card, "GAME");
            }
        });
        instructionPanel.setLayout(new BorderLayout());
        instructionPanel.add(backToGameView, BorderLayout.PAGE_END);
        // ********************** INSTRUCTION PANEL ***************************
        pack();

    }

    public static void main(String[] args) {

        MainView main = new MainView();
        main.setLocationRelativeTo(null);
        main.setVisible(true);
    }

    }
