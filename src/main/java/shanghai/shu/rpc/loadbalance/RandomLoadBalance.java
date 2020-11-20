package shanghai.shu.rpc.loadbalance;

import shanghai.shu.registry.RegistryNode;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机
 */
public class RandomLoadBalance implements LoadBalance{
    @Override
    public RegistryNode doSelect(List<RegistryNode> registryInfo) {
        int size = registryInfo.size();
        int index=0;
        if (size>1){
            index = ThreadLocalRandom.current().nextInt(size);
        }
        RegistryNode node = registryInfo.get(index);
        return node;
    }
}
