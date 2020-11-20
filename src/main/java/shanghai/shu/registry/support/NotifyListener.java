package shanghai.shu.registry.support;

import shanghai.shu.registry.RegistryNode;

import java.util.List;

public interface NotifyListener {
    /**
     * 全量通知
     * @param registryNodes
     */
    void notify(List<RegistryNode> registryNodes);
}
