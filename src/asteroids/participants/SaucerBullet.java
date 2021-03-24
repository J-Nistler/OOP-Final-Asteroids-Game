package asteroids.participants;

import asteroids.destroyers.SaucerBulletDestroyer;
import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipDestroyer;
import asteroids.game.Participant;
import asteroids.participants.Bullet;

public class SaucerBullet extends Bullet implements AsteroidDestroyer, ShipDestroyer
{
    public SaucerBullet (double x, double y, double direction)
    {
        super(x, y, direction);
    }

    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof SaucerBulletDestroyer)
        {
            Participant.expire(this);
        }
    }
}