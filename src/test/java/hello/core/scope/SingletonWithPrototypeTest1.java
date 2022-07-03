package hello.core.scope;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Provider;

/**
 * 프로토타입 빈을 언제 사용할까?
 * 사용할 때마다 의존관계 주입이 완료된 새로운 객체가 필요하면 사용하면 된다.
 * 그런데 실무에서 웹 애플리케이션을 개발해보면 싱글톤 빈으로 대부분의 문제를 해결할 수 있기 때문에
 * 프로토타입 빈을 직접적으로 사용하는 일은 매우 드물다.
 * @Lookup 애노테이션을 사용하는 방법도 있지만 이전 방법들로 충분하고 고려해야할 내용도 많아서 생략한다.
 *
 * 참고: 실무에서 자바 표준인 JSR-330 Provider를 사용할 것인지 아니면 스프링이 제공하는 ObjectProvider를 사용할 것인지
 * 고민이 된다. ObjectProvider는 DL을 위한 편의 기능을 많이 제공해주고 스프링 외에 별도의 의존관계 추가가 필요 없기 때문에 편리하다.
 * 만약(정말 그럴일은 거의 없겠지만) 코드를 스프링이 아닌 다른 컨테이너에서도 사용할 수 있어야 한다면 JSR-330 Provider를 사용해야 한다.
 * 스프링을 사용하다 보면 이 기능 뿐만 아니라 다른 기능들도 자바 표준과 스프링이 제공하는 기능이 겹칠 때가 많이 있다.
 * 대부분 스프링이 더 다양하고 편리한 기능을 제공해주기 때문에 특별히 다른 컨테이너를 사용할 일이 없다면 스프링이 제공하는 기능을 사용하면 된다.
 */
public class SingletonWithPrototypeTest1 {

    @Test
    void prototypeFind(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(PrototypeBean.class);
        PrototypeBean prototypeBean1=ac.getBean(PrototypeBean.class);
        prototypeBean1.addCount();
        Assertions.assertThat(prototypeBean1.getCount()).isEqualTo(1);

        PrototypeBean prototypeBean2=ac.getBean(PrototypeBean.class);
        prototypeBean2.addCount();
        Assertions.assertThat(prototypeBean2.getCount()).isEqualTo(1);
    }

    /**
     * 스프링은 일반적으로 싱글톤 빈을 사용하므로, 싱글톤 빈이 프로토타입 빈을 사용하게 된다. 그런데 싱글톤 빈은
     * 생성 시점에만 의존관계 주입을 받기 때문에, 프로토타입 빈이 새로 생성되기는 하지만 싱글톤 빈과 함께 계속 유지되는 것이 문제다.
     */
    @Test
    void singletonClientUsePrototype(){
        //이렇게 하면 자동 빈 등록이 된다.(그래서 따로 @Bean 등록 필요없음)
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(ClientBean.class, PrototypeBean.class);
        ClientBean clientBean1 = ac.getBean(ClientBean.class);

        int count1=clientBean1.logic();
        Assertions.assertThat(count1).isEqualTo(1);


        /**
         * 아마 원하는 것은 이런 것은 아닐 것이다. 프로토타입 빈을 주입 시점에만 새로 생성하는 게 아니라, 사용할 때마다 새로 생성해서 사용하는 것을 원할 것이다.
         */
        ClientBean clientBean2 = ac.getBean(ClientBean.class);

        int count2=clientBean2.logic();
        //똑같은 ClientBean를 clientBean1과 clientBean2에 주입 받았다. 그리고 logic()를 두번 호출 했더니
        //처음 생성해서 주입받았던 PrototypeBean를 똑같이 주입받아서 숫자가 logic() 두번을 호출해서 2번이 됐다.
        // Assertions.assertThat(count2).isEqualTo(2);
         Assertions.assertThat(count2).isEqualTo(1);

    }


    //java.inject의 provider 사용하기

    /**
     * provider.get()을 통해서 항상 새로운 프로토타입 빈이 생성되는 것을 확인할 수 있다.
     * provider의 get() 을 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환한다(DL)
     * 자바 표준이고 기능이 단순하므로 단위테스트를 만들거나 mock코드를 만들기는 훨씬 쉬워진다
     * Provider는 지금 딱 필요한 DL 정도의 기능만 제공한다. (gradle에 추가해줘야 사용가능)
     */
    @Scope("singleton")
    static class ClientBean{

        @Autowired
        private Provider<PrototypeBean> prototypeBeanProvider;

        @Autowired
        private ObjectFactory<PrototypeBean> prototypeBeanObjectFactory;

        public int logic(){
            PrototypeBean prototypeBean = prototypeBeanProvider.get();
            prototypeBean.addCount();
            int count= prototypeBean.getCount();
            return count;
        }
    }

/*    //scope의 디폴트가 싱글톤이라 안해도 되지만 그냥 명확하게 하기 위해서 적음
    @Scope("singleton")
    static class ClientBean{

        *//**
         * provider는 .getObject()를 호출하면 프로토타입 빈을 찾아서 반환해주는 역할을 한다.
         * 즉 찾아서 제공하는 역할만 한다. 우리가 원했던 필요할 때마다 객체를 새로 생성해주게 할 수 있는 것이다(Prototype scope)
         * ObjectProvider는 ObjectFactory를 상속받는다. 결론적으로 ObjectFactory는 기능이 하나 있는데 ObjectProvider는 좀 더 기능을 제공한다.
         * ObjectProvider의 getObject()를 호출하면 내부에서는 스프링 컨테이너를 통해 해당 빈을 찾아서 반환한다.
         * 스프링 제공하는 기능을 사용하지만, 기능이 단순하므로 단위테스트를 만들거나 mock코드를 만들기가 훨씬 쉬워진다.
         * ObjectProvider는 지금 딱 필요한 DL 정도의 기능만 제공한다.
         *//*
        @Autowired
        private ObjectProvider<PrototypeBean> prototypeBeanProvider;

        @Autowired
        private ObjectFactory<PrototypeBean> prototypeBeanObjectFactory;

        public int logic(){
            PrototypeBean prototypeBean = prototypeBeanProvider.getObject();
            prototypeBean.addCount();
            int count= prototypeBean.getCount();
            return count;
        }
    }*/
/*    //scope의 디폴트가 싱글톤이라 안해도 되지만 그냥 명확하게 하기 위해서 적음
    @Scope("singleton")
    static class ClientBean{
     //   private final PrototypeBean prototypeBean; //생성시점에 주입이 된다.

        @Autowired
        ApplicationContext applicationContext;

*//*        //이 시점에서 스프링컨테이너에 PrototypeBean를 요청하게 된다. 그러면 컨테이너는 PrototypeBean를 만들어서 할당해준다.
        @Autowired
        public ClientBean(PrototypeBean prototypeBean){
            this.prototypeBean=prototypeBean;
        }*//*

        public int logic(){
            PrototypeBean prototypeBean=applicationContext.getBean(PrototypeBean.class);

            //생성시점에 주입이된 prototypeBean이다.
            prototypeBean.addCount();
            int count= prototypeBean.getCount();
            return count;
        }
    }*/


    @Scope("prototype")
    static class PrototypeBean {
        private int count = 0;

        public void addCount(){
            count++;
        }

        public int getCount(){
            return count;
        }

        @PostConstruct
        public void init(){
            System.out.println("PrototypeBean.init "+this);
        }

        //이건 호출안되지만 그냥 해줬다. PrototypeTest.java 주석 참고
        @PreDestroy
        public void destroy(){
            System.out.println("PrototypeBean.destroy");
        }
    }
}
