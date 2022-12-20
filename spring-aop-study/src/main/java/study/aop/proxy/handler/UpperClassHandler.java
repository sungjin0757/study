package study.aop.proxy.handler;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import study.aop.proxy.Hello;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
@Slf4j
public class UpperClassHandler implements InvocationHandler {

    private final Object target;

    @Override
    public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
        Object ret = method.invoke(target, objects);

        if(ret instanceof String && (method.getName().equals("welcome") || method.getName().equals("goodBye")))
            return String.valueOf(ret).toUpperCase();
        return ret;
    }
}
