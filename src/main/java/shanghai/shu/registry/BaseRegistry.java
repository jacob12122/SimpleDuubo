package shanghai.shu.registry;

import shanghai.shu.registry.support.NotifyListener;

import java.util.List;

public interface BaseRegistry {
    /**
     * 注册服务
     * @param interfaceName
     * @return
     */
    boolean registry(String interfaceName);

    /**
     * 取消注册服务
     * @param interfaceName
     * @param registryNode
     */
    void unregistry(String interfaceName,RegistryNode registryNode);

    /**
     * 订阅服务
     * @param interfaceName
     * @param listener
     */
    void subscribe(String interfaceName,NotifyListener listener);

    /**
     * 取消订阅
     * @param interfaceName
     * @param listener
     */
    void unsubscribe(String interfaceName,NotifyListener listener);

    /**
     * 获取服务列表
     * @param interfaceName
     * @return
     */
    List<RegistryNode> getRegistry(String interfaceName);
}
