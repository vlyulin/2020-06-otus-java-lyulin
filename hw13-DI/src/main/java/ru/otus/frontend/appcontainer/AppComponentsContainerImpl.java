package ru.otus.frontend.appcontainer;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import ru.otus.frontend.appcontainer.api.AppComponent;
import ru.otus.frontend.appcontainer.api.AppComponentsContainer;
import ru.otus.frontend.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.frontend.appcontainer.exceptions.AppComponentExistsException;
import ru.otus.frontend.appcontainer.exceptions.AppComponentsContainerException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<Object> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    // Опциональные варианты
    //AppComponentsContainer container = new AppComponentsContainerImpl(AppConfig1.class, AppConfig2.class);
    public AppComponentsContainerImpl(Class<?>... initialConfigClasses) {
        for (Class<?> initialConfigClass : initialConfigClasses) {
            processConfig(initialConfigClass);
        }
    }

    // Тут можно использовать библиотеку Reflections (см. зависимости)
    //AppComponentsContainer container = new AppComponentsContainerImpl("ru.otus.config");
    public AppComponentsContainerImpl(String packageName) {
        ConfigurationBuilder configurationBuilder = new ConfigurationBuilder()
                .setScanners(new MethodAnnotationsScanner(),
                        new TypeAnnotationsScanner()
                                .filterResultsBy(c -> c.getClass().equals(AppComponentsContainerConfig.class)))
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName)))
                .setUrls(ClasspathHelper.forPackage(packageName));

        Reflections reflections = new Reflections(configurationBuilder);
        List<Method> allMethods = new ArrayList<>(reflections.getMethodsAnnotatedWith(AppComponent.class));

        // Деление по классам
        Map<Class<?>, List<Method>> groups =
                allMethods.stream().collect(
                        Collectors.groupingBy(Method::getDeclaringClass)
                );

        // Сортировка по порядку конфигурационных классов
        List<Class<?>> classes = new ArrayList<>(groups.keySet());
        Collections.sort(classes, Comparator.comparingInt(
                c -> c.getAnnotation(AppComponentsContainerConfig.class).order()
        ));

        // Создание объектов каждого конфигурационного класса
        for (Class<?> clazz : classes) {
            // Получение экземпляра configClass
            Object configClassInstance;
            try {
                configClassInstance = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new AppComponentsContainerException("Instantiating of "
                        + clazz.getName() + ". Error: " + e.getMessage());
            }

            List<Method> methods = groups.get(clazz);
            // Выстраивание порядка создания компонентов
            Collections.sort(methods,
                    Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order())
            );
            // Создание и сохранение компонентов
            getComponents(configClassInstance, methods);
        }
    }


    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        // You code here...

        List<Method> methods = new Reflections(configClass, new MethodAnnotationsScanner())
                .getMethodsAnnotatedWith(AppComponent.class)
                .stream()
                .filter(c -> c.getDeclaringClass().equals(configClass))
                .sorted(Comparator.comparingInt(m -> m.getAnnotation(AppComponent.class).order()))
                .collect(Collectors.toList());

        // Получение экземпляра configClass
        Object configClassInstance;
        try {
            configClassInstance = configClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new AppComponentsContainerException("Instantiating of "
                    + configClass.getName() + ". Error: " + e.getMessage());
        }

        getComponents(configClassInstance, methods);
    }

    private void getComponents(Object configClassInstance, List<Method> methods) {
        // Создание объектов
        for (Method method : methods) {
            System.out.println(method.getName() + " processing ...");

            Object[] args = getMethodArgs(method);
            Object component;
            try {
                // Создание и сохранение компоненты
                component = (args == null) ? method.invoke(configClassInstance) : method.invoke(configClassInstance, args);
                addComponent(component);
                appComponentsByName.put(method.getAnnotation(AppComponent.class).name(), component);
            } catch (AppComponentExistsException ex) {
                throw new AppComponentExistsException("Component duplication: " + method.getName()
                        + "; " + ex.getMessage());
            } catch (Exception e) {
                throw new AppComponentsContainerException("Invoking "
                        + method.getName() + ". Error: " + e.getMessage());
            }
        }
    }

    private Object[] getMethodArgs(Method method) {
        int parametersCount = method.getParameterCount();
        if (parametersCount == 0) return null;

        Object[] args = new Object[parametersCount];
        Parameter[] parameters = method.getParameters();
        for (int idx = 0; idx < parameters.length; idx++) {
            Object appComponent = getAppComponent(parameters[idx].getType());
            if (appComponent == null) {
                throw new RuntimeException("Method: " + method.getName() +
                        ", arg[" + idx + "]: " + parameters[idx].getName() +
                        ", type: " + parameters[idx].getType() +
                        ". Instance doesn't not found.");
            }
            args[idx] = appComponent;
        }
        return args;
    }

    // Используется для исключения дублирования
    private void addComponent(Object component) throws AppComponentExistsException {
        if (component == null) return;
        Optional existing = appComponents.stream()
                .filter(instance -> component.getClass().isAssignableFrom(instance.getClass()))
                .findFirst();
        if (existing.isPresent()) {
            throw new AppComponentExistsException("Existing instance: " + existing.get().getClass());
        }
        appComponents.add(component);
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        return (C) appComponents.stream()
                .filter(instance -> componentClass.isAssignableFrom(instance.getClass()))
                .findFirst()
                .get();
    }

    @Override
    public <C> C getAppComponent(String componentName) {
        return (C) appComponentsByName.get(componentName);
    }
}
