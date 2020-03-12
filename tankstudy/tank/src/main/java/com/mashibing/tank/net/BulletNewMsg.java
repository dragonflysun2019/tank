package com.mashibing.tank.net;

import com.mashibing.tank.Tank;
import com.mashibing.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class BulletNewMsg extends Msg {
    UUID id;

    @Override
    public void handle() {
        if(this.id.equals(TankFrame.INSTENCE.getMainTank().getId())){return;}
        Tank t = (Tank)TankFrame.INSTENCE.findByUUID(this.id);
        if (t != null){
            t.fire();
        }
    }

    public BulletNewMsg(){}

    public BulletNewMsg(Tank t){
        this.id = t.getId();
    }

    @Override
    public byte[] toBytes() {
        ByteArrayOutputStream baos = null;
        DataOutputStream dos = null;
        byte[] bytes = null;
        baos = new ByteArrayOutputStream();
        dos = new DataOutputStream(baos);
        try {
            dos.writeLong(id.getMostSignificantBits()); //高64位
            dos.writeLong(id.getLeastSignificantBits());//低64位
            dos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (baos != null){
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (dos != null){
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }

    @Override
    public void parse(byte[] bytes) {
        DataInputStream dis = new DataInputStream(new ByteArrayInputStream(bytes));
        try {
            this.id = new UUID(dis.readLong(),dis.readLong());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public MsgType getMsgType() {
        return MsgType.BulletNew;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BulletNewMsg{" +
                "id=" + id +
                '}';
    }
}
