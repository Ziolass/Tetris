import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Michau on 10.05.2015.
 */
public class InternetProtocol {

    private Socket clientSocket;

    private PrintWriter printWriter;
    private BufferedReader reader;

public Socket getClientSocket() {
    return clientSocket;
}
    public void InternetProtocolInit(String host) {

        try {
            clientSocket=new Socket(host, 4444);
            printWriter=new PrintWriter(clientSocket.getOutputStream(), true);
            reader=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.println("Connection established");
        } catch (Exception e) {
            System.out.println("Connection could not be established");
        }

    }
    public  String getHighscores() {
        String returnable="";
        try {

            printWriter.println("SCORES");

            String input;
            while ((input = reader.readLine()) != null) {

                returnable=input;
                break;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return returnable;
    }

    /**
     * Set new highscore
     * @param score new score
     * @param lvl current level
     * @param name current name
     */
    public void setHighscore(int score,int lvl,String name) {
        try {
            int finalscore = (lvl+1)*1000 + score;
            printWriter.println("SET" +"_"+name+"_"+finalscore);

        } catch (Exception e) {

        }
    }

    public Level getLev(int index) {

        String output = "GET_" + index;
        String returnable = null;
        // test site

        try {

            printWriter.println(output);
            String input;

            while ((input = reader.readLine()) != null) {

                returnable = input;
                break;
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        String temp [] = returnable.split("_");
        Color[] table = new Color[8];
        for (int i = 0; i < 8; i++) {
            String temporable = temp[5+i];
            Color color = new Color(Integer.parseInt(temporable));
            table[i] = color;
        }
        int height = Integer.parseInt(temp[0]);
        int width = Integer.parseInt(temp[1]);
        int amp = Integer.parseInt(temp[2]);
        int score = Integer.parseInt(temp[3]);
        int osbt = Integer.parseInt(temp[4]);
        Level a = new Level(height, width, amp, score, osbt, table);
        return a;

    }
    /**
     * Method used after successful veryfication
     * used for obtaining all available levels from server
     * @return Returns instance of {@link LevelList} which contains levels from server
     */
    public  LevelList getLevels() {
        try
        {
            // String instances, will be used for storing server and client messages

           String output = "ALL";
            printWriter.println(output);


            // after sending GET_LEVEL flag
            // opening Objectinputstream for incoming serialized LevelList
            java.io.InputStream is = clientSocket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(is);
            LevelList list = (LevelList)objectInputStream.readObject();
            return list;

        } catch (UnknownHostException e) {
            System.out.println("There's no such host");
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    /**
     * Main client method, used for verificating user in server
     * @param login user login
     * @param password user password
     * @return Returns {@link Boolean} value depending on whether the user exist in server memory or not
     */
    public  Returnable userVerification(String login, String password) {

        Returnable returnable = new Returnable();
        try {
            // String instances that will be used for storing server input and client output
            String input, output;
            output = login + "_" + password;
            printWriter.println(output);
            while ((input = reader.readLine()) != null) {
                System.out.println(input);

                switch (input) {

                    case "WRONG": {
                        returnable.setB(false);
                        return returnable;

                    }
                    case "CORRECT":
                       // output = "BYE";
                     //   pw.println(output);
                        returnable.setB(true);
                        return returnable;
                }
                printWriter.println(output);
            }

        } catch (Exception e) {
            returnable.setE(e);
            System.out.println(e);
        }
        returnable.setB(false);
        return returnable;
    }
    public int levelCount() {

        int returnable=0;

        try {
            // String instances that will be used for storing server input and client output
            String input, output;
            output = "QUANTITY";
            printWriter.println(output);


            while ((input = reader.readLine()) != null) {

                returnable=Integer.parseInt(input);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return returnable;
    }
    public Returnable userRegister(String name, String password) {
        Returnable returnable = new Returnable();
        try {
            // String instances that will be used for storing server input and client output
            String input, output;
            output = "REGISTER" + "_" + name+"_"+password;
            printWriter.println(output);
            while ((input = reader.readLine()) != null) {

                System.out.println(input);

                switch (input) {

                    case "WRONG": {
                        returnable.setB(false);
                        returnable.setS("There is already user with that name\n" +
                                "Try another one");
                        return returnable;
                    }
                    case "CORRECT":
                        returnable.setB(true);
                        return returnable;
                }
            }

        } catch (Exception e) {
            returnable.setE(e);
            System.out.println(e);
        }
        returnable.setB(false);
        return returnable;
    }
}
