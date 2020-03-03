package com.mashibing.tank.abstractfactory;

import com.mashibing.tank.Audio;
import com.mashibing.tank.ResourceMgr;
import com.mashibing.tank.TankFrame;

import java.awt.*;

public class RectExplode extends BaseExplode{
    private int x,y;
    public static int WIDTH = ResourceMgr.explodes[0].getWidth();
    public static int HEIGHT = ResourceMgr.explodes[0].getHeight();
    private boolean live = true;
    private TankFrame tf = null;
    private int step = 0;


    public RectExplode(int x, int y,TankFrame tf) {
        this.x = x;
        this.y = y;
        this.tf = tf;

        new Audio("audio/explode.wav").play();
    }

    @Override
    public void paint(Graphics g){
        //g.drawImage(ResourceMgr.explodes[step++],x,y,null);
        Color c = g.getColor();
        g.setColor(Color.red);
        g.fillRect(x,y,10*step,10*step);
        ++step;
        g.setColor(c);
        if (step >= 5){
            tf.explodes.remove(this);
        }
    }
}
