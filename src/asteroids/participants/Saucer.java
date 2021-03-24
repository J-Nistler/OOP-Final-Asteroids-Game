package asteroids.participants;

import asteroids.destroyers.SaucerDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipBulletDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Constants;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.participants.Ship;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.util.concurrent.ThreadLocalRandom;

public class Saucer extends Participant implements AsteroidDestroyer, ShipBulletDestroyer, ShipDestroyer
{
    private Shape outline;
    private int size;
    private Controller controller;
    boolean changeDirection = false;

    public Saucer (int size, Controller controller)
    {
        this.size = size;

        this.controller = controller;

        // Draws the saucer shape
        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(20, 0);
        poly.lineTo(10, 8);
        poly.lineTo(-10, 8);
        poly.lineTo(-20, 0);
        poly.lineTo(20, 0);
        poly.lineTo(-20, 0);
        poly.lineTo(-10, -8);
        poly.lineTo(10, -8);
        poly.lineTo(-8, -8);
        poly.lineTo(-6, -15);
        poly.lineTo(6, -15);
        poly.lineTo(8, -8);
        poly.lineTo(10, -8);
        poly.closePath();

        this.outline = poly;

        // Assigns appropriate scaler from constants
        double scaler = Constants.ALIENSHIP_SCALE[size];

        // Scale the saucer
        poly.transform(AffineTransform.getScaleInstance(scaler, scaler));

        // Timer to shoot
        new asteroids.game.ParticipantCountdownTimer(this, "shoot", 1500);

        // Timer to change the saucer's direction
        new asteroids.game.ParticipantCountdownTimer(this, "direction", 1000);

        // Plays the appropriate saucer sound loop
        if (this.getSize() == 1)
        {
            Saucer.getSounds().playSaucerBig();
        }
        else
        {
            Saucer.getSounds().playSmallSaucer();
        }
    }

    // Returns the outline
    @Override
    public Shape getOutline ()
    {
        return this.outline;
    }

    // Returns the size
    public int getSize ()
    {
        return this.size;
    }

    // Shoots a bullet from the Saucer
    public void shoot ()
    {
        SaucerBullet bullet = new SaucerBullet(this.getX(), this.getY(), shootingTrajectory());
        bullet.setSpeed(Constants.BULLET_SPEED);
        this.controller.addParticipant(bullet);
    }

    public double shootingTrajectory ()
    {

        // Large saucer shoots in a random direction
        if (this.size == 1)
        {
            return Constants.RANDOM.nextDouble() * 2 * Math.PI;

        }

        // Small saucer shoots within 5% of the ship's direction
        else
        {

            // Recieve the ship's current position
            Ship ship = this.controller.getShip();

            // Find the distance between the ship and the saucer
            double deltaX = ship.getX() - this.getX();
            double deltaY = ship.getY() - this.getY();

            // Get the direction of fire
            double distance = Math.hypot(deltaX, deltaY);
            double direction = Math.acos(deltaX / distance);
            direction = deltaY > 0.0 ? direction : -direction;

            // Return trajectory within 5% of the ship
            return ((ThreadLocalRandom.current().nextDouble(-0.05, 0.05)) * direction) + direction;
        }
    }

    @Override
    public void move ()
    {
        super.move();
        if (this.changeDirection)
        {
            this.changeDirection = false;
            if (Math.cos(this.getDirection()) > 0.0)
            {
                this.setDirection(Constants.RANDOM.nextInt(3) - 1);
            }
            else
            {
                this.setDirection(Math.PI + (double) Constants.RANDOM.nextInt(3) - 1.0);
            }
            new asteroids.game.ParticipantCountdownTimer(this, "direction", 1000);
        }
    }

    // Executes action at appropriate time.
    @Override
    public void countdownComplete (Object payload)
    {
        if ("shoot".equals(payload))
        {
            Ship ship = this.controller.getShip();
            if (ship != null)
            {
                this.shoot();
                Participant.getSounds().playFireNoise();
                new asteroids.game.ParticipantCountdownTimer(this, "shoot", 1500);
            }
        }
        else if ("direction".equals(payload))
        {
            this.changeDirection = true;
        }
    }

    // Whenever an appropriate participant collides with the saucer
    @Override
    public void collidedWith (Participant p)
    {

        if (p instanceof SaucerDestroyer)
        {

            // Destroyes the saucer
            Participant.expire(this);

            // Stops the appropriate saucer noise
            if (this.getSize() == 1)
            {
                Participant.getSounds().stopBigSaucer();
            }
            else
            {
                Saucer.getSounds().stopSaucerNoise();
            }

            // Shapes of "Debris"
            controller.addParticipant(new Parts(this.getX(), this.getY(), 12));
            controller.addParticipant(new Parts(this.getX(), this.getY(), 15));
            controller.addParticipant(new Parts(this.getX(), this.getY(), 9));
            controller.addParticipant(new Parts(this.getX(), this.getY(), 11));

            // Plays explosion sound
            Participant.getSounds().playBangAlienShipNoise();

            // Tells the controller a saucer has been destroyed
            this.controller.saucerDestroyed(this.size);
        }
    }
}