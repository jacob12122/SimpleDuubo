package shanghai.shu.rpc.invoke;

import shanghai.shu.config.Protocol;
import shanghai.shu.remoting.exchange.model.Request;
import shanghai.shu.remoting.netty.client.NettyClient;

public class NettyInvoke extends AbstractInvoke{
    @Override
    Object doInvoke(Protocol protocol, Request request, int timeout) throws Exception {
        return NettyClient.request(protocol,request,timeout);
    }
}
