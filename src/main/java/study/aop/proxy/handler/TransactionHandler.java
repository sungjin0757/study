package study.aop.proxy.handler;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class TransactionHandler implements InvocationHandler {

    private final Object target;
    private final PlatformTransactionManager transactionManager;
    private final String pattern;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(method.getName().startsWith(pattern))
            return invokeWithTransaction(method,args);
        return method.invoke(target,args);
    }

    private Object invokeWithTransaction(Method method,Object[] args) throws Throwable{
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try{
            Object invoke = method.invoke(target, args);
            transactionManager.commit(status);
            return invoke;
        }catch(InvocationTargetException e){
            transactionManager.rollback(status);
            throw e.getTargetException();
        }
    }
}
