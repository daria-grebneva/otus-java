package ru.otus.appcontainer;

import com.google.common.collect.TreeMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppComponentsContainerImpl.class);
    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        TreeMultimap<Integer, String> methodsMultiMap = getParsedAnnotations(configClass);
        Object config = initializeConfig(configClass);

        methodsMultiMap.forEach((t, v) -> {
            try {
                Method[] methods = configClass.getDeclaredMethods();
                for (Method method : methods) {
                    if (Objects.equals(v, method.getName())) {
                        Object component = (method.getParameterCount() > 0)
                                ? method.invoke(config, getComponentParameters(method))
                                : method.invoke(config);
                        appComponents.add(component);
                        appComponentsByName.put(component.getClass().getSimpleName(), component);
                    }
                }
            } catch (InvocationTargetException | IllegalAccessException e) {
                LOGGER.error("Not possible to invoke method of this instance ", e);
            }
        });
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

    private static TreeMultimap<Integer, String> getParsedAnnotations(Class<?> configClass) {
        TreeMultimap<Integer, String> methodsMultiMap = TreeMultimap.create();
        Arrays.stream(configClass.getDeclaredMethods()).forEach(m -> {
            Arrays.stream(m.getDeclaredAnnotations()).forEach(annotation -> {
                AppComponent appComponent = ((AppComponent) annotation);
                if (methodsMultiMap.containsValue(appComponent.name())) {
                    throw new RuntimeException("Found components with the same names");
                }
                methodsMultiMap.put(appComponent.order(), appComponent.name());
            });
        });
        return methodsMultiMap;
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {

        List<Object> componentList = appComponents.stream().filter(c -> c.getClass().getSimpleName().contains(componentClass.getSimpleName())).collect(Collectors.toList());
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
