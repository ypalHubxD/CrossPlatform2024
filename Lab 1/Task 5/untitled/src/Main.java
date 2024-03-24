import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Main {
    public static void main(String[] args) {
        MathFunction function1 = (MathFunction) Proxy.newProxyInstance(MathFunctions.class.getClassLoader(),
                new Class[]{MathFunction.class},
                new FunctionHandler(MathFunctions::function1));
        MathFunction function2 = (MathFunction) Proxy.newProxyInstance(MathFunctions.class.getClassLoader(),
                new Class[]{MathFunction.class},
                new FunctionHandler(MathFunctions::function2));
        double x = 1;
        System.out.println("Value of function 1, x = " + x + ": " + function1.calculate(x));
        System.out.println("Value of function 2, x = " + x + ": " + function2.calculate(x));
    }
}
interface MathFunction {
    double calculate(double x);
}
class FunctionHandler implements InvocationHandler {
    private MathFunction target;
    public FunctionHandler(MathFunction target) {
        this.target = target;
    }
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        long startTime = System.nanoTime();
        Object result = method.invoke(target, args);
        long endTime = System.nanoTime();

        System.out.println("Method: " + method.getName() + ", Argument: " + args[0] + ", Calc time: " + (endTime - startTime) + " ns");

        return result;
    }
}
class MathFunctions {
    public static double function1(double x) {
        return Math.exp(-Math.abs(2.5) * x) * Math.sin(x);
    }
    public static double function2(double x) {
        return x * x;
    }
}