package hello.core.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//컴포넌트 스캔을 하기 위해서 필요 , AutoAppConfig.java 주석 참고

/**
 * 일단 컴포넌트 스캔은 @Component가 붙인 것들을 자동으로 클래스 paths를 다 뒤져서
 * 여기 안에 있는 것들을 다 Spring Bean으로 등록해준다.(스프링 컨테이너에 다 등록해줌)
 * 그러면 그때부터 찾아 쓸 수 있게 된다. 그런데 자동으로 등록하니까 무슨 문제가 생기냐면
 * 의존 관계를 등록할 수 있는 방법이 없는 것이다. 이전에는 수동으로 설정할 때는
 * MemberServiceImpl은 이렇게 주입해 하고 설정정보를 AppConfig.java에서 등록해줬는데
 * 여기서는 설정정보를 안 쓰기 때문에 의존관계 자동 주입을 사용해서 사용하는 것이다. 그게 바로 @Autowired인 것이다.
 * 이때 타입을 기준으로 컨테이너를 조회해서 빈을 가져오게 된다.
 */
@Component
public class MemberServiceImpl implements MemberService{

    //이런 형식은 MemberServiceImpl이 추상화에도 의존(memberRepository)하고 구체화에도(Memory MemberRepository)에도 의존한다.
    //즉 둘다 의존한다 안좋다. 변경이 일어나게 되면 안 좋다. DIP를 위반하고 있다.
//    private final MemberRepository memberRepository=new MemoryMemberRepository();
    private final MemberRepository memberRepository;

    /**
     * 이 @Autowired는 스프링이 매개변수의 MemberRepository 만든 것을 찾아와서 의존관계 주입을 자동으로 연결해서 주입해준다.
     * 그래서 ComponentScan를 쓰면 이 Autowired를 사용하게 된다. 즉, @Autowired를 쓰면 이 현재 클래스(MemberSerivceImpl)를 자동으로
     * 빈으로 등록해주고 MemberRepository 타입에 맞는 스프링 빈을 여기에 주입해주는 것이다. 마치 ac.getBean(MemberRepository.class)으로
     * 동작한다고 생각하면 된다. 더 기능이 있긴 하다. 이전에는 AppConfig에서는 @Bean으로 직접 설정 정보를 작성했고 의존관계도 직접 명시했으나
     * 이제는 이런 설정 정보 자체가 없기 때문에 의존관계 주입도 이 클래스 안에서 해결해야 한다.
     */
    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public void join(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Member findMember(Long memberId) {
        return memberRepository.findById(memberId);
    }

    //테스트 용도
    public MemberRepository getMemberRepository(){
        return memberRepository;
    }
}
