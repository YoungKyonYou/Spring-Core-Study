package hello.core;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class HelloLombok {

    private String name;
    private int age;

    public static void main(String[] args) {
       HelloLombok helloLombok=new HelloLombok();
       helloLombok.setName("asdfas");

       String name=helloLombok.getName();
       System.out.println("name = " + name);
    }
}
