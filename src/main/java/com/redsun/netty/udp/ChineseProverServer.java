package com.redsun.netty.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class ChineseProverServer {

    public void run(int port) throws Exception {
        // 配置服务端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            // 设置socket参数支持广播，最后设置业务处理handler
            bootstrap.group(group).channel(NioDatagramChannel.class).option(ChannelOption.SO_BROADCAST, true)
                    .handler(new ChineseProverbServerHandler());

            bootstrap.bind(port).sync().channel().closeFuture().await();
            /*
             * 相比于TCP通信，UDP不存在客户端和服务端的实际连接，因此不需要为连接
             * （ChannelPipeline）设置handler，对于服务端，只需要设置启动辅助类的handler即可
             */
        } finally {
            // 优雅退出，释放线程池资源
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.valueOf(args[0]);
            } catch (NumberFormatException e) {
                // 采用默认值
                e.printStackTrace();
            }
        }
        new ChineseProverServer().run(port);
    }
}
