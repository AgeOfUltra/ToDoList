package com.srikanth.todolist;

import com.srikanth.todolist.dataModel.ToDoData;
import com.srikanth.todolist.dataModel.ToDoItem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.util.Callback;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class HelloController {
   @FXML
   private ListView<ToDoItem> todoListView;
   private List<ToDoItem> todoItems;
   @FXML
   private TextArea textAreaDetail;
   @FXML
   private Label deadLineLabel;
   @FXML
   private BorderPane mainWindow;
   @FXML
   private ContextMenu listContextmenu;
   @FXML
   private ToggleButton filterButton;

   private FilteredList<ToDoItem> filteredList;

//   here we are creating the instance of the predicate, because before this we are creating the instance of the predicate on every click
   private Predicate<ToDoItem> wantAll;
   private Predicate<ToDoItem> filteredItems;

   public void initialize(){
      listContextmenu = new ContextMenu();
      MenuItem deleteitem = new MenuItem("Delete");
      deleteitem.setOnAction(new EventHandler<ActionEvent>() {
         @Override
         public void handle(ActionEvent event) {
            ToDoItem item = todoListView.getSelectionModel().getSelectedItem();
            deleteItem(item);
         }
      });
//      adding delet menu to the list context menu
      listContextmenu.getItems().addAll(deleteitem);



//      ToDoItem iterm1 = new ToDoItem("Mail BirthDay card","Buy a 30th Birth Day card",
//              LocalDate.of(2023, Month.MAY,25));
//      ToDoItem iterm2 = new ToDoItem("Doctors Appointment","I have a Doctor Appointment on 31st may ",
//              LocalDate.of(2023, Month.MAY,31));
//      ToDoItem iterm3 = new ToDoItem("Sister Marriage","Have to attend on the sister's marriage",
//              LocalDate.of(2023, Month.MAY,30));
//      ToDoItem iterm4 = new ToDoItem("Off to Campus","Need to go back to the Campus",
//              LocalDate.of(2023, Month.JUNE,15));
//
//
//      todoItems = new ArrayList<ToDoItem>();
//      todoItems.add(iterm1);
//      todoItems.add(iterm2);
//      todoItems.add(iterm3);
//      todoItems.add(iterm4);
//
////      Storing the data to the file which was hardcoded in above
//      ToDoData.getInstance().setTodoItems(todoItems);

//      On-line '53' we have done to show the details after the selecting items on the left side
//      Now we are trying to do that, if there are items in the list we will select the first item and the show items
//      if not we don't select any item and wait until it selects.
      todoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItem>() {
         @Override
         public void changed(ObservableValue<? extends ToDoItem> observableValue, ToDoItem toDoItem, ToDoItem t1) {
            if(t1 != null){
               ToDoItem item = todoListView.getSelectionModel().getSelectedItem();
               textAreaDetail.fontProperty().set(Font.font("Arial",FontWeight.THIN, FontPosture.REGULAR,20));
               StringBuilder sb = new StringBuilder();
               sb.append("Details :- ").append("\n\t").append(item.getDetail());
               textAreaDetail.setText(sb.toString());
               textAreaDetail.setWrapText(true);
//
               deadLineLabel.fontProperty().set(Font.font("Times New Roman",FontWeight.BOLD, FontPosture.ITALIC,20));
//               Date Formatter.
               DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
               deadLineLabel.setText("\t"+df.format(item.getDeadLine()));
            }
         }
      });
//      creating the instance of the predicate
      wantAll = new Predicate<ToDoItem>() {
         @Override
         public boolean test(ToDoItem toDoItem) {
            return true;
         }
      };

      filteredItems = new Predicate<ToDoItem>() {
         @Override
         public boolean test(ToDoItem toDoItem) {
            return toDoItem.getDeadLine().equals(LocalDate.now());
         }
      };


//      on clicking the filtered item we have to show the filtered items
      filteredList = new FilteredList<>(ToDoData.getInstance().getTodoItems(), wantAll);


//      Entering data should be in sorted list.
//      instead of passing the instance of the items we are passing the filtered items which tells every item is filterd
      SortedList<ToDoItem> sortedList = new SortedList<>(filteredList,
              new Comparator<ToDoItem>(){

                 @Override
                 public int compare(ToDoItem o1, ToDoItem o2) {
                    return o1.getDeadLine().compareTo(o2.getDeadLine());
                 }

              });

//here we are adding the items to the list as well as we are updating items in sorted Lit

      todoListView.setItems(sortedList);
//      After showing the items on the left side, we have to check the only single item is selected on every click that can be done by
      todoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

//      Here, without selection of anything the Due text is showing, now we are making the ui sucha a way that when we first select anything the
//      only show the Text on the right-hand side
      todoListView.getSelectionModel().selectFirst();

//      Here we are trying to indicate the date which are near the deadline
      todoListView.setCellFactory(new Callback<ListView<ToDoItem>, ListCell<ToDoItem>>() {
         @Override
         public ListCell<ToDoItem> call(ListView<ToDoItem> toDoItemListView) {
            ListCell<ToDoItem> cell = new ListCell<>(){
               @Override
               protected void updateItem(ToDoItem toDoItem, boolean b) {
                  super.updateItem(toDoItem, b);
                  if(b){
                     setText(null);
                  }else{
                     setText(toDoItem.getShortDescription());
//                     showing in red color for the days which are overdue and the due today.
                     if(toDoItem.getDeadLine().isBefore(LocalDate.now().plusDays(1))){
                        setTextFill(Color.RED);
                        setTooltip(new Tooltip("You may be due or over due"));
                     }else if(toDoItem.getDeadLine().equals(LocalDate.now().plusDays(1))){
                        setTextFill(Color.GREEN);
                     }
                  }
               }
            };
            cell.emptyProperty().addListener(
                    (obs,wasEmpty,isNowEmpty)->{
                        if(isNowEmpty){
                           cell.setContextMenu(null);
                        }else{
                           cell.setContextMenu(listContextmenu);
                        }
                    }
            );
            return cell;
         }
      });

   }
@FXML
//   On main window after clicking the File and new we should open a new dialog Pane to add new items
   public void showNewToDoItems(){
//      creating a new instance of a dialogue
      Dialog<ButtonType> dialog = new Dialog<>();
//      loading the main window to the dialog
      dialog.initOwner(mainWindow.getScene().getWindow());
      dialog.setTitle("Add Task");
      dialog.setHeaderText("Add new task to complete it!");

//      Now the two controller show associates the process the data entered the new dialog window
      FXMLLoader fxmlLoader = new FXMLLoader();
      fxmlLoader.setLocation(getClass().getResource("todoItemDialogue.fxml"));


//      load method can throw an IOException
      try{
//         Parent root = FXMLLoader.load(getClass().getResource("todoItemDialogue.fxml"));
         dialog.getDialogPane().setContent(fxmlLoader.load());
      }catch (IOException e){
         System.out.println("couldn't load the dialog");
         e.printStackTrace();
         return;
      }
//      adding an inbuilt ok and cancel buttons
      dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
      dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

      Optional<ButtonType> result = dialog.showAndWait();
      if(result.isPresent() && result.get() == ButtonType.OK){
         DialogController dialogController = fxmlLoader.getController();
          ToDoItem newItem= dialogController.processResults();
//         updating the data after entering new item. explicitly updating the items (which is a problem)
//         todoListView.getItems().setAll(ToDoData.getInstance().getTodoItems());
         todoListView.getSelectionModel().select(newItem);

      }
   }

//   when there was a mouse operation on the item, we have to know which item was clicked
//   public void handleClickedListview(){
//      ToDoItem item =todoListView.getSelectionModel().getSelectedItem();
////      System.out.println(item);
//      textAreaDetail.fontProperty().set(Font.font("Arial",FontWeight.THIN, FontPosture.REGULAR,20));
//      StringBuilder sb = new StringBuilder();
//      sb.append("Details :- ").append("\n\t").append(item.getDetail());
////      sb.append("\n").append("DeadLine :-").append("\n").append("\t").append(item.getDeadLine());
//      textAreaDetail.setText(sb.toString());
//
//      deadLineLabel.fontProperty().set(Font.font("Arial",FontWeight.BOLD, FontPosture.ITALIC,20));
//      deadLineLabel.setText("\n"+item.getDeadLine().toString());
//   }

//   creating a method that will handle the keyEvent to delete the item
   @FXML
   public void handledKeyPressed(KeyEvent keyEvent){
      ToDoItem item = todoListView.getSelectionModel().getSelectedItem();
      if(item !=null){
         if(keyEvent.getCode().equals(KeyCode.DELETE)){
            deleteItem(item);
         }
      }
   }


//   when deleting we should get a conformation message
   public void deleteItem(ToDoItem item){
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("Delete item");
      alert.setHeaderText("Delete item :"+item.getShortDescription());
      alert.setContentText("Are you sure? , press ok to Confirm or cancel to back!");
      Optional<ButtonType> button = alert.showAndWait();
      if(button.isPresent() && button.get()==ButtonType.OK){
         ToDoData.getInstance().deleteItemData(item);
      }

   }
   @FXML
   public void filterTodayItem(){
      ToDoItem selectedItem = todoListView.getSelectionModel().getSelectedItem();
      if(filterButton.isSelected()){
         filteredList.setPredicate(filteredItems);
         if(filteredList.isEmpty()){
            textAreaDetail.clear();
            deadLineLabel.setText("");
         }else if (filteredList.contains(selectedItem)){
            todoListView.getSelectionModel().select(selectedItem);
         }else {
            todoListView.getSelectionModel().selectFirst();
         }
      } else {
         filteredList.setPredicate(wantAll);
         todoListView.getSelectionModel().select(selectedItem);
      }
   }
   @FXML
   public void handleExit(){
      Platform.exit();
   }


}