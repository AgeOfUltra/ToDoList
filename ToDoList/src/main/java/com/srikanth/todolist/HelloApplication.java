package com.srikanth.todolist;

import com.srikanth.todolist.dataModel.ToDoData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("mainwindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 500);
        stage.setTitle("ToDo List");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
//    Now we want to load the data at time of application initialize


    @Override
    public void init() throws Exception {
        try{
            ToDoData.getInstance().loadToDoItems();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }

    //every time when we stop the application, we have to store them in the file
    @Override
    public void stop() throws  Exception{
        try{
            ToDoData.getInstance().storeToDoItems();
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
    }
}