package com.mashibing.tank.net;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ServerFrame extends Frame {
    public static final ServerFrame INSTENCE = new ServerFrame();
    Button btnStart = new Button("start");
    TextArea taLeft = new TextArea();
    TextArea taRight = new TextArea();

    Server server = new Server();

    private ServerFrame(){
        this.setSize(1600,600);
        this.setLocation(300,30);
        this.add(btnStart, BorderLayout.NORTH);
        Panel p = new Panel(new GridLayout(1,2));
        p.add(taLeft);
        p.add(taRight);
        this.add(p);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

//        this.btnStart.addActionListener((e)->{
//            server.serverStart();
//        });
//
//        this.setVisible(true);
    }

    public static void main(String[] args){
        ServerFrame.INSTENCE.setVisible(true);
        ServerFrame.INSTENCE.server.serverStart();
    }

    public void updateServerMsg(String s) {
        taLeft.setText(taLeft.getText() + System.getProperty("line.separator") + s);
    }

    public void updateClientMsg(String s) {
        taRight.setText(taRight.getText() + System.getProperty("line.separator") + s);
    }
}
