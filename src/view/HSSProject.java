/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.TextAreaController;
import controller.TreeViewController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
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
import model.TextFieldTreeCellImpl;

/**
 *
 * @author weiyumou
 */
public class HSSProject extends Application {
    
    private static TreeView<String> errorTreeView;
    
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
    
    private Node initialiseTableView(){
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
    
    private Node initialiseMenuBar(){
        MenuBar menuBar = new MenuBar();
        Menu menuFile = new Menu("文件");
        Menu menuAbout = new Menu("关于");
        menuBar.getMenus().addAll(menuFile, menuAbout);
        return menuBar;
    }
    
    private Node intialiseCentre(){
        GridPane centerPane = new GridPane();
        
        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(70);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(30);
        centerPane.getColumnConstraints().add(col1);
        centerPane.getColumnConstraints().add(col2);
        
        for(int i = 0; i != 3; ++i){
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100/3.0);
            centerPane.getRowConstraints().add(row);
        }
        
        TextArea prevEssay = new TextArea();
        TextArea currEssay = new TextArea();
        TextArea nextEssay = new TextArea();
        prevEssay.setOpacity(0.5);
        prevEssay.setEditable(false);
        nextEssay.setOpacity(0.5);
        nextEssay.setEditable(false);
        
        prevEssay.setText("这里显示之前的句子。");
        nextEssay.setText("这里显示之后的句子。");
        currEssay.setText("这里显示正在处理的句子。");
        
        currEssay.requestFocus();
        
        currEssay.focusedProperty().addListener(TextAreaController.getChangeListener());
        
        centerPane.add(prevEssay, 0, 0, 1, 1);
        centerPane.add(currEssay, 0, 1, 1, 1);
        centerPane.add(nextEssay, 0, 2, 1, 1);

        TreeItem<String> errorRootItem = TreeViewController.buildTreeView();
        errorTreeView = new TreeView<>(errorRootItem);
        errorTreeView.setEditable(true);
        errorTreeView.setCellFactory((TreeView<String> p) -> new TextFieldTreeCellImpl());
        centerPane.add(errorTreeView, 1, 0, 1, 3);
        
        
        
        return centerPane;
    }
    
    
    private Parent initialiseUI(){
        
        BorderPane rootPane = new BorderPane();

        rootPane.setTop(initialiseMenuBar());

        rootPane.setCenter(intialiseCentre());

        rootPane.setBottom(initialiseTableView());

        return rootPane;
    }

    public static TreeView<String> getErrorTreeView() {
        return errorTreeView;
    }
}