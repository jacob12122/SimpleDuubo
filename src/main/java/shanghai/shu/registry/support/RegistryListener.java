package shanghai.shu.registry.support;

import shanghai.shu.registry.RegistryNode;

import java.util.List;

public class RegistryListener implements NotifyListener{
    private String interfaceName;

    public RegistryListener(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    @Override
    public void notify(List<RegistryNode> registryNodes) {
        RegistryLocalCache.setRegistry(interfaceName,registryNodes);
    }
}
