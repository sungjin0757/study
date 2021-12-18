package study.aop.proxy;

import lombok.RequiredArgsConstructor;

import java.util.Locale;


@RequiredArgsConstructor
public class HelloProxy implements Hello{

    private final Hello hello;

    @Override
    public String welcome(String name) {
        return hello.welcome(name).toUpperCase();
    }

    @Override
    public String goodBye(String name) {
        return hello.goodBye(name).toUpperCase();
    }
}
