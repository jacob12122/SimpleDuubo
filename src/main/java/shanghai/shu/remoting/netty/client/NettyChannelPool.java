package shanghai.shu.remoting.netty.client;

import io.netty.channel.Channel;
import shanghai.shu.config.Protocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class NettyChannelPool{
    /**
     * 每个ip端口建议一个长连接
     */
    private volatile Map<String,Channel[]> channelMap=new ConcurrentHashMap<>();

    private int connections;

    public NettyChannelPool(int connections){
        this.connections=connections;
    }

    /**
     * 同步获取netty channel
     * @param protocol
     * @param call
     * @return
     * @throws Exception
     */
    public Channel syncGetChannel(Protocol protocol,ConnectCall call) throws Exception{
        String host=protocol.getHost()+":"+protocol.getPort();
        Channel[] channels = channelMap.get(host);
        if (channels==null){
            synchronized (host.intern()){
                if (channelMap.get(host)==null){
                    channelMap.put(host,new Channel[connections]);
                }
            }
        }
        int index=connections==1?0:ThreadLocalRandom.current().nextInt(connections);
        Channel channel = channelMap.get(host)[index];
        if (channel!=null&&channel.isActive()){
            return channel;
        }
        channel=call.connect(protocol);
        channelMap.get(host)[index]=channel;
        return channel;
    }
    public interface ConnectCall{
        Channel connect(Protocol protocol) throws Exception;
    }
}
