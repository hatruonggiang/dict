module group7 {
    requires javafx.controls;
    requires javafx.fxml;
    requires freetts;
    requires json.simple;

    opens group7 to javafx.fxml;

    exports group7;
}
