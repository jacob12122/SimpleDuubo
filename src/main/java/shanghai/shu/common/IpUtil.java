package shanghai.shu.common;

import java.net.InetAddress;

public class IpUtil {
    /**
     * 获得本机ip
     * @return
     * @throws Exception
     */
    public static String getLocalHost() throws  Exception{
        InetAddress address = InetAddress.getLocalHost();
        String hostAddress = address.getHostAddress();
        return hostAddress;
    }
}
