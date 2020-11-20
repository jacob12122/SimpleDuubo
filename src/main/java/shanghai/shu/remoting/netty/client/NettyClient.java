package shanghai.shu.remoting.netty.client;

import shanghai.shu.config.Protocol;
import shanghai.shu.remoting.exchange.ResponseFuture;
import shanghai.shu.remoting.exchange.model.Request;

public class NettyClient {
    /**
     * 通过netty发送消息
     * @return
     */
    public static Object request(Protocol protocol, Request request,int timeout) throws Exception{
        //通过netty拿到响应结果
        ResponseFuture future = new ResponseFuture(request);
        try {
            NettyClientUtil.writeAndFlush(protocol, request);
        }catch (Exception e){
            future.cancel();
            throw e;
        }
        return future.start(timeout);
    }
}
