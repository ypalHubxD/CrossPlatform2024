import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the first number: ");
        double num1 = scanner.nextDouble();
        System.out.print("Enter the second number: ");
        double num2 = scanner.nextDouble();
        System.out.print("Select operation: \n");
        System.out.print("1. Addition (+)\n");
        System.out.print("2. Subtraction (-)\n");
        System.out.println("3. Multiplication (*)");
        System.out.println("4. Division (/)");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        double result = 0;
        switch (choice) {
            case 1:
                result = num1 + num2;break;
            case 2:
                result = num1 - num2;break;
            case 3:
                result = num1 * num2;break;
            case 4:
                if (num2 != 0) {
                    result = num1 / num2;
                } else {
                    System.out.println("Division by zero");return;
                }
                break;
            default:
                System.out.println("Invalid operation!");return;
        }
        System.out.println("Result: " + result);
    }
}
