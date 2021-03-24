package asteroids.game;

import static asteroids.game.Constants.*;
import java.awt.event.*;
import java.util.Iterator;
import java.util.Random;
import javax.swing.*;
import asteroids.participants.Asteroid;
import asteroids.participants.Saucer;
import asteroids.participants.Ship;

/**
 * Controls a game of Asteroids.
 */
public class Controller implements KeyListener, ActionListener
{

    /** The alien ship (if one is active or null (otherwise) */
    private Saucer saucer;

    /** The state of all the Participants */
    private ParticipantState pstate;

    /** The ship (if one is active) or null (otherwise) */
    private Ship ship;

    /** When this timer goes off, it is time to refresh the animation */
    private Timer refreshTimer;

    /** Left rotation */
    private boolean isLeft;

    /** Right rotation */
    private boolean isRight;

    /** Move forward */
    private boolean isForward;

    /** Number of score */
    private int numScore;

    /** Number of level */
    private int numLevel;

    /** Timer for music */
    private Timer beats;

    /** Alternator for beats */
    private int altBeat;

    /** For bullets */
    private boolean isBullet;

    /** Shooting is present or not */
    private boolean shoot;

    /**
     * The time at which a transition to a new stage of the game should be made. A transition is scheduled a few seconds
     * in the future to give the user time to see what has happened before doing something like going to a new level or
     * resetting the current level.
     */
    private long transitionTime;

    /** Number of lives left */
    private int lives;

    /** The game display */
    private Display display;

    private Timer saucerTimer;

    /**
     * Constructs a controller to coordinate the game and screen
     */
    public Controller ()
    {
        // Initialize the ParticipantState
        pstate = new ParticipantState();

        // Set up the refresh timer.
        refreshTimer = new Timer(FRAME_INTERVAL, this);

        saucerTimer = new Timer((5 + RANDOM.nextInt(6)) * 1000, this);

        // Beats Time
        beats = new Timer(INITIAL_BEAT, this);

        // Clear the transitionTime
        transitionTime = Long.MAX_VALUE;

        // Record the display object
        display = new Display(this);

        // Bring up the splash screen and start the refresh timer
        splashScreen();
        // Set the GUI to appear
        display.setVisible(true);
        // Starts the "Frame rate" of the game
        refreshTimer.start();
    }

    /**
     * Returns the ship, or null if there isn't one
     */
    public Ship getShip ()
    {
        // Returns the ship
        return ship;
    }

    public Saucer getAlienShip ()
    {
        return saucer;
    }

    /**
     * Configures the game screen to display the splash screen
     */
    private void splashScreen ()
    {
        // Clear the screen, reset the level, and display the legend
        clear();
        // Diplaying Asteriods during the splash screen
        display.setLegend("Asteroids");

        // Place four asteroids near the corners of the screen.
        placeAsteroids(1);

    }

    /**
     * The game is over. Displays a message to that effect.
     */
    private void finalScreen ()
    {
        if (saucerTimer.isRunning())
        {
            saucerTimer.stop();
        }
        // Displayed when lives = 0
        display.setLegend(GAME_OVER);
        display.removeKeyListener(this);
        Participant.getSounds().stopSaucerNoise();
    }

    /**
     * Place a new ship in the center of the screen. Remove any existing ship first.
     */
    private void placeShip ()
    {
        // Place a new ship
        Participant.expire(ship);
        ship = new Ship(SIZE / 2, SIZE / 2, -Math.PI / 2, this);
        addParticipant(ship);
        display.setLegend("");

        // Setting all the booleans to false and movements to false
        isLeft = false;
        isRight = false;
        isForward = false;
    }

    /**
     * Places an asteroid near one corner of the screen. Gives it a random velocity and rotation.
     */
    private void placeAsteroids (int level)
    {
        // Adding asteroids making them random and going in random directions
        addParticipant(new Asteroid(0, 2, EDGE_OFFSET, EDGE_OFFSET, MAXIMUM_LARGE_ASTEROID_SPEED, this));
        addParticipant(new Asteroid(1, 2, SIZE - EDGE_OFFSET, EDGE_OFFSET, MAXIMUM_LARGE_ASTEROID_SPEED, this));
        addParticipant(new Asteroid(2, 2, EDGE_OFFSET, SIZE - EDGE_OFFSET, MAXIMUM_LARGE_ASTEROID_SPEED, this));
        addParticipant(new Asteroid(3, 2, SIZE - EDGE_OFFSET, SIZE - EDGE_OFFSET, MAXIMUM_LARGE_ASTEROID_SPEED, this));
        for (int i = 1; i < level; i++)
        {
            addParticipant(new Asteroid(new Random().nextInt(4), 2, new Random().nextInt(5) * EDGE_OFFSET,
                    new Random().nextInt(5) * EDGE_OFFSET, MAXIMUM_LARGE_ASTEROID_SPEED, this));
        }
    }

    /**
     * Updates the level for the game
     */
    private void nextLevel (int level)
    {
        // Setting the level
        display.setLevel(level);
        // Making sure the legend does not display anything on the screen when a new level starts
        display.setLegend("");
        // Basic clean up up the board
        clear();
        // Placing asteroids based on levels
        placeAsteroids(level);
        // Centering the ship in the middle when the the level starts
        placeShip();

        saucerTimer.restart();

        // Stopping music since the level is just starting
        beats.stop();
        // This delays the timer so we can have time to clear everything and reset the screen to play the music again
        beats.setDelay(INITIAL_BEAT);
        // Starting the music again
        beats.start();
    }

    /**
     * Puts a limit on the bullets being fired
     */
    public boolean atBulletLimit (int bulletLimit)
    {
        if (this.pstate.countShipBullets() >= bulletLimit)
        {
            return true;
        }
        return false;
    }

    /**
     * Clears the screen so that nothing is displayed
     */
    private void clear ()
    {
        // Clearing the board
        pstate.clear();
        // Displaying nothing
        display.setLegend("");
        // Making sure the ship does not exist
        ship = null;
    }

    /**
     * Sets things up and begins a new game.
     */
    private void initialScreen ()
    {
        // Stops the Music for the beats
        beats.stop();
        // Delays the music for just a bit
        beats.setDelay(INITIAL_BEAT);
        // Starts up the music
        beats.start();

        // Clear the screen
        clear();

        // Place asteroids
        placeAsteroids(1);

        // Place the ship
        placeShip();

        // Reset stats for the game since these are private fields
        lives = 3;
        numLevel = 1;
        numScore = 0;

        // Changing the beats, this will affect the music in the actionPerformed method
        altBeat = 2;

        // Start listening to events (but don't listen twice)
        display.removeKeyListener(this);
        display.addKeyListener(this);

        // Give focus to the game screen
        display.requestFocusInWindow();

        // Settings the labels for lives, level, score
        display.setLives(lives);
        display.setLevel(numLevel);
        display.setScore(numScore);
    }

    /**
     * Adds a new Participant
     */
    public void addParticipant (Participant p)
    {
        pstate.addParticipant(p);
    }

    /**
     * The ship has been destroyed
     */
    public void shipDestroyed ()
    {
        // Null out the ship
        ship = null;

        // Display a legend
        display.setLegend("Ouch!");

        // Decrement lives
        lives--;

        // Ship destroyed noise
        Participant.getSounds().playBangShipNoise();

        // Stop beats Timer
        beats.stop();

        // Stop all noises
        Participant.getSounds().stopThrustNoise();

        Participant.getSounds().stopBigSaucer();

        Participant.getSounds().stopSaucerNoise();

        // Since the ship was destroyed, schedule a transition
        scheduleTransition(END_DELAY);
    }

    /**
     * Places a saucer depending on the level of the game
     */
    private void placeSaucer (int level)
    {

        Participant.expire(this.saucer);
        if (level > 1)
        {
            int saucerSize = level == 2 ? 1 : 0;
            this.saucer = new Saucer(saucerSize, this);
            this.saucer.setPosition(0.0, 750.0 * Constants.RANDOM.nextDouble());
            this.saucer.setVelocity(5 - saucerSize, (double) Constants.RANDOM.nextInt(2) * Math.PI);
            this.addParticipant(this.saucer);
        }
    }

    /**
     * An asteroid has been destroyed
     */
    public void asteroidDestroyed (int size)
    {
        // If all the asteroids are gone, schedule a transition
        if (pstate.countAsteroids() == 0)
        {
            scheduleTransition(END_DELAY);
        }

        scoreAdd(ASTEROID_SCORE[size]);
    }

    /**
     * A saucer has been destroyed.
     */
    public void saucerDestroyed (int size)
    {

        this.saucer = null;
        Participant.getSounds().stopBigSaucer();
        Participant.getSounds().stopSaucerNoise();

        scoreAdd(ALIENSHIP_SCORE[size]);

    }

    /**
     * Schedules a transition m msecs in the future
     */
    private void scheduleTransition (int m)
    {
        transitionTime = System.currentTimeMillis() + m;
    }

    /**
     * This method will be invoked because of button presses and timer events.
     */
    @Override
    public void actionPerformed (ActionEvent e)
    {
        // The start button has been pressed. Stop whatever we're doing
        // and bring up the initial screen
        if (e.getSource() instanceof JButton)
        {
            initialScreen();
        }

        else if (e.getSource() == beats && altBeat == 1)
        {
            // Getting the sounds to make a beat
            Participant.getSounds().playBeat1Noise();
            beats.setDelay(Math.max(FASTEST_BEAT, beats.getDelay() - BEAT_DELTA));
            // Changing to beat 2
            altBeat = 2;
        }

        else if (e.getSource() == beats && altBeat == 2)
        {
            // Basically reversing the beats from the previous if statement
            Participant.getSounds().playBeat2Noise();
            beats.setDelay(Math.max(FASTEST_BEAT, beats.getDelay() - BEAT_DELTA));
            altBeat = 1;
        }

        // Time to refresh the screen and deal with keyboard input
        else if (e.getSource() == refreshTimer)
        {
            // It may be time to make a game transition
            performTransition();

            // Move the participants to their new locations
            pstate.moveParticipants();

            // Refresh screen
            display.refresh();

            // Dealing with the movement of the ship while the timer goes on
            if (isLeft && ship != null)
            {
                ship.turnLeft();
            }
            if (isRight && ship != null)
            {
                ship.turnRight();
            }
            if (isForward && ship != null)
            {
                ship.accelerate();
                ship.getOutline();
            }
            if (ship != null)
            {
                ship.applyFriction(SHIP_FRICTION);
                ship.flameOff();
            }
            if (isBullet && ship != null)
            {

            }
            if (this.shoot && this.ship != null)
            {
                this.ship.shoot();
                this.shoot = false;
            }
        }

        // If the Saucer is not in play, call a Saucer
        if (e.getSource() == saucerTimer && (getLevel() == 2 || getLevel() == 1))
        {
            placeSaucer(getLevel());
            saucerTimer.stop();
        }

        display.refresh();
    }

    /**
     * Returns an iterator over the active participants
     */
    public Iterator<Participant> getParticipants ()
    {
        return pstate.getParticipants();
    }

    /**
     * If the transition time has been reached, transition to a new state
     */
    private void performTransition ()
    {

        // Do something only if the time has been reached
        if (transitionTime <= System.currentTimeMillis())
        {
            // Clear the transition time
            transitionTime = Long.MAX_VALUE;

            // If there are no lives left, the game is over. Show the final
            // screen.
            if (lives <= 0)
            {
                // Since the game was lost we end the game.
                finalScreen();
            }
            // If the ship has disappeared we can place a new one
            else if (ship == null)
            {
                Participant.getSounds().stopBigSaucer();
                Participant.getSounds().stopSaucerNoise();
                // Since the ship was destroyed we restart the timer
                beats.restart();
                // Placing the ship after it was destroyed
                placeShip();
            }
            // We check if the count of the asteroids
            if (pstate.countAsteroids() == 0 && ship != null)
            {
                Participant.getSounds().stopBigSaucer();
                Participant.getSounds().stopSaucerNoise();
                // If the asteroids have all been destroyed we increment the level by one
                numLevel++;
                // After we make a new level with the level up methods
                nextLevel(numLevel);
            }
            // Check if saucer is there, if not it places one
            else if (this.saucer == null)
            {
                Participant.getSounds().stopBigSaucer();
                Participant.getSounds().stopSaucerNoise();
                this.placeSaucer(getLevel());
            }
        }
    }

    /**
     * If a key of interest is pressed, record that it is down.
     */
    @Override
    public void keyPressed (KeyEvent e)
    {

        int keyCode = e.getKeyCode();
        // Rotate the ship left
        if ((keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) && ship != null)
        {
            ship.turnLeft();
            isLeft = true;
        }
        // Rotate the ship right
        if ((keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) && ship != null)
        {
            ship.turnRight();
            isRight = true;
        }
        // Accelerate the ship forward
        if ((keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) && ship != null)
        {
            Participant.getSounds().playThrustClip();
            ship.accelerate();
            isForward = true;
        }
        // Fire the bullets
        if ((keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_S) && ship != null)
        {
            Participant.getSounds().playFireNoise();
            isBullet = true;
            this.shoot = true;
        }
    }

    /**
     * Handles the release of the buttons
     */
    @Override
    public void keyReleased (KeyEvent e)
    {
        int keyCode = e.getKeyCode();
        if ((keyCode == KeyEvent.VK_A || keyCode == KeyEvent.VK_LEFT) && ship != null)
        {
            isLeft = false;
        }
        if ((keyCode == KeyEvent.VK_D || keyCode == KeyEvent.VK_RIGHT) && ship != null)
        {
            isRight = false;
        }
        if ((keyCode == KeyEvent.VK_W || keyCode == KeyEvent.VK_UP) && ship != null)
        {
            Participant.getSounds().stopThrustNoise();
            isForward = false;
            // ship.flameOff();
        }
        if ((keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_S) && ship != null)
        {
            isBullet = false;
        }
    }

    @Override
    public void keyTyped (KeyEvent e)
    {
    }

    /**
     * Increments the score
     */
    public void scoreAdd (int scorePlus)
    {
        this.numScore += scorePlus;
        display.setScore(numScore);
    }

    /**
     * Gets the amount of lives left
     */
    public int getLives ()
    {
        return lives;
    }

    /**
     * Gets the level that we are currently on
     */
    public int getLevel ()
    {
        return numLevel;
    }

    /**
     * Sets the level of the game
     */
    public void setLevel (int level)
    {
        this.numLevel = level;
    }

    /**
     * Gets the score for the game
     */
    public int getScore ()
    {
        return numScore;
    }
}
