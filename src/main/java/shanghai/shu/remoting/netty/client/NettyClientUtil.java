package shanghai.shu.remoting.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;
import shanghai.shu.config.Protocol;
import shanghai.shu.remoting.netty.MessageCodecConstant;
import shanghai.shu.remoting.netty.serialize.RpcSerializeFrame;

public class NettyClientUtil {
    /**
     * netty channel池
     */
    private volatile static NettyChannelPool nettyChannelPool;

    /**
     * 利用管道发送消息
     * @param protocol
     * @param request
     * @throws Exception
     */
    public static void writeAndFlush(Protocol protocol,Object request) throws Exception{
        if (nettyChannelPool==null){
            synchronized (NettyClientUtil.class){
                if (nettyChannelPool==null){
                    nettyChannelPool=new NettyChannelPool(2);
                }
            }
        }
        Channel channel = nettyChannelPool.syncGetChannel(protocol, new NettyChannelPool.ConnectCall() {
            @Override
            public Channel connect(Protocol protocol) throws Exception {
                return connectToServer(protocol);
            }
        });
        channel.writeAndFlush(request).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
            }
        });
    }

    /**
     * 连接客户端
     * @param protocol
     * @return
     * @throws InterruptedException
     */
    private static Channel connectToServer(final Protocol protocol) throws InterruptedException{
        final NettyClientInHandler nettyClientInHandler = new NettyClientInHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE,true)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE,0,MessageCodecConstant.MESSAGE_LENGTH,0,MessageCodecConstant.MESSAGE_LENGTH));
                        pipeline.addLast(new LengthFieldPrepender(MessageCodecConstant.MESSAGE_LENGTH));
                        RpcSerializeFrame.select(protocol.getSerialize(),pipeline);
                        pipeline.addLast(nettyClientInHandler);
                    }
                });
        ChannelFuture future = bootstrap.connect(protocol.getHost(), Integer.parseInt(protocol.getPort()));
        Channel channel = future.sync().channel();
        return channel;
    }
}
