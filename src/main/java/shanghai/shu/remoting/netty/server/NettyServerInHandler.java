package shanghai.shu.remoting.netty.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import shanghai.shu.config.Protocol;
import shanghai.shu.config.SpringContextHolder;
import shanghai.shu.remoting.exchange.model.Request;
import shanghai.shu.remoting.exchange.model.Response;

public class NettyServerInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //反序列化
        Request request=(Request) msg;
        Response response = new Response();
        Protocol protocol = SpringContextHolder.getBean(Protocol.class);
        int threads=Integer.parseInt(protocol.getThreads());
        ServiceExecutor.submit(new ServiceTask(request,response,ctx),threads,protocol);
    }
}
