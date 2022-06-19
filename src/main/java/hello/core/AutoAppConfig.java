package hello.core;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * 기존 AppConfig.java와는 다르게 @Bean으로 등록한 클래스가 하나도 없다.
 * 참고로 컴포넌트 스캔을 사용하면 @Configuration이 붙은 설정 정보도 자동으로 등록되기 때문에 AppConfig, TestConfig 등 앞서 만들어
 * 두었던 설정 정보도 함께 등록되고, 실행되어 버린다. 그래서 'excludeFilters'를 이용해서 설정정보는 컴포넌트 스캔 대상에서 제외한다.
 * 보통 설정 정보를 컴포넌트 스캔 대상에서 제외하지는 않지만, 기존 예제 코드를 최대한 남기고 유지하기 위해서 이 방법을 선택한다.
 */
@Configuration
//Component Scan를 사용하기 위한 어노테이션
@ComponentScan(
        //컴포넌트 스캔으로 스프링 빈을 다 등록하는데 Filter는 뺼 것을 등록하는 것이다.
        //Configuration을 뺀다. 왜냐면 @Configuration은 수동으로 등록하는데 자동으로 등록하면 안된다.
        excludeFilters = @ComponentScan.Filter(type= FilterType.ANNOTATION, classes=Configuration.class)
)
public class AutoAppConfig {

}
