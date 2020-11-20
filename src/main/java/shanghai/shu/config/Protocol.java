package shanghai.shu.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import shanghai.shu.remoting.netty.server.NettyServer;

public class Protocol extends BaseConfigBean implements ApplicationContextAware,ApplicationListener<ContextRefreshedEvent> {
    private static final long serialVersionUID = 7082032188443659845L;
    private String name;
    private String host;
    private String port;
    private String contextpath;
    private String threads;
    private String serialize;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getContextpath() {
        return contextpath;
    }

    public void setContextpath(String contextPath) {
        this.contextpath = contextPath;
    }


    public String getThreads() {
        return threads;
    }

    public void setThreads(String threads) {
        this.threads = threads;
    }

    public String getSerialize() {
        return serialize;
    }

    public void setSerialize(String serialize) {
        this.serialize = serialize;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
            SpringContextHolder.setApplicationContext(applicationContext);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (!ContextRefreshedEvent.class.getName().equals(event.getClass().getName())){
            return;
        }
        final Protocol protocol=this;
        if ("netty".equals(name)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        NettyServer.start(protocol);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}
