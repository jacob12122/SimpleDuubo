package shanghai.shu.registry;

import shanghai.shu.config.Protocol;
import shanghai.shu.config.Service;

public class RegistryNode {
    private Protocol protocol;
    private Service service;

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
