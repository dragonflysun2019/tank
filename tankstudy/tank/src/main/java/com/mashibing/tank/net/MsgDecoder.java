package com.mashibing.tank.net;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MsgDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 8) return;
        //标记
        in.markReaderIndex();
        MsgType msgType = MsgType.values()[in.readInt()];
        int length = in.readInt();

        if(in.readableBytes() < length){
            //重置
            in.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[length];
        in.readBytes(bytes);

        Msg msg = null;
        //reflect 不用该代码
        //Class.forName(msgType.toString + "Msg").constructor().newInstance();
        switch(msgType){
            case TankJoin:
                msg = new TankJoinMsg();
                break;
            case TankStartMoving:
                msg = new TankStartMovingMsg();
                break;
            case TankStop:
                msg = new TankStopMsg();
                break;
            case TankDie:
                msg = new TankDieMsg();
                break;
            case BulletNew:
                msg = new BulletNewMsg();
                break;
            default:
                break;
        }

        msg.parse(bytes);
        out.add(msg);
    }
}
