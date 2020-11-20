package shanghai.shu.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import shanghai.shu.registry.BaseRegistry;
import shanghai.shu.registry.zookeeper.ZookeeperRegistry;

import java.util.HashMap;
import java.util.Map;

public class Registry  extends BaseConfigBean implements ApplicationContextAware {
    private static final long serialVersionUID=6931270359014167547L;
    private String protocol;
    private String address;
    private static Map<String,BaseRegistry> registryMap=new HashMap<String,BaseRegistry>();

    public Map<String, BaseRegistry> getRegistryMap() {
        return registryMap;
    }
    static {
        registryMap.put("zookeeper", new ZookeeperRegistry());
    }

    public static void setRegistryMap(Map<String, BaseRegistry> registryMap) {
        Registry.registryMap = registryMap;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.setApplicationContext(applicationContext);
    }
}
