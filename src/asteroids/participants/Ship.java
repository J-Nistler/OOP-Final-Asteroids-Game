package asteroids.participants;

import static asteroids.game.Constants.*;
import java.awt.Shape;
import java.awt.geom.*;
import asteroids.destroyers.*;
import asteroids.game.Controller;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;

/**
 * Represents ships
 */
public class Ship extends Participant implements AsteroidDestroyer
{
    /** The outline of the ship */
    private Shape outline;

    private Shape outline_Flame;

    /** Game controller */
    private Controller controller;

    /** Flame */
    private boolean isOn;

    /** Knows if it is moving */
    private boolean isMoving;

    /**
     * Constructs a ship at the specified coordinates that is pointed in the given direction.
     */
    public Ship (int x, int y, double direction, Controller controller)
    {
        this.controller = controller;
        setPosition(x, y);
        setRotation(direction);
        isOn = true;
        isMoving = false;

        Path2D.Double poly = new Path2D.Double();
        poly.moveTo(20, 0);
        poly.lineTo(-20, 12);
        poly.lineTo(-13, 10);
        poly.lineTo(-13, -10);
        poly.lineTo(-20, -12);
        poly.closePath();

        outline = poly;
        poly = new Path2D.Double();
        poly.moveTo(20, 0);
        poly.lineTo(-20.0, 12.0);
        poly.lineTo(-13.0, 10.0);
        poly.lineTo(-13.0, -5.0);
        poly.lineTo(-25.0, 0.0);
        poly.lineTo(-13.0, 5.0);
        poly.lineTo(-13.0, -10.0);
        poly.lineTo(-20.0, -12.0);
        poly.closePath();
        outline_Flame = poly;
    }

    /** Getting the outline of the flame vs the ship when it is moving */
    @Override
    public Shape getOutline ()
    {
        if (isMoving)
        {
            if (isOn)
            {
                return outline_Flame;
            }
            return outline;
        }
        return outline;
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getXNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getX();
    }

    /**
     * Returns the x-coordinate of the point on the screen where the ship's nose is located.
     */
    public double getYNose ()
    {
        Point2D.Double point = new Point2D.Double(20, 0);
        transformPoint(point);
        return point.getY();
    }

    /**
     * Customizes the base move method by imposing friction
     */
    @Override
    public void move ()
    {
        applyFriction(SHIP_FRICTION);
        super.move();
    }

    /**
     * Turns right by Pi/20 radians
     */
    public void turnRight ()
    {
        rotate(Math.PI / 20);
    }

    /**
     * Turns left by Pi/20 radians
     */
    public void turnLeft ()
    {
        rotate(-Math.PI / 20);
    }

    /**
     * Accelerates by SHIP_ACCELERATION
     */
    public void accelerate ()
    {
        accelerate(SHIP_ACCELERATION);
        isMoving = true;
    }

    public void moving ()
    {
        isMoving = true;
    }

    /**
     * Fires a bullet
     */
    public void shoot ()
    {
        // Checks to see if the bullet limit has been reached
        if (!this.controller.atBulletLimit(BULLET_LIMIT))
        {
            // Creates a bullet at the nose coordinates, then shoots it
            ShipBullet bullet = new ShipBullet(this.getXNose(), this.getYNose(), this.getRotation(), this.getSpeed());
            bullet.setVelocity(BULLET_SPEED, this.getRotation());
            this.controller.addParticipant(bullet);
        }
    }

    /**
     * When a Ship collides with a ShipDestroyer, it expires
     */
    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipDestroyer)
        {
            // Expire the ship from the game
            Participant.expire(this);

            // Play sound of ship being destroyed
            Ship.getSounds().playBangShipNoise();

            // Shapes of "Debris"
            controller.addParticipant(new Parts(this.getX(), this.getY(), 12));
            controller.addParticipant(new Parts(this.getX(), this.getY(), 15));
            controller.addParticipant(new Parts(this.getX(), this.getY(), 9));
            controller.addParticipant(new Parts(this.getX(), this.getY(), 11));

            // Tell the controller the ship was destroyed
            controller.shipDestroyed();
        }
    }

    /**
     * This method is invoked when a ParticipantCountdownTimer completes its countdown.
     */
    @Override
    public void countdownComplete (Object payload)
    {
        // Give a burst of acceleration, then schedule another
        // burst for 200 msecs from now.
        if (payload.equals("move"))
        {
            accelerate();
            new ParticipantCountdownTimer(this, "move", 200);
        }
    }

    public void flameOff ()
    {
        isMoving = false;
    }

}
