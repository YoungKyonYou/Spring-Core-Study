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
        //라이브러리는 대부분 'close', 'shutdown'이라는 이름의 종료 메서드를 사용한다.
        //이 추론(inferred) 기능은 'close, 'shutdown'라는 이름의 메서드를 자동으로 호출해준다. 이름 그대로 종료 메서드를 추론해서 호출해준다.
        //따라서 직접 스프링 빈으로 등록하면 종료 메서드는 따로 적어주지 않아도 잘 동작한다.
        //추론 기능을 사용하기 싫으면 'destroyMethod=""'처럼 공백으로 지정하면 된다.
/*        @Bean(initMethod="init", destroyMethod="close")
        public NetworkClient networkClient(){
            NetworkClient networkClient=new NetworkClient();
            networkClient.setUrl("http:/hello-spring.dev");
            return networkClient;
        }*/

        @Bean
        public NetworkClient networkClient(){
            NetworkClient networkClient=new NetworkClient();
            networkClient.setUrl("http:/hello-spring.dev");
            return networkClient;
        }
    }
}
