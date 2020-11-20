package shanghai.shu.rpc.cluster;

import shanghai.shu.rpc.invoke.Invocation;
import shanghai.shu.rpc.invoke.Invoke;

/**
 * 直接忽略
 */
public class FailsafeClusterInvoke implements Cluster{
    @Override
    public Object invoke(Invocation invocation) throws Exception {
        Invoke invoke = invocation.getInvoke();

        try {
            Object result = invoke.invoke(invocation);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return "忽略";
        }
    }
}
