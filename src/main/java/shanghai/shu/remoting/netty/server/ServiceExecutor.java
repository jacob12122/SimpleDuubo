package shanghai.shu.remoting.netty.server;

import shanghai.shu.common.ThreadPoolFactory;
import shanghai.shu.config.Protocol;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

/**
 * 业务方法执行器
 */
public class ServiceExecutor {
    private volatile static ExecutorService threadPool;

    public static void submit(Callable<Object> call, int threads, Protocol protocol){
        if (threadPool==null){
            synchronized (ServiceExecutor.class){
                if (threadPool==null){
                    threadPool=ThreadPoolFactory.initThreadPool(threads,protocol);
                }
            }
        }
        threadPool.submit(call);
    }

}
