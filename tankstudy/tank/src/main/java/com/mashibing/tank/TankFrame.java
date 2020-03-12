package com.mashibing.tank;

import com.mashibing.tank.abstractfactory.*;
import com.mashibing.tank.net.BulletNewMsg;
import com.mashibing.tank.net.Client;
import com.mashibing.tank.net.TankStartMovingMsg;
import com.mashibing.tank.net.TankStopMsg;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.List;

public class TankFrame extends Frame {

    public static final TankFrame INSTENCE = new TankFrame();
    Random r = new Random();

     Tank myTank = new Tank(r.nextInt(GAME_WIDTH),r.nextInt(GAME_HEIGHT),Dir.DOWN,Group.GOOD,this);
     public Map<UUID,BaseTank> tanks = new HashMap<>();
     public List<BaseBullet> bullets = new ArrayList<>();
     public List<BaseExplode> explodes = new ArrayList<>();

     public GameFactory gf = new DefaultFactory();
     public static final int GAME_WIDTH = 800, GAME_HEIGHT=600;

     private TankFrame()  {
        this.setSize(GAME_WIDTH,GAME_HEIGHT);
        this.setResizable(false);
        this.setTitle("tank war");
        this.setVisible(true);
        myTank.setMoving(false);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        addKeyListener(new MyKeyListener());

    }

    //双缓存，消除屏幕闪烁
    Image offScreenImage = null;
     @Override
     public void update(Graphics g){
         if (null == offScreenImage){
             offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
         }
         Graphics gOffScreen = offScreenImage.getGraphics();
         Color c = gOffScreen.getColor();
         gOffScreen.setColor(Color.BLACK);
         gOffScreen.fillRect(0,0,GAME_WIDTH,GAME_HEIGHT);
         gOffScreen.setColor(c);
         paint(gOffScreen);
         g.drawImage(offScreenImage,0,0,null);
     }

    @Override
    public void paint(Graphics g){
         Color c = g.getColor();
         g.setColor(Color.WHITE);
         g.drawString("子弹的数量：" + bullets.size(),10,60);
         g.drawString("敌人的数量：" + tanks.size(),10,80);
         g.drawString("爆炸的数量：" + explodes.size(),10,100);
         g.setColor(c);
         myTank.paint(g);
         for (int i = 0; i < bullets.size();++i){
            bullets.get(i).paint(g);
        }
        //Java 8 stream api
        tanks.values().stream().forEach((e)->e.paint(g));

        for (int i = 0; i < explodes.size();++i){
            explodes.get(i).paint(g);
        }

        Collection<BaseTank> values = tanks.values();
        for  (int i = 0; i < bullets.size(); ++i){
            for (BaseTank t : values){
                bullets.get(i).collideWith(t);
            }
        }
         //有迭代器问题
//        for (Bullet b : bullets){
//            b.paint(g);
//        }
    }

    public void addTank(Tank t) {
        tanks.put(t.getId(),t);
    }

    public BaseTank findByUUID(UUID id) {
         return tanks.get(id);
    }

    class MyKeyListener extends KeyAdapter{
         boolean bL = false;
         boolean bU = false;
        boolean bR = false;
        boolean bD = false;
        @Override
        public void keyPressed(KeyEvent e) {
            //System.out.println("Key press");
            int key = e.getKeyCode();
            switch (key){
                case KeyEvent.VK_LEFT:
                    bL = true;
                    break;
                case KeyEvent.VK_UP:
                    bU = true;
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = true;
                    break;
                case KeyEvent.VK_DOWN:
                    bD = true;
                    break;
                default:
                    break;
            }

            setMainTankDir();
        }

        @Override
        public void keyReleased(KeyEvent e) {
            //System.out.println("Key release");
            int key = e.getKeyCode();
            switch (key){
                case KeyEvent.VK_LEFT:
                    bL = false;
                    break;
                case KeyEvent.VK_UP:
                    bU = false;
                    break;
                case KeyEvent.VK_RIGHT:
                    bR = false;
                    break;
                case KeyEvent.VK_DOWN:
                    bD = false;
                    break;
                case KeyEvent.VK_SPACE:
                    myTank.fire();
                    Client.INSTANCE.send(new BulletNewMsg(getMainTank()));
                    break;
                default:
                    break;
            }
            setMainTankDir();
        }
        private void setMainTankDir() {
            if (!bL && !bU && !bR && !bD){
                myTank.setMoving(false);
                Client.INSTANCE.send(new TankStopMsg(getMainTank()));
            }else {
                myTank.setMoving(true);
                if(bL) myTank.setDir(Dir.LEFT);
                if(bU) myTank.setDir(Dir.UP);
                if(bR) myTank.setDir(Dir.RIGHT);
                if(bD) myTank.setDir(Dir.DOWN);
                Client.INSTANCE.send(new TankStartMovingMsg(getMainTank()));
            }
        }
    }

    public Tank getMainTank(){
         return myTank;
    }

}
