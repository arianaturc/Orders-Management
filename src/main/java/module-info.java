module org.example.pt2025_30422_turc_ariana_assignment_3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens org.example.pt2025_30422_turc_ariana_assignment_3 to javafx.fxml;
    exports org.example.pt2025_30422_turc_ariana_assignment_3;


    exports presentation;
    exports util;
}