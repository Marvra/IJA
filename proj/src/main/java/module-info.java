module com.example.proj {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.proj to javafx.fxml;
    exports com.example.proj;
}