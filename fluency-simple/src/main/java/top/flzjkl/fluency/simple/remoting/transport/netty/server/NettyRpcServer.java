package top.flzjkl.fluency.simple.remoting.transport.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.flzjkl.fluency.simple.common.extension.SPI;
import top.flzjkl.fluency.simple.common.factory.SingletonFactory;
import top.flzjkl.fluency.simple.common.utils.RuntimeUtil;
import top.flzjkl.fluency.simple.common.utils.concurrent.threadpool.ThreadPoolFactoryUtil;
import top.flzjkl.fluency.simple.config.RpcServiceConfig;
import top.flzjkl.fluency.simple.provider.ServiceProvider;
import top.flzjkl.fluency.simple.provider.impl.RedisProviderImpl;
import top.flzjkl.fluency.simple.remoting.transport.netty.codec.RpcMessageDecoder;
import top.flzjkl.fluency.simple.remoting.transport.netty.codec.RpcMessageEncoder;

import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class NettyRpcServer {

    public static final int PORT = 3939;

    private final ServiceProvider serviceProvider = SingletonFactory.getInstance(RedisProviderImpl.class);

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    @SneakyThrows
    public void start() {
        String host = InetAddress.getLocalHost().getHostAddress();
        EventLoopGroup boss = new NioEventLoopGroup(1);
        EventLoopGroup worker = new NioEventLoopGroup();
        DefaultEventExecutorGroup serviceHandlerGroup = new DefaultEventExecutorGroup(
                RuntimeUtil.cpus() * 2,
                ThreadPoolFactoryUtil.createThreadFactory("service-handler-group", false)
        );
        ServerBootstrap b = new ServerBootstrap();
        b.group(boss, worker)
                .channel(NioServerSocketChannel.class)
                // TCP默认开启了 Nagle 算法，该算法的作用是尽可能的发送大数据快，减少网络传输。TCP_NODELAY 参数的作用就是控制是否启用 Nagle 算法。
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                //表示系统用于临时存放已完成三次握手的请求的队列的最大长度,如果连接建立频繁，服务器处理创建新连接较慢，可以适当调大这个参数
                .option(ChannelOption.SO_BACKLOG, 128)
                // 当客户端第一次进行请求的时候才会进行初始化
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) {
                        // 30 秒之内没有收到客户端请求的话就关闭连接
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new IdleStateHandler(30, 0, 0, TimeUnit.SECONDS));
                        p.addLast(new RpcMessageEncoder());
                        p.addLast(new RpcMessageDecoder());
                        p.addLast(serviceHandlerGroup, new NettyRpcServerHandler());
                    }
                });
        ChannelFuture f = b.bind(host, PORT).sync();
        f.channel().closeFuture().sync();
    }

}
