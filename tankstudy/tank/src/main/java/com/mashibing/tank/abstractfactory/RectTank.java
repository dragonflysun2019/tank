package com.mashibing.tank.abstractfactory;

import com.mashibing.tank.*;

import java.awt.*;
import java.util.Random;

public class RectTank extends BaseTank {
    int x,y;
    Dir dir = Dir.DOWN;
    private static final int SPEED = 2;
    public static int WIDTH = ResourceMgr.goodTankU.getWidth();
    public static int HEIGHT = ResourceMgr.goodTankU.getHeight();
    private boolean moving = true;
    private boolean living = true;
    TankFrame tf = null;
    private Random random = new Random();
    private Group group = Group.BAD;
    //public Rectangle rect = new Rectangle();
    FireStrategy fs;

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }


    public Dir getDir() {
        return dir;
    }

    public void setDir(Dir dir) {
        this.dir = dir;
    }



    public RectTank(int x, int y, Dir dir, Group group, TankFrame tf) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.tf = tf;
        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;
        if (this.getGroup() == Group.GOOD){
            try {
                fs = (FireStrategy) Class.forName(PropertyMgr.get("goodfs")).newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            try {
                fs = (FireStrategy) Class.forName(PropertyMgr.get("badfs")).getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        if (!living){
            tf.tanks.remove(this);
        };
        Color c = g.getColor();
        g.setColor(Color.BLUE);
        g.fillRect(x,y,40,40);
        g.setColor(c);

        move();

    }

    private void move() {
        if(!moving) return;
        switch(dir){
            case LEFT:
                x -= SPEED;
                break;
            case UP:
                y -= SPEED;
                break;
            case RIGHT:
                x += SPEED;
                break;
            case DOWN:
                y += SPEED;
                break;
            default:
                break;
        }

        if (this.group == Group.BAD && random.nextInt(100) > 95){
            this.fire();
        }

        if (this.group == Group.BAD && random.nextInt(10) > 8){
            randomDir();
        }

        boundsCkeck();

        rect.x = this.x;
        rect.y = this.y;
    }

    private void boundsCkeck() {
        if (this.x < 0){
            x = 0;
        }else if (this.y < 30){
            y = 30;
        }else if (this.x > TankFrame.GAME_WIDTH - RectTank.WIDTH){
            x = TankFrame.GAME_WIDTH - RectTank.WIDTH;
        }else if (this.y > TankFrame.GAME_HEIGHT - RectTank.HEIGHT){
            y = TankFrame.GAME_HEIGHT - RectTank.HEIGHT;
        }
    }

    private void randomDir() {
        this.dir = Dir.values()[random.nextInt(4)];
    }

    public void fire() {
        //fs.fire(this);
        int bX = x + Tank.WIDTH/2 - Bullet.WIDTH/2;
        int bY = y + Tank.HEIGHT/2 - Bullet.HEIGHT/2;

        Dir[] dirs = Dir.values();
        for(Dir dir: dirs){
            tf.gf.createBullet(bX,bY,dir,getGroup(),tf);
        }

        if (getGroup() == Group.GOOD) new Thread(()->new Audio("audio/tank_fire.wav").play()).start();

    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void die() {
        this.living = false;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
