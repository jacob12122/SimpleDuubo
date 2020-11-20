package shanghai.shu.remoting.netty.server;

import io.netty.channel.ChannelHandlerContext;
import shanghai.shu.remoting.ResponseClientUtil;
import shanghai.shu.remoting.exchange.model.Request;
import shanghai.shu.remoting.exchange.model.Response;

import java.util.concurrent.Callable;

public class ServiceTask implements Callable<Object> {
    private Request request;
    private Response response;
    private ChannelHandlerContext ctx;

    public ServiceTask(Request request, Response response, ChannelHandlerContext ctx) {
        this.request = request;
        this.response = response;
        this.ctx = ctx;
    }

    @Override
    public Object call() throws Exception {
        Response response = ResponseClientUtil.response(request, this.response);
        return true;
    }
}
