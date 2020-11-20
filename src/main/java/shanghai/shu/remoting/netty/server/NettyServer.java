package shanghai.shu.remoting.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import shanghai.shu.config.Protocol;
import shanghai.shu.remoting.netty.MessageCodecConstant;
import shanghai.shu.remoting.netty.serialize.RpcSerializeFrame;

import java.util.concurrent.TimeUnit;

public class NettyServer {
    /**
     * 启动netty服务
     * @param protocol
     * @throws Exception
     */
    public static void start(final Protocol protocol) throws Exception{
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG,1024)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childOption(ChannelOption.SO_KEEPALIVE,true)
                    .childOption(ChannelOption.TCP_NODELAY,true)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new IdleStateHandler(5, 5, 10, TimeUnit.SECONDS));
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, MessageCodecConstant.MESSAGE_LENGTH, 0, MessageCodecConstant.MESSAGE_LENGTH));
                            pipeline.addLast(new LengthFieldPrepender(MessageCodecConstant.MESSAGE_LENGTH));
                            RpcSerializeFrame.select(protocol.getSerialize(), pipeline);
                            pipeline.addLast(new NettyServerInHandler());
                            pipeline.addLast(new HeartBeatHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind(Integer.parseInt(protocol.getPort())).sync();
            System.out.println("RPC provider server is listening at"+protocol.getPort());
            future.channel().closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
