package shanghai.shu.rpc.cluster;

import shanghai.shu.rpc.invoke.Invocation;
import shanghai.shu.rpc.invoke.Invoke;

/**
 *  失效转移，调用失败就切换到其他节点
 */
public class FailoverClusterInvoke implements Cluster{
    @Override
    public Object invoke(Invocation invocation) throws Exception {
        String retries = invocation.getReference().getRetries();
        Integer retriesInt = Integer.parseInt(retries);
        for (int i = 0; i < retriesInt; i++) {
            try {
                Invoke invoke = invocation.getInvoke();
                Object result = invoke.invoke(invocation);
                return result;
            }catch (Exception e){
                e.printStackTrace();
                continue;
            }
        }
        throw new RuntimeException("retries"+retries+"全部失败!");
    }
}
