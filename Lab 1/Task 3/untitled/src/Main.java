import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

class FunctionNotFoundException extends Exception {
    public FunctionNotFoundException(String message) {
        super(message);
    }
}
public class Main {
    public static void callMethod(Object obj, String methodName, List<Object> parameters)
            throws FunctionNotFoundException {
        Class<?>[] parameterTypes = new Class<?>[parameters.size()];
        for (int i = 0; i < parameters.size(); i++) {
            parameterTypes[i] = parameters.get(i).getClass();
            if (parameterTypes[i] == Double.class) {
                parameterTypes[i] = double.class;
            } else if (parameterTypes[i] == Integer.class) {
                parameterTypes[i] = int.class;
            }
        }
        Method method = findMethod(obj.getClass(), methodName, parameterTypes);
        if (method == null) {
            throw new FunctionNotFoundException("Method '" + methodName + "' not found or cannot be invoked.");
        }
        try {
            Object result = method.invoke(obj, parameters.toArray());
            System.out.println("Types: " + Arrays.toString(parameterTypes) + ", Values: " + parameters);
            System.out.println("The result of the call: " + result);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new FunctionNotFoundException("Method '" + methodName + "' cannot be invoked.");
        }
    }
    private static Method findMethod(Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName) && Arrays.equals(method.getParameterTypes(), parameterTypes)) {
                return method;
            }
        }
        return null;
    }
    public static void main(String[] args) {
        TestClass obj = new TestClass(1.0);
        try {
            callMethod(obj, "compute", Arrays.asList(1.0));
            callMethod(obj, "computeOverloaded", Arrays.asList(1.0, 1));
        } catch (FunctionNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
class TestClass {
    private double a;
    public TestClass(double a) {
        this.a = a;
    }
    public double compute(double x) {
        return Math.exp(-Math.abs(a) * x) * Math.sin(x);
    }
    public double computeOverloaded(double x, int y) {
        return Math.exp(-Math.abs(a) * x) * Math.sin(x);
    }
}