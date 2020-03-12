package com.mashibing.tank.net;

import com.mashibing.tank.Dir;
import com.mashibing.tank.Tank;
import com.mashibing.tank.TankFrame;

import java.io.*;
import java.util.UUID;

public class TankStopMsg extends Msg {
    UUID id;
    int x,y;
    @Override
    public void handle() {
        if(this.id.equals(TankFrame.INSTENCE.getMainTank().getId())){return;}
        Tank t = (Tank)TankFrame.INSTENCE.findByUUID(this.id);
        if (t != null){
            t.setMoving(false);
            t.setX(this.x);
            t.setY(this.y);
        }
    }

    public TankStopMsg(){}

    public TankStopMsg(Tank t){
        this.id = t.getId();
        this.x = t.getX();
        this.y = t.getY();
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
            dos.writeInt(x);
            dos.writeInt(y);
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
            this.x = dis.readInt();
            this.y = dis.readInt();
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
        return MsgType.TankStop;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "TankStopMsg{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
