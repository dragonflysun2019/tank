package com.mashibing.tank.abstractfactory;

import com.mashibing.tank.Group;

import java.awt.*;
import java.util.UUID;

public abstract class BaseTank {
    public Rectangle rect = new Rectangle();

    public boolean living = true;

    public abstract void paint(Graphics g);

    public abstract Group getGroup();

    public abstract void die();

    public abstract int getX();

    public abstract int getY();

    public abstract UUID getId();
}
