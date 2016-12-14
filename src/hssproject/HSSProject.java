/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hssproject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import model.Mark;
import model.Sentence;
import model.Error;

/**
 *
 * @author weiyumou
 */
public class HSSProject extends Application {
    
    
    private final Sentence sentence = new Sentence("15", "3", "这一句话。");
    private final Error error = new Error("句", "句子成分残缺", 
            "谓语残缺", "这一句话。", "缺少\"是\"");
    
    private final Mark mark = new Mark("1", sentence, error);
    
    private final ObservableList<Mark> data =
        FXCollections.observableArrayList(
            new Mark("1", sentence, error),
                new Mark("1", sentence, error),
                new Mark("1", sentence, error),
                new Mark("1", sentence, error),
                new Mark("1", sentence, error),
                new Mark("1", sentence, error),
                new Mark("1", sentence, error),
                new Mark("1", sentence, error),
                new Mark("1", sentence, error),
                new Mark("1", sentence, error)
        );
    
    @Override
    public void start(Stage primaryStage) {
        
        Parent root = initialiseUI();
        
        Scene scene = new Scene(root, 1280, 720);
        
//        System.out.println(mark.toString());
        
        primaryStage.setTitle("作文标注");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    private TableView<Mark> initialiseTableView(){
        final int numOfCol = 9;
        
        TableView<Mark> table = new TableView<>();
        
        TableColumn authorIDCol = new TableColumn("作者序号");
        authorIDCol.prefWidthProperty().bind(table.widthProperty().divide(numOfCol * 2));
        authorIDCol.setCellValueFactory(new PropertyValueFactory<>("authorID"));
 
        TableColumn idInEssayCol = new TableColumn("句子序号");
        idInEssayCol.prefWidthProperty().bind(table.widthProperty().divide(numOfCol * 2));
        idInEssayCol.setCellValueFactory(new PropertyValueFactory<>("idInEssay"));
 
        TableColumn idInParaCol = new TableColumn("句子在段落中序号");
        idInParaCol.prefWidthProperty().bind(table.widthProperty().divide(numOfCol));
        idInParaCol.setCellValueFactory(new PropertyValueFactory<>("idInParagraph"));
        
        TableColumn contentCol = new TableColumn("句子");
        contentCol.prefWidthProperty().bind(table.widthProperty().divide(numOfCol));
        contentCol.setCellValueFactory(new PropertyValueFactory<>("sentenceContent"));
        
        TableColumn typeIErrorCol = new TableColumn("错误类型");
        typeIErrorCol.prefWidthProperty().bind(table.widthProperty().divide(numOfCol));
        typeIErrorCol.setCellValueFactory(new PropertyValueFactory<>("typeIError"));
        
        TableColumn typeIIErrorCol = new TableColumn("具体错误类型");
        typeIIErrorCol.prefWidthProperty().bind(table.widthProperty().divide(numOfCol));
        typeIIErrorCol.setCellValueFactory(new PropertyValueFactory<>("typeIIError"));
        
        TableColumn typeIIIErrorCol = new TableColumn("病句及句子成分错误类型");
        typeIIIErrorCol.prefWidthProperty().bind(table.widthProperty().divide(numOfCol));
        typeIIIErrorCol.setCellValueFactory(new PropertyValueFactory<>("typeIIIError"));
 
        TableColumn errorSegmentCol = new TableColumn("错误所在片段");
        errorSegmentCol.prefWidthProperty().bind(table.widthProperty().divide(numOfCol));
        errorSegmentCol.setCellValueFactory(new PropertyValueFactory<>("errorSegment"));
        
        TableColumn remarkCol = new TableColumn("备注");
        remarkCol.prefWidthProperty().bind(table.widthProperty().divide(numOfCol / 2.0));
        remarkCol.setCellValueFactory(new PropertyValueFactory<>("remark"));
        
        table.setItems(data);
        table.getColumns().addAll(authorIDCol, idInEssayCol, idInParaCol,
            contentCol, typeIErrorCol, typeIIErrorCol, typeIIIErrorCol,
            errorSegmentCol, remarkCol);
        
        table.setPrefHeight(200);
        
        return table;
    }
    
    
    private Parent initialiseUI(){
        
        BorderPane rootPane = new BorderPane();
        
//        GridPane rootPane = new GridPane();
//        ColumnConstraints column1 = new ColumnConstraints();
//        column1.setPercentWidth(50);
//        ColumnConstraints column2 = new ColumnConstraints();
//        column2.setPercentWidth(50);
//        rootPane.getColumnConstraints().addAll(column1, column2);
        
//        for(int i = 0; i != 4; ++i){
//            RowConstraints row = new RowConstraints();
//            row.setPercentHeight(25);
//            rootPane.getRowConstraints().add(row);
//        }
        
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("File");
        Menu menuAbout = new Menu("Aboout");
        menuBar.getMenus().addAll(menuFile, menuAbout);
        
        rootPane.setTop(menuBar);
        
        GridPane centerPane = new GridPane();
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(75);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(25);
        centerPane.getColumnConstraints().add(col1);
        centerPane.getColumnConstraints().add(col2);
        
        for(int i = 0; i != 3; ++i){
            
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100/3.0);
            centerPane.getRowConstraints().add(row);
            
        }
        
        TextArea passagePrev = new TextArea();
        TextArea passageCurr = new TextArea();
        TextArea passageNext = new TextArea();
        passagePrev.setOpacity(0.5);
        passageNext.setOpacity(0.5);
        
        
        centerPane.add(passagePrev, 0, 0, 1, 1);
        centerPane.add(passageCurr, 0, 1, 1, 1);
        centerPane.add(passageNext, 0, 2, 1, 1);

        
        TreeItem<String> errorRootItem = new TreeItem<> ("Inbox");
        errorRootItem.setExpanded(true);
        for (int i = 1; i < 6; i++) {
            TreeItem<String> item = new TreeItem<> ("Message" + i);            
            errorRootItem.getChildren().add(item);
        }
        
        TreeView<String> errorTree = new TreeView<>(errorRootItem);
        centerPane.add(errorTree, 1, 0, 1, 3);
        
        rootPane.setCenter(centerPane);
        
        
        
        
        rootPane.setBottom(initialiseTableView());
            
        
        
//        private final SimpleStringProperty authorID;
//    private final SimpleStringProperty idInEssay;
//    private final SimpleStringProperty idInParagraph;
//    private final SimpleStringProperty sentenceContent;
//    private final SimpleStringProperty typeIError;
//    private final SimpleStringProperty typeIIError;
//    private final SimpleStringProperty typeIIIError;
//    private final SimpleStringProperty errorSegment;
//    private final SimpleStringProperty remark;
//        
//        
//        MenuBar menuBar = new MenuBar();
//        Menu menuFile = new Menu("File");
//        Menu menuAbout = new Menu("Aboout");
//        menuBar.getMenus().addAll(menuFile, menuAbout);
//        
//        rootPane.setTop(menuBar);
//        
//        GridPane centerPane = new GridPane();
//        rootPane.setCenter(centerPane);
//        
////        VBox textareaPane = new VBox();
//        
//        
//        TextArea passagePrev = new TextArea();
//        TextArea passageCurr = new TextArea();
//        TextArea passageNext = new TextArea();
//        passagePrev.setOpacity(0.5);
//        passageNext.setOpacity(0.5);
//        
//        HBox passagePrevHBox = new HBox();
//        passagePrevHBox.getChildren().add(passagePrev);
//        HBox passageCurrHBox = new HBox();
//        passageCurrHBox.getChildren().add(passageCurr);
//        HBox passageNextHBox = new HBox();
//        passageNextHBox.getChildren().add(passageNext);
//        
//        GridPane.setConstraints(passagePrevHBox, 0, 0);
//        GridPane.setConstraints(passageCurrHBox, 0, 1);
//        GridPane.setConstraints(passageNextHBox, 0, 2);
//        
////        textareaPane.getChildren().addAll(passagePrev, passageCurr, passageNext);
//        
////        rootPane.setCenter(textareaPane);
//        
//        VBox errorPane = new VBox();
//        
//        
//        
        
//        GridPane.setConstraints(errorPane, 1, 0, 1, 3);
          
        
//        rootPane.setRight(errorPane);
//        GridPane gridpane = new GridPane();
        
//        borderPane.setCenter(gridpane);
        
//        GridPane.setConstraints(menuBox, 0, 0);
        
//        gridpane.getChildren().add(menuBox);
        
//        Button btn = new Button();
//        btn.setText("Say 'Hello World'");
//        btn.setOnAction((ActionEvent event) -> {
//            System.out.println("Hello World!");
//        });
        
//        StackPane root = new StackPane();
//        root.getChildren().add(btn);
        return rootPane;
    }
}