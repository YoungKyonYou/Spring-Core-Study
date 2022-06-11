package hello.core.member;

public class MemberServiceImpl implements MemberService{

    //이런 형식은 MemberServiceImpl이 추상화에도 의존(memberRepository)하고 구체화에도(Memory MemberRepository)에도 의존한다.
    //즉 둘다 의존한다 안좋다. 변경이 일어나게 되면 안 좋다. DIP를 위반하고 있다.
//    private final MemberRepository memberRepository=new MemoryMemberRepository();
    private final MemberRepository memberRepository;

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
}
