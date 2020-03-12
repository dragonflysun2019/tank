package com.mashibing.tank.net;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.io.IOException;

public class Server {
    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static void main(String[] args) throws IOException {

    }

    public void serverStart() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);

        ServerBootstrap b = new ServerBootstrap();
        try {
            ChannelFuture f = b.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //禁用TCP Nagle算法
                    .option(ChannelOption.TCP_NODELAY,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            //System.out.println(ch);
                            //System.out.println(Thread.currentThread().getId());
                            ChannelPipeline pl = ch.pipeline();
                            pl.addLast(new MsgEncoder())
                                    .addLast(new MsgDecoder())
                                    .addLast(new ServerChildHandler());
                        }
                    })
                    .bind(8888)
                    .sync();
            ServerFrame.INSTENCE.updateServerMsg("Server started!");
            f.channel().closeFuture().sync();   //Close()->返回ChannelFuture
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }
}

class ServerChildHandler extends ChannelInboundHandlerAdapter { //SimpleInboundHandler


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Server.clients.add(ctx.channel());
        //System.out.println("channel:" + ctx.channel());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ServerFrame.INSTENCE.updateClientMsg(msg.toString());
        Server.clients.writeAndFlush(msg);


        //        ByteBuf buf = null;
//        try {
//            buf = (ByteBuf) msg;
//            byte[] bytes = new byte[buf.readableBytes()];
//            buf.getBytes(buf.readerIndex(),bytes);
//            String s = new String(bytes);
//            ServerFrame.INSTENCE.updateClientMsg(s);
//            if (s.equals("_bye_")){
//                ServerFrame.INSTENCE.updateServerMsg("客户端要求退出");
//                Server.clients.remove(ctx.channel());
//                ctx.close();
//            }else {
//                Server.clients.writeAndFlush(buf);
//            }
//        }finally {
//            if (buf != null){
//                //ReferenceCountUtil.release(buf);
//                //System.out.println(buf.refCnt());
//            }
//        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        Server.clients.remove(ctx.channel());
        ctx.close();
    }
}