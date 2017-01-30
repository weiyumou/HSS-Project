/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import javafx.util.Callback;
import model.Mark;
import model.Sentence;
import org.fxmisc.richtext.StyleSpan;
import org.fxmisc.richtext.StyleSpans;
import view.MainScreen;

/**
 *
 * @author weiyumou
 */
public class TableViewController {

    private static ObservableList<Mark> tableData;
    private static final ObservableList<Integer> highlightRows = FXCollections.observableArrayList();
    private static File dataFile;
    private static File excelFile;

    public static void loadData(Mark mark) {
        if (!tableData.contains(mark)) {
            tableData.add(mark);
            highlightRows.add(tableData.size() - 1);
            MainScreen.getMarkTableView().scrollTo(Integer.MAX_VALUE);
            highlightSeg(mark.getError().getSegment(), mark.getError().getErrorTypes().get(0));
        }
    }

    public static void load() {
        tableData.clear();
        try (ObjectInputStream ois
                = new ObjectInputStream(new FileInputStream(dataFile))) {
            while (true) {
                tableData.add((Mark) ois.readObject());
            }
        } catch (IOException | ClassNotFoundException ex) {
            highlightMarks(TextAreaController.getCurrentSentence());
            highlightErrors(TextAreaController.getCurrentSentence());
        }
    }

    public static void dump() {
        try (ObjectOutputStream oos
                = new ObjectOutputStream(new FileOutputStream(dataFile))) {
            for (int i = 0; i != tableData.size(); ++i) {
                oos.writeObject(tableData.get(i));
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void setDataFile(File dataFile) {
        TableViewController.dataFile = dataFile;
    }

    public static void setExcelFile(File excelFile) {

        TableViewController.excelFile = excelFile;
    }

    public static void clearSelections() {
        MainScreen.getMarkTableView().getSelectionModel().clearSelection();
    }

    public static void refreshTableView() {
        MainScreen.getMarkTableView().refresh();
    }

    public static void highlightMarks(Sentence currSentence) {
        highlightRows.clear();
        for (int i = 0; i != tableData.size(); ++i) {
            if (tableData.get(i).getSentence().equals(currSentence)) {
                highlightRows.add(i);
            }
        }
        if (!highlightRows.isEmpty()) {
            MainScreen.getMarkTableView().scrollTo(highlightRows.get(0));
        }

    }

    public static boolean updateSelectedItem(List<String> errorTypes) {
        int selectedIndex = MainScreen.getMarkTableView().getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            return false;
        }
        Mark currMark = tableData.get(selectedIndex);
        currMark.getError().setErrorTypes(errorTypes);
        highlightSeg(currMark.getError().getSegment(), currMark.getError().getErrorTypes().get(0));
        refreshTableView();
        return true;
    }

    public static void attachTableView(TableView<Mark> tableView) {
        tableData = FXCollections.observableArrayList();
        tableView.setItems(tableData);
    }

    public static EventHandler<KeyEvent> getDeleteKeyEventHandler() {
        return (KeyEvent keyEvent) -> {
            if (keyEvent.getCode() == KeyCode.DELETE) {
                int index = MainScreen.getMarkTableView()
                        .getSelectionModel().getSelectedIndex();
                Mark currMark = tableData.get(index);
                tableData.remove(index);
                highlightRows.remove((Integer) index);
                unhighlightSeg(currMark.getError().getSegment());
                refreshTableView();
            }
        };
    }

    public static Callback<TableView<Mark>, TableRow<Mark>> getRowFactory() {
        return (TableView<Mark> tableView) -> {
            final TableRow<Mark> row = new TableRow<Mark>() {
                @Override
                protected void updateItem(Mark mark, boolean empty) {
                    super.updateItem(mark, empty);

                    if (highlightRows.contains(getIndex())) {
                        if (!getStyleClass().contains("highlightedRow")) {
                            getStyleClass().add("highlightedRow");
                        }
                    } else {
                        getStyleClass().removeAll(Collections.singleton("highlightedRow"));
                    }
                }
            };
            row.setOnMouseClicked((MouseEvent mouseEvent) -> {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 1) {
                        if(row.getIndex() < tableData.size()){
                            Mark currMark = tableData.get(row.getIndex());
                            TextAreaController.scrollTo(currMark.getSentence().getIdInEssay());
//                        TextAreaController.highlightText(tableData.get(row.getIndex())
//                                .getError().getSegment());
                            highlightText(tableData.get(row.getIndex()).getError().getSegment());
                        }
                        
                    }
                }
            });
            highlightRows.addListener(new ListChangeListener<Integer>() {
                @Override
                public void onChanged(Change<? extends Integer> change) {
                    if (highlightRows.contains(row.getIndex())) {
                        if (!row.getStyleClass().contains("highlightedRow")) {
                            row.getStyleClass().add("highlightedRow");
                        }
                    } else {
                        row.getStyleClass().removeAll(Collections.singleton("highlightedRow"));
                    }
                }
            });
            tableData.addListener((ListChangeListener.Change<? extends Mark> change) -> {
                refreshTableView();
            });
            return row;
        };
    }

    public static void dumpToExcel() {
//        HSSFWorkbook excelWorkbook = new HSSFWorkbook();
//        HSSFSheet sheet = workbook.createSheet("Java Books");
        final String[] colNames = MainScreen.getTableColNames();
        try (PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(excelFile.getPath()), "UTF-8"));) {
            int i;
            for (i = 0; i != colNames.length - 1; ++i) {
                w.print(colNames[i] + ",");
            }
            w.println(colNames[i]);
            for (Mark item : tableData) {
                w.println(item.toString());
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setHeaderText(null);
            alert.setContentText("已保存");
            alert.showAndWait();
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }

    public static void highlightErrors(Sentence currSentence) {
        for (int i = 0; i != tableData.size(); ++i) {
            if (tableData.get(i).getSentence().equals(currSentence)) {
                highlightSeg(tableData.get(i).getError().getSegment(),
                        tableData.get(i).getError().getErrorTypes().get(0));
            }
        }
    }

    private static void highlightText(String segment) {
        MainScreen.getCurrEssay().selectRange(0, 0);
        int start = MainScreen.getCurrEssay().getText().indexOf(segment);
        if (start != -1) {
            MainScreen.getCurrEssay().selectRange(start, start + segment.length());
        }
    }

    private static void unhighlightSeg(String segment){
        MainScreen.getCurrEssay().selectRange(0, 0);
        int start = MainScreen.getCurrEssay().getText().indexOf(segment);
        if (start != -1) {
            MainScreen.getCurrEssay().setStyleClass(start, start + segment.length(), "norm");
        }
    }
    
    private static void highlightSeg(String segment, String typeIError) {
        int start = MainScreen.getCurrEssay().getText().indexOf(segment);
        StyleSpans<Collection<String>> curr_stylespan = MainScreen.getCurrEssay()
                .getStyleSpans(start, start + segment.length());
        Collection<String> curr_style = curr_stylespan.getStyleSpan(0).getStyle();
        
        if (start != -1) {
            switch (typeIError) {
                case "标点":
                    if(curr_style == null || curr_style.isEmpty()){
                        MainScreen.getCurrEssay().setStyleClass(start, start + segment.length(), "punc");
                    }else if(!curr_style.contains("punc")){
                        Collection<String> new_style = new ArrayList<>(curr_style);
                        new_style.add("punc");
                        MainScreen.getCurrEssay().clearStyle(start, start + segment.length());
                        MainScreen.getCurrEssay().setStyle(start, start + segment.length(), new_style);
                    }
                    break;
                case "字":
                    if(curr_style == null || curr_style.isEmpty()){
                        MainScreen.getCurrEssay().setStyleClass(start, start + segment.length(), "lett");
                    }else if(!curr_style.contains("lett")){
                        Collection<String> new_style = new ArrayList<>(curr_style);
                        new_style.add("lett");
                        MainScreen.getCurrEssay().clearStyle(start, start + segment.length());
                        MainScreen.getCurrEssay().setStyle(start, start + segment.length(), new_style);
                    }
                    break;
                case "词":
                    if(curr_style == null || curr_style.isEmpty()){
                        MainScreen.getCurrEssay().setStyleClass(start, start + segment.length(), "word");
                    }else if(!curr_style.contains("word")){
                        Collection<String> new_style = new ArrayList<>(curr_style);
                        new_style.add("word");
                        MainScreen.getCurrEssay().clearStyle(start, start + segment.length());
                        MainScreen.getCurrEssay().setStyle(start, start + segment.length(), new_style);
                    }
                    break;
                case "句":
                    if(curr_style == null || curr_style.isEmpty()){
                        MainScreen.getCurrEssay().setStyleClass(start, start + segment.length(), "sent");
                    }else if(!curr_style.contains("sent")){
                        Collection<String> new_style = new ArrayList<>(curr_style);
                        new_style.add("sent");
                        MainScreen.getCurrEssay().clearStyle(start, start + segment.length());
                        MainScreen.getCurrEssay().setStyle(start, start + segment.length(), new_style);
                    }
                    break;
                case "篇章":
                    if(curr_style == null || curr_style.isEmpty()){
                        MainScreen.getCurrEssay().setStyleClass(start, start + segment.length(), "para");
                    }else if(!curr_style.contains("para")){
                        Collection<String> new_style = new ArrayList<>(curr_style);
                        new_style.add("para");
                        MainScreen.getCurrEssay().clearStyle(start, start + segment.length());
                        MainScreen.getCurrEssay().setStyle(start, start + segment.length(), new_style);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}
