package shanghai.shu.remoting.netty.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import shanghai.shu.remoting.exchange.ResponseFuture;
import shanghai.shu.remoting.exchange.ResponseHolder;
import shanghai.shu.remoting.exchange.model.Response;

public class NettyClientInHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Response response=(Response)msg;
        Long sessionId = response.getSessionId();
        ResponseFuture future = ResponseHolder.remove(sessionId);
        if (future!=null){
            future.over(response);
        }
    }
}
