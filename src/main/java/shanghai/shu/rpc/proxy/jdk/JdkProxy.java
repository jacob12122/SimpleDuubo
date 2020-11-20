package shanghai.shu.rpc.proxy.jdk;

import shanghai.shu.common.ReflectionCache;
import shanghai.shu.config.Reference;
import shanghai.shu.rpc.invoke.Invoke;
import shanghai.shu.rpc.proxy.InvokeInvocationHandler;
import shanghai.shu.rpc.proxy.RpcProxy;

import java.lang.reflect.Proxy;

public class JdkProxy implements RpcProxy {
    /**
     * 生成代理对象
     * @param className
     * @param invoke
     * @param reference
     * @return
     * @throws Exception
     */
    @Override
    public Object getObject(String className, Invoke invoke, Reference reference) throws Exception {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class<?>[]{ReflectionCache.putAndGetClass(className)},
                new InvokeInvocationHandler(invoke,reference));
    }
}
