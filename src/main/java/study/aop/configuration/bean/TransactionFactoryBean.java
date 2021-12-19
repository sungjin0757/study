package study.aop.configuration.bean;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import study.aop.proxy.handler.TransactionHandler;

import java.lang.reflect.Proxy;

@RequiredArgsConstructor
@Getter
public class TransactionFactoryBean implements FactoryBean<Object> {

    private final Object target;
    private final PlatformTransactionManager transactionManager;
    private final String pattern;
    private final Class<?> interfaces;

    @Override
    public Object getObject() throws Exception {
        return Proxy.newProxyInstance(getClass().getClassLoader(),new Class[]{interfaces},new TransactionHandler(target,
                transactionManager,pattern));
    }

    @Override
    public Class<?> getObjectType() {
        return interfaces;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
