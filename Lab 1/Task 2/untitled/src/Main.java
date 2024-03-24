import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        TestClass testObj = new TestClass();
        inspect(testObj);
    }
    public static void inspect(Object obj) {
        System.out.println("The actual type of object: " + obj.getClass().getName());
        System.out.println("The state of object:");
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                System.out.println(field.getType().getName() + " " + field.getName() + " = " + field.get(obj));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        System.out.println("List of open methods:");
        Method[] methods = obj.getClass().getDeclaredMethods();
        int count = 1;
        for (Method method : methods) {
            if (Modifier.isPublic(method.getModifiers()) && method.getParameterCount() == 0) {
                System.out.println(count + "). " + method);
                count++;
            }
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the serial number of method [1 , " + (count - 1) + "]: ");
        int choice = scanner.nextInt();
        if (choice >= 1 && choice <= count - 1) {
            try {
                Method selectedMethod = methods[choice - 1];
                Object result = selectedMethod.invoke(obj);
                System.out.println("Result: " + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Wrong choose");
        }
    }
    static class TestClass {
        private double x = 3.0;
        private double y = 4.0;
        public double Dist() {
            return Math.sqrt(x * x + y * y);
        }
        public void setRandomData() {
            x = Math.random();
            y = Math.random();
        }
        public String toString() {
            return "x = " + x + ", y = " + y;
        }
        public void setData() {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter X");
            x = scanner.nextDouble();
            System.out.println("Enter Y");
            y = scanner.nextDouble();
        }
    }
}