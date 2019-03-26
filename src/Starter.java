import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Starter implements java.util.function.Consumer<String> {

    @Override
    public void accept(String s) {

        try {

            Class<?> reflectClass = Class.forName(s);

            Constructor<?> constructor = reflectClass.getConstructor();

            Object instance = constructor.newInstance();

            for (Method method : reflectClass.getMethods()) {

                Class[] parameterType = method.getParameterTypes();
                MethodToStart methodToStart = method.getAnnotation(MethodToStart.class);
                StringParameter stringParameter = method.getAnnotation(StringParameter.class);

                if (method.isAnnotationPresent(MethodToStart.class) && !method.isAnnotationPresent(MethodDisabled.class)) {

                    if ((parameterType.length == 0) || (parameterType.length == 1 && parameterType[0].equals(String.class))) {

                        for (int i = 0; i < methodToStart.value(); i++) {

                            if (method.isAnnotationPresent(StringParameter.class)) {

                                if (parameterType.length == 0) {
                                    Method methodCall = reflectClass.getDeclaredMethod(method.getName());
                                    methodCall.invoke(instance);
                                } else {
                                    Method methodCall = reflectClass.getDeclaredMethod(method.getName(), String.class);
                                    methodCall.invoke(instance, stringParameter.value());
                                }

                            } else {

                                Method methodCall = reflectClass.getDeclaredMethod(method.getName());
                                methodCall.invoke(instance);
                            }
                        }
                    }
                }
            }

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}