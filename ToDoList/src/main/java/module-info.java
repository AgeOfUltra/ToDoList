module com.srikanth.todolist {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.srikanth.todolist to javafx.fxml;
    exports com.srikanth.todolist;
}