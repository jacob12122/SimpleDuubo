package shanghai.shu.rpc.loadbalance;

import shanghai.shu.registry.RegistryNode;

import java.util.List;

/**
 * 轮询
 */
public class RoundrobinLoadBalance implements LoadBalance{
    @Override
    public RegistryNode doSelect(List<RegistryNode> registryInfo) {
        return null;
    }
}
