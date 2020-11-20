package shanghai.shu.remoting.http.server;

import shanghai.shu.common.ThreadPoolFactory;
import shanghai.shu.config.Protocol;
import shanghai.shu.config.SpringContextHolder;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.ThreadPoolExecutor;

public class ApplicationListener implements ServletContextListener {
    //拿到服务端协议配置
    private Protocol protocol=SpringContextHolder.getBean(Protocol.class);

    int threads=Integer.parseInt(protocol.getThreads());
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ThreadPoolExecutor executor = ThreadPoolFactory.initThreadPool(threads, protocol);
        sce.getServletContext().setAttribute("executor",executor);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) sce.getServletContext().getAttribute("executor");
        executor.shutdown();
    }
}
