package shanghai.shu.remoting.exchange;

import shanghai.shu.config.Protocol;
import shanghai.shu.remoting.exchange.model.Request;

public interface ExchangeClient {

    ResponseFuture request(Protocol protocol, Request request,int timeout);
}
