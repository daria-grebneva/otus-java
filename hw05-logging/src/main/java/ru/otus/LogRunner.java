package ru.otus;

import ru.otus.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class LogRunner {

    private LogRunner() {

    }

    static TestLoggingInterface createTestLoggingClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestLoggingImpl());
        return (TestLoggingInterface) Proxy.newProxyInstance(LogRunner.class.getClassLoader(),
                new Class<?>[]{TestLoggingInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final TestLoggingInterface testLoggingClass;

        DemoInvocationHandler(TestLoggingInterface testLoggingClass) {
            this.testLoggingClass = testLoggingClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isCurrentMethodWithLogging(testLoggingClass.getClass(), method)) {
                System.out.println("executed method: " + method.getName() + ", param:" + getParameters(args));
            }
            return method.invoke(testLoggingClass, args);
        }
    }

    private static List<Method> getLogMethods(Method[] methods) {
        return Arrays.stream(methods).filter(m -> m.isAnnotationPresent(Log.class)).toList();
    }

    private static boolean isCurrentMethodWithLogging(Class<?> clazz, Method currentMethod) {
        final Predicate<Method> methodPredicate = method ->
                currentMethod.getName().equals(method.getName())
                && (currentMethod.getParameterCount() == method.getParameterCount());

        return getLogMethods(clazz.getDeclaredMethods())
                .stream()
                .anyMatch(methodPredicate);
    }

    private static List<String> getParameters(Object[] args) {
        List<String> params = new ArrayList<>();
        for (Object a : args) {
            params.add(a.toString());
        }

        return params;
    }
}
