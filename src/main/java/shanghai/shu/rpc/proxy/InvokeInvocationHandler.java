package shanghai.shu.rpc.proxy;

import shanghai.shu.config.Reference;
import shanghai.shu.rpc.cluster.Cluster;
import shanghai.shu.rpc.invoke.Invocation;
import shanghai.shu.rpc.invoke.Invoke;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class InvokeInvocationHandler implements InvocationHandler {
    private Invoke invoke;
    private Reference reference;

    public InvokeInvocationHandler(Invoke invoke, Reference reference) {
        this.invoke = invoke;
        this.reference = reference;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Invocation invocation = new Invocation();
        invocation.setMethod(method);
        invocation.setObjs(args);
        invocation.setReference(reference);
        invocation.setInvoke(invoke);
        Cluster cluster = Reference.getClusters().get(reference.getCluster());
        Object result = cluster.invoke(invocation);
        return result;
    }
}
