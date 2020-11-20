package shanghai.shu.common.serialize.hessian;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * Hessian序列化/反序列化池
 */
public class HessianSerializePool {
    private GenericObjectPool<HessianSerialize> hessianPool;
    private volatile static HessianSerializePool poolFactory=null;
    private HessianSerializePool(){
        hessianPool=new GenericObjectPool<HessianSerialize>(new HessianSerializeFactory());
    }

    public static HessianSerializePool getHessianPoolInstance(){
        if (poolFactory==null){
            synchronized (HessianSerializePool.class){
                if (poolFactory==null){
                    poolFactory = new HessianSerializePool();
                }
            }
        }
        return poolFactory;
    }
    public HessianSerializePool(final int maxTotal, final int minIdle, final long maxWaitMillis, final long minEvictableIdleTimeMillis) {
        hessianPool = new GenericObjectPool<HessianSerialize>(new HessianSerializeFactory());
        GenericObjectPoolConfig config = new GenericObjectPoolConfig();
        //最大池对象总数
        config.setMaxTotal(maxTotal);
        //最小空闲数
        config.setMinIdle(minIdle);
        //最大等待时间， 默认的值为-1，表示无限等待
        config.setMaxWaitMillis(maxWaitMillis);
        //退出连接的最小空闲时间 默认1800000毫秒
        config.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        hessianPool.setConfig(config);
    }

    public HessianSerialize borrow() {
        try {
            return getHessianPool().borrowObject();
        } catch (final Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void restore(final HessianSerialize object) {
        getHessianPool().returnObject(object);
    }

    public GenericObjectPool<HessianSerialize> getHessianPool() {
        return hessianPool;
    }
}
