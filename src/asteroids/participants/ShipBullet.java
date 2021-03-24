package asteroids.participants;

import asteroids.destroyers.AsteroidDestroyer;
import asteroids.destroyers.ShipBulletDestroyer;
import asteroids.game.Participant;
import asteroids.participants.Bullet;

public class ShipBullet extends Bullet implements AsteroidDestroyer, ShipBulletDestroyer
{
    public ShipBullet (double x, double y, double direction, double velocity)
    {
        super(x, y, direction, velocity);
    }

    @Override
    public void collidedWith (Participant p)
    {
        if (p instanceof ShipBulletDestroyer)
        {
            Participant.expire(this);
        }
    }
}