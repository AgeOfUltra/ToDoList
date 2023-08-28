package com.srikanth.todolist;

import com.srikanth.todolist.dataModel.ToDoData;
import com.srikanth.todolist.dataModel.ToDoItem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogController {
    @FXML
    private TextField ShortDesc;
    @FXML
    private TextArea detail;
    @FXML
    private DatePicker date;

    public ToDoItem processResults(){
        String shortDesc = ShortDesc.getText().trim();
        String details = detail.getText().trim();
        LocalDate datePicker = date.getValue();

        ToDoItem item = new ToDoItem(shortDesc,details,datePicker);
        ToDoData.getInstance().addTodoItem(item);
        return item;
    }
}
