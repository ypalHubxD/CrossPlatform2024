import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.*;

public class Main {
    private static JTextArea textArea;
    public static void main(String[] args) {
        JFrame frame = new JFrame("Class Analyzer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        JLabel label = new JLabel("Enter the class name:");
        JTextField textField = new JTextField(20);
        JButton analyzeButton = new JButton("Analyze");
        JButton clearButton = new JButton("Clear");
        JButton finishButton = new JButton("Finish Work");
        textArea = new JTextArea(20, 40);
        JScrollPane scrollPane = new JScrollPane(textArea);
        analyzeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String className = textField.getText();
                if (!className.isEmpty()) {
                    try {
                        analyzeClass(className);
                    } catch (ClassNotFoundException ex) {
                        appendText("Class not found: " + ex.getMessage() + "\n");
                    }
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
            }
        });
        finishButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        panel.add(label);
        panel.add(textField);
        panel.add(analyzeButton);
        panel.add(clearButton);
        panel.add(finishButton);
        frame.add(panel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setVisible(true);
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
        appendText(result.toString());
    }
    private static void appendText(String text) {
        textArea.append(text);
    }
}