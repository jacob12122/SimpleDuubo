package shanghai.shu.common;

import shanghai.shu.config.Protocol;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolFactory {
    private static final AtomicInteger mThreadNum=new AtomicInteger(0);
    public static ThreadPoolExecutor initThreadPool(int threads, Protocol protocol){
        final String threadName="SimpleDubboThread-"+protocol.getHost()+":"+protocol.getPort();
        int corePoolSize=Runtime.getRuntime().availableProcessors()*2;
        int maxPoolSize=threads>corePoolSize?threads:corePoolSize;
        return new ThreadPoolExecutor(maxPoolSize, maxPoolSize, 0, TimeUnit.MILLISECONDS,
                new SynchronousQueue<Runnable>(), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread ret = new Thread(runnable, threadName + "-" + mThreadNum.incrementAndGet());
                ret.setDaemon(true);
                return ret;
            }
        },new AbortPolicyWithReport(threadName,protocol));
    }

    static class AbortPolicyWithReport extends ThreadPoolExecutor.AbortPolicy{
        String threadName;
        Protocol protocol;

        AbortPolicyWithReport(String threadName, Protocol protocol) {
            this.threadName = threadName;
            this.protocol = protocol;
        }


        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            String msg = String.format("Thread pool is EXHAUSTED!");
            throw new RejectedExecutionException(msg);
        }
    }
}
