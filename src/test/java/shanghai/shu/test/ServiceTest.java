package shanghai.shu.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class ServiceTest {
    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("spring-config-provider.xml");
        System.in.read();
    }
}
