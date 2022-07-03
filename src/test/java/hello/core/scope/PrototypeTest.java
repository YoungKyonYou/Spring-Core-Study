package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * 싱글톤 빈은 스프링 컨테이너 생성 시점에 초기화 메서드가 실행 되지만, 프로토타입 스코프의 빈은 스프링 컨테이너에서 빈을
 * 조회할 때 생성되고, 초기화 메서드도 실행된다.
 * 프로토아입 빈을 2번 조회했으므로 완전히 다른 스프링 빈이 생성되고 초기화도 2번 실행된 것을 확인할 수 있다.
 * 싱글톤 빈은 스프링 컨테이너가 관리하기 때문에 스프링 컨테이너가 종료될 때 빈의 종료 메서드가 실행되지만 프로토타입 빈은
 * 스프링 컨테이너가 생성과 의존관계 주입 그리고 초기화까지만 관여하고 더는 관리하지 않는다. 따라서 프로토타입 빈은 스프링 컨테이너가 종료될 때
 * @PreDestroy 같은 종료 메서드가 전혀 실행되지 않는다.
 *
 * 프로토타입 빈의 특징 정리
 * 스프링 컨테이너에 요청할 때마다 새로 생성된다
 * 스프링 컨테이너는 프로토타입 빈의 생성과 의존관계 주입 그리고 초기화까지만 관여한다.
 * 종료 메서드가 호출되지 않는다.
 * 그래서 프로토타입 빈은 프로토타입 빈을 조회한 클라이언트가 관리해야 한다. 종료 메서드에 대한 호출도 클라이언트가 직접 해야 한다.
 */
public class PrototypeTest {

    @Test
    void prototypeBeanFind(){
        //아래 static class PrototypeBean에 어노테이션 @Bean이 없다고 질문할 수 있지만 여기 AnnotationConfigApplicationContext 안에 PrototypeBean.class
        //를 지정해주면 컴포넌트 스캔 대상으로 등록을 해서 필요없다.
        AnnotationConfigApplicationContext ac=new AnnotationConfigApplicationContext(PrototypeBean.class);
        System.out.println("find prototypeBean1");
        PrototypeBean prototypeBean1 = ac.getBean(PrototypeBean.class);
        System.out.println("find prototypeBean2");
        PrototypeBean prototypeBean2 = ac.getBean(PrototypeBean.class);
        System.out.println("prototypeBean1 = " + prototypeBean2);
        System.out.println("prototypeBean2 = " + prototypeBean1);

        Assertions.assertThat(prototypeBean1).isNotSameAs(prototypeBean2);


/*        //싱글톤 빈과 다르게 destroy할때는 이렇게 수동으로 호출 해줘야 한다.
        prototypeBean1.destroy();
        prototypeBean2.destroy();*/
        //close를 해도 destroy()가 호출되지 않는다 (프로토타입 스코프에서는, 싱글톤에서는 호출됨)
        ac.close();
    }

    @Scope("prototype")
    static class PrototypeBean{
        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init");
        }
        @PreDestroy
        public void destroy(){
            System.out.println("PrototypeBean.destroy");
        }
    }
}
