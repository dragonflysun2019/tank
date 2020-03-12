package com.mashibing.tank.abstractfactory;

import com.mashibing.tank.*;

import java.util.UUID;

public class DefaultFactory extends GameFactory {
    @Override
    public BaseTank createTank(int x, int y, Dir dir, Group group, TankFrame tf) {
        return new Tank(x,y,dir,group,tf);
    }

    @Override
    public BaseExplode createExplode(int x, int y, TankFrame tf) {
        return new Explode(x,y,tf);
    }

    @Override
    public BaseBullet createBullet(UUID playerId, int x, int y, Dir dir, Group group, TankFrame tf) {
        return new Bullet(playerId,x,y,dir,group,tf);
    }
}
