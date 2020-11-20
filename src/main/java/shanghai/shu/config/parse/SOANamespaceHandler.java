package shanghai.shu.config.parse;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import shanghai.shu.config.Protocol;
import shanghai.shu.config.Reference;
import shanghai.shu.config.Registry;
import shanghai.shu.config.Service;

public class SOANamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("registry", new RegistryBeanDefinitionParse(Registry.class));
        registerBeanDefinitionParser("protocol", new ProtocolBeanDefinitionParse(Protocol.class));
        registerBeanDefinitionParser("reference", new ReferenceBeanDefinitionParse(Reference.class));
        registerBeanDefinitionParser("service", new ServiceBeanDefinitionParse(Service.class));
    }
}
