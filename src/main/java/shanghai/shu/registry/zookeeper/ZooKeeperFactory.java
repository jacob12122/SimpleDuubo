package shanghai.shu.registry.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class ZooKeeperFactory {
    public static CuratorFramework create(String address){
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString(address).sessionTimeoutMs(1000).retryPolicy(new ExponentialBackoffRetry(1000, 5))
                .build();
        client.start();
        return client;
    }
}
