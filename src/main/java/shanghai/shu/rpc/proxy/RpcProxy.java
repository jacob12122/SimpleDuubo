package shanghai.shu.rpc.proxy;

import shanghai.shu.config.Reference;
import shanghai.shu.rpc.invoke.Invoke;

public interface RpcProxy {
    Object getObject(String className, Invoke invoke, Reference reference) throws Exception;
}
