package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class ConfigurationSingletonTest {

    @Test
    void configurationTest(){
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);

        MemberServiceImpl memberService = ac.getBean("memberService", MemberServiceImpl.class);
        OrderServiceImpl orderService = ac.getBean("orderService", OrderServiceImpl.class);
        MemberRepository memberRepository = ac.getBean("memberRepository", MemberRepository.class);

        MemberRepository memberRepository1 = memberService.getMemberRepository();
        MemberRepository memberRepository2 = orderService.getMemberRepository();
        /**
         * 빈이 싱글톤으로 관리된다고 했었다(이것은 디폴트로) 그런데 AppConfig.java를 봐보자
         * 만약 여기서 빈으로 등록된 memberService()를 호출하게 되면 new MemberServiceImpl() 안에
         * memberRepository()가 호출될 것이고 그러면 바로 밑에 있는 빈으로 등록된
         * memberRepository()가 호출되고 그 안에 new MemoryMemberRepository()가 호출된다
         * 그랬을 때 만약 그 밑에 빈으로 등록된 orderService()가 호출될 때
         * 그 안에 new OrderServiceImpl()에 memberRepository()를 호출하게 되면
         * 또 memberRepository new해서 호출된다. 그리고 memberRepository를 직접 호출하게 되면
         * 또 new MemoryMemberRepository()가 호출됨으로 3번 객체가 생성되서 주소가 달라져야 하는데
         * 그렇지 않다!
         */
        System.out.println("memberService -> memberRepository = "+memberRepository1);
        System.out.println("orderService -> memberRepository = "+memberRepository);
        System.out.println("memberRepository = "+memberRepository2);

        Assertions.assertThat(memberService.getMemberRepository()).isSameAs(memberRepository);
        Assertions.assertThat(orderService.getMemberRepository()).isSameAs(memberRepository);
    }

    @Test
    void configurationDeep(){
        //참고로 AnnotationConfigApplicationContext() 안에 인자로 넘기는 클래스도 bean으로 등록이 된다.
        //즉, AppConfig도 bean으로 등록이 되는 것이다.
        AnnotationConfigApplicationContext ac = new AnnotationConfigApplicationContext(AppConfig.class);
        AppConfig bean=ac.getBean(AppConfig.class);

        //순수한 클래스라면 'class hello.core.AppConfig'라고 나와야 되겠찌만
        //bean = class hello.core.AppConfig$$EnhancerBySpringCGLIB$$df7dd67e 라고 나옴
        //이것은 스프링이 CGLIB라는 바이트코드 조작 라이브러리를 사용해서 AppConfig 클래스를 상속받은 임의의 다른 클래스를 만들고,
        //그 다른 클래스를 스프링 빈으로 등록한 것이다!.
        //요약하면 @Bean이 붙은 메서드마다 이미 스프링 빈이 존재하면 존재하는 빈을 반환하고, 스프링 빈이 없으면 생성해서 스프링 빈으로 등록하고
        //반환하는 코드가 동적으로 만들어진다 덕분에 싱글톤이 보장되는 것이다.
        //@Bean만 사용해도 스프링 빈으로 등록되지만, 싱글톤을 보장하지 않는다.
        //memberRepository()처럼 의존관계 주입이 필요해서 메서드를 직접 호출할 때 싱글톤을 보장하지 않는다.
        //크게 고민할 것이 없다 스프링 설정 정보에는 항상 @Configuration를 사용하자 그러면 싱글톤을 보장해준다.
        System.out.println("bean = " + bean.getClass());
    }
}
