package study.aop.proxy;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

public class SimpleProxyTest {

    @Test
    @DisplayName("Simple Proxy Test")
    void 간단한_프록시_테스트(){
        Hello hello=new HelloTarget();
        Assertions.assertThat(hello.welcome("h")).isEqualTo("h Welcome");
        Assertions.assertThat(hello.goodBye("h")).isEqualTo("h Good Bye");

        Hello decoratedHello=new HelloProxy(hello);
        Assertions.assertThat(decoratedHello.welcome("h")).isEqualTo("h Welcome".toUpperCase());
        Assertions.assertThat(decoratedHello.goodBye("h")).isEqualTo("h Good Bye".toUpperCase());

    }
}
