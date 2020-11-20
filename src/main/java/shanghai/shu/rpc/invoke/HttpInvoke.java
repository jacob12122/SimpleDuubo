package shanghai.shu.rpc.invoke;

import shanghai.shu.config.Protocol;
import shanghai.shu.remoting.exchange.model.Request;
import shanghai.shu.remoting.http.client.HttpClient;

public class HttpInvoke extends AbstractInvoke{
    @Override
    Object doInvoke(Protocol protocol, Request request, int timeout) throws Exception {
        return HttpClient.request(protocol,request,timeout);
    }
}
