package com.redsun.netty.protocol.stack;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class NettyClient {
    private ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

    EventLoopGroup group = new NioEventLoopGroup();

    public void connect(int port, String host) throws Exception {
        // 配置客户端NIO线程组
        try{
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
            .handler(new ChannelInitializer<SocketChannel>() {

                /*
                 * 利用Netty的ChannelPipeline和ChannelHandler机制，可以非常方便的实现功能解耦
                 * 和业务产品的定制。例如本例中的心跳定时器、握手请求和后端的业务处理可以通过不同的Handler
                 * 来实现，类似于AOP。通过Handler Chain的机制可以方便地实现切面拦截和定制，
                 * 相比于AOP它的性能更高
                 */
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    // 为了防止对于单条消息过大导致的内存溢出或者畸形码流导致解码错位引起内存分配失败
                    // 对单条消息最大长度进行了上限限制
                    ch.pipeline().addLast(new NettyMessageDecoder(1024 * 1024, 4,4));
                    ch.pipeline().addLast("MessageEncoder", new NettyMessageEncoder());
                    ch.pipeline().addLast("readTimeoutHandler", new ReadTimeoutHandler(50));
                    ch.pipeline().addLast("LoginAuthHandler", new LoginAuthReqHandler());
                    ch.pipeline().addLast("HeartBeatHandler", new HeartBeatReqHandler());
                }
            });
            // 发起异步连接操作
            ChannelFuture future = b.connect(new InetSocketAddress(host, port),
                    new InetSocketAddress(NettyConstant.LOCAL_IP,NettyConstant.LOCAL_PROT)).sync();
            future.channel().closeFuture().sync();
        } finally {
            // 所有资源释放完成之后，清空资源，再次发起重连操作
            executor.execute(() -> {
                try {
                    TimeUnit.SECONDS.sleep(5);
                    try{
                        // 发起重连操作
                        connect(NettyConstant.PROT, NettyConstant.REMOTEIP);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void main(String[] args) throws Exception {
        new NettyClient().connect(NettyConstant.PROT, NettyConstant.REMOTEIP);
    }
}
