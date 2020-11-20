package shanghai.shu.rpc.proxy.javassist;

import shanghai.shu.common.ReflectionCache;
import shanghai.shu.config.Reference;
import shanghai.shu.rpc.invoke.Invoke;
import shanghai.shu.rpc.proxy.InvokeInvocationHandler;
import shanghai.shu.rpc.proxy.RpcProxy;

public class JavassistProxy implements RpcProxy {
    @Override
    public Object getObject(String className, Invoke invoke, Reference reference) throws Exception {
        return Proxy.newProxyInstance(this.getClass().getClassLoader(),ReflectionCache.putAndGetClass(className),new InvokeInvocationHandler(invoke,reference));
    }
}
