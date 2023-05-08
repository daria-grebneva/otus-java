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

        List<Method> parsedMethods = getParsedAnnotations(configClass);
        Object config = initializeConfig(configClass);
        Set<String> uniqueComponentNames = new HashSet<>();

        parsedMethods.forEach((m) -> {
            checkForDuplicates(uniqueComponentNames, m);
            try {
                Object component = m.invoke(config, getComponentParameters(m));
                appComponents.add(component);
                appComponentsByName.put(component.getClass().getSimpleName(), component);
            } catch (InvocationTargetException | IllegalAccessException e) {
                LOGGER.error("Not possible to invoke method of this instance ", e);
            }
        });
    }

    private void checkForDuplicates(Set<String> uniqueComponentNames, Method m) {
        String componentName = m.getAnnotation(AppComponent.class).name();

        if (uniqueComponentNames.contains(componentName)) {
            throw new RuntimeException("Found components with the same names");
        } else {
            uniqueComponentNames.add(componentName);
        }
    }

    private Object[] getComponentParameters(Method method) {
        Parameter[] parameters = method.getParameters();
        List<Object> paramsList = new ArrayList<>();
        for (Parameter parameter : parameters) {
            Object param = this.getAppComponent(parameter.getType().getSimpleName());
            paramsList.add(param);
        }
        return paramsList.toArray();
    }

    private static Object initializeConfig(Class<?> configClass) {
        Object o = null;
        try {
            o = configClass.getConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            LOGGER.error("Not possible to call instance of class ", e);
        }
        return o;
    }

    private static List<Method> getParsedAnnotations(Class<?> configClass) {
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
        Object component = null;

        for (Map.Entry<String, Object> e : appComponentsByName.entrySet()) {
            if (e.getKey().toLowerCase().contains(componentName.toLowerCase())) {
                component = e.getValue();
            }
        }

        if (component != null) {
            return (C) component;
        } else {
            throw new RuntimeException("Component is not present");
        }
    }
}
