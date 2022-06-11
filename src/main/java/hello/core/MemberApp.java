package hello.core;

import hello.core.member.Grade;
import hello.core.member.Member;
import hello.core.member.MemberService;
import hello.core.member.MemberServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MemberApp {
    public static void main(String[] args) {
//        AppConfig appConfig=new AppConfig();
//        MemberService memberService=appConfig.memberService();
//        MemberService memberService=new MemberServiceImpl();

        //이제 스프링을 시작해본다. 스프링은 ApplicationContext부터 시작하는데 이것이 Spring container라고 생각하면 된다.
        //그리고 AnnotationConfigApplicationContext를 적어줘야 한다.여기보면 Annotation이라는 단어를 볼 수 있는데
        //우리가 AppConfig.java에서 어노테이션을 적어준 게 있는데 이것을 기반으로 설정을 하겠다는 것이다.(@Bean, @Configuration 이런 것들)
        //그리고 파라미터로 AppConfig.java에 있는 어노테이션 붙여진 것을 Spring Container에서 객체 생성해서 다 관리해준다.
        //기존에 우리가 이것을 하기 전에 AppConfig 객체를 만들어서 설정해줬다면 이제는 Spring Container를 통해서 찾아오게 된다.
        ApplicationContext applicationContext=new AnnotationConfigApplicationContext(AppConfig.class);

        //이것은 AppConfig.java에서 memberService() 메서드를 꺼내오겠다는 것이다. (@Bean이 붙여짐)
        //AppConfig.java를 보면 memberService()메서드 위에 @Bean이 붙여져 있는데 Spring Container에도 이 메서드 이름이 등록되기 때문이다.
        MemberService memberService = applicationContext.getBean("memberService", MemberService.class);

        Member member = new Member(1L, "memberA", Grade.VIP);
        memberService.join(member);

        Member findMember = memberService.findMember(1L);
        System.out.println("new member = "+member.getName());
        System.out.println("find Member = " + findMember.getName());

    }
}
