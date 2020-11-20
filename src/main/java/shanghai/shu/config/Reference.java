package shanghai.shu.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import shanghai.shu.common.ReflectionCache;
import shanghai.shu.registry.BaseRegistry;
import shanghai.shu.registry.BaseRegistryDelegate;
import shanghai.shu.registry.RegistryNode;
import shanghai.shu.registry.support.RegistryLocalCache;
import shanghai.shu.rpc.cluster.Cluster;
import shanghai.shu.rpc.cluster.FailfastClusterInvoke;
import shanghai.shu.rpc.cluster.FailoverClusterInvoke;
import shanghai.shu.rpc.cluster.FailsafeClusterInvoke;
import shanghai.shu.rpc.invoke.HttpInvoke;
import shanghai.shu.rpc.invoke.Invoke;
import shanghai.shu.rpc.invoke.NettyInvoke;
import shanghai.shu.rpc.loadbalance.LoadBalance;
import shanghai.shu.rpc.loadbalance.RandomLoadBalance;
import shanghai.shu.rpc.loadbalance.RoundrobinLoadBalance;
import shanghai.shu.rpc.proxy.RpcProxy;
import shanghai.shu.rpc.proxy.javassist.JavassistProxy;
import shanghai.shu.rpc.proxy.jdk.JdkProxy;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reference extends BaseConfigBean implements ApplicationContextAware,FactoryBean,InitializingBean {
    private static final long serialVersionUID = 8473037023470434275L;
    private String id;
    private String inf;
    private String loadbalance;
    private String protocol;
    private String cluster;
    private String retries;
    private String timeout;
    private String proxy;
    /**
     * 调用者
     */
    private static Map<String,Invoke> invokes=new HashMap<String, Invoke>();
    /**
     * 负载策略
     */
    private static Map<String,LoadBalance> loadBalances=new HashMap<>();
    /**
     * 集群容错策略
     */
    private static Map<String,Cluster> clusters=new HashMap<>();
    /**
     * 动态代理接口
     */
    private static Map<String,RpcProxy> proxys=new HashMap<>();

    static {
        invokes.put("netty",new NettyInvoke());
        invokes.put("http",new HttpInvoke());
        loadBalances.put("random",new RandomLoadBalance());
        loadBalances.put("rounddrob",new RoundrobinLoadBalance());
        clusters.put("failover",new FailoverClusterInvoke());
        clusters.put("failfast",new FailfastClusterInvoke());
        clusters.put("failsafe",new FailsafeClusterInvoke());
        proxys.put("javassist",new JavassistProxy());
        proxys.put("jdk",new JdkProxy());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInf() {
        return inf;
    }

    public void setInf(String inf) {
        this.inf = inf;
    }

    public String getLoadbalance() {
        return loadbalance;
    }

    public void setLoadbalance(String loadbalance) {
        this.loadbalance = loadbalance;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getRetries() {
        return retries;
    }

    public void setRetries(String retries) {
        this.retries = retries;
    }

    public String getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }

    public String getProxy() {
        return proxy;
    }

    public void setProxy(String proxy) {
        this.proxy = proxy;
    }

    public static Map<String, Invoke> getInvokes() {
        return invokes;
    }

    public static void setInvokes(Map<String, Invoke> invokes) {
        Reference.invokes = invokes;
    }

    public static Map<String, LoadBalance> getLoadBalances() {
        return loadBalances;
    }

    public static void setLoadBalances(Map<String, LoadBalance> loadBalances) {
        Reference.loadBalances = loadBalances;
    }

    public static Map<String, Cluster> getClusters() {
        return clusters;
    }

    public static void setClusters(Map<String, Cluster> clusters) {
        Reference.clusters = clusters;
    }

    public static Map<String, RpcProxy> getProxys() {
        return proxys;
    }

    public static void setProxys(Map<String, RpcProxy> proxys) {
        Reference.proxys = proxys;
    }

    /**
     * 返回一个spring管理的实例，可以从spring
     * 上下文得到
     * @return
     * @throws Exception
     */
    @Override
    public Object getObject() throws Exception {
        Invoke invoke;
        if (protocol!=null&&!"".equals(protocol)){
            invoke=invokes.get(protocol);
        }else {
            Protocol prot = SpringContextHolder.getBean(Protocol.class);
            if (prot!=null){
                invoke = invokes.get(prot.getName());
            }else {
                throw new RuntimeException("Protocol is null");
            }
        }
        return proxys.get(proxy).getObject(inf,invoke,this);
    }

    /**
     * 返回实例的类型
     * @return
     */
    @Override
    public Class<?> getObjectType() {
        try {
            if (inf != null && !"".equals(inf)) {
                return ReflectionCache.putAndGetClass(inf);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextHolder.setApplicationContext(applicationContext);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<RegistryNode> registryInfo = BaseRegistryDelegate.getRegistry(inf);
        RegistryLocalCache.setRegistry(inf,registryInfo);
    }
}
