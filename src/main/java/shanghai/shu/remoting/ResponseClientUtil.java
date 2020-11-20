package shanghai.shu.remoting;

import com.esotericsoftware.reflectasm.MethodAccess;
import shanghai.shu.common.ReflectionCache;
import shanghai.shu.config.SpringContextHolder;
import shanghai.shu.remoting.exchange.model.Request;
import shanghai.shu.remoting.exchange.model.Response;

public class ResponseClientUtil {
    public static Response response(Request request,Response response) {
        //取出对应的sessionId
        response.setSessionId(request.getSessionId());
        try {
            Object result = reflect(request);
            response.setResult(result);
            response.setResultCode(ErrorCode.SUCCESS.errorCode);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResultCode(ErrorCode.ERROR.errorCode);
            response.setErrorMsg(e.getMessage());
        }
        return response;
    }


    private static Object reflect(Request request) throws Exception{
        Class<?> clazz = ReflectionCache.putAndGetClass(request.getClassName());
        Object serviceBean = SpringContextHolder.getBean(clazz);
        MethodAccess methodAccess = ReflectionCache.putAndGetMethodAccess(serviceBean.getClass());
        Integer methodIndex = ReflectionCache.putAndGetMethodIndex(serviceBean.getClass(), request.getMethodName(), request.getParametersType(), methodAccess);
        Object result = methodAccess.invoke(serviceBean, methodIndex, request.getParametersValue());
        return result;
    }
}
