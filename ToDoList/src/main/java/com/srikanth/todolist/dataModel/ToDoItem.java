package com.srikanth.todolist.dataModel;

import java.time.LocalDate;

public class ToDoItem {
    private String shortDescription;
    private String detail;

    private LocalDate deadLine;

    public ToDoItem(String shortDescription, String detail, LocalDate deadLine) {
        this.shortDescription = shortDescription;
        this.detail = detail;
        this.deadLine = deadLine;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public LocalDate getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(LocalDate deadLine) {
        this.deadLine = deadLine;
    }
//    without the override the toString method in the list views it only showing the reference of the every item, but we need gto show it's a short description
//    for that we have to override the toString method.

//    we no longer use of this because we are updating the cell factory by using the getter method
//    @Override
//    public String toString() {
//        return  shortDescription ;
//    }
}
