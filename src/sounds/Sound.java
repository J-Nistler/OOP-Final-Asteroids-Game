package sounds;

import java.io.BufferedInputStream;
import java.io.IOException;
import javax.sound.sampled.*;

public class Sound
{
    /** Sound for Alien ship */
    private Clip bangAlienShip;

    /** Sound for large band */
    private Clip bangLarge;

    /** Sound for medium band */
    private Clip bangMedium;

    /** Sound for Ship band */
    private Clip bangShip;

    /** Sound for small bang */
    private Clip bangSmall;

    /** Sound for first beat */
    private Clip beat1;

    /** Sound for Second beat */
    private Clip beat2;

    /** Sound for fire */
    private Clip fire;

    /** Sound for big Saucer */
    private Clip saucerBig;

    /** Sound for Small Saucer */
    private Clip saucerSmall;

    /** Sound for Thrust */
    private Clip thrust;

    /**
     * Creating a sound object
     */
    public Sound ()
    {
        // Creating Sounds
        bangAlienShip = createClip("/sounds/bangAlienShip.wav");
        bangLarge = createClip("/sounds/bangLarge.wav");
        bangMedium = createClip("/sounds/bangMedium.wav");
        bangShip = createClip("/sounds/bangShip.wav");
        bangSmall = createClip("/sounds/bangSmall.wav");
        beat1 = createClip("/sounds/beat1.wav");
        beat2 = createClip("/sounds/beat2.wav");
        saucerBig = createClip("/sounds/saucerBig.wav");
        fire = createClip("/sounds/fire.wav");
        saucerSmall = createClip("/sounds/saucerSmall.wav");
        thrust = createClip("/sounds/thrust.wav");
        saucerSmall = createClip("/sounds/saucerSmall.wav");
    }

    /**
     * Creates an audio clip from a sound file.
     */
    public Clip createClip (String soundFile)
    {
        // Opening the sound file this way will work no matter how the
        // project is exported. The only restriction is that the
        // sound files must be stored in a package.
        try (BufferedInputStream sound = new BufferedInputStream(getClass().getResourceAsStream(soundFile)))
        {
            // Create and return a Clip that will play a sound file. There are
            // various reasons that the creation attempt could fail. If it
            // fails, return null.
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(sound));
            return clip;
        }
        catch (LineUnavailableException e)
        {
            return null;
        }
        catch (IOException e)
        {
            return null;
        }
        catch (UnsupportedAudioFileException e)
        {
            return null;
        }
    }

    // SOUNDS FOR THE GAME
    public void playBangAlienShipNoise ()
    {

        if (bangAlienShip != null)
        {
            if (bangAlienShip.isRunning())
            {
                bangAlienShip.stop();
            }
            bangAlienShip.setFramePosition(0);
            bangAlienShip.start();
        }
    }

    public void playBangLargeNoise ()
    {

        if (bangLarge != null)
        {
            if (bangLarge.isRunning())
            {
                bangLarge.stop();
            }
            bangLarge.setFramePosition(0);
            bangLarge.start();
        }
    }

    public void playBangMediumNoise ()
    {

        if (bangMedium != null)
        {
            if (bangMedium.isRunning())
            {
                bangMedium.stop();
            }
            bangMedium.setFramePosition(0);
            bangMedium.start();
        }
    }

    public void playBangSmallNoise ()
    {

        if (bangSmall != null)
        {
            if (bangSmall.isRunning())
            {
                bangSmall.stop();
            }
            bangSmall.setFramePosition(0);
            bangSmall.start();
        }
    }

    public void playBangShipNoise ()
    {

        if (bangShip != null)
        {
            if (bangShip.isRunning())
            {
                bangShip.stop();
            }
            bangShip.setFramePosition(0);
            bangShip.start();
        }
    }

    public void playBeat1Noise ()
    {

        if (beat1 != null)
        {
            if (beat1.isRunning())
            {
                beat1.stop();
            }
            beat1.setFramePosition(0);
            beat1.start();
        }
    }

    public void playBeat2Noise ()
    {

        if (beat2 != null)
        {
            if (beat2.isRunning())
            {
                beat2.stop();
            }
            beat2.setFramePosition(0);
            beat2.start();
        }
    }

    public void playFireNoise ()
    {

        if (fire != null)
        {
            if (fire.isRunning())
            {
                fire.stop();
            }
            fire.setFramePosition(0);
            fire.start();
        }
    }

    @SuppressWarnings("static-access")
    public void playSaucerBig ()
    {

        if (saucerBig != null)
        {
            if (saucerBig.isRunning())
            {
                saucerBig.stop();
            }
            saucerBig.setFramePosition(0);
            saucerBig.loop(saucerBig.LOOP_CONTINUOUSLY);
        }
    }

    public void stopBigSaucer ()
    {
        if (saucerBig != null && saucerBig.isRunning())
        {
            saucerBig.stop();
        }
    }

    @SuppressWarnings("static-access")
    public void playSmallSaucer ()
    {

        if (saucerSmall != null)
        {
            if (saucerSmall.isRunning())
            {
                saucerSmall.stop();
            }
            saucerSmall.setFramePosition(0);
            saucerSmall.loop(saucerSmall.LOOP_CONTINUOUSLY);
        }
    }

    public void stopSaucerNoise ()
    {
        if (saucerSmall != null && saucerSmall.isRunning())
        {
            saucerSmall.stop();
        }
    }

    @SuppressWarnings("static-access")
    public void playThrustClip ()
    {
        if (thrust != null)
        {
            if (thrust.isRunning())
            {
                thrust.stop();
            }
            thrust.setFramePosition(0);
            thrust.loop(thrust.LOOP_CONTINUOUSLY);
        }
    }

    public void stopThrustNoise ()
    {
        if (thrust != null && thrust.isRunning())
        {
            thrust.stop();
        }
    }
}
