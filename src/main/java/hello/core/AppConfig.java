package hello.core;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import hello.core.member.MemoryMemberRepository;
import hello.core.order.OrderService;
import hello.core.order.OrderServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 애플리케이션의 구성(설정) 정보를 담당한다.
 * 또한, @Configuration을 붙이면 바이트코드를 조작하는 CGLIB 기술을 사용해서
 * 싱글톤을 보장하지만, 만약 @Bean만 적용하면 CGLIB 기술을 사용하지 않는다.
 * ConfigurationSingletonTest.java의 configurationDeep() 메서드 주석 부분 확인
 */
@Configuration
public class AppConfig {
    //이렇게 하면 Spring Container에 등록이 된다.
    //이렇게 Spring Container에 등록하면 메서드 이름으로 등록이 되는데 @Bean(name="")으로 등록될 이름을 바꿀 수도 있음
    @Bean
    public MemberService memberService(){
        System.out.println("call AppConfig.memberService");
        return new MemberServiceImpl(memberRepository());
    }
    @Bean
    public MemoryMemberRepository memberRepository() {
        System.out.println("call AppConfig.memberRepository");
        return new MemoryMemberRepository();
    }
    @Bean
    public OrderService orderService(){
        System.out.println("call AppConfig.orderService");
        return new OrderServiceImpl(memberRepository(), discountPolicy());
    }
    @Bean
    public DiscountPolicy discountPolicy(){
        return new FixDiscountPolicy();
    }
}
