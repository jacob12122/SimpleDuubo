package shanghai.shu.rpc.loadbalance;

import shanghai.shu.registry.RegistryNode;

import java.util.List;

public interface LoadBalance {
    RegistryNode doSelect(List<RegistryNode> registryInfo);
}
