/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.MainScreenController;
import controller.TableViewController;
import controller.TextAreaController;
import controller.ToolbarController;
import controller.TreeViewController;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.EditingCell;
import model.Mark;
import model.TextFieldTreeCellImpl;
import org.fxmisc.richtext.StyleClassedTextArea;

/**
 *
 * @author weiyumou
 */
public class MainScreen extends Application {

    private static FileChooser openFileChooser;
    private static FileChooser saveFileChooser;
    private static TreeView<String> errorTreeView;
    private static TableView<Mark> markTableView;
    private static TextArea prevEssay;
//    private static TextArea currEssay;
    private static StyleClassedTextArea currEssay;
    private static TextArea nextEssay;

    private static Label usernameLabel;
    private static Label usercategoryLabel;
    private static Button authorinfoButton;
    private static Label titleLabel;
    private static Label authorIDLabel;
    
    private static Button saveToExcelButton;
    private static Button saveButton;
    
    private static final String[] tableColNames = {
        "作者序号", "句子序号", "句子在段落中序号", 
        "句子", "错误类型", "具体错误类型", 
        "病句及句子成分错误类型", "错误所在片段", "备注"
    };

    @Override
    public void start(Stage primaryStage) {
        MainScreenController.login();
    }

    @Override
    public void stop() {
        MainScreenController.applicationClose();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    private static Node initialiseTableView() {
        final int numOfCol = 9;

        markTableView = new TableView<>();
        markTableView.setPlaceholder(new Label("没有数据"));
        markTableView.setRowFactory(TableViewController.getRowFactory());

        TableColumn authorIDCol = new TableColumn(tableColNames[0]);
        authorIDCol.prefWidthProperty().bind(markTableView.widthProperty().divide(numOfCol * 2));
        authorIDCol.setCellValueFactory(new PropertyValueFactory<>("authorid"));

        TableColumn idInEssayCol = new TableColumn(tableColNames[1]);
        idInEssayCol.prefWidthProperty().bind(markTableView.widthProperty().divide(numOfCol * 2));
        idInEssayCol.setCellValueFactory(new PropertyValueFactory<>("idInEssay"));

        TableColumn idInParaCol = new TableColumn(tableColNames[2]);
        idInParaCol.prefWidthProperty().bind(markTableView.widthProperty().divide(numOfCol));
        idInParaCol.setCellValueFactory(new PropertyValueFactory<>("idInParagraph"));

        TableColumn contentCol = new TableColumn(tableColNames[3]);
        contentCol.prefWidthProperty().bind(markTableView.widthProperty().divide(numOfCol));
        contentCol.setCellValueFactory(new PropertyValueFactory<>("sentenceContent"));

        TableColumn typeIErrorCol = new TableColumn(tableColNames[4]);
        typeIErrorCol.prefWidthProperty().bind(markTableView.widthProperty().divide(numOfCol));
        typeIErrorCol.setCellValueFactory(new PropertyValueFactory<>("typeIError"));

        TableColumn typeIIErrorCol = new TableColumn(tableColNames[5]);
        typeIIErrorCol.prefWidthProperty().bind(markTableView.widthProperty().divide(numOfCol));
        typeIIErrorCol.setCellValueFactory(new PropertyValueFactory<>("typeIIError"));

        TableColumn typeIIIErrorCol = new TableColumn(tableColNames[6]);
        typeIIIErrorCol.prefWidthProperty().bind(markTableView.widthProperty().divide(numOfCol));
        typeIIIErrorCol.setCellValueFactory(new PropertyValueFactory<>("typeIIIError"));

        TableColumn errorSegmentCol = new TableColumn(tableColNames[7]);
        errorSegmentCol.prefWidthProperty().bind(markTableView.widthProperty().divide(numOfCol));
        errorSegmentCol.setCellValueFactory(new PropertyValueFactory<>("errorSegment"));

        Callback<TableColumn, TableCell> cellFactory = (TableColumn p) -> new EditingCell();
        TableColumn remarkCol = new TableColumn(tableColNames[8]);
        remarkCol.prefWidthProperty().bind(markTableView.widthProperty().divide(numOfCol / 2.0));
        remarkCol.setCellValueFactory(new PropertyValueFactory<>("remark"));
        remarkCol.setCellFactory(cellFactory);
        remarkCol.setOnEditCommit(
                new EventHandler<CellEditEvent<Mark, String>>() {
            @Override
            public void handle(CellEditEvent<Mark, String> t) {
                Mark currMark = t.getTableView().getItems().get(
                        t.getTablePosition().getRow());
                currMark.getError().setRemark(t.getNewValue());
            }
        }
        );

        markTableView.setEditable(true);

        markTableView.getColumns().addAll(authorIDCol, idInEssayCol, idInParaCol,
                contentCol, typeIErrorCol, typeIIErrorCol, typeIIIErrorCol,
                errorSegmentCol, remarkCol);

        markTableView.setPrefHeight(200);

        TableViewController.attachTableView(markTableView);

        markTableView.setOnKeyReleased(TableViewController.getDeleteKeyEventHandler());

        return markTableView;
    }

    private static Node initialiseToolBar() {
        final Button openButton = new Button("打开");
        openButton.setGraphic(new ImageView(
                new Image(MainScreen.class.getResourceAsStream(
                "/resources/img/glyphicons-145-folder-open.png"))));
        openButton.setPrefWidth(80);
        openButton.setOnAction(ToolbarController.getOpenFileEventHandler());

        saveButton = new Button("保存批注");
        saveButton.setGraphic(new ImageView(
                new Image(MainScreen.class.getResourceAsStream(
                "/resources/img/glyphicons-447-floppy-save.png"))));
        saveButton.setPrefWidth(100);
        saveButton.setOnAction(ToolbarController.getSaveFileEventHandler());
        saveButton.setDisable(true);

        saveToExcelButton = new Button("保存到Excel");
        saveToExcelButton.setGraphic(new ImageView(
                new Image(MainScreen.class.getResourceAsStream(
                "/resources/img/glyphicons-447-floppy-save.png"))));
        saveToExcelButton.setPrefWidth(120);
        saveToExcelButton.setDisable(true);
        saveToExcelButton.setOnAction(ToolbarController.getSaveToExcelEventHandler());
        
        final Button logoutButton = new Button();
        logoutButton.setGraphic(new ImageView(
                new Image(MainScreen.class.getResourceAsStream(
                "/resources/img/glyphicons-388-log-out.png"))));
        logoutButton.setOnAction(MainScreenController.getLogoutEventHandler());
        logoutButton.setTooltip(new Tooltip("注销"));

        usernameLabel = new Label("当前用户: ");
        usercategoryLabel = new Label("类别: ");

        titleLabel = new Label("当前文章: ");
        authorIDLabel = new Label("作者序号: ");
        authorIDLabel.setVisible(false);
        ToolbarController.resetEssayDisplay();

        authorinfoButton = new Button("查看作者信息");
        authorinfoButton.setVisible(false);
        authorinfoButton.setDisable(true);

        /*
     * Extending the default ToolBar has the benefit, that you inherit useful features, like auto-collapse
     * (try resizing the window to something small).
         */
        final ToolBar toolBar = new ToolBar();
        final HBox leftSection = new HBox(openButton, saveButton, saveToExcelButton);
        final HBox centerSection = new HBox(titleLabel, authorIDLabel, authorinfoButton);
        final HBox rightSection = new HBox(usernameLabel, usercategoryLabel, logoutButton);


        /* Center all sections and always grow them. Has the effect known as JUSTIFY. */
        HBox.setHgrow(leftSection, Priority.ALWAYS);
        HBox.setHgrow(centerSection, Priority.ALWAYS);
        HBox.setHgrow(rightSection, Priority.ALWAYS);

        leftSection.setAlignment(Pos.CENTER_LEFT);
        centerSection.setAlignment(Pos.CENTER);
        rightSection.setAlignment(Pos.CENTER_RIGHT);

        /* It might be harder to propagate some properties: */
        final int spacing = 8;
        toolBar.setPadding(new Insets(0, spacing, 0, spacing));
        leftSection.setSpacing(spacing);
        centerSection.setSpacing(spacing);
        rightSection.setSpacing(spacing);

        toolBar.getItems().addAll(leftSection, centerSection, rightSection);
        toolBar.setPrefHeight(40);

        return toolBar;
    }

    private static Node intialiseCentre() {
        GridPane centerPane = new GridPane();

        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(70);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(30);
        centerPane.getColumnConstraints().add(col1);
        centerPane.getColumnConstraints().add(col2);

        for (int i = 0; i != 3; ++i) {
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100 / 3.0);
            centerPane.getRowConstraints().add(row);
        }

        prevEssay = new TextArea();
//        currEssay = new TextArea();
        currEssay = new StyleClassedTextArea();
        nextEssay = new TextArea();

        prevEssay.setStyle("-fx-font-size: 20px;");
        currEssay.setStyle("-fx-font-size: 20px;");
        nextEssay.setStyle("-fx-font-size: 20px;");

        prevEssay.setContextMenu(new ContextMenu());
        currEssay.setContextMenu(new ContextMenu());
        nextEssay.setContextMenu(new ContextMenu());

        prevEssay.setOnKeyPressed(TextAreaController.getNoCopyKeyEventHandler());
        currEssay.setOnKeyPressed(TextAreaController.getNoCopyKeyEventHandler());
        currEssay.setOnKeyPressed(TextAreaController.getNoCopyKeyEventHandler());

        prevEssay.setOpacity(0.5);
        prevEssay.setEditable(false);
        nextEssay.setOpacity(0.5);
        nextEssay.setEditable(false);

        prevEssay.setWrapText(true);
        nextEssay.setWrapText(true);
        currEssay.setWrapText(true);

        currEssay.setOnScroll(TextAreaController.getScrollEventHandler());
        currEssay.setOnKeyReleased(TextAreaController.getScrollKeyEventHandler());

        currEssay.focusedProperty().addListener(TextAreaController.getChangeListener());

        centerPane.add(prevEssay, 0, 0, 1, 1);
        centerPane.add(currEssay, 0, 1, 1, 1);
        centerPane.add(nextEssay, 0, 2, 1, 1);

        TreeItem<String> errorRootItem = TreeViewController.buildTreeView();
        errorTreeView = new TreeView<>(errorRootItem);
        errorTreeView.setEditable(true);
        errorTreeView.setCellFactory((TreeView<String> p) -> new TextFieldTreeCellImpl());
        errorTreeView.setOnKeyReleased(TreeViewController.getDeleteKeyEventHandler());
        centerPane.add(errorTreeView, 1, 0, 1, 3);

        return centerPane;
    }

    private static Parent initialiseUI() {

        BorderPane rootPane = new BorderPane();

        rootPane.setTop(initialiseToolBar());

        rootPane.setCenter(intialiseCentre());

        rootPane.setBottom(initialiseTableView());

        return rootPane;
    }

    public static TreeView<String> getErrorTreeView() {
        return errorTreeView;
    }

    public static TextArea getPrevEssay() {
        return prevEssay;
    }

//    public static TextArea getCurrEssay() {
//        return currEssay;

    public static StyleClassedTextArea getCurrEssay() {
        return currEssay;
    }

//    }
    public static TextArea getNextEssay() {
        return nextEssay;
    }

    public static FileChooser getOpenFileChooser() {
        return openFileChooser;
    }

    public static void setOpenFileChooser(FileChooser fileChooser) {
        openFileChooser = fileChooser;
    }

    public static FileChooser getSaveFileChooser() {
        return saveFileChooser;
    }

    public static void setSaveFileChooser(FileChooser saveFileChooser) {
        MainScreen.saveFileChooser = saveFileChooser;
    }
    public static Parent buildUI() {
        return initialiseUI();
    }

    public static void setUsernameLabel(String username) {
        usernameLabel.setText(usernameLabel.getText() + username);
    }

    public static void setUsercategoryLabel(String userType) {
        usercategoryLabel.setText(usercategoryLabel.getText() + userType);
    }

    public static Button getAuthorinfoButton() {
        return authorinfoButton;
    }

    public static void clearEssayTitle() {
        titleLabel.setText("");
    }

    public static void setEssayTitle(String title) {
        titleLabel.setText(titleLabel.getText() + title);
    }

    public static void setAuthorID(String authorID) {
        authorIDLabel.setText(authorIDLabel.getText() + authorID);
    }

    public static void clearAuthorID() {
        authorIDLabel.setText("");
    }

    public static TableView<Mark> getMarkTableView() {
        return markTableView;
    }

    public static Button getSaveToExcelButton() {
        return saveToExcelButton;
    }

    public static Button getSaveButton() {
        return saveButton;
    } 

    public static String[] getTableColNames() {
        return tableColNames;
    }

    public static Label getAuthorIDLabel() {
        return authorIDLabel;
    }
}
