package homework;

import homework.annotations.Test;
import homework.annotations.Before;
import homework.annotations.After;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class TestsRunner {

    public static void main(String[] args) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        String testClassName = args[0];
        try {
            Class<?> testClass = Class.forName(testClassName);
            Method[] methods = testClass.getDeclaredMethods();

            invokeMethods(getBeforeMethod(methods), getAfterMethod(methods), getTestsMethods(methods), testClass);

        } catch (ClassNotFoundException e) {
            System.out.println("No class was found");
        }
    }

    // assume no arguments in class constructor
    private static Object getClassInstance(Class<?> testClass) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> cons = testClass.getDeclaredConstructor();
        return cons.newInstance();
    }

    private static Method getBeforeMethod(Method[] methods) {
        return Arrays.stream(methods).filter(m -> m.isAnnotationPresent(Before.class)).findFirst().orElse(null);
    }

    private static Method getAfterMethod(Method[] methods) {
        return Arrays.stream(methods).filter(m -> m.isAnnotationPresent(After.class)).findFirst().orElse(null);
    }

    private static List<Method> getTestsMethods(Method[] methods) {
        return Arrays.stream(methods).filter(m -> m.isAnnotationPresent(Test.class)).toList();
    }

    private static void invokeMethods(Method before, Method after, List<Method> testMethods, Class<?> testClass) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        int allTestsNumber = testMethods.size();
        int successfulTests = allTestsNumber;
        int failedTests = 0;

        for (Method testMethod : testMethods) {
            Object classInstance = getClassInstance(testClass);

            if (before != null) {
                before.invoke(classInstance);
            }

            try {
                testMethod.invoke(classInstance);
            } catch (Exception e) {
                failedTests++;
                successfulTests--;
                System.out.println("Test was failed with exception: " + e);
            }

            if (after != null) {
                after.invoke(classInstance);
            }
        }

        printStatistic(allTestsNumber, successfulTests, failedTests);
    }

    private static void printStatistic(int allTestsNumber, int successfulTests, int failedTests) {
        System.out.println("\nnumber of tests running: " + allTestsNumber + "\nsuccessful: " + successfulTests + "\nfailed: " + failedTests);
    }
}
