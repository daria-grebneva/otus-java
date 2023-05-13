package ru.otus.appcontainer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppComponentsContainerImpl.class);
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        List<Method> parsedMethods = getAnnotatedMethods(configClass);
        Object config = initializeConfig(configClass);

        parsedMethods.forEach((m) -> {
            String componentName = m.getAnnotation(AppComponent.class).name().toLowerCase();

            if (appComponentsByName.containsKey(componentName)) {
                throw new RuntimeException("Found components with the same names");
            }

            try {
                Object component = m.invoke(config, getComponentParameters(m));
                appComponents.add(component);
                appComponentsByName.put(componentName, component);
            } catch (InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException("Not possible to invoke method of this instance", e);
            }
        });
    }

    private Object[] getComponentParameters(Method method) {
        Parameter[] parameters = method.getParameters();

        return Arrays.stream(parameters)
                .map(p -> this.getAppComponent(p.getType().getSimpleName()))
                .toArray();
    }

    private static Object initializeConfig(Class<?> configClass) {
        Object o = null;
        try {
            o = configClass.getConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException("Not possible to call instance of class ", e);
        }
        return o;
    }

    private static List<Method> getAnnotatedMethods(Class<?> configClass) {
        return Arrays.stream(configClass.getDeclaredMethods()).
                filter(m -> m.isAnnotationPresent(AppComponent.class))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .toList();
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {

        List<Object> componentList = appComponents.stream().filter(c -> componentClass.isAssignableFrom(c.getClass())).toList();
        if (componentList.size() > 1) {
            throw new RuntimeException("Duplicated components were found");
        } else if (componentList.size() < 1) {
            throw new RuntimeException("Component is not present");
        }

        return (C) componentList.get(0);
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object component = appComponentsByName.get(componentName.toLowerCase());

        if (component != null) {
            return (C) component;
        } else {
            throw new RuntimeException("Component is not present");
        }
    }
}
