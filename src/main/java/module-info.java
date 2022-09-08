module bk.invmgmt {
    requires javafx.controls;
    requires javafx.fxml;


    opens bk.Model to javafx.fxml;
    exports bk.Model;
    exports bk.Controller;
    opens bk.Controller to javafx.fxml;
    exports bk.Main;
    opens bk.Main to javafx.fxml;
}