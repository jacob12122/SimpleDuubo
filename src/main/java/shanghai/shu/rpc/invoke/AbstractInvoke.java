package shanghai.shu.rpc.invoke;

import org.springframework.util.CollectionUtils;
import shanghai.shu.common.LogIds;
import shanghai.shu.config.Protocol;
import shanghai.shu.config.Reference;
import shanghai.shu.registry.RegistryNode;
import shanghai.shu.registry.support.RegistryLocalCache;
import shanghai.shu.remoting.exchange.model.Request;
import shanghai.shu.rpc.loadbalance.LoadBalance;

import java.util.List;

public abstract class AbstractInvoke implements Invoke{
    @Override
    public Object invoke(Invocation invocation) throws Exception {
        Reference reference = invocation.getReference();
        List<RegistryNode> registryInfo = RegistryLocalCache.getRegistry(reference.getInf());
        if (CollectionUtils.isEmpty(registryInfo)){
            String msg = String.format("server not fount! serverName: %s", reference.getInf());
            throw new RuntimeException(msg);
        }
        //负载均衡
        String loadbalance = reference.getLoadbalance();
        LoadBalance loadBalanceBean = Reference.getLoadBalances().get(loadbalance);
        RegistryNode registryNode = loadBalanceBean.doSelect(registryInfo);
        int timeout = this.getTimeout(registryNode, invocation);
        Request request = this.buildRequest(invocation);
        return this.doInvoke(registryNode.getProtocol(),request,timeout);

    }

    public int getTimeout(RegistryNode nodeInfo,Invocation invocation){
        int timeout = Integer.parseInt(invocation.getReference().getTimeout());
        if (timeout<0|| timeout==0){
            timeout= Integer.parseInt(nodeInfo.getService().getTimeout());
        }
        return timeout;
    }
    public Request buildRequest(Invocation invocation){
        Request request = new Request();
        request.setSessionId(LogIds.generate());
        request.setClassName(invocation.getReference().getInf());
        request.setMethodName(invocation.getMethod().getName());
        request.setParametersType(invocation.getMethod().getParameterTypes());
        request.setParametersValue(invocation.getObjs());
        return request;
    }
    abstract Object doInvoke(Protocol protocol,Request request,int timeout) throws Exception;
}
