package shanghai.shu.registry;

import shanghai.shu.config.Registry;
import shanghai.shu.config.SpringContextHolder;
import shanghai.shu.registry.support.RegistryListener;

import java.util.List;

public class BaseRegistryDelegate {
    public static void registry(String interfaceName){
        Registry registry = SpringContextHolder.getBean(Registry.class);
        String protocol = registry.getProtocol();
        BaseRegistry registryBean = registry.getRegistryMap().get(protocol);
        registryBean.registry(interfaceName);
    }

    public static List<RegistryNode> getRegistry(String interfaceName){
        Registry registry = SpringContextHolder.getBean(Registry.class);
        String protocol = registry.getProtocol();
        BaseRegistry registryBean = registry.getRegistryMap().get(protocol);
        List<RegistryNode> registryNodeList = registryBean.getRegistry(interfaceName);
        registryBean.subscribe(interfaceName,new RegistryListener(interfaceName));
        return registryNodeList;
    }

}
