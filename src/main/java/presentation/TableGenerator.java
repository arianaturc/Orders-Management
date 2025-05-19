package presentation;

import java.lang.reflect.Field;
import java.util.List;

public class TableGenerator {

    public static <T> void generateTable(List<T> list) {
        if (list == null || list.isEmpty()) {
            System.out.println("No data to display.");
            return;
        }

        Class<?> clazz = list.get(0).getClass();
        Field[] fields = clazz.getDeclaredFields();


        for (Field field : fields) {
            System.out.print(field.getName() + "\t");
        }
        System.out.println();

        for (T item : list) {
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(item);
                    System.out.print(value + "\t");
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            System.out.println();
        }
    }
}
