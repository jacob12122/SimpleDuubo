package shanghai.shu.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import shanghai.shu.test.service.CalcService;
import shanghai.shu.test.service.UserService;

public class ClientTest {
    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config-consumer.xml");
        CalcService calc = applicationContext.getBean(CalcService.class);
        System.out.println(calc.add(1,1));
        System.out.println(calc.add(2,2));
        System.out.println(calc.add(3,3));
        UserService userService = applicationContext.getBean(UserService.class);
        System.out.println(userService.sayHello("tstd"));
        System.out.println(userService.sayHello("yancey"));
    }
}
