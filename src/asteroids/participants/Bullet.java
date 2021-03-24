package asteroids.participants;

import asteroids.destroyers.SaucerDestroyer;
import asteroids.game.Constants;
import asteroids.game.Participant;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

public abstract class Bullet extends Participant implements SaucerDestroyer
{
    private Shape outline;

    public Bullet (double x, double y, double direction, double velocity)
    {
        this.setPosition(x, y);
        
        this.setVelocity(velocity + Constants.BULLET_SPEED, direction);
        
        this.outline = new Ellipse2D.Double(0.0, 0.0, 1.5, 1.5);
        
        new asteroids.game.ParticipantCountdownTimer(this, this, Constants.BULLET_DURATION);
        
    }

    public Bullet (double x, double y, double direction)
    {
        this.setPosition(x, y);
        
        this.setVelocity(Constants.BULLET_SPEED, direction);
        
        this.outline = new Ellipse2D.Double(0.0, 0.0, 1.5, 1.5);
        
        new asteroids.game.ParticipantCountdownTimer(this, this, Constants.BULLET_DURATION);
        
    }

    @Override
    protected Shape getOutline ()
    {
        return this.outline;
    }

    @Override
    public void countdownComplete (Object payload)
    {
        Participant.expire(this);
    }
}