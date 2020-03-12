package com.mashibing.tank;

import com.mashibing.tank.net.Client;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TankFrame tf = TankFrame.INSTENCE;

//        int initTankCount = Integer.valueOf(PropertyMgr.get("initTankCount"));
//        //初始化坦克
//        for (int i = 0; i < initTankCount; ++i){
//            tf.tanks.add(tf.gf.createTank(50 + i*80,300,Dir.DOWN,Group.BAD,tf));
//        }
        //System.out.println(Main.class.getClassLoader().getResource("images/bulletD.gif").getPath());
        //new  Thread(()->new Audio("audio/war1.wav").loop()).start();

        new Thread(()->{
            while(true){
                try {
                    Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                tf.repaint();
            }
        }).start();

        Client.INSTANCE.connect();
    }
}
