import javax.sound.sampled.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * Created by Michau on 16.04.2015.
 */
public class SoundController  {
    /**
     * static {@link Boolean}instance determining whether the background music is running or not
     */
   static boolean isrunning = false;

    /**
     * Parametrised constructor for SoundController
     * @param url path to file
     */
    public  SoundController(final String url) {
        try {
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(SoundController.class.getResourceAsStream(url));

            clip.open(inputStream);
        } catch (Exception e) {

        }
    }
    static Clip continousClip;

    /**
     * Plays a sound
     * @param url path to file
     */
    public static synchronized void  play(final String url) {
        // each clip is played by separate thread in synchronized block on purpose of non blocking the sound medium
        new Thread(new Runnable() {
            public void run() {
        try {
            // obtaining clip from system memory
            Clip clip = AudioSystem.getClip();
            AudioInputStream inputStream = AudioSystem.getAudioInputStream(SoundController.class.getResource(url));

            clip.open(inputStream);
            clip.start();
        } catch (Exception e) {
            System.out.println(e);
        }
            }
        }).start();

    }

    /**
     * Play in the back
     * @param url path to file
     */
    public static synchronized void  playContinously(final String url) {
        new Thread(new Runnable() {
            public void run() {
                if(isrunning)
                {
                    // if the clip is already running there's no need to open it once more
                    return;
                }
                try {

                    // obtaining clip from system memory
                    continousClip = AudioSystem.getClip();
                    AudioInputStream inputStream = AudioSystem.getAudioInputStream(SoundController.class.getResource(url));
                    continousClip.open(inputStream);
                    continousClip.loop(Clip.LOOP_CONTINUOUSLY);
                    isrunning = true;
                } catch (Exception e ) {
                    System.out.println(e);
                }

            }
        }).start();

    }

    public static synchronized void carryOn() {
        continousClip.setFramePosition(0);
        continousClip.start();
    }
    /**
     * Static synchronized method used for stoping background music
     */
    public static synchronized void stop() {
        continousClip.stop();

        isrunning=false;
    }

    public static void main(String[] args) {

    }
}
