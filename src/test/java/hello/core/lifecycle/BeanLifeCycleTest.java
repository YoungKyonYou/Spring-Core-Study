package hello.core.lifecycle;

import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanLifeCycleTest {
    @Test
    public void lifeCycleTest(){
        ConfigurableApplicationContext ac = new AnnotationConfigApplicationContext(LifeCycleConfig.class);
        NetworkClient client=ac.getBean(NetworkClient.class);
        ac.close();
    }

/*    @Configuration
    static class LifeCycleConfig{

        public NetworkClient networkClient(){
            NetworkClient networkClient=new NetworkClient();
            networkClient.setUrl("http:/hello-spring.dev");
            return networkClient;
        }
    }*/

    @Configuration
    static class LifeCycleConfig{
        //이렇게 하면 메서드 이름을 자유롭게 줄 수 있다.
        //스프링 코드에 의존하지 않는 장점이 있다.
        //코드가 아니라 설정 정보를 사용하기 때문에 코드를 고칠 수 없는 외부 라이브러리에도 초기화, 종료 메서드를 적용할 수 있다.
        //그리고 이 destroyMethod 옵션엔 특별한 기능이 있다. destroyMethod 옵션의 디폴트 값은 "(inferred)"이다.
        @Bean(initMethod="init", destroyMethod="close")
        public NetworkClient networkClient(){
            NetworkClient networkClient=new NetworkClient();
            networkClient.setUrl("http:/hello-spring.dev");
            return networkClient;
        }
    }
}
