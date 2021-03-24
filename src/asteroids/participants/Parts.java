package asteroids.participants;

import java.awt.Shape;
import java.awt.geom.Path2D;
import asteroids.game.Participant;
import asteroids.game.ParticipantCountdownTimer;
import static asteroids.game.Constants.*;

/**
 * This class is intended to model debris after a ship blows up
 */
public class Parts extends Participant
{
    // We need a shape because it is debris
    private Shape shape;

    /** Creates debris in the area where the ship crashed */
    public Parts (double x, double y, double length)
    {
        Path2D.Double line = new Path2D.Double();
        line.moveTo(0.0, length / 2);
        line.lineTo(0.0, -length / 2);

        // Making the rotation random, since everything else is random
        setRotation(RANDOM.nextDouble() * 4.5);

        // Velocity is also random for a more real life effect
        setVelocity(RANDOM.nextDouble(), RANDOM.nextDouble() * 4.5);

        // Sets the "explosion" where the ship was destroyed
        setPosition(x, y);

        shape = line;

        new ParticipantCountdownTimer(this, 2500);
    }

    @Override
    protected Shape getOutline ()
    {
        return shape;
    }

    @Override
    public void collidedWith (Participant p)
    {
        // DOES NOTHING
    }

    @Override
    public void countdownComplete (Object payload)
    {
        Participant.expire(this);
    }

}
