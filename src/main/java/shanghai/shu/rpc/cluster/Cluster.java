package shanghai.shu.rpc.cluster;

import shanghai.shu.rpc.invoke.Invocation;

/**
 * 集群容错接口
 */
public interface Cluster {
    Object invoke(Invocation invocation) throws Exception;
}
