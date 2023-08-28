package com.srikanth.todolist.dataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class ToDoData {
    private static ToDoData instance = new ToDoData();
    private static  String filename = "ToDoListItems.txt";
    private ObservableList<ToDoItem> todoItems;
    private DateTimeFormatter df ;

    private ToDoData( ) {
        df = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    public static  ToDoData getInstance(){
        return instance;
    }

    public ObservableList<ToDoItem> getTodoItems() {
        return todoItems;
    }
    public void addTodoItem(ToDoItem item){
        todoItems.add(item);
    }

//    public void setTodoItems(List<ToDoItem> todoItems) {
//        this.todoItems = todoItems;
//    }
//    now load the data
    public void loadToDoItems() throws IOException{
        todoItems = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);

        String input;
        try{
            while ((input = br.readLine())!=null){
                String[] itemPieces = input.split("\t");
                String shortDescription = itemPieces[0];
                String detail = itemPieces[1];
                String dateString = itemPieces[2];

                LocalDate date = LocalDate.parse(dateString,df);
                ToDoItem todoItem = new ToDoItem(shortDescription,detail,date);
                todoItems.add(todoItem);
            }
        }finally {
            if(br!=null){
                br.close();
            }
        }
    }
//    date store in the file
    public void storeToDoItems() throws IOException {
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try{
            Iterator<ToDoItem> itr = todoItems.iterator();
            while(itr.hasNext()){
                ToDoItem item = itr.next();
                bw.write(String.format("%s\t%s\t%s",item.getShortDescription(),item.getDetail(),item.getDeadLine().format(df)));
                bw.newLine();
            }
        }finally {
            if(bw!=null){
                bw.close();
            }
        }
    }
    public void deleteItemData(ToDoItem item){
        todoItems.remove(item);
    }

}
