package hello.core;

import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * 기존 AppConfig.java와는 다르게 @Bean으로 등록한 클래스가 하나도 없다.
 * 참고로 컴포넌트 스캔을 사용하면 @Configuration이 붙은 설정 정보도 자동으로 등록되기 때문에 AppConfig, TestConfig 등 앞서 만들어
 * 두었던 설정 정보도 함께 등록되고, 실행되어 버린다. 그래서 'excludeFilters'를 이용해서 설정정보는 컴포넌트 스캔 대상에서 제외한다.
 * 보통 설정 정보를 컴포넌트 스캔 대상에서 제외하지는 않지만, 기존 예제 코드를 최대한 남기고 유지하기 위해서 이 방법을 선택한다.
 *
 * 개인적으로 즐겨 사용하는 방법은 패키지 위치를 지정하지 않고, 설정 정보 클래스의 위치를 프로젝트 최상단에 두는 것이다. 최근 스프링 부트도 이 방법을 기본으로 제공한다.
 * 우리가 기본적으로 프로젝트를 만들면 메인 클래스가 하나 있는데 여기에 @SpringBootApplication이라는 어노테이션이 있다
 * 이것은 프로젝트 시작 루트 위치에 두는 것이 관례이고 이 설정 안에 바로 @ComponentScan이 들어 있다!!!!
 *
 * @ComponentScan의 기본 대상은
 * @Component 뿐만 아니라 @Controller, @Service, @Repository, @Configuration도 추가로 대상에 포함된다.
 */
@Configuration
//Component Scan를 사용하기 위한 어노테이션
@ComponentScan(
        //명시된 패키지를 컴포넌트 스캔을 실시한다. 다 하게 되면 무거울 수도 있으니까 그때마다 지정하거나 안 한다.
       // basePackages="hello.core.member",
        //컴포넌트 스캔으로 스프링 빈을 다 등록하는데 Filter는 뺼 것을 등록하는 것이다.
        //Configuration을 뺀다. 왜냐면 @Configuration은 수동으로 등록하는데 자동으로 등록하면 안된다.
        excludeFilters = @ComponentScan.Filter(type= FilterType.ANNOTATION, classes=Configuration.class)
)
public class AutoAppConfig {
    //빈 이름이 겹칠 때 수동 등록 빈이 우선권을 가진다. (오버라이딩 한다)
   // @Bean(name="memoryMemberRepository")
    public MemberRepository memberRepository(){
        return new MemoryMemberRepository();
    }
}
