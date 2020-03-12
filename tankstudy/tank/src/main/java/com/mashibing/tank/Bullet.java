package com.mashibing.tank;

import com.mashibing.tank.abstractfactory.BaseBullet;
import com.mashibing.tank.abstractfactory.BaseTank;
import com.mashibing.tank.net.Client;
import com.mashibing.tank.net.TankDieMsg;

import java.awt.*;
import java.util.UUID;

public class Bullet extends BaseBullet {
    private static final int SPEED = 10;
    private int x,y;
    private Dir dir;
    public static int WIDTH = ResourceMgr.bulletD.getWidth();
    public static int HEIGHT = ResourceMgr.bulletD.getHeight();
    private boolean live = true;
    private TankFrame tf = null;
    private Group group = Group.BAD;
    public Rectangle rect = new Rectangle();
    private UUID playerId;


    public Bullet(UUID playerId, int x, int y, Dir dir, Group group,TankFrame tf) {
        this.playerId = playerId;
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.group = group;
        this.tf = tf;
        rect.x = this.x;
        rect.y = this.y;
        rect.width = WIDTH;
        rect.height = HEIGHT;

        tf.bullets.add(this);
    }

    @Override
    public void paint(Graphics g){
        if(!live){
            TankFrame.INSTENCE.bullets.remove(this);
        }
        switch(dir){

            case LEFT:
                g.drawImage(ResourceMgr.bulletL,x,y,null);
                break;
            case UP:
                g.drawImage(ResourceMgr.bulletU,x,y,null);
                break;
            case RIGHT:
                g.drawImage(ResourceMgr.bulletR,x,y,null);
                break;
            case DOWN:
                g.drawImage(ResourceMgr.bulletD,x,y,null);
                break;
        }
        move();
    }


    private void move() {
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
        }
        rect.x = this.x;
        rect.y = this.y;
        if (x < 0||y < 0|| x > TankFrame.GAME_WIDTH||y > TankFrame.GAME_HEIGHT){
            live = false;
        }
    }


    @Override
    public void collideWith(BaseTank tank) {
        //if(this.playerId.equals(tank.getId())){return;}
        if (this.group == tank.getGroup()){
            return;
        }
        if (this.live && tank.living && this.rect.intersects(tank.rect)){
            tank.die();
            this.die();
            int eX = tank.getX() + Tank.WIDTH/2 - Explode.WIDTH/2;
            int eY = tank.getY() + Tank.HEIGHT/2 - Explode.HEIGHT/2;
            TankFrame.INSTENCE.explodes.add(TankFrame.INSTENCE.gf.createExplode(eX,eY,TankFrame.INSTENCE));
            Client.INSTANCE.send(new TankDieMsg((Tank)tank));
        }
    }

    private void die() {
        this.live = false;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
