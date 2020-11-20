package shanghai.shu.remoting.http.client;

import org.springframework.util.Base64Utils;
import shanghai.shu.common.serialize.hessian.HessianCodecUtil;
import shanghai.shu.config.Protocol;
import shanghai.shu.remoting.exchange.model.Request;
import shanghai.shu.remoting.exchange.model.Response;

import java.io.IOException;
import java.util.HashMap;

public class HttpClient {
    public static Object request(Protocol protocol, Request request,int timeout) throws IOException{
        //url
        String url="http://"+protocol.getHost()+":"+protocol.getPort()+protocol.getContextpath();
        //请求参数
        HashMap<String,String> requestMap = new HashMap<>(1);
        byte[] reqBody = HessianCodecUtil.encode(request);
        String q = Base64Utils.encodeToString(reqBody);
        requestMap.put("q",q);
        String result = HttpClientUtils.post(url, requestMap, timeout);
        byte[] respBody = Base64Utils.decodeFromString(result);
        Response response = (Response)HessianCodecUtil.decode(respBody);
        return response.getResult();
    }
}
