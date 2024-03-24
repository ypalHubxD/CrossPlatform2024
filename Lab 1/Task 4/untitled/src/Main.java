import java.lang.reflect.Array;

public class Main {
    public static void main(String[] args) {
        int a = 5;//Кількість ел. масиву
        int b = 3;//Кількість ствобців
        int c = 3;//Кількість рядків
        int[] intArray = (int[]) createArray(a, int.class);
        for (int i = 0; i < intArray.length; i++) {
            intArray[i] = i + 1;
        }
        System.out.printf("Array[%d] %s\n", a, arrayToString(intArray));
        String[] StringArray = (String[]) createArray(a, String.class);
        for (int i = 0; i < intArray.length; i++) {
            StringArray[i] = "ab"+ i;
        }
        System.out.printf("Array[%d] %s\n", a, arrayToString(StringArray));
        Integer[][] integerMatrix = (Integer[][]) createMatrix(b, c, Integer.class);
        int count = 1;
        for (int i = 0; i < integerMatrix.length; i++) {
            for (int j = 0; j < integerMatrix[i].length; j++) {
                integerMatrix[i][j] = count++;
            }
        }
        System.out.printf("Matrix[%d][%d] %s\n", c, b, matrixToString(integerMatrix));
        a = 10;//Кількість ел. масиву
        intArray = (int[]) resizeArray(intArray, a);
        System.out.printf("Array[%d] %s\n", a, arrayToString(intArray));
        StringArray = (String[]) resizeArray(StringArray, a);
        System.out.printf("Array[%d] %s\n", a, arrayToString(StringArray));
        b = 2;//Кількість ствобців
        c = 4;//Кількість рядків
        integerMatrix = (Integer[][]) resizeMatrix(integerMatrix, b, c);
        System.out.printf("Matrix[%d][%d] %s\n", c, b, matrixToString(integerMatrix));
    }
    public static Object createArray(int length, Class<?> type) {
        return Array.newInstance(type, length);
    }
    public static Object createMatrix(int rows, int cols, Class<?> type) {
        return Array.newInstance(type, rows, cols);
    }
    public static Object resizeArray(Object array, int newLength) {
        Class<?> type = array.getClass().getComponentType();
        Object newArray = Array.newInstance(type, newLength);
        System.arraycopy(array, 0, newArray, 0, Math.min(Array.getLength(array), newLength));
        return newArray;
    }
    public static Object resizeMatrix(Object matrix, int newRows, int newCols) {
        Class<?> type = matrix.getClass().getComponentType().getComponentType();
        Object newMatrix = Array.newInstance(type, newRows, newCols);
        int rows = Math.min(Array.getLength(matrix), newRows);
        int cols = Math.min(Array.getLength(Array.get(matrix, 0)), newCols);
        for (int i = 0; i < rows; i++) {
            System.arraycopy(Array.get(matrix, i), 0, Array.get(newMatrix, i), 0, cols);
        }
        return newMatrix;
    }
    public static String arrayToString(Object array) {
        StringBuilder sb = new StringBuilder("{");
        int length = Array.getLength(array);
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(Array.get(array, i));
        }
        sb.append("}");
        return sb.toString();
    }
    public static String matrixToString(Object matrix) {
        StringBuilder sb = new StringBuilder("\n");
        int rows = Array.getLength(matrix);
        for (int i = 0; i < rows; i++) {
            if (i > 0) {
                sb.append(",\n");
            }
            sb.append(arrayToString(Array.get(matrix, i)));
        }
        sb.append("");
        return sb.toString();
    }
}