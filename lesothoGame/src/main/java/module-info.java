module com.example.lesothogame {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lesothogame to javafx.fxml;
    exports com.example.lesothogame;
}