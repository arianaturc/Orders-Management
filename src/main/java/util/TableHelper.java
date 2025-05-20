package util;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.lang.reflect.Field;
import java.util.List;

/**
 * TableHelper is a utility class to populate a JavaFX TableView dynamically using reflection.
 */
public class TableHelper {

    /**
     * Creates table columns in the given TableView based on the declared fields of the provided class type,
     * then sets the list of data as items of the TableView.
     *
     * @param tableView the TableView to populate
     * @param data the list of data objects to display
     * @param claSS the class type of the data objects
     */
    public static <T> void populateTableView(TableView<T> tableView, List<T> data, Class<T> claSS) {

        tableView.getItems().clear();

        Field[] fields = claSS.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            TableColumn<T, Object> column = new TableColumn<>(field.getName());
            column.setCellValueFactory(cellData -> {
                try {
                    Object value = field.get(cellData.getValue());
                    return new SimpleObjectProperty<>(value);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                    return null;
                }
            });

            tableView.getColumns().add(column);
        }
        tableView.getItems().setAll(data);
    }
}