import java.lang.reflect.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Enter the class name:");
            String className = scanner.nextLine();
            analyzeClass(className);
        } catch (ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        }
    }
    public static void analyzeClass(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        String packageName = clazz.getPackage() != null ? clazz.getPackage().getName() : "";
        int modifiers = clazz.getModifiers();
        String modifiersStr = Modifier.toString(modifiers);
        String classNameStr = clazz.getSimpleName();
        String superClass = clazz.getSuperclass() != null ? clazz.getSuperclass().getName() : "";
        Class<?>[] interfaces = clazz.getInterfaces();
        StringBuilder result = new StringBuilder();
        result.append("Package: ").append(packageName).append("\n");
        result.append("Modifiers: ").append(modifiersStr).append("\n");
        result.append("Class Name: ").append(classNameStr).append("\n");
        result.append("Super Class: ").append(superClass).append("\n");
        result.append("Implemented Interfaces: ");
        for (Class<?> interf : interfaces) {
            result.append(interf.getName()).append(", ");
        }
        result.delete(result.length() - 2, result.length());
        result.append("\n\n");
        result.append("Fields:\n");
        for (Field field : clazz.getDeclaredFields()) {
            result.append(Modifier.toString(field.getModifiers())).append(" ")
                    .append(field.getType().getSimpleName()).append(" ")
                    .append(field.getName()).append("\n");
        }
        result.append("\n");
        result.append("Constructors:\n");
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            result.append(Modifier.toString(constructor.getModifiers())).append(" ")
                    .append(constructor.getName()).append("(");
            Class<?>[] params = constructor.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                result.append(params[i].getSimpleName());
                if (i < params.length - 1) {
                    result.append(", ");
                }
            }
            result.append(")\n");
        }
        result.append("\n");
        result.append("Methods:\n");
        for (Method method : clazz.getDeclaredMethods()) {
            result.append(Modifier.toString(method.getModifiers())).append(" ")
                    .append(method.getReturnType().getSimpleName()).append(" ")
                    .append(method.getName()).append("(");
            Class<?>[] params = method.getParameterTypes();
            for (int i = 0; i < params.length; i++) {
                result.append(params[i].getSimpleName());
                if (i < params.length - 1) {
                    result.append(", ");
                }
            }
            result.append(")\n");
        }
        System.out.println(result.toString());
    }
}