package ru.otus.ioc;

import ru.otus.SomeInterface;
import ru.otus.SomeInterfaceImpl;
import ru.otus.annotations.Log;
import ru.otus.ioservice.ConsoleIOService;
import ru.otus.ioservice.IOService;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class Ioc {

    private Ioc() {
    }

    static public SomeInterface createSomeInterfaceObj() {
        InvocationHandler handler = new MyInvocationHandler(new SomeInterfaceImpl(), new ConsoleIOService());
        return (SomeInterface) Proxy.newProxyInstance(Ioc.class.getClassLoader(),
                new Class<?>[]{SomeInterface.class}, handler);
    }

    static class MyInvocationHandler implements InvocationHandler {
        private final SomeInterface someInterfaceImplObj;
        private final IOService ioService; // логирование на консоль, в файл и т.д.
        private List<Method> classMethods = new ArrayList<>();

        MyInvocationHandler(SomeInterface someInterfaceObj, IOService ioService) {
            this.someInterfaceImplObj = someInterfaceObj;
            this.ioService = ioService;
            Class<?> clazz = someInterfaceObj.getClass();
            // Сохраняем список всех методов отмеченных анотацией @Log
            this.classMethods = getMethodsAnnotatedWith(clazz, Log.class);
        }

        private static List<Method> getMethodsAnnotatedWith(final Class<?> clazz, final Class<? extends Annotation> annotation) {
            final List<Method> methods = new ArrayList<Method>();
            // final List<Method> methods = new ArrayList<>();
            for (final Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(annotation)) {
                    methods.add(method);
                }
            }
            return methods;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (isLogMethod(method)) {
                Log(method, args);
            }
            return method.invoke(someInterfaceImplObj, args);
        }

        private boolean isLogMethod(Method calledMethod) {
            for (Method method : classMethods) {
                // имя метода, количество параметров и типы параметров должны совпадать
                // требуется точное совпадение типов, подтипы не проверяются
                if (method.getName().equals(calledMethod.getName())
                        && method.getParameterCount() == calledMethod.getParameterCount()) {
                    Class<?>[] methodParametersTypes = method.getParameterTypes();
                    Class<?>[] calledMethodParametersTypes = calledMethod.getParameterTypes();
                    for (int i = 0; i < methodParametersTypes.length; i++) {
                        if (methodParametersTypes[i].getTypeName() != calledMethodParametersTypes[i].getTypeName()) {
                            // типы параметров не совпадают
                            return false;
                        }
                    }
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "MyInvocationHandler{" +
                    "someInterfaceImplObj = " + someInterfaceImplObj +
                    '}';
        }

        private void Log(Method method, Object[] args) {
            ioService.print("executed method: " + method.getName());
            for (Object param : args) {
                ioService.print( ", " + "param: " + param);
            }
            ioService.printLn("");
        }
    }
}
