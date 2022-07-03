package hello.core.scan.filter;


import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * 이 MyIncludeComponent가 붙은 것은 컴포넌트 스캔에 추가한다는 것
 */
//Target이 중요하다. 어디에 붙는지 지정해주는 것이다.
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MyIncludeComponent {

}
