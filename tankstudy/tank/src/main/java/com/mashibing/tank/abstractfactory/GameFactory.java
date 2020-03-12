package com.mashibing.tank.abstractfactory;

import com.mashibing.tank.Dir;
import com.mashibing.tank.Group;
import com.mashibing.tank.TankFrame;

import java.util.UUID;

public abstract class GameFactory {
    public abstract BaseTank createTank(int x, int y, Dir dir, Group group, TankFrame tf);
    public abstract BaseExplode createExplode(int x, int y,TankFrame tf);
    public abstract BaseBullet createBullet(UUID playerId, int x, int y, Dir dir, Group group, TankFrame tf);

}
