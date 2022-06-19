package hello.core.singleton;

import hello.core.AppConfig;
import hello.core.member.MemberRepository;
import hello.core.member.MemberServiceImpl;
import hello.core.order.OrderServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
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
}
